package com.example.uppersky_movil.instagram_api;

import com.google.gson.Gson;

/**
 * Created by UPPERSKY-MOVIL on 01/09/2015.
 */
public class InstagramImage {

    private LowResolution low_resolution;
    private Thumbnail thumbnail;
    private StandardResolution standard_resolution;

    public LowResolution getLow_resolution() {
        return low_resolution;
    }

    public void setLow_resolution(LowResolution low_resolution) {
        this.low_resolution = low_resolution;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public StandardResolution getStandard_resolution() {
        return standard_resolution;
    }

    public void setStandard_resolution(StandardResolution standard_resolution) {
        this.standard_resolution = standard_resolution;
    }

    public class LowResolution extends PropertieImage {
    }

    public class Thumbnail extends PropertieImage {
    }

    public class StandardResolution extends PropertieImage {
    }

    private class PropertieImage {
        private String url;
        private int width;
        private int height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }

}
