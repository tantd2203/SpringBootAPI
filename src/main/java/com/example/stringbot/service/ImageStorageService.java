package com.example.stringbot.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements  IStorageService{

    private  final  Path storageFolder = Paths.get("uploads");

    // costructor
    // tạo thư mục để lưu image  chỉ tạo 1 lần
     public  ImageStorageService (){
         try {
             Files.createDirectories(storageFolder);

         }catch ( Exception e){
       throw  new RuntimeException("Cant intiaLize storage" , e);
         }
     }
      // Find File has a package
    private  boolean isImageFile( MultipartFile file){
         String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        // kiểm tra đuôi ảnh
         return Arrays.asList( new String[] {"png","jpa","jpg"})
                 .contains(fileExtension.trim().toLowerCase());
    }


    @Override
    public String storeFile(MultipartFile file) {
         try {
             System.out.println("heh");

              if (file.isEmpty()){
                  throw  new RuntimeException("Failed to store empty file");
              }
               // check file image?
              if (!isImageFile(file)){
                  throw  new RuntimeException(" You can only upload image file");
              }
              // file must be <= 5Mb

             float fileSizeMegabytes = file.getSize() / 1_000_000.0f;
              if (fileSizeMegabytes >5.0f){
                  throw  new RuntimeException(" file must be <5Mb");
              }

                 // file must be rename, why ?
                // create new name file when upload on sever
             String fileExtension  = FilenameUtils.getExtension(file.getOriginalFilename());
              String generatedFileName= UUID.randomUUID().toString().replace("-","");
              generatedFileName = generatedFileName +"."+fileExtension;
              Path destinationFilePath = this.storageFolder.resolve(
                      Paths.get(generatedFileName)).normalize().toAbsolutePath();


              try(InputStream inputStream = file.getInputStream()){
                  Files.copy(inputStream,destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

              }
             return  generatedFileName;

         }catch (IOException  e){
             throw  new RuntimeException(" Fileed to store file", e);
         }

    }

    @Override
    public Stream<Path> loadAll() {
        try {
            //list all files in storageFolder
            //How to fix this ?
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load stored files", e);
        }

    }

    @Override
    public byte[] readFileContent(String fileName) {

        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }
            else {
                throw new RuntimeException(
                        "Could not read file: " + fileName);
            }
        }
        catch (IOException exception) {
            throw new RuntimeException("Could not read file: " + fileName, exception);
        }
    }

    @Override
    public void deleteAllFiles() {

    }
}
