package com.example.modeltelegrambot.model.responce.service;

import com.example.modeltelegrambot.model.Result;
import com.example.modeltelegrambot.model.responce.Collage.CollageItem;
import com.example.modeltelegrambot.model.responce.Object.InstaObject;
import com.google.gson.Gson;
import org.glassfish.grizzly.http.server.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;

public class ResultService {
    public static Result getResult(String link) throws IOException, InterruptedException {
        Gson gson=new Gson();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://codewithashu.pythonanywhere.com/GetVideoDetails/?URL="+link))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        String response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        Result result=new Result();

        try {
            var obj=gson.fromJson(response, InstaObject.class);
            result.setInstaObject(obj);
            return result;
        }catch (Exception e){
            var items=gson.fromJson(response, CollageItem[].class);
            result.setItems(new ArrayList<>(Arrays.asList(items)));
            return result;
        }
    }
}
