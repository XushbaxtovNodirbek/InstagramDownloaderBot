package com.example.modeltelegrambot.model.responce.Object;

import java.util.List;

import lombok.Data;

@Data
public class InstaObject{
	Object sd;
	String thumb;
	String hosting;
	Meta meta;
	Object hd;
	List<UrlItem> url;
	Integer timestamp;
}