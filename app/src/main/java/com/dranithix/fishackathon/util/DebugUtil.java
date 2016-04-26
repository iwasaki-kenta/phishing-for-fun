package com.dranithix.fishackathon.util;

import com.dranithix.fishackathon.entities.GhostGear;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 4/23/2016.
 */
public class DebugUtil {
    public static List<GhostGear> testGearData() {
        List<GhostGear> testData = new ArrayList<GhostGear>();
        testData.add(new GhostGear(0, "Fishing Net", new LatLng(22.396428, 114.10949700000003), "http://www.thehindu.com/multimedia/dynamic/00136/nets_136621f.jpg"));
        testData.add(new GhostGear(1, "Harpoon", new LatLng(22.61432941, 114.2028815), "https://www.topsknives.com/media/catalog/product/cache/1/image/464x/040ec09b1e35df139433887a97daa66f/h/o/hofhar01_1_.jpg"));
        return testData;
    }

    public static String getFormattedLocationInDegree(double latitude, double longitude) {
        try {
            int latSeconds = (int) Math.round(latitude * 3600);
            int latDegrees = latSeconds / 3600;
            latSeconds = Math.abs(latSeconds % 3600);
            int latMinutes = latSeconds / 60;
            latSeconds %= 60;

            int longSeconds = (int) Math.round(longitude * 3600);
            int longDegrees = longSeconds / 3600;
            longSeconds = Math.abs(longSeconds % 3600);
            int longMinutes = longSeconds / 60;
            longSeconds %= 60;
            String latDegree = latDegrees >= 0 ? "N" : "S";
            String lonDegrees = latDegrees >= 0 ? "E" : "W";

            return Math.abs(latDegrees) + "°" + latMinutes + "'" + latSeconds
                    + "\"" + latDegree + " " + Math.abs(longDegrees) + "°" + longMinutes
                    + "'" + longSeconds + "\"" + lonDegrees;
        } catch (Exception e) {

            return "" + String.format("%8.5f", latitude) + "  "
                    + String.format("%8.5f", longitude);
        }


    }
}
