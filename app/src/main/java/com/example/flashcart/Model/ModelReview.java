package com.example.flashcart.Model;

public class ModelReview {

    String uid,ratings,review,timestamps;

    public ModelReview(){

    }


    public ModelReview(String uid, String ratings, String review, String timestamps) {
        this.uid = uid;
        this.ratings = ratings;
        this.review = review;
        this.timestamps = timestamps;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(String timestamps) {
        this.timestamps = timestamps;
    }
}
