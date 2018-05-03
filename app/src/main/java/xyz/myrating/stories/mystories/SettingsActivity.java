package xyz.myrating.stories.mystories;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.myrating.stories.mystories.tools.PostRequest;
import xyz.myrating.stories.mystories.tools.hash;
import xyz.myrating.stories.mystories.tools.userdata;

public class SettingsActivity extends AppCompatActivity {

    EditText pass_input;
    EditText UserName_input;
    EditText Email_input;
    userdata mUserdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mUserdata = new userdata(this);
        final Boolean[] everything_valid = {false};

        final TextInputLayout text_input_layout_email =  findViewById(R.id.text_input_layout_email);
        Email_input = findViewById(R.id.Email_input);
        Email_input.setText(mUserdata.getEmail());
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

        final TextInputLayout text_input_layout_name =  findViewById(R.id.text_input_layout_name);
        UserName_input = findViewById(R.id.UserName_input);
        UserName_input.setText(mUserdata.getUsername());
        UserName_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && (text_input_layout_email.getVisibility()==View.VISIBLE)){
                    new PostRequest(SettingsActivity.this,text_input_layout_name).execute("username_availability_check_update", String.valueOf(UserName_input.getText()), String.valueOf(mUserdata.getId()));
                    text_input_layout_name.setError(getString(R.string.checkuingusername));

                }
            }
        });


        final TextInputLayout text_input_layout_password =  findViewById(R.id.text_input_layout_password);
         pass_input = findViewById(R.id.pass_input);

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

    }
    void updateProfile(View v) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(hash.SHA512(String.valueOf(pass_input.getText())).equals(mUserdata.getPass())){
            new PostRequest(this).execute("update", String.valueOf(UserName_input.getText()), hash.SHA512(String.valueOf(pass_input.getText())),String.valueOf(Email_input.getText()), String.valueOf(mUserdata.getId()),mUserdata.getAdvertiserID(),"update");//"language","selected_author","username"

        }else
            Snackbar.make((findViewById(R.id.layout)), R.string.passwordnotmatching, Snackbar.LENGTH_LONG).show();

        Log.d("PassInput: ",hash.SHA512(String.valueOf(pass_input.getText())));
        Log.d("PassActual: ",mUserdata.getPass());

    }

    void logout(View v){
        mUserdata.erase(false);
        startActivity(new Intent(this, MainActivity.class).putExtra("snackbar",getString(R.string.logoutsuccess)));
    }

    void resetPassword(View v){
        new PostRequest(this).execute("reset_password", String.valueOf(mUserdata.getId()));//"language","selected_author","username"

        Snackbar.make((findViewById(R.id.layout)), R.string.checkemails, Snackbar.LENGTH_LONG).show();

    }
    void showPrivacyPolicy(View v){
        String url = "http://stories.myrating.xyz/Privacy.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }
    void showTermsOfUse(View v){
        String url = "http://stories.myrating.xyz/Terms.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    void deletaAccount(View v){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(R.string.delacc)
                .setMessage(R.string.thiscannotbeundone)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new PostRequest(SettingsActivity.this).execute("delete_account", String.valueOf(mUserdata.getId()),mUserdata.getPass());//"language","selected_author","username"

                        mUserdata.erase(true);
                        startActivity(new Intent(SettingsActivity.this, MainActivity.class).putExtra("snackbar",getString(R.string.sadtoseeyougo)));


                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
