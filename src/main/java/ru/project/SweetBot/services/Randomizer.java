package ru.project.SweetBot.services;

import org.springframework.stereotype.Service;

@Service
public class Randomizer  {

     public int startRandom( int maxNumber) {
         int result = (int) (Math.random() * maxNumber);
         return result;
     }


}
