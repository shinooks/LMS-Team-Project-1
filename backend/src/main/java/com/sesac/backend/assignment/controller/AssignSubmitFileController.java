//package com.sesac.backend.assignment.controller;
//
//import com.sesac.backend.assignment.dto.AssignSubmitDto;
//import com.sesac.backend.assignment.service.AssignSubmitService;
//import com.sesac.backend.global.service.S3Service;
//import java.net.URL;
//import java.util.Map;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/assignment/files")
//public class AssignSubmitFileController {
//
//    private final S3Service s3Service;
//    private final AssignSubmitService assignSubmitService;
//
//    @Autowired
//    public AssignSubmitFileController(S3Service s3Service, AssignSubmitService assignSubmitService) {
//        this.s3Service = s3Service;
//        this.assignSubmitService = assignSubmitService;
//    }
//
//    @GetMapping("/upload-url")
//    public Map<String, String> getUploadUrl(String fileName) {
//        URL uploadUrl = s3Service.generateUploadPresignedUrl(fileName);
//        return Map.of("url", uploadUrl.toString());
//    }
//
//
//
//    @GetMapping("/download-url")
//    public Map<String, String> getDownloadUrl(String fileName) {
//        URL downloadUrl = s3Service.generateDownloadPresignedUrl(fileName);
//        return Map.of("url", downloadUrl.toString());
//    }
//}
