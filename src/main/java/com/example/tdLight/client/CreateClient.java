package com.example.tdLight.client;

import com.example.tdLight.hendlers.DefaultHandler;
import com.example.tdLight.hendlers.ErrorHandler;
import it.tdlight.common.Init;
import it.tdlight.common.ResultHandler;
import it.tdlight.common.TelegramClient;
import it.tdlight.common.utils.CantLoadLibrary;
import it.tdlight.jni.TdApi;
import it.tdlight.tdlight.ClientManager;

import java.io.IOError;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateClient {
    private static TelegramClient client;
    private static final Lock  authorizationLock=new ReentrantLock();
    private static final Condition getAuthorization=authorizationLock.newCondition();
    private static volatile boolean haveAuthorization=false;

    public static TelegramClient getClient(){
        try {
            Init.start();
        }catch (CantLoadLibrary cantLoadLibrary){
            cantLoadLibrary.printStackTrace();
        }
        client = ClientManager.create();
        client.initialize(new RegHandler(),new ErrorHandler(),new ErrorHandler());

        client.execute(new TdApi.SetLogVerbosityLevel(0));

        if (client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdapi.log",1<<27,false)))instanceof TdApi.Error){
            throw new IOError(new IOException("Write access to the current directory is required"));
        }

            TdApi.Object object= DefaultHandler.result(client.execute(new TdApi.GetTextEntities("@telegram /test_command https://telegram.org telegram.me @gif @test")));
            System.out.println(object);

            authorizationLock.lock();
            try {
                while (!haveAuthorization){
                    try {
                        getAuthorization.await();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }finally {
                authorizationLock.unlock();
            }
            if(haveAuthorization){
                return client;
            }else {
                return null;
            }
    }
    private static class RegHandler implements ResultHandler{

        @Override
        public void onResult(TdApi.Object object) {
            if (object.getConstructor() == TdApi.UpdateAuthorizationState.CONSTRUCTOR){
                onAuthorizationStateUpdated(((TdApi.UpdateAuthorizationState)object).authorizationState);
            }

        }

        private static void onAuthorizationStateUpdated(TdApi.AuthorizationState authorizationState) {
            if (authorizationState!=null){
                authorizationState = authorizationState;
            }
            switch (authorizationState.getConstructor()){
                case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                    TdApi.SetTdlibParameters parameters=new TdApi.SetTdlibParameters();
                    parameters.databaseDirectory="tdlib";
                    parameters.useMessageDatabase=true;
                    parameters.useSecretChats=true;
                    parameters.apiId=9944969;
                    parameters.apiHash="8a2f7841e663d366274ea911e1efcfa2";
                    parameters.applicationVersion="1.0";
                    parameters.systemLanguageCode="en";
                    parameters.deviceModel="Desktop";
                    parameters.enableStorageOptimizer=true;
                    client.send(parameters,new AuthorizationRequestHandler());
                }
                case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR->{
                    String phoneNumber=getString("Please enter phone number: ");
                    client.send(new TdApi.SetAuthenticationPhoneNumber(phoneNumber,null),new AuthorizationRequestHandler());
                }
                case TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR->{
                    String link=((TdApi.AuthorizationStateWaitOtherDeviceConfirmation)authorizationState).link;
                    System.out.println("Please confirm this login link on another device: "+link);
                }
                case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR->{
                    String code=getString("Please enter authentication code: ");
                    client.send(new TdApi.CheckAuthenticationCode(code),new AuthorizationRequestHandler());
                }
                case TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR->{
                    String firstName=getString("Please enter firstName: ");
                    String lastName=getString("Please enter lastName: ");
                    client.send(new TdApi.RegisterUser(firstName,lastName),new AuthorizationRequestHandler());
                }
                case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR->{
                    String password=getString("Please enter the password:");
                    client.send(new TdApi.CheckAuthenticationPassword(password),new AuthorizationRequestHandler());
                }
                case TdApi.AuthorizationStateReady.CONSTRUCTOR->{
                    haveAuthorization=true;
                    authorizationLock.lock();
                    try {
                        getAuthorization.signal();
                    }finally {
                        authorizationLock.unlock();
                    }
                }
                default -> {
                    System.out.println("Unsupported Authorization state "+authorizationState);
                }
            }
        }
        private static String getString(String str){
            String consoleString=null;
            Scanner sc=new Scanner(System.in);
            do {
                System.out.println(str);
                consoleString = sc.nextLine();
                consoleString = consoleString.trim();
                if(consoleString.length()<1){
                    consoleString =null;
                    continue;
                }else {break;}
            }while (consoleString == null);
            return consoleString;
        }
    }
    private static class AuthorizationRequestHandler implements ResultHandler{

        @Override
        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()){
                case TdApi.Error.CONSTRUCTOR->{
                    System.err.println("Receive an error : "+object);
                    RegHandler.onAuthorizationStateUpdated(null);
                }
                case TdApi.Ok.CONSTRUCTOR -> {

                }
                default -> {
                    System.err.println("Receive  wrong response from TDLib : "+object);
                }
            }
        }
    }

}

