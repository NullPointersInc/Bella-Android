package com.example.android.Bella;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.mikepenz.iconics.context.IconicsContextWrapper;

public class SettingActivity extends MaterialAboutActivity {

    public static final String THEME_EXTRA = "";
    public static final int THEME_LIGHT_DARKBAR = 1;

    protected int colorIcon = R.color.colorIconDark;

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull final Context c) {
        return AboutApp.createMaterialAboutList(c, colorIcon, getIntent().getIntExtra(THEME_EXTRA, THEME_LIGHT_DARKBAR));
    }

    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.mal_title_about);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_MaterialAboutActivity);
        super.onCreate(savedInstanceState);
    }
}
