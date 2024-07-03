package ru.project.SweetBot.services;

import org.springframework.stereotype.Service;
import ru.project.SweetBot.bd.services.NumberServices;

@Service
public class CheckServices {
    private final NumberServices numberServices;

    public CheckServices(NumberServices numberServices) {
        this.numberServices = numberServices;
    }

//    public boolean checkNumber(int number) {
//        boolean result = false;
//        if (numberServices.checkNumber(number) == number) {
//            result = true;
//        }
//            return result;
//    }
}
