package com.stdev.futurebox.controller;

import com.stdev.futurebox.service.GcpStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final GcpStorageService storageService;

    @GetMapping
    public String list(@RequestParam(required = false) String path, Model model) {
        Map<String, List<GcpStorageService.FileItem>> items = storageService.listFilesAndDirectories(path);
        model.addAttribute("items", items.get("items"));
        model.addAttribute("currentPath", path);
        model.addAttribute("parentPath", getParentPath(path));
        return "storage/list";
    }

    private String getParentPath(String currentPath) {
        if (currentPath == null || currentPath.isEmpty()) {
            return null;
        }
        int lastSlash = currentPath.lastIndexOf('/');
        return lastSlash < 0 ? "" : currentPath.substring(0, lastSlash);
    }

    @PostMapping("/directory")
    @ResponseBody
    public ResponseEntity<?> createDirectory(@RequestParam String directoryName) {
        try {
            storageService.createDirectory(directoryName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/directory/{directoryPath}")
    @ResponseBody
    public ResponseEntity<?> deleteDirectory(@PathVariable String directoryPath) {
        try {
            storageService.deleteDirectory(directoryPath);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("files") List<MultipartFile> files,
                            @RequestParam(required = false) String currentPath,
                            RedirectAttributes redirectAttributes) {
        try {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fullPath = currentPath != null ? 
                        currentPath + "/" + file.getOriginalFilename() : 
                        file.getOriginalFilename();
                    String fileUrl = storageService.uploadFile(file, fullPath);
                }
            }
            redirectAttributes.addFlashAttribute("message", "파일들이 성공적으로 업로드되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "파일 업로드에 실패했습니다: " + e.getMessage());
        }
        return "redirect:/storage" + (currentPath != null ? "?path=" + currentPath : "");
    }

    @PostMapping("/delete/{fileName}/**")
    public String deleteFile(@PathVariable String fileName, 
                            HttpServletRequest request,
                            RedirectAttributes redirectAttributes) {
        try {
            String uri = request.getRequestURI();
            String fullPath = uri.substring(uri.indexOf(fileName));
            
            // 현재 디렉토리 경로 추출
            String currentPath = fullPath.substring(0, fullPath.lastIndexOf('/'));
            if (currentPath.isEmpty()) {
                currentPath = null;
            }
            
            storageService.deleteFile(fullPath);
            redirectAttributes.addFlashAttribute("message", "파일이 성공적으로 삭제되었습니다.");
            
            // 현재 경로로 리다이렉트
            return "redirect:/storage" + (currentPath != null ? "?path=" + currentPath : "");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "파일 삭제에 실패했습니다: " + e.getMessage());
            return "redirect:/storage";
        }
    }

    @PostMapping("/delete-multiple")
    public ResponseEntity<String> deleteMultiple(@RequestBody List<DeleteRequest> items) {
        try {
            for (DeleteRequest item : items) {
                if (item.getType().equals("directory")) {
                    storageService.deleteDirectory(item.getPath());
                } else {
                    storageService.deleteFile(item.getPath());
                }
            }
            return ResponseEntity.ok("성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteRequest {
        private String path;
        private String type;
    }
} 