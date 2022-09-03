package com.example.stringbot.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageService {
    public   String storeFile(MultipartFile file);
    public Stream<Path> loadAll();
    // đường dần để lưu tất cả các ảnh
    public  byte[] readFileContent(String fileName);

    public  void deleteAllFiles();

}
