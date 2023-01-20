package patrick.study.common.entity;

import java.io.Serializable;
import java.util.Date;

import mlibrary.util.CoreUtils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TmpSample implements Serializable {
	private static final long serialVersionUID = 745143633420011870L;
	private String id;
	private String name;
	private Integer age;
	private String creatorId;
	private Date createdDt;
	private String updaterId;
	private Date updatedDt;

	public String getFmtCreatedDt() {
		if (createdDt == null) {
			return null;
		}
		String fmt = CoreUtils.date.format(createdDt, "yyyy-MM-dd HH:mm:ss");
		return fmt;
	}
}
