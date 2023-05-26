package hr.franmatesic.projectiis.controller;

import hr.franmatesic.projectiis.dto.api.ObjectFactory;
import hr.franmatesic.projectiis.dto.soap.Club;
import hr.franmatesic.projectiis.dto.soap.GetClubRequest;
import hr.franmatesic.projectiis.dto.soap.GetClubResponse;
import hr.franmatesic.projectiis.service.ApiService;
import jakarta.xml.bind.JAXBContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Endpoint
public class SoapController {

    public static final String SOAP_NAMESPACE = "localhost/clubs";
    private static final String XPATH = "/standings/rows/team[contains(name, '%s')]";
    private static final String XPATH_NAME = XPATH.concat("/name/text()");
    private static final String XPATH_GAMES = XPATH.concat("/../matches/text()");
    private static final String XPATH_POINTS = XPATH.concat("/../points/text()");

    private final File xsdSchemaFile;

    public SoapController() throws IOException {
        xsdSchemaFile = new ClassPathResource("files/soap-xsd-schema.xsd").getFile();
    }

    @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "getClubRequest")
    @ResponsePayload
    public GetClubResponse getClub(@RequestPayload GetClubRequest request) {
        return new GetClubResponse(filter(request.getName()));
    }

    private List<Club> filter(final String filter) {
        try {
            final var xpathFactory = XPathFactory.newInstance();
            final var xpath = xpathFactory.newXPath();
            final var clubNameExpression = xpath.compile(String.format(XPATH_NAME, filter));
            final var clubGamesExpression = xpath.compile(String.format(XPATH_GAMES, filter));
            final var clubPointsExpression = xpath.compile(String.format(XPATH_POINTS, filter));

            final var document = readStandings();
            if (!validateStandings()) {
                return Collections.emptyList();
            }

            final var nameList = (NodeList) clubNameExpression.evaluate(document, XPathConstants.NODESET);
            final var gamesList = (NodeList) clubGamesExpression.evaluate(document, XPathConstants.NODESET);
            final var pointsList = (NodeList) clubPointsExpression.evaluate(document, XPathConstants.NODESET);

            if (nameList.getLength() == 0) {
                return Collections.emptyList();
            }

            final var result = new ArrayList<Club>();
            for (int i = 0; i < nameList.getLength(); i++) {
                final var club = Club.builder()
                        .name(nameList.item(i).getNodeValue())
                        .games(Long.parseLong(gamesList.item(i).getNodeValue()))
                        .points(Long.parseLong(pointsList.item(i).getNodeValue()))
                        .build();
                result.add(club);
            }
            return result;
        } catch (Exception e) {
            log.error("Soap controller XPATH error: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    private boolean validateStandings() {
        try {
            final var context = JAXBContext.newInstance(ObjectFactory.class);
            final var unmarshaller = context.createUnmarshaller();
            final var schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            final var schema = schemaFactory.newSchema(xsdSchemaFile);
            unmarshaller.setSchema(schema);

            unmarshaller.unmarshal(new File(ApiService.STANDINGS_XML));
            log.info("Validated generated xml file");
            return true;
        } catch (Exception e) {
            log.error("Error while validating generated file: {}", e.getMessage());
        }
        return false;
    }

    private Document readStandings() throws ParserConfigurationException, IOException, SAXException {
        final var factory = DocumentBuilderFactory.newInstance();
        final var builder = factory.newDocumentBuilder();
        return builder.parse(ApiService.STANDINGS_XML);
    }
}
