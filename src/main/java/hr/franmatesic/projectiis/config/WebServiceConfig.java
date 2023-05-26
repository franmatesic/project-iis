package hr.franmatesic.projectiis.config;

import hr.franmatesic.projectiis.controller.SoapController;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        final var servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "clubs")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema clubsSchema) {
        final var wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ClubsPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(SoapController.SOAP_NAMESPACE);
        wsdl11Definition.setSchema(clubsSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema clubsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("soap.xsd"));
    }
}
