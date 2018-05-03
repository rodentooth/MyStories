package xyz.myrating.stories.mystories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.myrating.stories.mystories.tools.PostRequest;
import xyz.myrating.stories.mystories.tools.authenticate;
import xyz.myrating.stories.mystories.tools.hash;
import xyz.myrating.stories.mystories.tools.userdata;


public class MyStories extends Fragment {

    int position=0;
    Integer tries =0;


    public MyStories() {
        // Required empty public constructor
    }


    public static MyStories newInstance(String param1, String param2) {
        MyStories fragment = new MyStories();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view;
        final userdata mUserdata= new userdata(getContext());
        if(!mUserdata.getUsername().equals("")) {
             view= inflater.inflate(R.layout.fragment_my_stories, container, false);

            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    authenticate.authenticate(getContext());
                    startActivity(new Intent(getContext(), CreateStory.class));
                }
            });


            LinearLayout linear = (LinearLayout) view.findViewById(R.id.my_stories_list);

            new PostRequest(getContext(), linear, position).execute("get_stories", "", mUserdata.getUsername(), "");//"language","selected_author","username"


            view.findViewById(R.id.settingsbtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(getContext(), SettingsActivity.class));
                }
            });

        }else{

            final Boolean[] everything_valid = {false};
            view= inflater.inflate(R.layout.fragment_signup, container, false);

            final TextInputLayout text_input_layout_email =  view.findViewById(R.id.text_input_layout_email);
            text_input_layout_email.setVisibility(View.GONE);
            final EditText Email_input = view.findViewById(R.id.Email_input);
            Email_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        Pattern rfc2822 = Pattern
                                .compile ("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");



                            Matcher matcher = rfc2822.matcher(String.valueOf(Email_input.getText()));

                        if(matcher.matches()){
                            text_input_layout_email.setError(null);
                        }else{
                            text_input_layout_email.setError(getString(R.string.needyourmailerrror));
                        }

                    }
                }
            });

            final TextInputLayout text_input_layout_name =  view.findViewById(R.id.text_input_layout_name);
            text_input_layout_name.setVisibility(View.GONE);
            if(text_input_layout_email.getVisibility()==View.VISIBLE)
                text_input_layout_name.setError(getString(R.string.pleasechooseusername));
            final EditText UserName_input = view.findViewById(R.id.UserName_input);
            UserName_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus && (text_input_layout_email.getVisibility()==View.VISIBLE)){
                        new PostRequest(getContext(),text_input_layout_name).execute("username_availability_check", String.valueOf(UserName_input.getText()));
                        text_input_layout_name.setError(getString(R.string.checkuingusername));

                    }
                }
            });


            final TextInputLayout text_input_layout_password =  view.findViewById(R.id.text_input_layout_password);
            text_input_layout_password.setVisibility(View.GONE);
            final EditText pass_input = view.findViewById(R.id.pass_input);

            pass_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        if(4 > String.valueOf(pass_input.getText()).length()){
                            text_input_layout_password.setError(getString(R.string.pleasechooselongerpass));
                        }else{
                            text_input_layout_password.setError(null);

                        }
                    }
                }
            });


            final LinearLayout agree_layout =  view.findViewById(R.id.agree_layout);
            agree_layout.setVisibility(View.GONE);

            final CheckBox checkBox = view.findViewById(R.id. checkBox);

            TextView more_textview =  view.findViewById(R.id.more_textview);
            more_textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo dialog with two checkboxes
                }
            });

            final Button email_login_btn = view.findViewById(R.id.email_login_btn);
            email_login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    email_login_btn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    text_input_layout_name.setVisibility(View.VISIBLE);
                    text_input_layout_password.setVisibility(View.VISIBLE);


                    if (text_input_layout_email.getVisibility()==View.VISIBLE) {
                        text_input_layout_email.setVisibility(View.GONE);
                        agree_layout.setVisibility(View.GONE);
                        text_input_layout_name.setError(null);

                    }else{
                        // POST REQUEST, check if password(hashed) is O.K. on server and log in
                        try {
                            new PostRequest(getContext(),tries).execute("login", String.valueOf(UserName_input.getText()), hash.SHA512(String.valueOf(pass_input.getText())),"", String.valueOf(mUserdata.getId()),mUserdata.getAdvertiserID());//"language","selected_author","username"
                        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.d("tries", String.valueOf(tries));
                        if(tries>2){
                            Snackbar.make((view.findViewById(R.id.layout)), R.string.resetpassask, Snackbar.LENGTH_LONG)
                            .setAction(R.string.reset, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new PostRequest(getContext()).execute("reset_password", String.valueOf(mUserdata.getId()));//"language","selected_author","username"

                                    Snackbar.make((view.findViewById(R.id.layout)), R.string.checkemails, Snackbar.LENGTH_LONG).show();

                                }
                            })
                            .show();

                        }

                    }


                }
            });

            final Button email_signin_btn = view.findViewById(R.id.email_signin_btn);
            email_signin_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email_signin_btn.requestFocus();

                    if (text_input_layout_email.getVisibility()==View.GONE){
                        text_input_layout_name.setVisibility(View.VISIBLE);
                        text_input_layout_email.setVisibility(View.VISIBLE);
                        text_input_layout_password.setVisibility(View.VISIBLE);
                        agree_layout.setVisibility(View.VISIBLE);


                    }else if(text_input_layout_name.getError()!=null ){
                        Snackbar.make((view.findViewById(R.id.layout)),getString(R.string.checkusername), Snackbar.LENGTH_LONG).show();

                    }else if(text_input_layout_email.getError()!=null){
                        Snackbar.make((view.findViewById(R.id.layout)), R.string.checkmailaddress, Snackbar.LENGTH_LONG).show();


                    }else if(text_input_layout_password.getError()!=null){
                        Snackbar.make((view.findViewById(R.id.layout)), R.string.checkpass, Snackbar.LENGTH_LONG).show();

                    }else if(!checkBox.isChecked()){
                        Snackbar.make((view.findViewById(R.id.layout)), R.string.agreetotheterms, Snackbar.LENGTH_LONG).show();
                    }else if (everything_valid[0]){

                        // POST REQUEST CREATE ACCOUNT, hash password first
                        try {
                            new PostRequest(getContext()).execute("login", String.valueOf(UserName_input.getText()), hash.SHA512(String.valueOf(pass_input.getText())),String.valueOf(Email_input.getText()), String.valueOf(mUserdata.getId()),mUserdata.getAdvertiserID());//"language","selected_author","username"
                            mUserdata.setEmail(String.valueOf(Email_input.getText()));
                        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }


                }
            });



            final Handler ha1 = new Handler();

            ha1.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(!UserName_input.getText().equals("") && text_input_layout_name.getError()==null && !(Email_input.getText()).equals("")&& text_input_layout_email.getError()==null && !pass_input.getText().equals("") && text_input_layout_password.getError()==null && checkBox.isChecked()){
                        everything_valid[0] = true;

                        Log.d("valid:","true");

                        if(text_input_layout_email.getVisibility()==View.VISIBLE) {
                            email_signin_btn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                            email_login_btn.setBackgroundResource(android.R.drawable.btn_default);
                        }


                    }else{
                        everything_valid[0] = false;
                        email_signin_btn.setBackgroundResource(android.R.drawable.btn_default);
                    }
                    if(text_input_layout_email.getVisibility()==View.VISIBLE) {
                        email_login_btn.setBackgroundResource(android.R.drawable.btn_default);
                    }
                    ha1.postDelayed(this, 100);

                }
            }, 0);



        }
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);



    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
