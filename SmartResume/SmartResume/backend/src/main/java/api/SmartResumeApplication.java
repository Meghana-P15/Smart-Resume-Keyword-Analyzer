package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts the Spring Boot server (http://localhost:8080).
 * The DSA analyzer (packages analysis, algorithms, model, processing) is plain Java
 * and is used as it is - Spring is only the API + database layer around it.
 */
@SpringBootApplication
public class SmartResumeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartResumeApplication.class, args);
    }
}
