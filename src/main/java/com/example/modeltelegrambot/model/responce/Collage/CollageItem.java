package com.example.modeltelegrambot.model.responce.Collage;


import com.example.modeltelegrambot.model.responce.Object.Meta;
import com.example.modeltelegrambot.model.responce.Object.UrlItem;
import lombok.Data;

import java.util.List;

@Data
public class CollageItem{
	String thumb;
	Meta meta;
	List<UrlItem> url;
}