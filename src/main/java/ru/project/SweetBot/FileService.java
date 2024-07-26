package ru.project.SweetBot;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.project.SweetBot.bd.entity.AppDocument;

public interface FileService {
    AppDocument processDocument(Message externalMessage);
}
