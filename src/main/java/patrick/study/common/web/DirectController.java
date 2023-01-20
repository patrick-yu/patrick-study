package patrick.study.common.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import mlibrary.util.CoreUtils.string;

@Controller
public class DirectController {

	@GetMapping("/html/**")
	public String direct(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String uri = request.getRequestURI();
		if (string.isNotBlank(contextPath)) {
			uri = uri.substring(string.length(contextPath));
		}
		uri = string.substring(uri, 1);
		return uri;
	}
}
