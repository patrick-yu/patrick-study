package patrick.study.svc.login.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/svc/login")
public class LoginController {
	@GetMapping("/index.do")
	public String index(Model model) {
		model.addAttribute("_title", "로그인");
		return "svc/login/index";
	}
}
