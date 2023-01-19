package patrick.study.config.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mframework.common.dao.FwAuthrtDao;
import mframework.common.dao.BnLoginDao;
import mframework.common.vo.SecMbr;
import mframework.common.vo.CustomUserDetails;
import mframework.common.vo.FwAuthrt;

/**
 * 로그인할 때, Spring security가 사용한다.
 * 사용자 정보를 조회하고, Spring security가 이를 이용해
 * 아이디/비번을 비교 판단한다.
 *
 * @author patrick
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private BnLoginDao loginDao;

	@Autowired
    private FwAuthrtDao authorityDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	SecMbr member = null;
   		member = loginDao.selectSecMbrByLgnId(username);

//    	else if(string.startsWith(username, "insider:")){
//    		username = string.substring(username, 8);
//    		member = insiderDao.selectBnMember_loginId(username);
//    	}

    	if (member == null) {
    		throw new UsernameNotFoundException(username + " -> not found");
    	}
    	return createUserDetails(member);
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(SecMbr member) {
    	List<GrantedAuthority> authorityList = new ArrayList<>();
    	List<FwAuthrt> authrtList = authorityDao.selectMbrAuthrtList(member.getMbrId());
    	for (FwAuthrt role : authrtList) {
    		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getAuthrtCd());
    		authorityList.add(grantedAuthority);
    	}
    	if (member.getEnabled() == null) {
    		member.setEnabled(true);
    	}

    	CustomUserDetails details = new CustomUserDetails(member.getLgnId(), member.getPswd(), member.getEnabled(), authorityList, member);
    	return details;
    }
}

