package ru.project.SweetBot.services.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.project.SweetBot.FileService;
import ru.project.SweetBot.bd.entity.AppDocument;
import ru.project.SweetBot.bd.entity.BinaryContent;
import ru.project.SweetBot.bd.repository.AppDocumentRepository;
import ru.project.SweetBot.bd.repository.BinaryContentRepository;
import ru.project.SweetBot.exeption.UploadFileException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


@Service
public class FileServicesImpl implements FileService {

    @Value("${bot.token}")
    private String token;

    @Value("${service.file_info.uri}")
    private String fileInfoUri;

    @Value("${service.file_storage.uri}")
    private String fileStorageUri;

    private final AppDocumentRepository appDocumentRepository;
    private final BinaryContentRepository binaryContentRepository;

    public FileServicesImpl(AppDocumentRepository appDocumentRepository, BinaryContentRepository binaryContentRepository) {
        this.appDocumentRepository = appDocumentRepository;
        this.binaryContentRepository = binaryContentRepository;
    }


    @Override
    public AppDocument processDocument(Message telegramMessage) {
        String field = telegramMessage.getDocument().getFileId();
        ResponseEntity<String> response = getFilePath(field);
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            String filePath = String.valueOf(jsonObject
                    .getJSONObject("result")
                    .getString("file_path"));
            byte[] fileInByte = downLoadFile(filePath);
            BinaryContent transientBinaryContent = BinaryContent.builder()
                    .fileArrayOfBytes(fileInByte)
                    .build();
            BinaryContent persistentBinaryContent = binaryContentRepository.save(transientBinaryContent);
            Document telegramDoc = telegramMessage.getDocument();
            AppDocument appDocument = buildTransientAppDoc(telegramDoc, persistentBinaryContent);
           return appDocumentRepository.save(appDocument);

        }else
           throw  new UploadFileException("Файл не загружен!");
    }

    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                  token,fileId
        );
    }

    private byte[] downLoadFile(String filePath) {
           String fullUri = fileStorageUri.replace("{bot.token}", token)
                   .replace("{filePath}",filePath);
        URL urlObj = null;
        try {
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new UploadFileException(e);
        }

        try (InputStream inputStream = urlObj.openStream()) {
               return  inputStream.readAllBytes();
        } catch (IOException e) {
            throw new UploadFileException(urlObj.toExternalForm(), e);
        }
    }

    private AppDocument buildTransientAppDoc(Document telegramDoc, BinaryContent persistentBinaryContent) {
        return AppDocument.builder()
                .telegramFileId(telegramDoc.getFileId())
                .docName(telegramDoc.getFileName())
                .binaryContent(persistentBinaryContent)
                .mimeType(telegramDoc.getMimeType())
                .fileSize(telegramDoc.getFileSize())
                .build();
    }
}
