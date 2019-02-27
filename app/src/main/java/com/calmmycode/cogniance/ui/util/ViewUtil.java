package com.calmmycode.cogniance.ui.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.calmmycode.cogniance.R;

public class ViewUtil {
    public static void showExitToast(Context context){
        Toast toast = Toast.makeText(context, context.getString(R.string.close_app), Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }
    public static Animation getFragmentFadeInFadeOutAnimation(Context context, boolean enter, View fragmentView){
        Animation animation = AnimationUtils.loadAnimation(context, enter ? R.anim.fade_in : R.anim.fade_out);
        animation.setInterpolator(context, enter ? android.R.anim.decelerate_interpolator :  android.R.anim.accelerate_interpolator);

        if (fragmentView != null) {
            fragmentView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    fragmentView.setLayerType(View.LAYER_TYPE_NONE, null);
                }
                @Override
                public void onAnimationRepeat(Animation arg0) { }
                @Override
                public void onAnimationStart(Animation arg0) { }
            });
        }

        return animation;
    }
}
