package ru.project.SweetBot.bd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Builder
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "app_document")
public class AppDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_Id")
    private String telegramFileId;
    @Column(name = "doc_name")
    private String docName;

    @OneToOne
    private BinaryContent binaryContent;
    @Column(name = "mime_type")
    private String mimeType;
    @Column(name = "file_size")
    private Long fileSize;


    public AppDocument() {

    }
}
