package com.example.webhook.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MsgInDto {

    private List<PhotoDto> photo;

    private DocumentDto document;

}
