package com.example.webhook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DocumentDto {

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_unique_id")
    private String fileUniqueId;

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("file_size")
    private long fileSize;

    @JsonProperty("mime_type")
    private String mimeType;

}
