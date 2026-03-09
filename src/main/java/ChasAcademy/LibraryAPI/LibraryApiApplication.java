package ChasAcademy.LibraryAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApiApplication {

    //Default served at
    //http://localhost:8080/swagger-ui/index.html

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
