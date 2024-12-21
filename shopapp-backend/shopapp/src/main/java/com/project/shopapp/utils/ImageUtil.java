package com.project.shopapp.utils;

import com.project.shopapp.exceptions.AppException;
import com.project.shopapp.exceptions.ErrorCode;
import com.project.shopapp.respone.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

public class ImageUtil {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String FILE_TOO_LARGE = "File is too large! Maximum size is 10MB";
    private static final String INVALID_FILE_TYPE = "File must be an image";
    public static  String storeFile(MultipartFile file) throws IOException {
        //Lấy tên file
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm UUID vào trước tên file để đảm bảo file là duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        // Đường dẫn đến thư mục bạn muốn lưu file
        Path uploadDir = Paths.get("uploadDir");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    public static void checkImage(MultipartFile file){
        String contentType = file.getContentType();
        if (contentType == null) {
           throw new AppException(ErrorCode.MISSING_FORMATTED_DATA);
        }
    }
    public static void deleteImage(String imageName) throws IOException {
            Path filePath = Paths.get("uploadDir",imageName);
            Files.deleteIfExists(filePath);
    }
}
