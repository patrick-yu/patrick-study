package patrick.study.config.exception;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import mframework.common.util.SecurityUtils;
import mframework.common.vo.SecMbr;
import mframework.config.EnvConfig;
import mframework.log.LogUtils;
import mframework.log.vo.ErrorLog;
import mlibrary.exception.CommunicationException;
import mlibrary.exception.DoubleLoginException;
import mlibrary.exception.ExceptionMessage;
import mlibrary.exception.ForbiddenException;
import mlibrary.exception.InvalidationException;
import mlibrary.exception.InvalidationsException;
import mlibrary.exception.LoggableException;
import mlibrary.exception.UnauthorizedException;
import mlibrary.util.CoreUtils;
import lombok.extern.slf4j.Slf4j;
import patrick.study.config.exception.vo.ErrorResponse;
import patrick.study.config.exception.vo.ErrorResponseItem;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	private static final String JSON_VIEW_NAME = "jsonView";
	private static final String PAGE_VIEW_NAME = "error/default";

	private static final Class<?>[] EXCEPT_THROWABLES = {
	        InvalidationException.class,
	        InvalidationsException.class,
	        SocketException.class,
	        DoubleLoginException.class,
	        HttpRequestMethodNotSupportedException.class,
	        UnauthorizedException.class,
	        ForbiddenException.class,
	        BadCredentialsException.class,
	        ClientAbortException.class
	    };

	@Autowired
	private EnvConfig config;

	@Autowired
	private LogUtils logUtils;

	private ModelAndView sendError(ErrorResponse error, HttpStatus status, HttpServletRequest request) {
		boolean ajax = CoreUtils.webutils.isAjax(request);

		ModelAndView mv = new ModelAndView();
		if (ajax) {
			mv.setViewName(JSON_VIEW_NAME);
			mv.addObject(error);
		}
		else {
			mv.setViewName(PAGE_VIEW_NAME);
			mv.addObject("error", error);
		}

		mv.setStatus(status);
		return mv;
	}

	@ExceptionHandler(LoggableException.class)
	public ModelAndView handleLoggableException(LoggableException e, HttpServletRequest request) {
		writeLog(request, e);
		final ErrorResponse error = ErrorResponse.builder()
				.error("Exception")
				.status(e.getExceptionMessage().getStatus())
				.message(e.getMessage())
				.errors(new ArrayList<ErrorResponseItem>())
				.build();

		return sendError(error, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler(InvalidationException.class)
	public ModelAndView handleInvalidationException(InvalidationException e, HttpServletRequest request) {
		writeLog(request, e);
		final ErrorResponse error = ErrorResponse.builder()
				.error("Invalid")
				.status(e.getExceptionMessage().getStatus())
				.message(e.getMessage())
				.errors(new ArrayList<ErrorResponseItem>())
				.build();

		return sendError(error, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(InvalidationsException.class)
	public ModelAndView handleInvalidationsException(InvalidationsException e, HttpServletRequest request) {
		writeLog(request, e);

		List<ErrorResponseItem> list = new ArrayList<>();
		for (ExceptionMessage msg : e.getExceptionMessages()) {
			ErrorResponseItem item = ErrorResponseItem.builder()
					.field(msg.getField())
					.message(msg.getMessage())
					.build();
			list.add(item);
		}

		final ErrorResponse error = ErrorResponse.builder()
				.error("Invalid")
				.status(HttpStatus.BAD_REQUEST.value())
				.message("errors")
				.errors(list)
				.build();

		return sendError(error, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(CommunicationException.class)
	public ModelAndView handleBatchException(CommunicationException e, HttpServletRequest request) {
		writeLog(request, e);

		final ErrorResponse error = ErrorResponse.builder()
				.error("Invalid")
				.status(HttpStatus.BAD_REQUEST.value())
				.message(e.getMessage())
				.errors(new ArrayList<ErrorResponseItem>())
				.build();

		return sendError(error, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ModelAndView handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
		final ErrorResponse error = ErrorResponse.builder()
				.error("Invalid")
				.status(HttpStatus.NOT_FOUND.value())
				.message("지원하지 않는 기능입니다.")
				.errors(new ArrayList<ErrorResponseItem>())
				.build();

		return sendError(error, HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ModelAndView handleAuthenticationException(UnauthorizedException e, HttpServletRequest request) {
		writeLog(request, e);

		final ErrorResponse error = ErrorResponse.builder()
				.error("Unauthorized")
				.status(HttpStatus.UNAUTHORIZED.value())
				.message("로그인을 하세요.")
				.errors(new ArrayList<>())
				.build();

		return sendError(error, HttpStatus.UNAUTHORIZED, request);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ModelAndView handleForbiddenException(ForbiddenException e, HttpServletRequest request) {
		writeLog(request, e);

		final ErrorResponse error = ErrorResponse.builder()
				.error("Forbidden")
				.status(HttpStatus.FORBIDDEN.value())
				.message("권한이 없습니다.")
				.errors(new ArrayList<>())
				.build();

		return sendError(error, HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ModelAndView handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request) {
		writeLog(request, e);

		final ErrorResponse error = ErrorResponse.builder()
				.error("Invalid")
				.status(HttpStatus.UNAUTHORIZED.value())
				.message("아이디 또는 비밀번호가 올바르지 않습니다.")
				.errors(new ArrayList<>())
				.build();

		return sendError(error, HttpStatus.UNAUTHORIZED, request);
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView exception(Exception e, HttpServletRequest request) throws IOException {
		writeLog(request, e);

		final ErrorResponse error = ErrorResponse.builder()
				.error("Exception")
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message("시스템 오류로 작업을 중단하였습니다.")
				.errors(new ArrayList<>())
				.build();

		return sendError(error, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

    private void writeLog(HttpServletRequest request, Exception e) {
        String requestURI = request.getRequestURI();

        String errorTrace = CoreUtils.exception.getStackTraceString(e);
        log.error(errorTrace);

        if (logUtils == null) {
            return;
        }
        for (Class<?> cls: EXCEPT_THROWABLES) {
            if (cls.isAssignableFrom(e.getClass()) ) {
                return;
            }
        }

        ErrorLog log = new ErrorLog();
        log.setSystemId(config.getSystemId());
        log.setLogDt(new Date());
        log.setMberIp(CoreUtils.webutils.getRemoteIp(request));
        log.setErrorCode(e.getClass().getName());
        log.setErrorMsg(errorTrace);
        log.setUrl(requestURI);
        SecMbr user = SecurityUtils.getCurrentMember();
        if (user == null) {
        	user = SecurityUtils.getAnonymousUser();
        }
        log.setMberId(user.getMbrId());
        log.setLoginId(user.getLgnId());
        log.setMberNm(user.getMbrNm());
        log.setMberType(user.getMbrType());
//        log.setGender(user.getGender());
//        log.setAge(CoreUtils.date.getAge(user.getBirthday()));
//        log.setPositionNm(user.getPositionNm());
//        log.setDeptNm(user.getDeptNm());
//        log.setPstinstNm(user.getPstinstNm());

        logUtils.saveErrorLog(log);
    }

}
