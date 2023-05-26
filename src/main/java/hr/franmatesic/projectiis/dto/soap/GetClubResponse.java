package hr.franmatesic.projectiis.dto.soap;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"club"})
@XmlRootElement(name = "getClubResponse")
public class GetClubResponse {

    @XmlElement(required = true)
    protected List<Club> club;
}