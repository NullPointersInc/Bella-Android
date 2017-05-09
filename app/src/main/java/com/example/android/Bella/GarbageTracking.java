package com.example.android.Bella;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.AnimationSet;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class GarbageTracking extends AppCompatActivity {
    ImageView truck,bin1,bin2,bin3;
    int green = R.color.green_700;
    int red = R.color.red_700;
    Animation anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_tracking);

        truck = (ImageView)findViewById(R.id.truck);
        bin1 = (ImageView)findViewById(R.id.bin1);
        bin2 = (ImageView)findViewById(R.id.bin2);
        bin3 = (ImageView)findViewById(R.id.bin3);


        truck.setBackgroundDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_truck)
                .color(ContextCompat.getColor(this, green))
                .sizeDp(18));

        bin1.setBackgroundDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_bitbucket)
                .color(ContextCompat.getColor(this, red))
                .sizeDp(18));

        bin2.setBackgroundDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_bitbucket)
                .color(ContextCompat.getColor(this, red))
                .sizeDp(18));

        bin3.setBackgroundDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_bitbucket)
                .color(ContextCompat.getColor(this, red))
                .sizeDp(18));


      /*  Animation t1 = new TranslateAnimation(0,400,0,0);
        Animation t2 = new TranslateAnimation(0,0,0,100);
        Animation t3 = new TranslateAnimation(0,100,0,0);
        Animation t4 = new TranslateAnimation(0,0,0,500);
        Animation t5 = new TranslateAnimation(0,-500,0,0);
        Animation t6 = new TranslateAnimation(0,0,0,300);
        AnimationSet s = new AnimationSet(false);
      //  s.addAnimation(t5);
      //  s.addAnimation(t4);
      //  s.addAnimation(t3);
        s.addAnimation(t2);
        s.addAnimation(t1);





        s.setDuration(3000);
        s.setFillAfter(true);
       truck.startAnimation(s); */

       /* ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {


            }
        });
        ta.setRepeatCount(5);

        ta.setFillEnabled(true);*/

       anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate);
       truck.startAnimation(anim);



    }

}
