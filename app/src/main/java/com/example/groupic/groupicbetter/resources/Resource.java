package com.example.groupic.groupicbetter.resources;

/**
 * Created by giladkl on 29/08/15.
 */
public class Resource {
    public static String baseUrl;
    public static ServerHandler server;
    static{
        baseUrl = "http://groupic.photos/groupic/index.php/";
        server = new ServerHandler();
    }
}