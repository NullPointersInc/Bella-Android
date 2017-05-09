package com.example.android.Bella;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

public class GarbageTracking extends AppCompatActivity {
    ImageView truck,bin1,bin2,bin3;
    int green = R.color.green_700;
    int red = R.color.red_700;

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
    }
}
