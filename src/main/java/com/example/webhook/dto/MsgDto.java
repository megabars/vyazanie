package com.example.webhook.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MsgDto {

    @JsonProperty("channel_data")
    private ChannelDataDto channelData;

}
