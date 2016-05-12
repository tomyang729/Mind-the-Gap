package ca.ubc.cs.cpsc210.mindthegap.parsers;


import ca.ubc.cs.cpsc210.mindthegap.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parser for route strings in TfL line data
 */
public class BranchStringParser {

    /**
     * Parse a branch string obtained from TFL
     *
     * @param branch  branch string
     * @return       list of lat/lon points parsed from branch string
     */
    public static List<LatLon> parseBranch(String branch) {
        List<LatLon> pts = new ArrayList<LatLon>();
        String sub;

        if(branch.length() >= 3) {
             sub = branch.substring(1, branch.length() - 1);
        } else {
            sub = "[]";
        }

        try{
            JSONArray jarray = new JSONArray(sub);
            for(int i = 0; i < jarray.length(); i++) {

                Double lon = jarray.getJSONArray(i).getDouble(0);
                Double lat = jarray.getJSONArray(i).getDouble(1);

                LatLon point = new LatLon(lat, lon);
                pts.add(point);
            }
        }
        catch(JSONException e) {
            return pts = new ArrayList<LatLon>();
        }

        return pts;
    }
}
