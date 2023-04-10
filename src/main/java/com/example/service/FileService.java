package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.FileDto;
import com.example.repository.FileRepository;


@Service
public class FileService {

	@Autowired
	private FileRepository fileRepository;

	public void save(FileDto dto) {
		fileRepository.save(dto);
	}

	public List<FileDto> findAll() {
		return fileRepository.findAll();
	}
	
	
}
