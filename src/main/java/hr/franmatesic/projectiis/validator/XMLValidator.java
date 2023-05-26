package hr.franmatesic.projectiis.validator;

import hr.franmatesic.projectiis.dto.xml.ValidationResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;

@Component
public class XMLValidator {

    private final File xsdSchemaFile;
    private final File rngSchemaFile;

    public XMLValidator() throws IOException {
        xsdSchemaFile = new ClassPathResource("files/xsd-schema.xsd").getFile();
        rngSchemaFile = new ClassPathResource("files/rng-schema.rng").getFile();

        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory");
    }

    public ValidationResponse validateXSD(final MultipartFile file) {
        return validate(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI), xsdSchemaFile, file);
    }

    public ValidationResponse validateRNG(final MultipartFile file) {
        return validate(SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI), rngSchemaFile, file);
    }

    private ValidationResponse validate(final SchemaFactory schemaFactory, final File schemaFile, final MultipartFile file) {
        if (file.isEmpty()) {
            return ValidationResponse.builder().valid(false).message("Please upload a file").build();
        }
        try {
            final var source = new StreamSource(schemaFile);
            final var schema = schemaFactory.newSchema(source);
            final var validator = schema.newValidator();
            validator.validate(new StreamSource(file.getInputStream()));
        } catch (Exception e) {
            return ValidationResponse.builder().valid(false).message(e.getMessage()).build();
        }
        return ValidationResponse.builder().valid(true).message("Valid").build();
    }
}
