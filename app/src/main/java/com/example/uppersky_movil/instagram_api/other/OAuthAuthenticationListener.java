package com.example.uppersky_movil.instagram_api.other;

/**
 * Created by UPPERSKY-MOVIL on 01/09/2015.
 */
public interface OAuthAuthenticationListener {
    public abstract void onSuccess();

    public abstract void onFail(String error);
}
