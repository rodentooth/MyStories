package xyz.myrating.stories.mystories.tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Emanuel Graf on 29.04.2018.
 */

public class userdata {

    private SharedPreferences prefs;

    public userdata(Context c){
        prefs = c.getSharedPreferences(
                "userdata", Context.MODE_PRIVATE);

    }

    public String getUsername() {
        return prefs.getString("username","");
    }

    public void setUsername(String username) {
        prefs.edit().putString("username", username).apply();
    }

    public String getEmail() {
        return prefs.getString("email","");
    }

    public void setEmail(String email) {
        prefs.edit().putString("email", email).apply();
    }

    public int getId() {
        return prefs.getInt("id",0);

    }

    public void setId(int id) {
        prefs.edit().putInt("id", id).apply();
    }

    public String getPass() {
        return prefs.getString("pass","");
    }

    public void setPass(String pass) {
        prefs.edit().putString("pass",  (pass)).apply();
    }

    public String getAdvertiserID() {
        return prefs.getString("advertiserId","");
    }

    public void setAdvertiserID(String advertiserID) {
        prefs.edit().putString("advertiserId", advertiserID).apply();
    }
    public void erase(Boolean b){
        int id = getId();
        prefs.edit().clear().apply();
        if(!b)
        setId(id);
    }



}
