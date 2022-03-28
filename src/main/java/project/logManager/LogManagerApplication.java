package project.logManager;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LogManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(LogManagerApplication.class, args);
  }

  @Bean
  public OpenAPI openAPIConfig() {
    return new OpenAPI().info(apiInfo());
  }

  public Info apiInfo() {
    Info info = new Info();
    info
        .title("LogManager API")
        .description("LogManager System Swagger Open API")
        .version("v1.0.0");
    return info;
  }
}
