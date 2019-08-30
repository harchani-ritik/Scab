package com.example.android.scab69;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public class Constants {
    public static final HashMap<String, LatLng> suggestionPoints = new HashMap<String, LatLng>();
    static {

        suggestionPoints.put("IIITA", new LatLng(25.429301, 81.770068));

        suggestionPoints.put("Allahabad Junction", new LatLng(25.445424, 81.825234));

        suggestionPoints.put("Civil Lines", new LatLng(25.454712, 81.834838));
    }
}
