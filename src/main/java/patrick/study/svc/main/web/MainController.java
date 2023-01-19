package patrick.study.svc.main.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mframework.common.vo.SecMbr;
import bnet.library.util.CoreUtils.string;
import patrick.study.svc.main.vo.Code;

@Controller
public class MainController {

	@GetMapping(value = {"/"})
	public String index(Model model) {
		return "redirect:/svc/login/index.do";
	}

	@GetMapping(value = {"/svc/main/index.do"})
	public String main(Model model) {
		return "svc/main/index";
	}

	@GetMapping(value = {"/svc/main/use-case.do"})
	public String useCase(Model model) {
		List<Code> codeList = new ArrayList<>();
		codeList.add(new Code("code1", "코드1"));
		codeList.add(new Code("code2", "코드2"));
		codeList.add(new Code("code3", "코드3"));
		model.addAttribute("codeList", codeList);

		SecMbr worker = SecMbr.builder()
				.mbrId(string.getNewId("mber-"))
				.lgnId("sampleloginid")
				.mbrNm("유영민")
				.build();
		model.addAttribute("worker", worker);

		return "svc/main/use-case";
	}

	@GetMapping("/svc/main/date.do")
	@ResponseBody
	public Date getDate() {
		Date now = new Date();
		return now;
	}
}
