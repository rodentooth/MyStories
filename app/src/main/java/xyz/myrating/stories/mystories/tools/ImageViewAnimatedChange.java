package xyz.myrating.stories.mystories.tools;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Emanuel Graf on 28.04.2018.
 */

public class ImageViewAnimatedChange{
public static void ImageViewAnimatedChange(Context c, final ViewPager v, final int new_image, final int i) {
final Animation anim;
        if (i==1){
        anim = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        }else{
        anim  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);

        }
        anim.setDuration(12000);
        anim.setAnimationListener(new Animation.AnimationListener()
        {
@Override public void onAnimationStart(Animation animation) {
        if (i!=1)
        v.setBackgroundResource(new_image);
        v.setVisibility(View.VISIBLE);
        }
@Override public void onAnimationRepeat(Animation animation) {}
@Override public void onAnimationEnd(Animation animation) {
        if(i==1)
        v.setVisibility(View.INVISIBLE);
        }
        });
        v.startAnimation(anim);
        }
}