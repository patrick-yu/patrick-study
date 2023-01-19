package patrick.study.config.exception.vo;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {
	private static final long serialVersionUID = 7900992209359387432L;
	private String error;
	private String message;
	private Integer status;
	private List<ErrorResponseItem> errors;
}
