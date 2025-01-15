package com.stdev.futurebox.service;

import com.google.api.gax.paging.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class GcpStorageService {

    private final Storage storage;

    private final String assetPrefixPath = "";
    
    @Value("${gcp.storage.bucket-name}")
    private String bucketName;

    // 파일 업로드
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            BlobId blobId = BlobId.of(bucketName, assetPrefixPath + fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

            Blob blob = storage.create(blobInfo, file.getBytes());
            return blob.getMediaLink();
            
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new RuntimeException("파일 업로드에 실패했습니다.");
        }
    }

    // 파일 다운로드
    public byte[] downloadFile(String fileName) {
        try {
            Blob blob = storage.get(BlobId.of(bucketName, assetPrefixPath + fileName));
            return blob.getContent();
        } catch (StorageException e) {
            log.error("파일 다운로드 실패: {}", e.getMessage());
            throw new RuntimeException("파일을 찾을 수 없습니다.");
        }
    }

    // 파일 삭제
    public void deleteFile(String fileName) {
        try {
            storage.delete(BlobId.of(bucketName, assetPrefixPath + fileName));
        } catch (StorageException e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            throw new RuntimeException("파일 삭제에 실패했습니다.");
        }
    }

    // 디렉토리 생성
    public void createDirectory(String directoryName) {
        try {
            String fullPath = assetPrefixPath + directoryName + "/";
            BlobId blobId = BlobId.of(bucketName, fullPath);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("application/x-directory")
                .build();
            storage.create(blobInfo, new byte[0]);
        } catch (StorageException e) {
            log.error("디렉토리 생성 실패: {}", e.getMessage());
            throw new RuntimeException("디렉토리 생성에 실패했습니다.");
        }
    }

    // 디렉토리 삭제 (재귀적으로 내부 파일도 삭제)
    public void deleteDirectory(String directoryPath) {
        try {
            String prefix = assetPrefixPath + directoryPath + "/";
            Page<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(prefix));
            for (Blob blob : blobs.iterateAll()) {
                blob.delete();
            }
        } catch (StorageException e) {
            log.error("디렉토리 삭제 실패: {}", e.getMessage());
            throw new RuntimeException("디렉토리 삭제에 실패했습니다.");
        }
    }

    // 파일/디렉토리 목록 조회 (계층 구조 포함)
    public Map<String, List<FileItem>> listFilesAndDirectories(String currentPath) {
        Map<String, List<FileItem>> result = new HashMap<>();
        List<FileItem> files = new ArrayList<>();
        Set<String> directories = new HashSet<>();
        
        String prefix = currentPath == null ? assetPrefixPath : assetPrefixPath + currentPath + "/";
        Page<Blob> blobs = storage.list(bucketName, Storage.BlobListOption.prefix(prefix));
        
        for (Blob blob : blobs.iterateAll()) {
            String relativePath = blob.getName().replace(prefix, "");
            if (relativePath.isEmpty()) continue;
            
            int firstSlash = relativePath.indexOf('/');
            if (firstSlash > 0) {
                // 하위 디렉토리
                String dirName = relativePath.substring(0, firstSlash);
                directories.add(dirName);
            } else {
                // 파일
                String publicUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, blob.getName());
                files.add(new FileItem(relativePath, publicUrl, false));
            }
        }
        
        // 디렉토리 항목 추가
        for (String dir : directories) {
            files.add(new FileItem(dir, "", true));
        }
        
        result.put("items", files);
        result.put("path", Collections.singletonList(new FileItem(currentPath == null ? "" : currentPath, "", true)));
        return result;
    }

    // 파일 아이템 DTO
    @Data
    @AllArgsConstructor
    public static class FileItem {
        private String name;
        private String publicUrl;
        private boolean isDirectory;
    }
} 