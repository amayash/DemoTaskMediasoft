package com.mediasoft.warehouse.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.mediasoft.warehouse.configuration.properties.AwsConfigurationProperties;
import com.mediasoft.warehouse.model.ProductImage;
import com.mediasoft.warehouse.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final AmazonS3 s3Client;
    private final AwsConfigurationProperties awsConfigurationProperties;
    private final ProductImageRepository productImageRepository;
    private final ProductService productService;

    /**
     * Загружает файл в S3 и сохраняет соответствующие метаданные в БД.
     *
     * @param file      файл для загрузки
     * @param productId идентификатор товара
     * @return S3 ключ загруженного файла
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Transactional
    public String uploadFile(MultipartFile file, UUID productId) throws IOException {
        Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
        UUID key = UUID.randomUUID();
        try {
            file.transferTo(tempFile.toFile());
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("filename", file.getOriginalFilename());
            PutObjectRequest request = new PutObjectRequest(
                    awsConfigurationProperties.getBucketName(),
                    key.toString(),
                    tempFile.toFile()
            ).withMetadata(metadata);
            s3Client.putObject(request);

            productService.getProductById(productId);
            ProductImage productImage = new ProductImage(key, productId);
            productImageRepository.save(productImage);
        } finally {
            Files.delete(tempFile);
        }
        return key.toString();
    }

    /**
     * Загружает все файлы, связанные с товаром, в виде ZIP-архива.
     *
     * @param productId    идентификатор товара
     * @param outputStream поток вывода для записи ZIP-архива
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Transactional(readOnly = true)
    public void downloadFiles(UUID productId, OutputStream outputStream) throws IOException {
        List<ProductImage> images = productImageRepository.findByProductId(productId);
        Set<String> usedFileNames = new HashSet<>();

        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
            for (ProductImage image : images) {
                UUID key = image.getS3_key();
                S3Object s3Object = s3Client.getObject(new GetObjectRequest(awsConfigurationProperties.getBucketName(), key.toString()));

                try (InputStream inputStream = s3Object.getObjectContent()) {
                    String fileName = s3Object.getObjectMetadata().getUserMetaDataOf("filename");
                    if (fileName == null) {
                        fileName = key.toString();
                    }

                    fileName = getUniqueFileName(fileName, usedFileNames);

                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();
                }
            }
        }
    }

    /**
     * Генерирует уникальное имя файла, добавляя число, если такое имя уже существует в наборе.
     *
     * @param fileName       исходное имя файла
     * @param usedFileNames  набор уже использованных имен файлов
     * @return уникальное имя файла
     */
    private String getUniqueFileName(String fileName, Set<String> usedFileNames) {
        String uniqueFileName = fileName;
        int counter = 1;

        while (usedFileNames.contains(uniqueFileName)) {
            int extensionIndex = fileName.lastIndexOf('.');
            if (extensionIndex == -1) {
                uniqueFileName = fileName + " (" + counter + ")";
            } else {
                uniqueFileName = fileName.substring(0, extensionIndex) + " (" + counter + ")" + fileName.substring(extensionIndex);
            }
            counter++;
        }

        usedFileNames.add(uniqueFileName);
        return uniqueFileName;
    }
}
