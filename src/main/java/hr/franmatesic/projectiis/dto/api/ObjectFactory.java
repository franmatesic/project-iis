package hr.franmatesic.projectiis.dto.api;

import jakarta.xml.bind.annotation.XmlRegistry;
import lombok.NoArgsConstructor;

@XmlRegistry
@NoArgsConstructor
public class ObjectFactory {

    public Standings createStandings() {
        return new Standings();
    }

    public Row createRow() {
        return new Row();
    }

    public ApiTextObject createApiTextObject() {
        return new ApiTextObject();
    }

    public Team createTeam() {
        return new Team();
    }
}
