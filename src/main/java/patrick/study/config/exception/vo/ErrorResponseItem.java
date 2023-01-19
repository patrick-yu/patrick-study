package patrick.study.config.exception.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseItem implements Serializable {
	private static final long serialVersionUID = -4264359674405906569L;
	private String field;
	private String message;
}
