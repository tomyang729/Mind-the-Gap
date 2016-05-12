package ca.ubc.cs.cpsc210.mindthegap.parsers;

import ca.ubc.cs.cpsc210.mindthegap.model.Arrival;
import ca.ubc.cs.cpsc210.mindthegap.model.Line;
import ca.ubc.cs.cpsc210.mindthegap.model.LineResourceData;
import ca.ubc.cs.cpsc210.mindthegap.model.Station;
import ca.ubc.cs.cpsc210.mindthegap.parsers.exception.TfLArrivalsDataMissingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * A parser for the data returned by the TfL station arrivals query
 */
public class TfLArrivalsParser extends TfLAbstractParser {

    /**
     * Parse arrivals from JSON response produced by TfL query.  All parsed arrivals are
     * added to given station assuming that corresponding JSON object as all of:
     * timeToStation, platformName, lineID and one of destinationName or towards.  If
     * any of the aforementioned elements is missing, the arrival is not added to the station.
     *
     * @param stn             station to which parsed arrivals are to be added
     * @param jsonResponse    the JSON response produced by TfL
     * @throws JSONException  when JSON response does not have expected format
     * @throws TfLArrivalsDataMissingException  when all arrivals are missing at least one of the following:
     * <ul>
     *     <li>timeToStation</li>
     *     <li>platformName</li>
     *     <li>lineId</li>
     *     <li>destinationName and towards</li>
     * </ul>
     */
    public static void parseArrivals(Station stn, String jsonResponse)
            throws JSONException, TfLArrivalsDataMissingException {
        // create JSONArray
        JSONArray arrivals = new JSONArray(jsonResponse);

        // if all arrivals miss key data, throw exception
        boolean flag = false;
        for(int i = 0; i < arrivals.length(); i++) {
            flag = arrivals.getJSONObject(i).has("timeToStation") && arrivals.getJSONObject(i).has("platformName") &&
                    arrivals.getJSONObject(i).has("lineId") && (arrivals.getJSONObject(i).has("destinationName") ||
                    arrivals.getJSONObject(i).has("towards"));

            if(flag) {
                // create an arrival

                String platformName = arrivals.getJSONObject(i).getString("platformName");
                String lineId = arrivals.getJSONObject(i).getString("lineId");
                String destination;
                if(arrivals.getJSONObject(i).has("destinationName")) {
                    destination = TfLAbstractParser.parseName(arrivals.getJSONObject(i).getString("destinationName"));
                } else {
                    destination = arrivals.getJSONObject(i).getString("towards");
                }
                int timeToStation = arrivals.getJSONObject(i).getInt("timeToStation");

                Arrival arvl = new Arrival(timeToStation, destination, platformName);

                // create a line using lineId
                Line line = new Line(LineResourceData.CENTRAL, " ", " ");
                for(Line l : stn.getLines()) {
                    if(l.getId().equals(lineId)) {
                        line = l;
                        break;
                    }
                }

                // add arrival to stn
                stn.addArrival(line, arvl);
            }
        }

        if(stn.getNumArrivalBoards() == 0) {
            throw new TfLArrivalsDataMissingException("missing key data");
        }
    }
}
