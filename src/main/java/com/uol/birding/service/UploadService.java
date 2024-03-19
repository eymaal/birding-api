// referred https://medium.com/@raviyasas/spring-boot-file-upload-with-google-cloud-storage-5445ed91f5bc
package com.uol.birding.service;

import com.uol.birding.entity.ImageFile;
import com.uol.birding.repository.ImageFileRepository;
import com.uol.birding.util.BirdingMessageUtils;
import com.uol.birding.util.DataBucketUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {


    private final ImageFileRepository imageFileRepository;
    private final DataBucketUtil dataBucketUtil;

    public ResponseEntity uploadFiles(MultipartFile[] files) {
        try {
            List<ImageFile> imageFiles = new ArrayList<>();

            Arrays.asList(files)
                    .forEach((file) -> {
                        String imageName = file.getOriginalFilename();
                        if (imageName == null) throw new RuntimeException("Image name is null");
                        Path path = new File(imageName).toPath();
                        try {
                            String contentType = Files.probeContentType(path);
                            ImageFile image = dataBucketUtil.uploadImage(file, imageName, contentType);

                            if (image != null) imageFiles.add(image);
                        } catch (Exception e) {
                            throw new RuntimeException("Error occurred while uploading");
                        }
                    });
            imageFileRepository.saveAll(imageFiles);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(imageFiles);
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }
}
