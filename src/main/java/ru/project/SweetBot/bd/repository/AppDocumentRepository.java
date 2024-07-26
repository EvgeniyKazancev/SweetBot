package ru.project.SweetBot.bd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.SweetBot.bd.entity.AppDocument;

public interface AppDocumentRepository extends JpaRepository<AppDocument, Long> {
}
