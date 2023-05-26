package hr.franmatesic.projectiis.dto.soap;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"name"})
@XmlRootElement(name = "getClubRequest")
public class GetClubRequest {

    @XmlElement(required = true)
    protected String name;
}
