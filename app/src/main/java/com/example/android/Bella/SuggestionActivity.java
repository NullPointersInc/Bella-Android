package com.example.android.Bella;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;

public class SuggestionActivity extends MaterialAboutActivity {

    int colorIcon = R.color.colorIconDark;

    @Override
    protected MaterialAboutList getMaterialAboutList(Context c) {

    MaterialAboutCard.Builder homeCardBuilder = new MaterialAboutCard.Builder();
        homeCardBuilder.title("Home");

        homeCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Check my home status")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_home)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

        homeCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                    .text("Turn on light 1 in room")
                    .icon(new IconicsDrawable(c)
                            .icon(CommunityMaterial.Icon.cmd_lightbulb_on)
                            .color(ContextCompat.getColor(c, colorIcon))
                            .sizeDp(18))
                    .build());

        homeCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Turn off Sprinkler")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_blur_radial)
                        .color(ContextCompat.getColor(c, colorIcon))
            .sizeDp(18))
            .build());

        homeCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Give me the status of my kitchen")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_food_variant)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

    MaterialAboutCard.Builder detailCardBuilder = new MaterialAboutCard.Builder();
        detailCardBuilder.title("Current Details");

        detailCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("How's the weather today?")
                .icon(new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_wb_sunny)
                        .color(ContextCompat.getColor(c, colorIcon))
            .sizeDp(18))
            .build());

        detailCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Get me the latest news")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_newspaper)
                        .color(ContextCompat.getColor(c, colorIcon))
            .sizeDp(18))
            .build());

    MaterialAboutCard.Builder aiCardBuilder = new MaterialAboutCard.Builder();

        aiCardBuilder.title("AI");

        aiCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Play the song closer")
                .icon(new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_music_note)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

        aiCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Is this a good song?")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_bookmark_music)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

        aiCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("What is 15 * 10?")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_calculator)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

        aiCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Tell me some joke")
                .icon(new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_tag_faces)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

        aiCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("What date is it today?")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_calendar_today)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

        aiCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("What is your favourite food?")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_food)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

    MaterialAboutCard.Builder taskCardBuilder = new MaterialAboutCard.Builder();
        taskCardBuilder.title("Tasks");

        taskCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Set an alarm")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_alarm)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

        taskCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Remind me to feed my cat")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_calendar_clock)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

        taskCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Add apple mango to shopping list")
                .icon(new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_shopping_cart)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());

        taskCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Navigate me to Yelahanka")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_navigation)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .build());


        return new MaterialAboutList(homeCardBuilder.build(), detailCardBuilder.build(), aiCardBuilder.build(), taskCardBuilder.build());
}

    @Override
    protected CharSequence getActivityTitle() {
        return "Suggestion";
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }
}
