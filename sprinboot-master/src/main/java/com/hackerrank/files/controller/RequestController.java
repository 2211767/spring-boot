package com.hackerrank.files.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hackerrank.files.model.XFile;
import com.hackerrank.files.repository.XFileRepository;

@RestController
@RequestMapping()
public class RequestController {
	// define endpoints for download and upload
	@Autowired
	XFileRepository xFileRepository;

	@PostMapping("/uploader")
	public ResponseEntity<String> uploadFile(@RequestParam() MultipartFile files, @PathVariable String fileGroup)

	{

		XFile xlfile1 = xFileRepository.findByFileGroupAndFileName(fileGroup, files.getOriginalFilename());
		if (xlfile1.getFileName() != null && xlfile1.getFileGroup() != null) {
			XFile xlfine = new XFile();
			xlfine.setFileName(files.getOriginalFilename());
			xlfine.setFileGroup(fileGroup);
			xlfine.setFile(files.getContentType().getBytes());
			xFileRepository.save(xlfine);
		} else {
			xlfile1.setFileName(files.getOriginalFilename());
			xlfile1.setFileGroup(fileGroup);
			xlfile1.setFile(files.getContentType().getBytes());
			xFileRepository.save(xlfile1);
		}
		return new ResponseEntity<>("file uploaded", HttpStatus.OK);
	}

	@GetMapping("/downloader")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileGroup) {

		XFile xlfile1 = (XFile) xFileRepository.findByFileGroup(fileGroup);
		byte[] data = xlfile1.getFile();
		ByteArrayResource resource = new ByteArrayResource(data);
		return ResponseEntity.ok().contentLength(data.length).header("content-type", "application/octet-stream")
				.header("content-disposition", "attachment; filename\"" + fileGroup + "\"").body(resource);

	}
}
