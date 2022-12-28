package com.example.modeltelegrambot.model;

import com.example.modeltelegrambot.model.responce.Collage.CollageItem;
import com.example.modeltelegrambot.model.responce.Object.InstaObject;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Result {
    private InstaObject instaObject;
    private ArrayList<CollageItem> items;
}
