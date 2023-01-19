package patrick.study.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtCode implements Serializable {
	private static final long serialVersionUID = -6749866300097959201L;
	private String codeGroup;
	private String locale;
	private String code;
	private String codeNm;
	private String remark;
	private String codeType;
	private String useYn;
	private Long sortOrder;
	private String creatorId;
	private Date createdDt;
	private String updaterId;
	private Date updatedDt;
}
