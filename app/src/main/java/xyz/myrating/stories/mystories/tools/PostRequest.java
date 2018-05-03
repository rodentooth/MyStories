package xyz.myrating.stories.mystories.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import xyz.myrating.stories.mystories.MainActivity;
import xyz.myrating.stories.mystories.R;
import xyz.myrating.stories.mystories.RecyclerList.MyAdapter;
import xyz.myrating.stories.mystories.story.story;

/**
 * Created by Emanuel Graf on 29.04.2018.
 */

public class PostRequest extends android.os.AsyncTask<String, Void, String> {

    private Context context;
    private String URL="";
    private ArrayList<story> feedsList;
    private LinearLayout MyStoriesLayout;
    private TextInputLayout username_available;
    String post_data;
    int position=0;
    int seed;
    MyAdapter mAdapter;
    Integer tries;


    public PostRequest(Context activity,Object... o){
        context=activity;



        if(o.length>0 && o[0] instanceof ArrayList){
            feedsList = (ArrayList)o[0];

        }else if(o.length>0 && o[0] instanceof TextInputLayout) {
            username_available = (TextInputLayout) o[0];
        }else if(o.length>0 && o[0] instanceof Integer) {
            tries = (Integer) o[0];
        }else if(o.length>0 && o[0] instanceof LinearLayout) {
            MyStoriesLayout = (LinearLayout) o[0];
            position = (int)o[1];
        }
        if(o.length>1 && o[1] instanceof MyAdapter){
            mAdapter= (MyAdapter)o[1];
            seed = (int)o[2];
            position = (int)o[3];
        }

    }

