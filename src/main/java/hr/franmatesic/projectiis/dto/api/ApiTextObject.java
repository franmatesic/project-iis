package hr.franmatesic.projectiis.dto.api;

import jakarta.xml.bind.annotation.XmlTransient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiTextObject {

    @XmlTransient
    private int id;
    private String text;
}
