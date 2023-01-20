package patrick.study.config.security;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import mframework.common.dao.FwMenuDao;
import mframework.common.util.FwSessionUtils;
import mframework.common.vo.CustomUserDetails;
import mframework.common.vo.FwMenu;
import mframework.common.vo.SecMbr;
import mframework.config.EnvConfig;
import mframework.log.LogUtils;
import mframework.log.vo.LoginLog;
import mlibrary.util.CoreUtils;
import mlibrary.util.CoreUtils.string;

// SecurityConfig.java에서 successHandler 설정
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private LogUtils logUtils;

    @Autowired
    private EnvConfig envConfig;

    @Autowired
    private FwMenuDao bnMenuDao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    	CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
    	SecMbr member = userDetails.getMember();

    	//TODO (유영민)로그인 로그 파일 생성
        writeLog(request, member);

        // 현재 사용자의 메뉴를 세션에 저장한다.
        List<FwMenu> menuList = bnMenuDao.selectList_mbrId(envConfig.getSystemId(), member.getMbrId());
        FwSessionUtils.userMenu.set(menuList);

        //TODO (유영민)로그인 성공 후 처리할 작업
//        userDetailsService.afterLoginSuccess(request, response, user);

        /*
         * 권한에 따라, Default target url을 바꿀 필요가 있다면, DefaultTargetUrl을 변경해 준다.
         */
    	String targetUrl = envConfig.getUrlAfterLogin();
    	if (string.isBlank(targetUrl)) {
    		targetUrl = "/";
    	}
        this.setDefaultTargetUrl(targetUrl);

        /*
         * 만일, 무조건 defaultTargetUrl로 이동해야 한다면, 아래와 같이 설정한다.
         * 예) 비밀번호 갱신 등 ... 그런데..., 이동하려는 주소가 있었을 경우에는 그리로 보내는 것이 맞을 듯...
         * this.setAlwaysUseDefaultTargetUrl(true);
         * this.setDefaultTargetUrl(targetUrl);
         */
        //
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void writeLog(HttpServletRequest request, SecMbr user) {
    	//TODO (유영민) 로그인로그 보강
        if (logUtils == null) {
            return;
        }

        LoginLog log = new LoginLog();

        log.setLogDt(new Date());
        log.setSystemId(envConfig.getSystemId());
        log.setMberIp(CoreUtils.webutils.getRemoteIp(request));

        log.setMberId(user.getMbrId());
        log.setLoginId(user.getLgnId());
        log.setMberNm(user.getMbrNm());
        log.setMberType(user.getMbrType());

        logUtils.saveLoginLog(log);
    }

}
