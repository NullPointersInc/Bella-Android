package com.example.android.Bella;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.transition.Fade;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;

public class NewsActivity extends MaterialAboutActivity {

    int red = R.color.red_500;
    int lightRed = R.color.red_400;
    int blue = R.color.light_blue_500;
    int yellow = R.color.yellow_600;
    int green = R.color.green_700;
    int purple = R.color.purple_500;
    int orange = R.color.orange_600;
    int grey = R.color.grey_500;
    int lightGreen = R.color.green_500;
    int brown = R.color.brown_500;

    @Override
    protected MaterialAboutList getMaterialAboutList(final Context c) {

        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text("News for Today!")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_newspaper)
                        .color(ContextCompat.getColor(c, blue))
                        .sizeDp(18))
                .build());

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Publication")
                .subText("The Hindu")
                .icon(new IconicsDrawable(c)
                          .icon(CommunityMaterial.Icon.cmd_view_dashboard)
                          .color(ContextCompat.getColor(c, yellow))
                          .sizeDp(18))
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "News", "http://www.thehindu.com", true, false))
                .build());

        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title("Headlines");

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(MainActivity.obj[0].title)
                .subText(MainActivity.obj[0].desc)
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book_open)
                        .color(ContextCompat.getColor(c, grey))
                        .sizeDp(18))
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "News", MainActivity.obj[0].url, true, false))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(MainActivity.obj[1].title)
                .subText(MainActivity.obj[1].desc)
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book_open)
                        .color(ContextCompat.getColor(c, grey))
                        .sizeDp(18))
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "News", MainActivity.obj[1].url, true, false))
                .build());

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(MainActivity.obj[2].title)
                .subText(MainActivity.obj[2].desc)
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_book_open)
                        .color(ContextCompat.getColor(c, grey))
                        .sizeDp(18))
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "News", MainActivity.obj[2].url, true, false))
                .build());

        MaterialAboutCard.Builder appCardBuilder2 = new MaterialAboutCard.Builder();
        appCardBuilder2.title("Business News");

        appCardBuilder2.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_chart_line)
                        .color(ContextCompat.getColor(c, orange))
                        .sizeDp(18))
                .text(MainActivity.obj[6].title)
                .subText(MainActivity.obj[6].desc)
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "News", MainActivity.obj[6].url, true, false))
                .build()
        );


        appCardBuilder2.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_chart_line)
                        .color(ContextCompat.getColor(c, orange))
                        .sizeDp(18))
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "News", MainActivity.obj[7].url, true, false))
                .text(MainActivity.obj[7].title)
                .subText(MainActivity.obj[7].desc)
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .build()
        );

        appCardBuilder2.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_chart_line)
                        .color(ContextCompat.getColor(c, orange))
                        .sizeDp(18))
                .text(MainActivity.obj[8].title)
                .subText(MainActivity.obj[8].desc)
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "News", MainActivity.obj[8].url, true, false))
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .build()
        );


        MaterialAboutCard.Builder appCardBuilder1 = new MaterialAboutCard.Builder();
        appCardBuilder1.title("Sports News");

        appCardBuilder1.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_football)
                        .color(ContextCompat.getColor(c, brown))
                        .sizeDp(18))
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "News", MainActivity.obj[3].url, true, false))
                .text(MainActivity.obj[3].title)
                .subText(MainActivity.obj[3].desc)
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .build()
        );

        appCardBuilder1.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_football)
                        .color(ContextCompat.getColor(c, brown))
                        .sizeDp(18))
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "News", MainActivity.obj[4].url, true, false))
                .text(MainActivity.obj[4].title)
                .subText(MainActivity.obj[4].desc)
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .build()
        );

        appCardBuilder1.addItem(new MaterialAboutActionItem.Builder()
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_football)
                        .color(ContextCompat.getColor(c, brown))
                        .sizeDp(18))
                .text(MainActivity.obj[5].title)
                .subText(MainActivity.obj[5].desc)
                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "News", MainActivity.obj[5].url, true, false))
                .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                .build()
        );

        return new MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build(), appCardBuilder2.build(), appCardBuilder1.build());


    }

    @Override
    protected CharSequence getActivityTitle() {
        return "News";
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fade s = new Fade();
        s.setDuration(1000);
        getWindow().setEnterTransition(s);

        super.onCreate(savedInstanceState);

        /*setContentView(R.layout.newslayout);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.newscontainer, new NewsFragment())
                    .commit();
        }*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

}
