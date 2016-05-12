package ca.ubc.cs.cpsc210.mindthegap.tests.parsers;

import ca.ubc.cs.cpsc210.mindthegap.TfL.DataProvider;
import ca.ubc.cs.cpsc210.mindthegap.TfL.FileDataProvider;
import ca.ubc.cs.cpsc210.mindthegap.model.LineResourceData;
import ca.ubc.cs.cpsc210.mindthegap.parsers.BranchStringParser;
import ca.ubc.cs.cpsc210.mindthegap.parsers.TfLLineParser;
import ca.ubc.cs.cpsc210.mindthegap.parsers.exception.TfLLineDataMissingException;
import ca.ubc.cs.cpsc210.mindthegap.util.LatLon;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;


/**
 * Unit test for TfLLineParser
 */
public class TfLLineParserBasicTest {
    private String lineData;
    private String lineData2;

    @Before
    public void setUp() throws Exception {
        DataProvider dataProvider = new FileDataProvider("./res/raw/central_inbound.json");
        lineData = dataProvider.dataSourceToString();


    }

    @Test
    public void testBranchStringParserOK() {
//        LatLon locn1 = new LatLon(51.6037,0.093493);
//        LatLon locn2 = new LatLon(51.5956,0.091015);
//        LatLon locn3 = new LatLon(51.5857,0.088596);
//        LatLon locn4 = new LatLon(51.5757,0.090015);
//        LatLon locn5 = new LatLon(51.5765,0.066195);
//        List<LatLon> locns = new ArrayList<LatLon>();
//        locns.add(locn1);
//        locns.add(locn2);
//        locns.add(locn3);
//        locns.add(locn4);
//        locns.add(locn5);

        String testString = "[[[0.093493,51.6037],[0.091015,51.5956],[0.088596,51.5857],[0.090015,51.5757],[0.066195,51.5765]]]";

        List<LatLon> pts = BranchStringParser.parseBranch(testString);

        assertEquals(5, pts.size());

    }

    @Test
    public void testBranchStringemptyString() {
        String testString = "";
        List<LatLon> pts = BranchStringParser.parseBranch(testString);
        assertEquals(0, pts.size());
    }

    @Test //(expected = JSONException.class)
    public void testBranchStringParserException()  {
        String testString = "[]";
        List<LatLon> pts = BranchStringParser.parseBranch(testString);
        assertEquals(0, pts.size());
    }


    @Test
    public void testBasicLineParsing() {
        try {
            TfLLineParser.parseLine(LineResourceData.CENTRAL, lineData);
        } catch (JSONException e) {
            fail("JSONException should not have been thrown while parsing data in central_inbound.json");
        } catch (TfLLineDataMissingException e) {
            fail("TfLLineDataMissingException should not have been thrown while parsing data in central_inbound.json");
        }
    }

    // The single test above is very basic - it simply checks that your parser
    // doesn't throw an exception.


}