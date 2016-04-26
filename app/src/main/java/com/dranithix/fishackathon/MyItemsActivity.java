package com.dranithix.fishackathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.dranithix.fishackathon.entities.GhostGear;
import com.dranithix.fishackathon.events.GearSelectedEvent;
import com.dranithix.fishackathon.ui.GearListAdapter;
import com.dranithix.fishackathon.util.DebugUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyItemsActivity extends AppCompatActivity {
    @Bind(R.id.list)
    RecyclerView list;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new GearListAdapter(DebugUtil.testGearData()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onGearSelected(GearSelectedEvent event) {
        Intent intent = new Intent(this, GearDetailsActivity.class);
        intent.putExtra("gear", event.gear);
        startActivity(intent);
    }
}
