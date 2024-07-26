package ru.project.SweetBot.bd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.SweetBot.bd.entity.BinaryContent;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {
}
