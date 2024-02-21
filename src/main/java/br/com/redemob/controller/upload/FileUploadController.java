package br.com.redemob.controller.upload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.service.security.SegUsuarioService;

@RestController
public class FileUploadController {

	@Autowired
	private SegUsuarioService userService;

	public static String uploadDirectory = System.getProperty("user.dir")+"/uploadPath";

  @GetMapping("/upload")
  public ModelAndView UploadPage(SegUsuario user) {
	ModelAndView modelAndView = new ModelAndView("upload/uploadFiles");
	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	  user = userService.findUserByEmail(auth.getName());
	  modelAndView.addObject("segUsuario", user);

  	return modelAndView;
  }


  @PostMapping("/upload")
  public ModelAndView upload(Model model, @RequestParam("files") MultipartFile[] files) {

	  ModelAndView modelAndView = new ModelAndView("upload/uploadFiles");
	  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	  SegUsuario user = userService.findUserByEmail(auth.getName());
	  modelAndView.addObject("segUsuario", user);

	  StringBuilder fileNames = new StringBuilder();

	  System.out.println(uploadDirectory);

	  for (MultipartFile file : files) {
		  Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
		  fileNames.append(file.getOriginalFilename()+" ");
		  try {
			Files.write(fileNameAndPath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }

	 modelAndView.addObject("mensagemRetorno", "Successfully uploaded files "+fileNames.toString() + uploadDirectory);

	  return modelAndView;
  }
  
  
}
