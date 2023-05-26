package hr.franmatesic.projectiis.dto.soap;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "club", propOrder = {"games", "points"})
public class Club {

    @XmlSchemaType(name = "unsignedInt")
    protected long games;
    @XmlSchemaType(name = "unsignedInt")
    protected long points;
    @XmlAttribute(name = "name", required = true)
    protected String name;
}

