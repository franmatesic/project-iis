package hr.franmatesic.projectiis.dto.xml;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationResponse {

  private String message;
  private Boolean valid;
}
