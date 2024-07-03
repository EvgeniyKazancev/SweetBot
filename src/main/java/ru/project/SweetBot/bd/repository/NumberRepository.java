package ru.project.SweetBot.bd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.SweetBot.bd.entity.BuyNumber;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface NumberRepository extends JpaRepository<BuyNumber, Integer> {

    List<Integer> findByNumber = new ArrayList<>();

    BuyNumber findByUserId(Long userId);


}
