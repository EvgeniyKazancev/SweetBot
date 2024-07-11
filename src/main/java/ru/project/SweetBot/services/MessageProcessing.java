package ru.project.SweetBot.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.telegram.telegrambots.meta.api.objects.Update;

import ru.project.SweetBot.bd.entity.BuyNumber;
import ru.project.SweetBot.bd.entity.Users;
import ru.project.SweetBot.bd.services.NumberServices;
import ru.project.SweetBot.bd.services.UsersManagementService;
import ru.project.SweetBot.bot.TelegramSweetBot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class MessageProcessing extends InputDate {


    private final Map<Long, Boolean> waitingForNumberMap;
    private final UsersManagementService usersManagementService;
    private final NumberServices numberServices;
    private final CheckServices checkServices;
    private final Randomizer randomizer;

//
//     String sweets = "Торт \"Наполеон\"";
//    int maxNumber = 20;

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
                "Разыгрываем вкуснейший " + sweets;
        bot.sendMessage(chatId, endMessage);
        numberList = numberServices.getNumbers();
        if (numberList.size() == 20) {
           int result = randomizer.startRandom(maxNumber);
            String response = " Выпало число : " + result;
            bot.sendMessage(chatId, response);
        }else {
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

    public void handleUserInput(Update update, String messageText, Users user, TelegramSweetBot bot) {
        if (isWaitingForNumber(update.getMessage().getChatId())) {
            if (messageText.matches("\\d+")) {
                int number = Integer.parseInt(messageText);

                if (number >= 1 && number <= maxNumber) {
                    handNumberInput(update.getMessage().getChatId(), number, bot);
                    processUserNumberInput(update.getMessage().getChatId(), number, bot);
                    endMessage(update.getMessage().getChatId(), bot);
                } else {
                    bot.sendMessage(update.getMessage().getChatId(), "Пожалуйста, введите число от 1 до " + maxNumber + ".");

                }
            } else {
                bot.sendMessage(update.getMessage().getChatId(), "Пожалуйста, введите корректное число.");

            }

        }

    }

    public void handleUserDataInput(Long chatId, String userData, String telegramName) {
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
            try {
                usersManagementService.save(user);
            } catch (Exception e) {
                e.getMessage();
            }
        } else {

        }
    }

    public void handNumberInput(Long chatId, int number, TelegramSweetBot bot) {
        BuyNumber bayNumber = new BuyNumber();
        if (number >= 1 && number <= maxNumber) {
            bayNumber.setNumber(number);

            bayNumber.setDateTime(LocalDateTime.now());
            //  bayNumber.setUser(users);
            numberServices.save(bayNumber);
        } else {
            bot.sendMessage(chatId, "Введите значение не больше " + maxNumber + "\n" +
                    "Но возможно такое числу уже занято, выберете другое)). ");

        }

    }
//    public String getWinning(TelegramSweetBot bot){
//         boolean result;
//         List<Integer> lengthNumbers = numberServices.getNumbers();
//        if (lengthNumbers.le )
//            return
//    }

    public void setWaitingForNumber(Long chatId, boolean value) {
        waitingForNumberMap.put(chatId, value);
    }

    public boolean isWaitingForNumber(Long chatId) {
        return waitingForNumberMap.getOrDefault(chatId, false);
    }


}
