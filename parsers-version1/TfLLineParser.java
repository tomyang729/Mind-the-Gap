package ca.ubc.cs.cpsc210.mindthegap.parsers;

import ca.ubc.cs.cpsc210.mindthegap.model.*;
import ca.ubc.cs.cpsc210.mindthegap.parsers.exception.TfLLineDataMissingException;
import ca.ubc.cs.cpsc210.mindthegap.util.LatLon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A parser for the data returned by TFL line route query
 */
public class TfLLineParser extends TfLAbstractParser {

    /**
     * Parse line from JSON response produced by TfL.  No stations added to line if TfLLineDataMissingException
     * is thrown.
     *
     * @param lmd              line meta-data
     * @return                 line parsed from TfL data
     * @throws JSONException   when JSON data does not have expected format
     * @throws TfLLineDataMissingException when
     * <ul>
     *  <li> JSON data is missing lineName, lineId or stopPointSequences elements </li>
     *  <li> for a given sequence: </li>
     *    <ul>
     *      <li> the stopPoint array is missing </li>
     *      <li> all station elements are missing one of name, lat, lon or stationId elements </li>
     *    </ul>
     * </ul>
     */
    public static Line parseLine(LineResourceData lmd, String jsonResponse)
            throws JSONException, TfLLineDataMissingException {
        // get jsonobject
        JSONObject lineInfo = new JSONObject(jsonResponse);

        // create line object, if missing data throws exception
        if(!lineInfo.has("lineId") || !lineInfo.has("lineName") ||
                !lineInfo.has("stopPointSequences")) {
            throw new TfLLineDataMissingException("missing key data");
        }
        String lineId = lineInfo.getString("lineId");
        String lineName = lineInfo.getString("lineName");
        Line line = new Line(lmd, lineId, lineName);

        // build up branches in line
        JSONArray branchStrings = lineInfo.getJSONArray("lineStrings");

        for(int i = 0; i < branchStrings.length(); i++) {
            Branch branch = new Branch(branchStrings.getString(i));
            line.getBranches().add(branch);
        }

        // build up stations in line  (stopPointSequences has colloection<stopPoint>; stopPoint is List<station data>)
        JSONArray stopPointSequences = lineInfo.getJSONArray("stopPointSequences");

        for(int i = 0; i < stopPointSequences.length(); i++) {
            parseStopPoint(line, stopPointSequences.getJSONObject(i));
        }

        return line;
    }

    // helper
    private static void parseStopPoint(Line line, JSONObject stopPointObj)
            throws JSONException, TfLLineDataMissingException {
        // if missing "stopPoint" key, throws TfLLineDataMissingException
        if(!stopPointObj.has("stopPoint")) {
            throw new TfLLineDataMissingException("missing key data");
        } else {
            // get the stopPoint JSONArray
            JSONArray stopPoint = stopPointObj.getJSONArray("stopPoint");

            // if all stations missing data throws TfLLineDataMissingException
            boolean flag = false;              // indicate if there is a common missing key
            for(int i = 0; i < stopPoint.length(); i++) {
                flag =  stopPoint.getJSONObject(i).has("name") && stopPoint.getJSONObject(i).has("lat") &&
                        stopPoint.getJSONObject(i).has("lon") && stopPoint.getJSONObject(i).has("stationId");
                if(flag == true) {
                    JSONObject obj = stopPoint.getJSONObject(i);
                    String name = parseName(obj.getString("name"));  // use TfLAbstractParser
                    String stationId = obj.getString("stationId");
                    Double lon = obj.getDouble("lon");
                    Double lat = obj.getDouble("lat");
                    LatLon locn = new LatLon(lat, lon);

                    // create stnManager, check whether the stn is in the stnManager
                    StationManager stnManager = StationManager.getInstance();
                    Station stn = stnManager.getStationWithId(stationId);

                    if(stn == null) {
                        stn = new Station(stationId, name, locn);
                    }

                    line.addStation(stn);
                }
            }

            if(line.getStations().size() == 0) {
                throw new TfLLineDataMissingException("missing key data");
            }


        }


    }
}
