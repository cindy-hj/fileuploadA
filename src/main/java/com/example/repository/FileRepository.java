package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.FileDto;

public interface FileRepository extends JpaRepository<FileDto, Long> {

	
}
