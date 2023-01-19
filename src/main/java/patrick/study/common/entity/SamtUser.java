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
public class SamtUser implements Serializable {
	private static final long serialVersionUID = 3581440734358985382L;
	String mberId;
	String loginId;
	String mberNm;
	String mbrPhotoId;
	String atchFileGroupId;
	String creatorId;
	Date createdDt;
	String updaterId;
	Date updatedDt;
}
