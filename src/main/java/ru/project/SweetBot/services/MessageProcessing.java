package ru.project.SweetBot.services;


import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.telegram.telegrambots.meta.api.objects.Update;
import ru.project.SweetBot.bd.entity.BuyNumber;
import ru.project.SweetBot.bd.entity.Users;
import ru.project.SweetBot.bd.services.NumberServices;
import ru.project.SweetBot.bd.services.UsersManagementService;
import ru.project.SweetBot.bot.TelegramSweetBot;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

@Component
public class MessageProcessing extends ru.project.SweetBot.services.InputDate {


    private final Map<Long, Boolean> waitingForNumberMap;
    private final UsersManagementService usersManagementService;
    private final NumberServices numberServices;
    private final ru.project.SweetBot.services.CheckServices checkServices;
    private final ru.project.SweetBot.services.Randomizer randomizer;


    @Autowired
    public MessageProcessing(Map<Long, Boolean> waitingForNumberMap, UsersManagementService usersManagementService, NumberServices numberServices, CheckServices checkServices, Randomizer randomizer) {
        this.usersManagementService = usersManagementService;
        this.numberServices = numberServices;
        this.checkServices = checkServices;
        this.randomizer = randomizer;
        this.waitingForNumberMap = new HashMap<>();
    }

    List<BuyNumber> numberList = new ArrayList<>();

    public void startCommandReceived(Long chatId, String name, TelegramSweetBot bot) {

        String answer = "Привет, " + name + "! Мы рады видеть тебя!" + "\n" +
                "Предлагаем поучавствовать в нашей сладкой лотереи" + "\n" +
                "от \"Сладости для радости\"!" + "\n" +
                "Сегодня разыгрываем : " + sweets + "\n" +
                "С любовью Зоя Малофеева";

        bot.sendMessage(chatId, answer);
    }

    public void playMessage(Long chatId, TelegramSweetBot bot) {
        String data = "Теперь введите свои данные: имя, фамилию, телефон и email.";
        bot.sendMessage(chatId, data);

    }

    public void endMessage(Long chatId, TelegramSweetBot bot) {
        String endMessage = "Супер))! \n" +
                "Как только будут куплены все номера розыгрышь пройдет автоматически \n" +
                "и вам будет сообщен результат! Желаем удачи! \n" +
                "Разыгрываем вкуснейший " + sweets + "\n" +
                "Пожалуйста отправте  квитанцию, что бы мы могли отследить платеж) \n" +
                "Выберете в меню /docks";
        bot.sendMessage(chatId, endMessage);
        numberList = numberServices.getNumbers();
        if (numberList.size() == 20) {
            result = randomizer.startRandom(maxNumber);
            winsMessage(chatId, bot);
//            String response = " Выпало число : " + result;
//            bot.sendMessage(chatId, response);
        } else {
            String wait = "Пока куплено " + numberList.size() + " номерков! Ждите)!";
            bot.sendMessage(chatId, wait);
        }


    }

    public void processUserNumberInput(Long chatId, int number, TelegramSweetBot bot) {

        bot.sendMessage(chatId, "Вы выбрали число: " + number);
        waitingForNumberMap.put(chatId, false);
    }

    public void welcomeMessage(Long chatId, TelegramSweetBot bot) {
        String messageText = "Отлично! \n" +
                "Теперь выбери число от 1  до " + maxNumber + ")! \n";
        bot.sendMessage(chatId, messageText);

        waitingForNumberMap.put(chatId, true);
    }

//    public void handleUserInput(Update update, String messageText, Users user, TelegramSweetBot bot) {
//        if (isWaitingForNumber(update.getMessage().getChatId())) {
//            if (messageText.matches("\\d+")) {
//                int number = Integer.parseInt(messageText);
//
//                if (number >= 1 && number <= maxNumber) {
//                    handNumberInput(update.getMessage().getChatId(), number, bot);
//                    processUserNumberInput(update.getMessage().getChatId(), number, bot);
//                    endMessage(update.getMessage().getChatId(), bot);
//                } else {
//                    bot.sendMessage(update.getMessage().getChatId(), "Пожалуйста, введите число от 1 до " + maxNumber + ".");
//
//                }
//            } else {
//                bot.sendMessage(update.getMessage().getChatId(), "Пожалуйста, введите корректное число.");
//
//            }
//
//        }
//
//    }

    public void handleUserDataInput(Long chatId, String userData, String telegramName) throws SQLException {
        String[] userDataArray = userData.split(",");
        if (userDataArray.length == 4) {
            String firstName = userDataArray[0].trim();
            String lastName = userDataArray[1].trim();
            Long phoneNumber = Long.parseLong(userDataArray[2].trim());
            String email = userDataArray[3].trim();


            Users user = new Users(firstName, lastName, phoneNumber, email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);
            user.setEmail(email);
            user.setTelegramId(telegramName);

            usersManagementService.save(user);

           // usersManagementService.save(user);


        } else {

        }
    }

    public void handNumberInput(Long chatId, int number, TelegramSweetBot bot, Users user) throws SQLException {
        BuyNumber bayNumber = new BuyNumber();
        if (number >= 1 && number <= maxNumber) {
            bayNumber.setNumber(number);

            bayNumber.setDateTime(LocalDateTime.now());

            bayNumber.setUser(user);
            System.out.println(user);
            numberServices.save(bayNumber);

        } else {
            bot.sendMessage(chatId, "Введите значение не больше " + maxNumber + "\n" +
                    "Но возможно такое числу уже занято, выберете другое)). ");

        }

    }
    public  void  seyGetDocks(Long chatId, TelegramSweetBot bot) {
        String getMess = "Прикрепите пожалуйста квитанцию!";
        bot.sendMessage(chatId, getMess);

    }

//    public void getDocks(Long chatId, TelegramSweetBot bot, String fileName, String fileId) throws IOException {
//       // System.out.println("*******************************");
//
//         URL url = new URL("https://api.telegram.org/bot" + bot.getBotToken() + "/getFile?file_id=" + fileId);
//
//         BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//         String getFileInput = br.readLine();
//
//         JSONObject jsonResult = new JSONObject(getFileInput);
//         JSONObject jsonPath = jsonResult.getJSONObject("result");
//         String filePAth = jsonPath.getString("file_path");
//         System.out.println(filePAth);
//
//         File file = new File("src/main/resources/uploadFile/my_file" + fileName);
//         InputStream inputStream = new URL("https://api.telegram.org/file/bot" + bot.getBotToken() + "/" + filePAth).openStream();
//
//         FileUtils.copyInputStreamToFile(inputStream, file);
//         String outDocksMess = "Спасибо файл успешно отправлен!";
//         bot.sendMessage(chatId, outDocksMess);
//         waitingForNumberMap.put(chatId, false);
//
//         br.close();
//         inputStream.close();
//
//    }

    //    public String getWinning(TelegramSweetBot bot){
//         boolean result;
//         List<Integer> lengthNumbers = numberServices.getNumbers();
//        if (lengthNumbers.le )
//            return
//    }
    public void winsMessage(Long chatId, TelegramSweetBot bot) {
        String winsMess = "ПОЗДРАВЛЯЕМ!!!! Победителем стал номер : " + result + "В ближайшее время с вами свяжутся и расскажут как забрать ваш выигрыш!!! ";
        bot.sendMessage(chatId, winsMess);
    }


    public void setWaitingForNumber(Long chatId, boolean value) {
        waitingForNumberMap.put(chatId, value);
    }

    public boolean isWaitingForNumber(Long chatId) {
        return waitingForNumberMap.getOrDefault(chatId, false);
    }


}
