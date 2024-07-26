package ru.project.SweetBot.bd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Builder
@AllArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "binary_content")
public class BinaryContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "array_bytes")
    private byte[] fileArrayOfBytes;

    public BinaryContent() {

    }

    public BinaryContent(byte[] fileArrayOfBytes) {
        this.fileArrayOfBytes = fileArrayOfBytes;
    }
}
