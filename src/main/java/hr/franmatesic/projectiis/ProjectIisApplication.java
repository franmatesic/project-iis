package hr.franmatesic.projectiis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ProjectIisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectIisApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {
        new XmlRpcServerRunner().start();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        final var marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("hr.franmatesic.projectiis.dto.api");
        return marshaller;
    }
}
