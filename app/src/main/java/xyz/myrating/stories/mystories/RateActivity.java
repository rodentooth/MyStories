package xyz.myrating.stories.mystories;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import xyz.myrating.stories.mystories.RecyclerList.MyAdapter;
import xyz.myrating.stories.mystories.RecyclerList.VerticalViewPager;
import xyz.myrating.stories.mystories.story.story;
import xyz.myrating.stories.mystories.tools.PostRequest;
import xyz.myrating.stories.mystories.tools.hash;
import xyz.myrating.stories.mystories.tools.userdata;
import xyz.myrating.stories.mystories.tools.authenticate;

import static xyz.myrating.stories.mystories.tools.ImageViewAnimatedChange.ImageViewAnimatedChange;


/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link RateActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RateActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RateActivity extends Fragment {

    RelativeLayout empty_state_your_suggestions;
    int bgimagenr = ThreadLocalRandom.current().nextInt(0  , 7 + 1);
    Boolean play = false;
    int position =0;
    userdata mUserdata;

    Random rand = new Random();
    int seed = rand.nextInt(1000) + 1;

    public static RateActivity newInstance(String param1, String param2) {
        RateActivity fragment = new RateActivity();
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
        View view = inflater.inflate(R.layout.fragment_rate, container, false);

        mUserdata = new userdata(getContext());
        if(mUserdata.getId()==0)
        authenticate.authenticate(getContext());


        final ArrayList<story> feedsList = new ArrayList<>();


        //Setting the adapter for the stories
        final VerticalViewPager mPager =  view.findViewById(R.id.pager);

        final MyAdapter mAdapter = new MyAdapter(getActivity().getSupportFragmentManager(),feedsList,mPager);

        mPager.setAdapter(mAdapter);
        mPager.getAdapter().notifyDataSetChanged();


            //setting the background change
        empty_state_your_suggestions = view.findViewById(R.id.empty_state_your_suggestions);
        empty_state_your_suggestions.setVisibility(View.VISIBLE);
        empty_state_your_suggestions.setVisibility(View.INVISIBLE);


        new PostRequest(getContext(),feedsList,mAdapter,seed,position).execute("get_stories","","", String.valueOf(mUserdata.getId()));//"language","selected_author","username"

        final Handler ha1 = new Handler();

        ha1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (feedsList.size()>0) {

                    if (mAdapter.getCount() < mPager.getCurrentItem() + 1 && !feedsList.get(feedsList.size() - 1).getAuthor().equals("Huckleberry Finn")) {
                        Log.d("triggered by: ","last view"+mAdapter.getCount() );
                        new PostRequest(getContext(), feedsList, mAdapter, seed, position).execute("get_stories", "", "", String.valueOf(mUserdata.getId()));//"language","selected_author","username"
                    }

                    if (feedsList.get(mPager.getCurrentItem() ).isLoadingTrigger()) {
                        Log.d("triggered by: ","TiggeringView");
                        new PostRequest(getContext(), feedsList, mAdapter, seed, position).execute("get_stories", "", "", String.valueOf(mUserdata.getId()));//"language","selected_author","username"
                    }


                    if (feedsList.get(0).getStory().equals("NOSTORIES")) {
                        empty_state_your_suggestions.setVisibility(View.VISIBLE);
                        mPager.setVisibility(View.INVISIBLE);
                    } else {
                        empty_state_your_suggestions.setVisibility(View.INVISIBLE);
                        mPager.setVisibility(View.VISIBLE);
                    }
                }

                ha1.postDelayed(this, 1000);

            }
        }, 0);



        final ViewPager bg1 =  view.findViewById(R.id.bgpager1);
        final ViewPager bg2 =  view.findViewById(R.id.bgpager2);

        final Handler ha2 = new Handler();

        ha2.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(play){
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

                    ImageViewAnimatedChange(getActivity(), bg1, icon, i1);
                    ImageViewAnimatedChange(getActivity(), bg2, icon, i2);


                    ha2.postDelayed(this, 24000);
                }
            }
        }, 0);





        return view;
    }





    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        play=true;




    }

    @Override
    public void onDetach() {
        super.onDetach();
        play=false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
