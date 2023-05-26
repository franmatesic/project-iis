package hr.franmatesic.projectiis.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    private String name;
    private String shortName;
    private String nameCode;
}
