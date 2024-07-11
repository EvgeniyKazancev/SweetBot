//package ru.project.SweetBot.services;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//
//import java.io.File;
//import java.io.IOException;
//
//public class TelegramPhotoSender {
//    CloseableHttpClient httpClient = HttpClients.createDefault();
//File photoBill  =new File("");
//    HttpPost request = new HttpPost("https://api.telegram.org/6908613074:AAH51qmcEwzT5PfCfUG0OZbhhxjk4shD95c/sendPhoto");
//
//    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.addTextBody("chat_id", "YourChatID");
//        builder.add("photo", new  File(""), ContentTa, "photo.jpg");
//
//    HttpEntity multipart = builder.build();
//        request.setEntity(multipart);
//
//        try {
//        HttpResponse response = null;
//        try {
//            response = httpClient.execute(request);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//        // Обработка ответа от сервера
//        System.out.println(response.getStatusLine());
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//}
