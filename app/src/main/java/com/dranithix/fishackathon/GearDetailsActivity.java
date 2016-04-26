package com.dranithix.fishackathon;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.dranithix.fishackathon.entities.GhostGear;
import com.dranithix.fishackathon.util.DebugUtil;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GearDetailsActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.imgPlaylistItemBg)
    ImageView image;

    @Bind(R.id.positionText)
    TextView positionText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear_details);
        ButterKnife.bind(this);

        GhostGear gear = getIntent().getParcelableExtra("gear");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(gear.getName());

        Picasso.with(this).load(gear.getImagePath()).fit().centerCrop().into(image);
        positionText.setText(DebugUtil.getFormattedLocationInDegree(gear.getPosition().latitude, gear.getPosition().longitude));
    }
}
