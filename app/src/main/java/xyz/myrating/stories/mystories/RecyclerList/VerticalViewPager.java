package xyz.myrating.stories.mystories.RecyclerList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.Serializable;

import xyz.myrating.stories.mystories.R;

/**
 * Created by Emanuel Graf on 27.04.2018.
 */
public class VerticalViewPager extends ViewPager implements Serializable {

    public VerticalViewPager(Context context) {
        super(context);
        init();
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_ALWAYS);



    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        float ratingBarY =0;
        float avgY =0;


        @Override
        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }




            RatingBar ratingBar = view.findViewById(R.id.ratingBar);
            if(ratingBarY ==0)
             ratingBarY = ratingBar.getY();
            ratingBar.setY(position < 0 ? ratingBarY+position*view.getHeight(): ratingBarY-position*view.getHeight());

            CardView mC = view.findViewById(R.id.cardView);

            if(Math.abs(mC.getY()-ratingBarY) >30)
                ratingBarY = mC.getY()+30;

            if(Math.abs(ratingBarY-avgY) >50 || Math.abs(ratingBarY-avgY) <20)
                avgY = (float)(ratingBarY+(view.getHeight()*0.06));


            TextView txt = view.findViewById(R.id.avg_rating);
            if(avgY ==0)
                avgY = ratingBar.getY();
            txt.setY(position < 0 ? avgY+position*view.getHeight(): avgY-position*view.getHeight());

            //Log.d("Y","ratingBarY: "+ ratingBarY+" avgY: "+avgY);



        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev); // return touch coordinates to original reference frame for any child views
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapXY(ev));
    }

}