package ru.project.SweetBot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.project.SweetBot.FileService;
import ru.project.SweetBot.bd.entity.AppDocument;
import ru.project.SweetBot.bd.entity.Users;
import ru.project.SweetBot.bd.services.UsersManagementService;
import ru.project.SweetBot.config.BotConfig;
import ru.project.SweetBot.exeption.UploadFileException;
import ru.project.SweetBot.services.MessageProcessing;

import java.io.IOException;
import java.sql.SQLException;

@Component
public class TelegramSweetBot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(TelegramSweetBot.class);
    String previousOperation;
    private final BotConfig botConfig;
    private final MessageProcessing messageProcessing;
    private final UsersManagementService usersServices;
    private final FileService fileService;

    @Autowired
    public TelegramSweetBot(BotConfig botConfig, MessageProcessing messageProcessing, UsersManagementService usersServices, FileService fileService) {
        this.botConfig = botConfig;
        this.messageProcessing = messageProcessing;

        this.usersServices = usersServices;
        this.fileService = fileService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        //  Document document = update.getMessage().getDocument();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    messageProcessing.startCommandReceived(chatId, update.getMessage().getChat().getFirstName(), this);
                    break;

                case "/play":
                    messageProcessing.playMessage(chatId, this);
                    previousOperation = "play";
                    messageProcessing.setWaitingForNumber(chatId, true);
                    break;
                case "/docks":
                      try {
                          AppDocument doc = fileService.processDocument(update.getMessage());
                             String answer = "Документ успешно загружен \n" +
                                     "Ссылка для скачивания: svsvsvs ";
                             sendMessage(chatId,answer);
                      }catch (UploadFileException e){
                         log.error(e.getMessage());
                         String error = "К сожалению файл не был загружен. Повторите попытку позже.";
                         sendMessage(chatId,error);
                      }

//                    if (update.hasMessage() && update.getMessage().hasDocument()) {
//                        Document document = update.getMessage().getDocument();
//
//                        if (document != null) {
//
//                            try {
//                                messageProcessing.getDocks(chatId, this, document.getFileName(), document.getFileId());
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                            // продолжить обработку файла
//                        } else {
//                            sendMessage(chatId, "Документ не загружен, попробуйте еще раз.");
//                        }
//                    } else {
//                        sendMessage(chatId, "Пожалуйста, отправьте документ.");
//                    }


//                        try {
//                            update.getMessage().hasDocument();
//                            messageProcessing.getDocks(chatId, this, document.getFileName(), document.getFileId());
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }

                    break;

                default:

                    if (previousOperation.equals("play") && messageProcessing.isWaitingForNumber(chatId)) {
                        messageProcessing.welcomeMessage(chatId, this);
                        previousOperation = "welcome";

                        try {
                            messageProcessing.handleUserDataInput(chatId, messageText, update.getMessage().getChat().getUserName());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    } else if (messageText.matches("\\d+") && previousOperation.equals("welcome")) {
                        int number = Integer.parseInt(messageText);
                        Users user = usersServices.getUser(update.getMessage().getChat().getId());
                        messageProcessing.processUserNumberInput(chatId, number, this);
                        try {
                            messageProcessing.handNumberInput(chatId, number, this, user);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        messageProcessing.endMessage(chatId, this);
                        messageProcessing.seyGetDocks(chatId, this);

                    }
                    break;
            }
        }
    }


    public void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();

    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();

    }

}
