package patrick.study.svc.main.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Code implements Serializable {
	private static final long serialVersionUID = -5638996040474540330L;
	private String code;
	private String codeNm;
}
