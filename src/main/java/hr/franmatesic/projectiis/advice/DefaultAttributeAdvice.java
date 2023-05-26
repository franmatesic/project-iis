package hr.franmatesic.projectiis.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class DefaultAttributeAdvice {

    @ModelAttribute("tasks")
    public List<String[]> tasks() {
        final var result = new ArrayList<String[]>();
        for (var i = 1; i <= 6; i++) {
            if (i == 4) {
                continue;
            }
            final var tabName = i == 3 ? "3. i 4. Zadatak" : (i + ". Zadatak");
            result.add(new String[]{tabName, "tab" + i, "tab" + i + "-pane"});
        }
        return result;
    }
}
