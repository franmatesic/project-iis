package hr.franmatesic.projectiis.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.stereotype.Service;

@Service
public class XmlRpcClientService {

  private final XmlRpcClient client;

  public XmlRpcClientService() throws MalformedURLException {
    client = new XmlRpcClient();
    final var config = new XmlRpcClientConfigImpl();
    config.setEnabledForExtensions(true);
    config.setServerURL(new URL("http://localhost:8081/"));
    client.setConfig(config);
  }

  public String getWeatherFromCity(final String cityName) throws XmlRpcException {
    return (String) client.execute("server.getWeatherFromCity", List.of(cityName));
  }
}
