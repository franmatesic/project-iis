package hr.franmatesic.projectiis;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Slf4j
public class XmlRpcServerRunner {

  private static final String WEATHER_URL = "http://vrijeme.hr/hrvatska_n.xml";
  private static final String XPATH_CITIES = "/Hrvatska/Grad[contains(GradIme, '%s')]";
  private static final String XPATH_CITY_NAMES = XPATH_CITIES.concat("/GradIme/text()");
  private static final String XPATH_CITY_TEMPS = XPATH_CITIES.concat("/Podatci/Temp/text()");

  public void start() {
    try {
      final var webServer = new WebServer(8081);
      final var xmlRpcServer = webServer.getXmlRpcServer();

      final var config = new XmlRpcServerConfigImpl();
      config.setEnabledForExtensions(true);
      xmlRpcServer.setConfig(config);

      final var propertyMapping = new PropertyHandlerMapping();
      propertyMapping.addHandler("server", XmlRpcServerRunner.class);
      xmlRpcServer.setHandlerMapping(propertyMapping);

      log.info("XML RPC Server listening...");
      webServer.start();
    }
    catch (IOException | XmlRpcException e) {
      log.error("XML RPC Server error: {}", e.getMessage());
    }
  }

  public String getWeatherFromCity(final String cityName) {
    try {
      final var xpathFactory = XPathFactory.newInstance();
      final var xpath = xpathFactory.newXPath();
      final var cityNameExpression = xpath.compile(String.format(XPATH_CITY_NAMES, cityName));
      final var cityTempExpression = xpath.compile(String.format(XPATH_CITY_TEMPS, cityName));

      final var document = readWeatherXML();
      final var nameList = (NodeList) cityNameExpression.evaluate(document, XPathConstants.NODESET);
      final var tempList = (NodeList) cityTempExpression.evaluate(document, XPathConstants.NODESET);

      if (nameList.getLength() == 0) {
        return String.format("%s not found", cityName);
      }

      final var builder = new StringBuilder();
      for (var i = 0; i < nameList.getLength(); i++) {
        var name = nameList.item(i).getNodeValue();
        var temp = tempList.item(i).getNodeValue();
        builder.append(String.format("%s   %s°%n", name, temp));
      }
      return builder.toString();
    }
    catch (Exception e) {
      log.error("XML RPC Server error: {}", e.getMessage());
    }
    return String.format("%s not found", cityName);
  }

  private Document readWeatherXML() throws ParserConfigurationException, IOException, SAXException {
    final var factory = DocumentBuilderFactory.newInstance();
    final var builder = factory.newDocumentBuilder();
    return builder.parse(WEATHER_URL);
  }
}
