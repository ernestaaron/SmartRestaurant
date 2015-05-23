package com.engineeringbits.smartrestuarant;

import java.util.List;
import com.google.gson.Gson;

/**
 * Created by lveenis on 2015-05-23.
 */
public class ResponseData {

    private List<Location> results;

    public List<Location> getResults(){
        return results;
    }
    public static class Location {

        private String name;
        private String vicinity;
        private List<String> types;
        private Hours opening_hours;

        public String getName() { return name; }
        public String getVicinity() { return vicinity; }
        public List<String> getTypes() { return types; }
        public Hours getHours() { return opening_hours; }
    }

    public static class Hours {

        private Boolean open_now;
        private List<String> weekday_text;

        public String openStatus() { return open_now ? "OPEN" : "CLOSED"; }
        public List<String> getWeekdayText() { return weekday_text; }
    }
}
