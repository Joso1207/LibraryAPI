package ChasAcademy.LibraryAPI.servlets.ratelimit;

import ChasAcademy.LibraryAPI.api.core.exceptions.RateLimitExceededException;
import ChasAcademy.LibraryAPI.servlets.ratelimit.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@ConditionalOnProperty(
        name = "ratelimit.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final HandlerExceptionResolver resolver;

    public RateLimitFilter(
            RateLimitService rateLimitService,
            @Qualifier("handlerExceptionResolver")
            HandlerExceptionResolver resolver
    ) {
        this.rateLimitService = rateLimitService;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String ip = extractClientIp(request);

        Bucket bucket = rateLimitService.resolveBucket(ip);

        if (!bucket.tryConsume(1)) {

            resolver.resolveException(
                    request,
                    response,
                    null,
                    new RateLimitExceededException(
                            "Request rate exceeded, try again later"
                    )
            );

            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractClientIp(HttpServletRequest request) {

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}