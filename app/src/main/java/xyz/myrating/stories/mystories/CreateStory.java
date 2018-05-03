package xyz.myrating.stories.mystories;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import xyz.myrating.stories.mystories.tools.PostRequest;
import xyz.myrating.stories.mystories.tools.userdata;

import static xyz.myrating.stories.mystories.tools.ImageViewAnimatedChange.ImageViewAnimatedChange;


public class CreateStory extends AppCompatActivity {

    RelativeLayout empty_state_your_suggestions;
    int bgimagenr = ThreadLocalRandom.current().nextInt(0  , 7 + 1);

    String selectedLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        final Context context = CreateStory.this;

        final EditText storyText = findViewById(R.id.new_story_text);

        final SharedPreferences prefs = this.getSharedPreferences(
                "storyDraft", Context.MODE_PRIVATE);
        if(!prefs.getString("draft","").equals("")){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setTitle(R.string.load_draft)
                    .setMessage(R.string.load_saved_draft)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            storyText.setText(prefs.getString("draft",""), TextView.BufferType.EDITABLE);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            prefs.edit().putString("draft", "").apply();

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else if(!prefs.getString("draft","").equals("")){
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setTitle(R.string.waitingforinternet)
                    .setMessage(R.string.postisstillposting)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


        Button create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userdata mUserdata = new userdata(v.getContext());
                new PostRequest(v.getContext()).execute("create_story", String.valueOf(mUserdata.getId()),mUserdata.getPass(), String.valueOf(storyText.getText()),selectedLanguage);//"language","selected_author","username"

                prefs.edit().putString("draft", "").apply();
                prefs.edit().putString("temp", String.valueOf(storyText.getText())).apply();
                prefs.edit().putString("temp_lang", selectedLanguage).apply();

            }
        });

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(storyText.getText()).equals(""))
                startActivity(new Intent(CreateStory.this, MainActivity.class));
                else{
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle(R.string.save_draft)
                            .setMessage(R.string.do_yu_want_to_save_draft)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    prefs.edit().putString("draft", String.valueOf(storyText.getText())).apply();
                                    startActivity(new Intent(CreateStory.this, MainActivity.class));

                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    prefs.edit().putString("draft", "").apply();
                                    startActivity(new Intent(CreateStory.this, MainActivity.class));

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        Spinner languageChooser = findViewById(R.id.language_chooser);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        final ArrayList<Locale> countries = new ArrayList<>();

        int default_lan = 0;
        for (int i =0; i<(Locale.getISOLanguages()).length;i++) {

            Locale l = new Locale(Locale.getISOLanguages()[i]);
            adapter.add(l.getDisplayLanguage());
            countries.add(l);
            if((Locale.getDefault().getLanguage()).equals(l.getLanguage()))
                default_lan=(i);
            selectedLanguage =l.toLanguageTag();

        }

        languageChooser.setAdapter(adapter);
        languageChooser.setSelection(default_lan);

        languageChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedLanguage = countries.get(position).toLanguageTag();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







        final ViewPager bg1 =  findViewById(R.id.bgpager1);
        final ViewPager bg2 =  findViewById(R.id.bgpager2);

        final Handler ha2 = new Handler();

        ha2.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(true) {
                    int icon = (R.mipmap.bg_suggestions_1);
                    int i1 = 0;
                    int i2 = 1;
                    switch (bgimagenr) {
                        case 0:
                            icon = (R.mipmap.bg_suggestions_2);
                            bgimagenr = 1;
                            i1 = 1;
                            i2 = 0;
                            break;
                        case 1:
                            icon = (R.mipmap.bg_suggestions_3);
                            bgimagenr = 2;
                            break;
                        case 2:
                            icon = (R.mipmap.bg_suggestions_4);
                            bgimagenr = 3;
                            i1 = 1;
                            i2 = 0;
                            break;
                        case 3:
                            icon = (R.mipmap.bg_suggestions_5);
                            bgimagenr = 4;
                            break;
                        case 4:
                            icon = (R.mipmap.bg_suggestions_6);
                            bgimagenr = 5;
                            i1 = 1;
                            i2 = 0;
                            break;
                        case 5:
                            icon = (R.mipmap.bg_suggestions_7);
                            bgimagenr = 6;
                            break;
                        case 6:
                            icon = (R.mipmap.bg_suggestions_8);
                            bgimagenr = 7;
                            i1 = 1;
                            i2 = 0;
                            break;
                        case 7:
                            icon = (R.mipmap.bg_suggestions_1);
                            bgimagenr = 0;
                            break;
                    }

                    ImageViewAnimatedChange(CreateStory.this, bg1, icon, i1);
                    ImageViewAnimatedChange(CreateStory.this, bg2, icon, i2);
                    if(!String.valueOf(storyText.getText()).equals(""))
                    prefs.edit().putString("draft", String.valueOf(storyText.getText())).apply();

                    ha2.postDelayed(this, 24000);
                }
            }
        }, 0);

    }
}
