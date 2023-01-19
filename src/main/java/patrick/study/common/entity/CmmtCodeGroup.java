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
public class CmmtCodeGroup implements Serializable {
	private static final long serialVersionUID = -2974304148421187744L;
	private String codeGroup;
	private String codeGroupNm;
	private String remark;
	private String useYn;
	private String creatorId;
	private Date createdDt;
	private String updaterId;
	private Date updatedDt;
}
