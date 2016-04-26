package com.dranithix.fishackathon.events;

import com.dranithix.fishackathon.entities.GhostGear;

public class GearSelectedEvent {
    public final GhostGear gear;
    public GearSelectedEvent(GhostGear gear) {
        this.gear = gear;
    }
}
