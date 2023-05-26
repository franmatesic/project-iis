package hr.franmatesic.projectiis.dto.api;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tournament {

  private String name;
  private List<Row> rows;
  private ApiTextObject tieBreakingRule;
}
