package com.hfad.mathquiz.utilities;

public class TwitterUser {

    private boolean isLoggedIn;
    private static TwitterUser instance;

    private TwitterUser(){}

    public static synchronized TwitterUser getInstance(){
        if(instance == null){
            instance = new TwitterUser();
        }
        return instance;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

}
