package com.example.uppersky_movil.instagram_api.interfaces;

/**
 * Created by UPPERSKY-MOVIL on 01/09/2015.
 */
public interface OAuthDialogListener {
    public abstract void onComplete(String accessToken);
    public abstract void onError(String error);
}
