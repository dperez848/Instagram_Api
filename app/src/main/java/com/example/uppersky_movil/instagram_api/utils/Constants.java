package com.example.uppersky_movil.instagram_api.utils;

/**
 * Created by UPPERSKY-MOVIL on 31/08/2015.
 */
public class Constants {

    public static final String CLIENT_ID = "1850a571edf74073b75c11215bde6a8c";

    public static final String CLIENT_SECRET = "5925b5018e8b405db3cf63e20b0d148f";

    public static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";

    public static final String TOKENURL =  "https://api.instagram.com/oauth/access_token";

    public static final String APIURL = "https://api.instagram.com/v1";

    public static String CALLBACKURL = "http://oauth.net/";

    public static String authURLString = AUTHURL + "?client_id=" + CLIENT_ID + "&redirect_uri=" + CALLBACKURL + "&response_type=code&display=touch&scope=likes+comments+relationships";

    public static String tokenURLString = TOKENURL + "?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&redirect_uri=" + CALLBACKURL + "&grant_type=authorization_code";


}
