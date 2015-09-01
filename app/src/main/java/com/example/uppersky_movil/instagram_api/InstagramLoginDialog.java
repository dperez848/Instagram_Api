package com.example.uppersky_movil.instagram_api;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.uppersky_movil.instagram_api.utils.Constants;

/**
 * Created by UPPERSKY-MOVIL on 01/09/2015.
 */
public class InstagramLoginDialog extends DialogFragment {

    private static final String TAG = "DIALOG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(
                R.layout.fragment_instagram_login, container, false);

        getDialog().setTitle("tralalallaa");
        WebView webView = (WebView) rootView.findViewById(R.id.webview);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new AuthWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Constants.authURLString);

        return rootView;
    }

    public class AuthWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "should");
            if (url.startsWith(InstagramConnector.CALLBACK_URL)) {
                String parts[] = url.split("=");
                InstagramConnector.getInstance().setAccessTokenUser(parts[1]);
                InstagramLoginDialog.this.dismiss();
                InstagramConnector.getInstance().builInstagramCredentials(
                        new InstagramConnector.ListenerBuildInstagramCredentials() {
                            @Override
                            public void buildCredentialsListener() {
                                ((InstagramListener) getActivity())
                                        .listenerFinishInstagramConnection();
                            }
                        });
                return true;
            }
            return false;
        }

    }

}