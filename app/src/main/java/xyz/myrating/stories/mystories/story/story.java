package xyz.myrating.stories.mystories.story;

import java.io.Serializable;

/**
 * Created by Emanuel Graf on 27.04.2018.
 */

public class story implements Serializable {

    String story = "";
    double avg_rating = 0.0;
    String author = "";
    Boolean loadingTrigger = false;
    int id = 0;

    public story(String s, double a, String au) {
        this.story = s;
        this.avg_rating = a;
        this.author = au;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public double getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(double avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isLoadingTrigger() {
        return loadingTrigger;
    }

    public void setLoadingTrigger(Boolean loadingTrigger) {
        this.loadingTrigger = loadingTrigger;
    }

}
