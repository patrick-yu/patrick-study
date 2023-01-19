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
public class CmmtMessageSource implements Serializable {
	private static final long serialVersionUID = -4587222919787451706L;
	protected String messageSourceId;
	protected String locale;
	protected String messageCode;
	protected String message;
	protected String remark;
	protected String creatorId;
	protected Date createdDt;
	protected String updaterId;
	protected Date updatedDt;

	/*
	 * Helper
	 */
	protected Long rn;
}
