package com.aboutdk.note;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhouyang
 */
@SpringBootApplication
@MapperScan("com.aboutdk.note.mapper")
@Controller
public class NoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteApplication.class, args);
	}

	@GetMapping(path = "/")
	public void homeSite(HttpServletResponse response) throws IOException {
		response.sendRedirect("/pc/index.html");
	}

	@GetMapping(path = "/pc")
	public void pcSite(HttpServletResponse response) throws IOException {
		response.sendRedirect("/pc/index.html");
	}

	@GetMapping("/mobile")
	public void mobileSite(HttpServletResponse response) throws IOException {
		response.sendRedirect("/mobile/index.html");
	}
}
