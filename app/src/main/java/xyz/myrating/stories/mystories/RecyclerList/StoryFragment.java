package xyz.myrating.stories.mystories.RecyclerList;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import xyz.myrating.stories.mystories.R;
import xyz.myrating.stories.mystories.story.story;
import xyz.myrating.stories.mystories.tools.PostRequest;
import xyz.myrating.stories.mystories.tools.userdata;


public class StoryFragment extends Fragment {


    story  story           = new story("",0.0,"");
    VerticalViewPager mPager;




    public StoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            story             =(story)getArguments().getSerializable("story"           ) ;
            mPager             =(VerticalViewPager)getArguments().getSerializable("pager"           ) ;

        }
    }
    public static Fragment init(story story2, VerticalViewPager Pager) {

        Fragment fragment = new StoryFragment();

        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putSerializable(   "story"             , story2);
        args.putSerializable(   "pager"             , Pager);


        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
                final View layoutView = inflater.inflate(R.layout.fragment_story,
                        container, false);

        final LinearLayout storyText = layoutView.findViewById(R.id.story_text_linear_layout);


        //Here I get The width of the display:
        android.view.Display display = ((android.view.WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int)(display.getWidth()*0.75),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, 0, (int)(display.getWidth()*0.05), 0);


        String text = story.getStory();
        while(text.length()>500){

            TextView t = new TextView(storyText.getContext());
            t.setText(text.substring(0,500));
            text =text.substring(500);

            RelativeLayout rl = new RelativeLayout(storyText.getContext());
            rl.setLayoutParams(params);
            rl.addView(t);
            storyText.addView(rl);

        }
        TextView t = new TextView(storyText.getContext());
        t.setText(text);

        params.setMargins(0, 0, 100, 0);
        RelativeLayout rl = new RelativeLayout(storyText.getContext());
        rl.setLayoutParams(params);

        rl.addView(t);
        storyText.addView(rl);


        TextView author = layoutView.findViewById(R.id.author_text);
        author.setText(story.getAuthor());

        final TextView avg_rating = layoutView.findViewById(R.id.avg_rating);
        avg_rating.setText("Ø "+story.getAvg_rating());


        final Boolean[] canTurn = {true};



        RatingBar ratingBar =  layoutView.findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                try {
                    if (mPager.getChildCount() >= mPager.getCurrentItem() + 1 && ratingBar.getVisibility()!=(View.GONE) && avg_rating.getVisibility()!=View.VISIBLE)

                    //mPager.setCurrentItem(3,true);
                    new PostRequest(getContext()).execute("rate",String.valueOf(rating), String.valueOf(story.getId()), String.valueOf( new userdata(getContext()).getId()));//"language","selected_author","username"

                    Toast.makeText(getContext(), "rating..", Toast.LENGTH_SHORT).show();

                }catch (IllegalStateException e){

                }
                avg_rating.setVisibility(View.VISIBLE);
                if(story.getAvg_rating()==0.0)avg_rating.setText("Ø "+String.valueOf(rating));
                //ratingBar.setVisibility(View.GONE);
                canTurn[0] =false;


            }
        });



        return layoutView;
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
