package ca.ubc.cs.cpsc210.mindthegap.TfL;

import ca.ubc.cs.cpsc210.mindthegap.model.Line;
import ca.ubc.cs.cpsc210.mindthegap.model.Station;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Wrapper for TfL Arrival Data Provider
 */
public class TfLHttpArrivalDataProvider extends AbstractHttpDataProvider {
    private Station stn;

    public TfLHttpArrivalDataProvider(Station stn) {
        super();
        this.stn = stn;
    }

    @Override
    /**
     * Produces URL used to query TfL web service for expected arrivals at
     * station specified in call to constructor.
     *
     * @returns URL to query TfL web service for arrival data
     */
    protected URL getURL() throws MalformedURLException {
        String request = "";
        String stopPointId = stn.getID();
        String lineId = "";
        for(Line line : stn.getLines()) {
            lineId = lineId.concat(line.getId() + ",");
        }
        lineId = lineId.substring(0, lineId.length()-1);

        request = "https://api.tfl.gov.uk/Line/" + lineId + "/Arrivals?stopPointId=" + stopPointId + "&app_id=&app_key= ";

        return new URL(request);
    }
}
