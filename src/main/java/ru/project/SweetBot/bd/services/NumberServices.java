package ru.project.SweetBot.bd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.SweetBot.bd.entity.BuyNumber;
import ru.project.SweetBot.bd.repository.NumberRepository;

import java.util.List;

@Service
public class NumberServices {
    @Autowired
    private final NumberRepository numberRepository;


    public NumberServices(NumberRepository numberRepository) {
        this.numberRepository = numberRepository;
    }


//    public int getNumber(Long userId) {
//
//        return numberRepository.findByUserId(userId);
//    }

    public void save(BuyNumber bayNumber) {
        numberRepository.save(bayNumber);
    }


    public List<BuyNumber> getNumbers() {

        return numberRepository.findAll();
    }

}
