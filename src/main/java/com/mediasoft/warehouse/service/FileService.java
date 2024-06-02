package com.mediasoft.warehouse.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Интерфейс для сервиса управления файлами.
 */
public interface FileService {
    String uploadFile(MultipartFile file, UUID productId) throws IOException;
    void downloadFiles(UUID productId, OutputStream outputStream) throws IOException;
}
