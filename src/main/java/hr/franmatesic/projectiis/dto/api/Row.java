package hr.franmatesic.projectiis.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Row {

    private Team team;
    private ApiTextObject promotion;
    private int position;
    private int matches;
    private int wins;
    private int losses;
    private int draws;
    private int scoresFor;
    private int scoresAgainst;
    private int points;
}
