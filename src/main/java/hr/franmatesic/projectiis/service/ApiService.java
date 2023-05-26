package hr.franmatesic.projectiis.service;

import hr.franmatesic.projectiis.dto.api.ApiResponse;
import hr.franmatesic.projectiis.dto.api.Row;
import hr.franmatesic.projectiis.dto.api.Standings;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ApiService {

    public static final String STANDINGS_XML = "standings.xml";

    private final RestTemplate restTemplate;
    private final Jaxb2Marshaller jaxb2Marshaller;

    @Value("${app.api.url}")
    private String apiUrl;

    @Value("${app.api.key}")
    private String apiKey;

    @Value("${app.api.host}")
    private String apiHost;

    public List<Row> getStandings() throws URISyntaxException, JAXBException {
        final var headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", apiKey);
        headers.set("X-RapidAPI-Host", apiHost);
        headers.setContentType(MediaType.APPLICATION_JSON);
        final var response = restTemplate.exchange(new URI(apiUrl), HttpMethod.GET, new HttpEntity<String>(headers), ApiResponse.class);
        if (Objects.isNull(response.getBody())) {
            return Collections.emptyList();
        }
        final var standings = response.getBody().getData().get(0).getRows();
        storeXml(new Standings(standings));
        return standings;
    }

    private void storeXml(final Standings standings) throws JAXBException {
        final var marshaller = jaxb2Marshaller.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(standings, new File(STANDINGS_XML));
    }
}
