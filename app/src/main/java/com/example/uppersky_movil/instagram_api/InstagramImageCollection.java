package com.example.uppersky_movil.instagram_api;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by UPPERSKY-MOVIL on 01/09/2015.
 */
public class InstagramImageCollection {

    private Pagination pagination;
    private ArrayList<InstagramImage> instagramImages;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public ArrayList<InstagramImage> getInstagramImages() {
        return instagramImages;
    }

    public void setInstagramImages(ArrayList<InstagramImage> instagramImages) {
        this.instagramImages = instagramImages;
    }

    public class Pagination {

        private String next_url;
        private String next_max_id;

        public String getNext_url() {
            return next_url;
        }

        public void setNext_url(String next_url) {
            this.next_url = next_url;
        }

        public String getNext_max_id() {
            return next_max_id;
        }

        public void setNext_max_id(String next_max_id) {
            this.next_max_id = next_max_id;
        }

        public String toJSON() {
            return new Gson().toJson(this);
        }

    }

}
