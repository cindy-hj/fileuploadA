package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "filedto")
public class FileDto {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String uuid;
	private String fileName;
	private String contentType;
	
	public FileDto() {}
	
	public FileDto(String uuid, String fileName, String contentType) {
		this.uuid = uuid;
		this.fileName = fileName;
		this.contentType = contentType;
		System.out.println(contentType);
	}
}
