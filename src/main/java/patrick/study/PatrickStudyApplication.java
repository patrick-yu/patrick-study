package patrick.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"patrick", "bnet", "mframework", "egovframework"})
@ServletComponentScan(basePackages = {"patrick", "bnet", "mframework"})
public class PatrickStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatrickStudyApplication.class, args);
	}

}
