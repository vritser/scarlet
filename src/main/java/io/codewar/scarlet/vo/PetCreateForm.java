package io.codewar.scarlet.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PetCreateForm {
    private String name;
    private LocalDateTime birthDate;
}
