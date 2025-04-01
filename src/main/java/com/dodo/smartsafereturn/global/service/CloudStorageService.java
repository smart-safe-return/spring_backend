package com.dodo.smartsafereturn.global.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudStorageService {

    private final Storage storage;

    @Value("${google.cloud.storage.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) {
        try {
            // 원본 파일명에서 확장자 추출
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";

            // UUID로 고유한 파일명 생성
            String fileName = UUID.randomUUID() + extension;

            // 업로드 경로 설정
            String filePath = "member/profile/" + fileName;

            // BlobInfo 객체 생성
            BlobId blobId = BlobId.of(bucketName, filePath);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            // 파일 업로드
            storage.create(blobInfo, file.getBytes());

            // 파일의 공개 URL 반환
            return "https://storage.googleapis.com/" + bucketName + "/" + filePath;
        } catch (IOException e) {
            log.error("파일 업로드 실패: ", e);
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            // URL 에서 경로 추출
            String filePath = fileUrl.replace("https://storage.googleapis.com/" + bucketName + "/", "");

            // 파일 삭제
            storage.delete(BlobId.of(bucketName, filePath));
            log.info("파일 삭제 완료: {}", filePath);
        } catch (Exception e) {
            log.error("파일 삭제 실패: ", e);
            throw new RuntimeException("파일 삭제에 실패했습니다.", e);
        }
    }

}
