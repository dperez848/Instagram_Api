package com.example.uppersky_movil.instagram_api;

import android.os.AsyncTask;
import android.os.StrictMode;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by UPPERSKY-MOVIL on 01/09/2015.
 */
public class InstagramConnector {

    // CREDENTIALS
    public static final String CLIENT_ID = "1850a571edf74073b75c11215bde6a8c";
    public static final String CLIENT_SECRET = "5925b5018e8b405db3cf63e20b0d148f";

    // CALLBACK URL (OUR SERVER)
    public static final String CALLBACK_URL = "http://localhost";

    // CONNECTION URL INSTAGRAM
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    public static final String API_URL = "https://api.instagram.com/v1";
    public static final String AUTH_URL_STRING = String
            .format("%s?client_id=%s&redirect_uri=%s&response_type=code&display=touch&scope=likes+comments+relationships",
                    AUTH_URL, CLIENT_ID, CALLBACK_URL);
    public static final String TOKEN_URL_STRING = TOKEN_URL;

    // INTERFACES
    public interface ListenerBuildInstagramCredentials {
        void buildCredentialsListener();
    }

    private String idUser;
    private String username;
    private String accessTokenUser;

    private static InstagramConnector _instance;

    private InstagramConnector() {
    }

    public static final InstagramConnector getInstance() {
        if (_instance == null)
            _instance = new InstagramConnector();
        return _instance;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessTokenUser() {
        return accessTokenUser;
    }

    public void setAccessTokenUser(String accessTokenUser) {
        this.accessTokenUser = accessTokenUser;
    }

    public void builInstagramCredentials(
            ListenerBuildInstagramCredentials listener) {
        if (getAccessTokenUser() == null || getAccessTokenUser() == "")
            return;
        new AsyncInstagramCredentials().doInBackground(listener);
    }

    // METHODS FOR GET IMAGES

    public InstagramImageCollection getImages(int count) {
        String url = String.format(
                "%s/users/%s/media/recent/?access_token=%s&count=%s", API_URL,
                getIdUser(), getAccessTokenUser(), count);
        return getImageCollection(new AsyncConnectionInstagram()
                .doInBackground(url));
    }

    public InstagramImageCollection getImages(int count, String maxId) {
        String url = String.format(
                "%s/users/%s/media/recent/?access_token=%s&count=%s&max_id=%s",
                API_URL, getIdUser(), getAccessTokenUser(), count, maxId);
        return getImageCollection(new AsyncConnectionInstagram()
                .doInBackground(url));
    }

    public ArrayList<InstagramImage> getImages() {
        String url = String.format("%s/users/%s/media/recent/?access_token=%s",
                API_URL, getIdUser(), getAccessTokenUser());
        return getArrayImages(new AsyncConnectionInstagram()
                .doInBackground(url));
    }

    private InstagramImageCollection getImageCollection(JsonObject jsonObject) {
        if (jsonObject == null)
            return null;
        InstagramImageCollection collection = new InstagramImageCollection();
        collection.setPagination(new Gson().fromJson(
                jsonObject.getAsJsonObject("pagination"),
                InstagramImageCollection.Pagination.class));
        collection.setInstagramImages(getArrayImages(jsonObject));
        return collection;
    }

    private ArrayList<InstagramImage> getArrayImages(JsonObject jsonObject) {
        if (jsonObject == null)
            return null;
        ArrayList<InstagramImage> images = new ArrayList<InstagramImage>();
        for (JsonElement jsonElement : jsonObject.getAsJsonArray("data"))
            images.add(new Gson().fromJson(
                    jsonElement.getAsJsonObject().get("images"),
                    InstagramImage.class));
        return images;
    }

    // ASYNC CONNECTION TO INSTAGRAM API

    private class AsyncConnectionInstagram extends
            AsyncTask<String, Void, JsonObject> {

        @Override
        protected JsonObject doInBackground(String... params) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                HttpResponse response = httpclient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                return new Gson().fromJson(sb.toString(), JsonObject.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private class AsyncInstagramCredentials extends
            AsyncTask<ListenerBuildInstagramCredentials, Void, String> {

        public String streamToString(InputStream p_is) {
            try {
                BufferedReader m_br;
                StringBuffer m_outString = new StringBuffer();
                m_br = new BufferedReader(new InputStreamReader(p_is));
                String m_read = m_br.readLine();
                while (m_read != null) {
                    m_outString.append(m_read);
                    m_read = m_br.readLine();
                }
                return m_outString.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected String doInBackground(
                ListenerBuildInstagramCredentials... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                URL url = new URL(InstagramConnector.TOKEN_URL_STRING);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
                        .openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                        httpsURLConnection.getOutputStream());

                outputStreamWriter
                        .write("client_id="
                                + InstagramConnector.CLIENT_ID
                                + "&client_secret="
                                + InstagramConnector.CLIENT_SECRET
                                + "&grant_type=authorization_code"
                                + "&redirect_uri="
                                + InstagramConnector.CALLBACK_URL
                                + "&code="
                                + InstagramConnector.getInstance()
                                .getAccessTokenUser());
                outputStreamWriter.flush();
                String response = streamToString(httpsURLConnection
                        .getInputStream());
                JSONObject jsonObject = (JSONObject) new JSONTokener(response)
                        .nextValue();

                getInstance().setIdUser(
                        jsonObject.getJSONObject("user").getString("id"));
                getInstance().setAccessTokenUser(
                        jsonObject.getString("access_token"));
                getInstance().setUsername(
                        jsonObject.getJSONObject("user").getString("username"));

                params[0].buildCredentialsListener();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}