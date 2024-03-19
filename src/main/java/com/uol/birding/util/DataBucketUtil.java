// referred https://medium.com/@raviyasas/spring-boot-file-upload-with-google-cloud-storage-5445ed91f5bc
package com.uol.birding.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.uol.birding.entity.ImageFile;
import io.micrometer.common.util.StringUtils;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DataBucketUtil {

    private String gcpConfigFile = "turing-lyceum-289114-ecd49e9babb4.json";
    private String gcpProjectId = "turing-lyceum-289114";
    private String gcpDirectoryName = "forum";
    private String gcpBucketId = "birding-image-bucket";

    public ImageFile uploadImage(MultipartFile multipartFile, String fileName, String contentType) {
        try {
            byte[] file = FileUtils.readFileToByteArray(convertFile(multipartFile));
            InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();
            StorageOptions options = StorageOptions
                    .newBuilder()
                    .setProjectId(gcpProjectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            Storage storage = options.getService();
            Bucket bucket = storage.get(gcpBucketId, Storage.BucketGetOption.fields());

            RandomString id = new RandomString(6, ThreadLocalRandom.current());
            Blob bucketBlob = bucket.create(String.format("%s/%s-%s", gcpDirectoryName, fileName, id.nextString()+confirmFileType(fileName)), file, contentType);
            String urlRef = String.format("/%s/%s",bucketBlob.getBucket(), bucketBlob.getName());
            if(bucketBlob!=null) return new ImageFile(bucketBlob.getName(), urlRef);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while uploading");
        }
        return null;
    }

    private File convertFile(MultipartFile file) {
        try {
            if(file.getOriginalFilename() == null) throw new Exception("File name cannot be null");
            File converted = new File(file.getOriginalFilename());
            FileOutputStream fileOutputStream = new FileOutputStream(converted);
            fileOutputStream.write(file.getBytes());
            return converted;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while converting file.");
        }
    }

    private String confirmFileType(String fileName) {
        if(fileName!=null && fileName.contains(".")) {
            List<String> permittedTypes = Arrays.asList(".png", ".jpg", ".webp", ".jpeg");

            String extension = permittedTypes.stream().filter(type -> fileName.endsWith(type)).findFirst().orElse(null);
            if(StringUtils.isNotEmpty(extension)) {
                return extension;
            }
        }
        throw new RuntimeException("Invalid file type");
    }
}
