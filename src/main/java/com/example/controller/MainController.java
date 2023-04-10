package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.FileDto;
import com.example.service.FileService;

@RestController
public class MainController {

	@Autowired private FileService fileService;
	
	@PostMapping("/api/upload")
	public ResponseEntity upload(@RequestParam("uploadfile") MultipartFile[] uploadfile )throws IllegalStateException, IOException{
		List<FileDto> list = new ArrayList<>();
		
		for(MultipartFile file : uploadfile) {
			if(!file.isEmpty()) {
				FileDto dto = new FileDto(UUID.randomUUID().toString(),
						file.getOriginalFilename(), 
						file.getContentType());
				list.add(dto);
				
				File newFileName = new File(dto.getUuid()+"_"+dto.getFileName());
				file.transferTo(newFileName);
				fileService.save(dto);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/api/filelist")
	public ResponseEntity<List<FileDto>> filelist() {
		List<FileDto> filelist = fileService.findAll();
		
		return new ResponseEntity<List<FileDto>>(filelist, HttpStatus.OK);
	}
	
	@Value("${spring.servlet.multipart.location}")
	String filePath;
	
	@GetMapping("/api/download")
	public ResponseEntity<Resource> download(@ModelAttribute FileDto dto) throws IOException{
		System.out.println("디티오"+ dto);
		
		Path path = Paths.get(filePath + "/" + dto.getUuid() + "_" + dto.getFileName());
		String contentType = Files.probeContentType(path);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(dto.getFileName(),
				StandardCharsets.UTF_8).build());
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	@GetMapping("/api/display")
	public ResponseEntity<Resource> display(@ModelAttribute FileDto dto) throws IOException {
		String path = "C:\\Temp\\upload\\";
		String folder = "";
		
		Resource resource = new FileSystemResource(path + folder + dto.getUuid()+"_"+dto.getFileName());
		if(!resource.exists())
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		HttpHeaders header = new HttpHeaders();
		Path filePath = null;
		try {
			filePath = Paths.get(path + folder + dto.getUuid() + "_" + dto.getFileName());
			header.add("Content-type", Files.probeContentType(filePath));
		} catch(IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
}
