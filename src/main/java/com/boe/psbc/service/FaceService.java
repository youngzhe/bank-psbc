package com.boe.psbc.service;

import org.springframework.web.multipart.MultipartFile;

public interface FaceService {

    Object faceMatch(MultipartFile file) throws Exception;

    Object faceMatchTest(MultipartFile file) throws Exception;
}
