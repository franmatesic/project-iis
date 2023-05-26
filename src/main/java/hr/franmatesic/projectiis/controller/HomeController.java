package hr.franmatesic.projectiis.controller;

import hr.franmatesic.projectiis.service.ApiService;
import hr.franmatesic.projectiis.service.XmlRpcClientService;
import hr.franmatesic.projectiis.validator.XMLValidator;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@SessionAttributes({"tasks", "tournaments"})
public class HomeController {

    private final XMLValidator xmlValidator;
    private final XmlRpcClientService xmlRpcClientService;
    private final ApiService apiService;

    @GetMapping
    public String getHome() {
        return "home";
    }

    @PostMapping("/validateXSD")
    public String validateXSD(@RequestParam("fileXSD") MultipartFile file, Model model) {
        model.addAttribute("validXSD", xmlValidator.validateXSD(file));
        return "fragments/tab1";
    }

    @PostMapping("/validateRNG")
    public String validateRNG(@RequestParam("fileRNG") MultipartFile file, Model model) {
        model.addAttribute("validRNG", xmlValidator.validateRNG(file));
        return "fragments/tab2";
    }

    @PostMapping("/fetchWeather")
    public String fetchWeather(@RequestParam("cityName") String cityName, Model model) throws XmlRpcException {
        model.addAttribute("weather", xmlRpcClientService.getWeatherFromCity(cityName));
        return "fragments/tab5";
    }

    @PostMapping("/api")
    public String getStandings(Model model) throws URISyntaxException, JAXBException {
        model.addAttribute("rows", apiService.getStandings());
        return "fragments/tab6";
    }
}