        @Override
        protected String doInBackground(String... params) {

        try {
                    if (params[0].equals("get_stories")) {

                        URL = "https://stories.myrating.xyz/stories/get_stories.php";
                        post_data =
                                URLEncoder.encode("language", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                                URLEncoder.encode("author", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                                URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&" +
                                URLEncoder.encode("seed", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(seed), "UTF-8") + "&" +
                                URLEncoder.encode("offset", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(position), "UTF-8");
                    }else if(params[0].equals("authenticate")) {

                        URL = "https://stories.myrating.xyz/stories/authenticate.php";
                        post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                                URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                                URLEncoder.encode("advertisingId", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");

                    } else if(params[0].equals("delete_account")) {

                        URL = "https://stories.myrating.xyz/stories/delete_account.php";
                        post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                                URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8");

                    } else if(params[0].equals("create_story")) {

                        URL = "https://stories.myrating.xyz/stories/create_story.php";
                        post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                                URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                                URLEncoder.encode("story", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8")+ "&" +
                                URLEncoder.encode("lang", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8");

                    } else if(params[0].equals("rate")) {

                        URL = "https://stories.myrating.xyz/stories/rate.php";
                        post_data = URLEncoder.encode("rating", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                                URLEncoder.encode("story_id", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                                URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");

                    } else if(params[0].equals("username_availability_check")) {

                        URL = "https://stories.myrating.xyz/stories/username_availability_check.php";
                        post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

                    }  else if(params[0].equals("reset_password")) {

                        URL = "https://stories.myrating.xyz/stories/reset_password.php";
                        post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

                    } else if(params[0].equals("username_availability_check_update")) {

                        URL = "https://stories.myrating.xyz/stories/username_availability_check.php";
                        post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                        URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8")
                        ;

                    }else if(params[0].equals("login")) {

                        URL = "https://stories.myrating.xyz/stories/login.php";
                        post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                                URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                                URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&" +
                                URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8") + "&" +
                                URLEncoder.encode("advertisingId", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8");

                    }else if(params[0].equals("update")) {

                        URL = "https://stories.myrating.xyz/stories/login.php";
                        post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                                URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&" +
                                URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&" +
                                URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8") + "&" +
                                URLEncoder.encode("advertisingId", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8")+ "&" +
                                URLEncoder.encode("mode", "UTF-8") + "=" + URLEncoder.encode(params[6], "UTF-8");

                    }
            Log.d("url : ",params[0]+" :"+post_data);


                    java.net.URL url = new URL(URL);
                    HttpURLConnection httpurlconn = (HttpURLConnection) url.openConnection();
                    httpurlconn.setRequestMethod("POST");
                    httpurlconn.setDoOutput(true);
                    httpurlconn.setDoInput(true);
                    OutputStream outputStream = httpurlconn.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpurlconn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpurlconn.disconnect();

                    return result;


                } catch (IOException e) {
                    e.printStackTrace();
                }






            return "k";
        }


        @Override
        protected void onPreExecute() {



        }

        @Override
        protected void onPostExecute(String result) {


            if (result != null) {
                if (result == "") {
                    result = "0";
                }else if(result.equals("k")){
                    Toast.makeText(context, R.string.lostconnection, Toast.LENGTH_SHORT).show();
                }
                Log.d("AsyncTask says: ",result);
            }

                try {

                    JSONObject jsonObj = new JSONObject(result);
                    String s = jsonObj.keys().next();

                    if (s.equals("STORIES")) {

                        // Getting JSON Array node

                        position = position + 10;


                        JSONArray toplevels = jsonObj.getJSONArray("STORIES");


                        if (feedsList == null) {
                            feedsList = new ArrayList<story>();

                        }


                        // looping through All Stories
                        for (int i = 0; i < toplevels.length(); i++) {
                            JSONObject c = toplevels.getJSONObject(i);


                            final int id = Integer.valueOf(c.getString("id"));
                            final String story = c.getString("story");
                            final String author = c.getString("author");
                            final double rating = Double.valueOf(c.getString("rating").equals("")?"0.0":c.getString("rating"));
                            final int trigger = Integer.valueOf(c.getString("trigger"));



                            story item = new story(story,rating,author);
                            item.setId(id);
                            item.setLoadingTrigger(trigger == 1);


                            int added = 0;
                            if(MyStoriesLayout ==null) {

                                feedsList.add(item);


                                mAdapter.notifyDataSetChanged();
                            }else{

                                LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.my_stories_layout, null, false);
                                TextView summary = layout.findViewById(R.id.my_story);
                                summary.setText(item.getStory().length()>80?item.getStory().substring(0,80)+"...":item.getStory());
                                TextView avgRating = layout.findViewById(R.id.my_avg_story_rating);
                                avgRating.setText("Ø"+item.getAvg_rating());

                                layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(layout.getContext(), "Dialog here", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                MyStoriesLayout.addView(layout);
                            }

                                                }
                        if (feedsList!=null &&feedsList.size() == 0) {


                            story item = new story("",0.0,"");
                            item.setId(0);
                            item.setLoadingTrigger(true);
                            feedsList.add(item);

                        } else {
                            mAdapter.notifyDataSetChanged();

                        }
                    }else if(feedsList!=null &&s.equals("NOSTORIES")){
                        if(feedsList.size()==0)
                            feedsList.add(new story("NOSTORIES",0.0,"Huckleberry Finn"));
                        else

                            feedsList.add(new story("Hey! You reached the end :) Why don't you create a story of your own?",0.0,"Huckleberry Finn"));
                        mAdapter.notifyDataSetChanged();

                    }else if(s.equals("CREATE")){
                        JSONArray toplevels = jsonObj.getJSONArray("CREATE");

                        // looping through All Contacts
                        for (int i = 0; i < toplevels.length(); i++) {
                            JSONObject c = toplevels.getJSONObject(i);

                            if (c.getInt("CREATED") == (1)) {

                                //story successfully posted, go back to  ainactivity, start a snackbar and clean up the SharedPreferences
                                context.startActivity(new Intent(context, MainActivity.class).putExtra("snackbar",context.getString(R.string.storycreatedsuccess)));



                                final SharedPreferences prefs = context.getSharedPreferences(
                                        "storyDraft", Context.MODE_PRIVATE);
                                prefs.edit().putString("temp", "").apply();

                            }
                        }


                    }else if (s.equals("AUTHENTICATION")) {

                        // Getting JSON Array node

                        userdata mUserdata = new userdata(context);
                        JSONArray toplevels = jsonObj.getJSONArray("AUTHENTICATION");

                        // looping through All Contacts
                        for (int i = 0; i < toplevels.length(); i++) {
                            JSONObject c = toplevels.getJSONObject(i);

                            if(c.getInt("AUTHENTICATED")==(1)) {

                                mUserdata.setId(Integer.valueOf(c.getString("id")));
                                mUserdata.setUsername(c.getString("username"));

                            }else if(c.getInt("AUTHENTICATED")==(2)){
                                mUserdata.setId(Integer.valueOf(c.getString("id")));
                            }
                            else{
                                mUserdata.setUsername("");
                                context.startActivity(new Intent(context, MainActivity.class).putExtra("snackbar",context.getString(R.string.notauthenticated)));

                            }



                        }

                    }else if (s.equals("LOGIN")) {

                        // Getting JSON Array node

                        userdata mUserdata = new userdata(context);
                        JSONArray toplevels = jsonObj.getJSONArray("LOGIN");

                        // looping through All Contacts
                        for (int i = 0; i < toplevels.length(); i++) {
                            JSONObject c = toplevels.getJSONObject(i);

                            if(c.getInt("SUCCESS")==(1)) {

                                mUserdata.setId(Integer.valueOf(c.getString("id")));
                                mUserdata.setUsername(c.getString("username"));
                                mUserdata.setPass(c.getString("pass"));
                                mUserdata.setEmail(c.getString("email"));
                                context.startActivity(new Intent(context, MainActivity.class).putExtra("snackbar",context.getString(R.string.loginsuccess)));

                            }
                            else{
                                tries++;
                                Toast.makeText(context, R.string.didnotwork, Toast.LENGTH_SHORT).show();

                            }



                        }

                    }else if (s.equals("USERNAME_AVAILABILITY")) {

                        // Getting JSON Array node
                        JSONArray toplevels = jsonObj.getJSONArray("USERNAME_AVAILABILITY");

                        // looping through All Contacts
                        for (int i = 0; i < toplevels.length(); i++) {
                            JSONObject c = toplevels.getJSONObject(i);

                            if(c.getInt("AVAILABLE")==(1)) {

                                username_available.setError(null);

                            }else {
                                username_available.setError(context.getString(R.string.unamenotavailable));
                            }




                        }

                    }

                    } catch( final JSONException e){

                        Log.d("json", String.valueOf(e));


                    }







        }

    }



