package com.example.modeltelegrambot.bots.bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;

public class BotService {
    public static SendMessage sayHello(Long chatId){
        SendMessage sendMessage=new SendMessage();
        sendMessage.setText("Assalomu Alaykum Botga Xush Kelibsiz");
        sendMessage.setChatId(chatId);
        return sendMessage;
    }
    public static SendMessage sendText(Long chatId,String text){
        SendMessage sendMessage=new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }
    public static SendVideo sendVideo(String chatId,String fileId,String caption){
        SendVideo sendVideo=new SendVideo();
        sendVideo.setCaption(caption);
        sendVideo.setChatId(chatId);
        sendVideo.setVideo(new InputFile(fileId));
        return sendVideo;
    }
    public static SendPhoto sendPhoto(String chatId,String fileId,String caption){
        SendPhoto sendPhoto=new SendPhoto();
        sendPhoto.setPhoto(new InputFile(fileId));
        sendPhoto.setCaption(caption);
        sendPhoto.setChatId(chatId);
        return sendPhoto;
    }
    public static SendDocument sendDocument(String chatId,String fileId,String caption){
        SendDocument sendDocument=new SendDocument();
        sendDocument.setDocument(new InputFile(fileId));
        sendDocument.setChatId(chatId);
        sendDocument.setCaption(caption);
        return sendDocument;
    }
    public static DeleteMessage deleteMessage(Long chatId,int messageId){
        DeleteMessage deleteMessage=new DeleteMessage();
        deleteMessage.setMessageId(messageId);
        deleteMessage.setChatId(chatId);
        return deleteMessage;
    }
}
