package com.example.tdLight;

import com.example.tdLight.hendlers.DefaultHandler;
import it.tdlight.common.TelegramClient;
import it.tdlight.jni.TdApi;

import java.io.File;

public class ClientService {
    public static void sendDocument(TelegramClient client, File file,String caption,Long chatId){
        TdApi.TextEntity entities = new TdApi.TextEntity(0, caption.length(), new TdApi.TextEntityTypeItalic());
        client.send(new TdApi.SendMessage(chatId
                ,0
                ,0
                ,new TdApi.MessageSendOptions()
                ,null
                ,new TdApi.InputMessageDocument(
                new TdApi.InputFileLocal(file.getAbsolutePath())
                ,null
                ,true
                ,new TdApi.FormattedText(caption,new TdApi.TextEntity[]{entities})
        )),new DefaultHandler());
    }
    public static void sendPhoto(TelegramClient client,File file,String caption,Long chatId){
        TdApi.TextEntity entities = new TdApi.TextEntity(0, caption.length(), new TdApi.TextEntityTypeItalic());
        TdApi.InputMessagePhoto inputMessagePhoto = new TdApi.InputMessagePhoto(new TdApi.InputFileLocal(file.getAbsolutePath()), null, null, 3877, 5815, new TdApi.FormattedText(caption,new TdApi.TextEntity[]{entities}), 0);
        client.send(new TdApi.SendMessage(chatId,0,0,new TdApi.MessageSendOptions(),null,inputMessagePhoto),new DefaultHandler());
    }
    public static void sendVideo(TelegramClient client,File file,String caption,Long chatId){
        TdApi.TextEntity entities = new TdApi.TextEntity(0, caption.length(), new TdApi.TextEntityTypeItalic());
        TdApi.InputMessageVideo inputMessageVideo=new TdApi.InputMessageVideo(new TdApi.InputFileLocal(file.getAbsolutePath()),null,null,0,600,900,true,new TdApi.FormattedText(caption,new TdApi.TextEntity[]{entities}),0);
        client.send(new TdApi.SendMessage(chatId,0,0,new TdApi.MessageSendOptions(),null,inputMessageVideo),new DefaultHandler());
    }
}
