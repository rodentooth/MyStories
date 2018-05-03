package xyz.myrating.stories.mystories.RecyclerList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import xyz.myrating.stories.mystories.story.story;

/**
 * Created by Emanuel Graf on 28.04.2018.
 */


public class MyAdapter extends FragmentStatePagerAdapter {

    ArrayList<story> feedList =null;
    VerticalViewPager mPager;

    public MyAdapter(FragmentManager fragmentManager, ArrayList<story> s, VerticalViewPager Pager) {

        super(fragmentManager);
        feedList=s;
        mPager=Pager;
    }

    @Override
    public Fragment getItem(int position) {

        story item = new story("",0.0,"");

        if(feedList!=null && feedList.size()!=0) {
            item = feedList.get(position);

        }

        return StoryFragment.init(item,mPager);
    }


    @Override
    public int getCount() {
        if(feedList!=null) {
            return feedList.size();
        }
        else{
            return 0;
        }
    }


}
