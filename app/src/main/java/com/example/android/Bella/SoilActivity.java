package com.example.android.Bella;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;


public class SoilActivity extends MaterialAboutActivity {
    public String id;
    public static String info;
    public static String cityName, type, farm, crop, cover, ssnS, ssnE;

    int red = R.color.red_500;
    int lightRed = R.color.red_400;
    int blue = R.color.light_blue_900;
    int yellow = R.color.yellow_600;
    int green = R.color.green_700;
    int purple = R.color.purple_500;
    int orange = R.color.orange_600;
    int grey = R.color.grey_500;
    int lightGreen = R.color.green_500;
    int brown = R.color.brown_500;

    @Override
    protected MaterialAboutList getMaterialAboutList(final Context c) {

        Log.d("City: ",cityName);


        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text("Crop Prediction")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_auto_fix)
                        .color(ContextCompat.getColor(c, red))
                        .sizeDp(18))
                .build());

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("City")
                .subText(cityName)
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_city)
                        .color(ContextCompat.getColor(c, brown))
                        .sizeDp(18))
                .build());

        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title("Type");

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Soil Type")
                .subText(type)
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_blackberry)
                        .color(ContextCompat.getColor(c, brown))
                        .sizeDp(18))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Farm Type")
                .subText(farm)
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_blur)
                        .color(ContextCompat.getColor(c, grey))
                        .sizeDp(18))
                .build());

        MaterialAboutCard.Builder appCardBuilder2 = new MaterialAboutCard.Builder();
        appCardBuilder2.title("Crop Details");

        appCardBuilder2.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_barley)
                        .color(ContextCompat.getColor(c, brown))
                        .sizeDp(18))
                .text("Crop Type")
                .subText(crop)
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .build()
        );


        appCardBuilder2.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_basket_fill)
                        .color(ContextCompat.getColor(c, orange))
                        .sizeDp(18))
                .text("Cover")
                .subText(cover)
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .build()
        );


        MaterialAboutCard.Builder appCardBuilder1 = new MaterialAboutCard.Builder();
        appCardBuilder1.title("Season");

        appCardBuilder1.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_ray_start_arrow)
                        .color(ContextCompat.getColor(c, brown))
                        .sizeDp(18))
                .text("Start Season")
                .subText(ssnS)
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .build()
        );

        appCardBuilder1.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_ray_end_arrow)
                        .color(ContextCompat.getColor(c, orange))
                        .sizeDp(18))
                .text("End Season")
                .subText(ssnE)
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .build()
        );

        return new MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build(), appCardBuilder2.build(), appCardBuilder1.build());


    }

    @Override
    protected CharSequence getActivityTitle() {
        return "Crop";
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }
}
