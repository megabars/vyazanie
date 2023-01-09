package com.example.webhook;

import com.example.webhook.dto.MessageDto;
import com.example.webhook.dto.MsgInDto;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@RestController
@Slf4j
public class Rest {

    @Value("${bot-to}")
    private String botTo;

    @Value("${bot-from}")
    private String botFrom;

    @Value("${my-id}")
    private long myId;

    @PostMapping("/webhook")
    public void webhook(
            @RequestBody List<MessageDto> list
    ) throws IOException {
        for(MessageDto message : list) {
            if (
                    message != null
                            && message.getInfo() != null
                            && message.getInfo().getMessage() != null
                            && message.getInfo().getMessage().getChannelData() != null
                            && message.getInfo().getMessage().getChannelData().getMessage() != null) {
                MsgInDto msg = message.getInfo().getMessage().getChannelData().getMessage();
                if (msg.getPhoto() != null && !msg.getPhoto().isEmpty()) {
                    resendFile(msg.getPhoto().get(msg.getPhoto().size() - 1).getFileId(), false, null);
                } else if (msg.getDocument() != null) {
                    resendFile(msg.getDocument().getFileId(), true, msg.getDocument().getFileName());
                }
            }
        }
        log.info(list.toString());
    }

    @GetMapping("/")
    public String test() {
        return "Hello";
    }

    private void resendFile(String fileId, boolean isDocument, String fileName) throws IOException {
        TelegramBot botFrom = new TelegramBot(this.botFrom);
        GetFile getFile = new GetFile(fileId);
        GetFileResponse getFileResponse = botFrom.execute(getFile);
        File file = getFileResponse.file();
        String fullPath = botFrom.getFullFilePath(file);

        ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
        try (BufferedInputStream in = new BufferedInputStream(new URL(fullPath).openStream())) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // handle exception
        }

        try {
            TelegramBot botTo = new TelegramBot(this.botTo);

            if (isDocument) {
                SendDocument request = new SendDocument(myId, fileOutputStream.toByteArray());
                request.fileName(fileName);
                botTo.execute(request);
            } else {
                SendPhoto request = new SendPhoto(myId, fileOutputStream.toByteArray());
                botTo.execute(request);
            }
        } finally {
            fileOutputStream.close();
        }
    }

}
