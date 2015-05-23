package com.engineeringbits.smartrestuarant;

import java.util.List;
import com.google.gson.Gson;

/**
 * Created by lveenis on 2015-05-23.
 */
public class ResponseData {

    private List<Location> results;

    public static class Location {

        private String name;
        private String vicinity;
        private List<String> types;
        private Hours opening_hours;
    }

    public static class Hours {

        private Boolean open_now;
        private List<String> weekday_text;
    }
}
