package ChasAcademy.LibraryAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableCaching
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class LibraryApiApplication {

    //Default served at
    //http://localhost:8080/swagger-ui/index.html

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
