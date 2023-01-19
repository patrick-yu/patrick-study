package patrick.study.common.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmmtLocale implements Serializable {

	private static final long serialVersionUID = -2929429645463519560L;
	private String locale;
	private String localeNm;
	private Long sortOrder;
	private String remark;
	private String creatorId;
	private Date createdDt;
	private String updaterId;
	private Date updatedDt;

	/*
	 * Helper
	 */
	protected Long rn;
}
