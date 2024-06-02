package com.mediasoft.warehouse.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.mediasoft.warehouse.configuration.properties.AwsConfigurationProperties;
import com.mediasoft.warehouse.model.ProductImage;
import com.mediasoft.warehouse.model.ProductImageKey;
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
import java.util.List;
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
        String key = file.getOriginalFilename();
        try {
            file.transferTo(tempFile.toFile());
            PutObjectRequest request = new PutObjectRequest(awsConfigurationProperties.getBucketName(), key, tempFile.toFile());
            s3Client.putObject(request);

            productService.getProductById(productId);
            ProductImage productImage = new ProductImage(new ProductImageKey(key, productId));
            productImageRepository.save(productImage);
        } finally {
            Files.delete(tempFile);
        }
        return key;
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
        List<ProductImage> images = productImageRepository.findById_ProductId(productId);
        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
            for (ProductImage image : images) {
                String key = image.getId().getS3_key();
                S3Object s3Object = s3Client.getObject(new GetObjectRequest(awsConfigurationProperties.getBucketName(), key));

                try (InputStream inputStream = s3Object.getObjectContent()) {
                    ZipEntry zipEntry = new ZipEntry(key);
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
}