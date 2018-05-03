package xyz.myrating.stories.mystories;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private int currentFrag=0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_rate:
                    selectedFragment = RateActivity.newInstance("test","test2");
                    if(currentFrag==0){
                        return true;
                    }
                    currentFrag=0;

                    break;
                case R.id.navigation_your_ratings:
                    selectedFragment = MyStories.newInstance("test","test2");
                    if(currentFrag==1){
                        return true;
                    }
                    currentFrag=1;

                    break;
            }

            transaction.replace(R.id.mainActivity_frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainActivity_frame_layout, RateActivity.newInstance("test","test2"));
        transaction.commit();

       Intent intent = getIntent();

        if(intent.hasExtra("snackbar")){
                      Snackbar.make((findViewById(R.id.mainActivity_frame_layout)),intent.getStringExtra("snackbar"), Snackbar.LENGTH_LONG).show();
        }


    }




}
