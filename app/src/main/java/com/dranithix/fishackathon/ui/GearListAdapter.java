package com.dranithix.fishackathon.ui;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dranithix.fishackathon.R;
import com.dranithix.fishackathon.entities.GhostGear;
import com.dranithix.fishackathon.events.GearSelectedEvent;
import com.dranithix.fishackathon.util.DebugUtil;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GearListAdapter extends RecyclerView.Adapter<GearListAdapter.ViewHolder> {
    private final int NOTIFY_DELAY = 500;

    private List<GhostGear> gears;

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.image)
        ImageView coverArt;
        @Bind(R.id.positionText)
        TextView positionText;

        GhostGear gear;

        public void setGear(GhostGear gear) {
            this.gear = gear;
        }

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            EventBus.getDefault().post(new GearSelectedEvent( gear));
        }
    }

    public GearListAdapter(List<GhostGear> gears) {
        this.gears = gears;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_gear, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        GhostGear gear = gears.get(position);
        viewHolder.setGear(gear);

        Picasso.with(viewHolder.coverArt.getContext())
                .load(gear.getImagePath())
                .fit()
                .centerCrop()
                .into(viewHolder.coverArt);

        viewHolder.title.setText(gear.getName());
        viewHolder.positionText.setText(DebugUtil.getFormattedLocationInDegree(gear.getPosition().latitude, gear.getPosition().longitude));
    }

    @Override
    public long getItemId(int position) {
        return gears.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return gears.size();
    }

    public void addGear(final GhostGear gear, final int position) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gears.add(position, gear);
                notifyItemInserted(position);
            }
        }, NOTIFY_DELAY);
    }

    public void removeGear(final int position) {
        gears.remove(position);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyItemRemoved(position);
            }
        }, NOTIFY_DELAY);
    }

    // endregion
}