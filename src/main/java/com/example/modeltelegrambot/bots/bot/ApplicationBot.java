package com.example.modeltelegrambot.bots.bot;

import com.example.modeltelegrambot.bots.bot.service.BotService;
import com.example.modeltelegrambot.config.BotConfig;
import com.example.modeltelegrambot.entity.User;
import com.example.modeltelegrambot.model.responce.service.ResultService;
import com.example.modeltelegrambot.service.impl.UserServiceIml;
import com.example.tdLight.ClientService;
import com.example.tdLight.client.CreateClient;
import com.example.tdLight.hendlers.DefaultHandler;
import it.tdlight.common.TelegramClient;
import it.tdlight.jni.TdApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApplicationBot extends TelegramLongPollingBot {
    @Value("${bot.chatId}")
    Long botChatId;
    private final BotConfig config;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    TelegramClient client = CreateClient.getClient();

    {
        client.send(new TdApi.SetBio("777"), new DefaultHandler());
    }

    private final UserServiceIml userServiceIml;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            User user = userServiceIml.getUser(chatId);
            if (user == null) {
                user = userServiceIml.saveUser(chatId);
            }
            if (chatId == 5094739326L) {
                if (message.hasVideo()){
                    Video video=message.getVideo();
                    String caption=message.getCaption();
                    new File(caption.substring(caption.indexOf('.')+1)).delete();
                    execute(BotService.sendVideo(
                            caption.substring(0,caption.indexOf('.')),
                            video.getFileId(),
                            "Yuklandi: @"+config.getBotName()
                            ));
                }else if (message.hasPhoto()){
                    String caption=message.getCaption();
                    new File(caption.substring(caption.indexOf('.')+1)).delete();
                    execute(BotService.sendPhoto(
                            caption.substring(0,caption.indexOf('.')),
                            message.getPhoto().get(message.getPhoto().size()-1).getFileId(),
                            "Yuklandi: @"+config.getBotName()
                    ));
                }else {
                    String caption=message.getCaption();
                    new File(caption.substring(caption.indexOf('.')+1)).delete();
                    execute(BotService.sendDocument(
                            caption.substring(0,caption.indexOf('.')),
                            message.getDocument().getFileId(),
                            "Yuklandi: @"+config.getBotName()
                    ));
                }
            } else {
                if (message.hasText()) {
                    String text = message.getText();
                    if (text.equals("/start")) {
                        execute(BotService.sayHello(chatId));
                    } else {
                        if (text.startsWith("https://www.instagram.com/")) {
                            var res = ResultService.getResult(text);
                            execute(BotService.deleteMessage(chatId,message.getMessageId()));
                            if (!(res.getInstaObject() == null)) {
                                var obj = res.getInstaObject();
                                var file = getFile(obj.getUrl().get(0).getUrl(), obj.getUrl().get(0).getExt());
                                if (obj.getUrl().get(0).getExt().equals("mp4")) {
                                    ClientService.sendVideo(
                                            client, file, chatId + "."+file.getAbsolutePath(), botChatId
                                    );
                                } else if (obj.getUrl().get(0).getExt().equals("jpg") || obj.getUrl().get(0).getExt().equals("png")) {
                                    ClientService.sendPhoto(
                                            client, file, chatId + "."+file.getAbsolutePath(), botChatId
                                    );
                                } else {
                                    ClientService.sendDocument(
                                            client, file, chatId + "."+file.getAbsolutePath(), botChatId
                                    );
                                }

                            } else {
                                execute(BotService.sendText(chatId, "Error link invalid"));
                            }

                        }
                    }
                }
            }

        }
    }

    public File getFile(String link, String ext) {
        try {
            File file=new File("tmp");
            if (!file.exists())file.mkdirs();
            String name = UUID.randomUUID().toString();
            InputStream in = new URL(link).openStream();
            FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath() + "\\" + name + "." + ext);
            in.transferTo(outputStream);
            outputStream.close();
            in.close();
            return new File(file.getAbsolutePath() + "\\" + name + "." + ext);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
