package ca.ubc.cs.cpsc210.mindthegap.tests.model;

import ca.ubc.cs.cpsc210.mindthegap.TfL.DataProvider;
import ca.ubc.cs.cpsc210.mindthegap.TfL.FileDataProvider;
import ca.ubc.cs.cpsc210.mindthegap.model.*;
import ca.ubc.cs.cpsc210.mindthegap.model.exception.StationException;
import ca.ubc.cs.cpsc210.mindthegap.parsers.TfLLineParser;
import ca.ubc.cs.cpsc210.mindthegap.util.LatLon;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Unit tests for StationManager
 */
public class StationManagerTest {
    private StationManager stnManager;

    private String lineData;
    Line linesample;

    @Before
    public void setUp() throws Exception{
        stnManager = StationManager.getInstance();
        stnManager.clearSelectedStation();
        stnManager.clearStations();


        DataProvider dataProvider = new FileDataProvider("./res/raw/central_inbound.json");
        lineData = dataProvider.dataSourceToString();
        linesample = TfLLineParser.parseLine(LineResourceData.CENTRAL, lineData);
    }


    /*
    Begin with ArrivalTests
    */
    @Test
    public void testArrivalConstructor() {
        Arrival arvl = new Arrival(30, "Victoria", "  West - Westminster  ");
        assertEquals("Victoria", arvl.getDestination());
        assertEquals("  West - Westminster  ", arvl.getPlatform());
    }

    @Test
    public void testGetTravelDirn() {
        Arrival arvl = new Arrival(30, "Victoria", "  West - Westminster  ");
        assertEquals("West", arvl.getTravelDirn());
    }

    @Test
    public void testGetPlatformName() {
        Arrival arvl = new Arrival(30, "Victoria", "  West - Oxford Circus  ");
        assertEquals("Oxford Circus", arvl.getPlatformName());
    }

    @Test
    public void testGetTimeToStationInMins1() {
        Arrival arvl = new Arrival(0, "Victoria", "  West - Westminster  ");
        assertEquals(0, arvl.getTimeToStationInMins());
    }

    @Test
    public void testGetTimeToStationInMins2() {
        Arrival arvl = new Arrival(30, "Victoria", "  West - Westminster  ");
        assertEquals(1, arvl.getTimeToStationInMins());
    }

    @Test
    public void testGetTimeToStationInMins3() {
        Arrival arvl = new Arrival(20, "Victoria", "  West - Westminster  ");
        assertEquals(1, arvl.getTimeToStationInMins());
    }

    @Test
    public void testGetTimeToStationInMins4() {
        Arrival arvl = new Arrival(60, "Victoria", "  West - Westminster  ");
        assertEquals(1, arvl.getTimeToStationInMins());
    }

    @Test
    public void testGetTimeToStationInMins5() {
        Arrival arvl = new Arrival(130, "Victoria", "  West - Westminster  ");
        assertEquals(3, arvl.getTimeToStationInMins());
    }

    /*
    Then ArrivalBoardTests
     */

    @Test
    public void testArrivalBoardConstructor() {
        Line line = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        Arrival arvl = new Arrival(130, "Queensway", "  East - Lancaster Gate  ");
        ArrivalBoard ab = new ArrivalBoard(line, arvl.getTravelDirn());

        assertEquals(0, ab.getNumArrivals());
        assertEquals("East", ab.getTravelDirn());
        Line line2 = ab.getLine();
        assertEquals(line, line2);
    }

    @Test
    public void testGetLine() {
        Line line = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        Arrival arvl = new Arrival(130, "Queensway", "  East - Lancaster Gate  ");
        ArrivalBoard ab = new ArrivalBoard(line, arvl.getTravelDirn());

        assertEquals(line, ab.getLine());
    }

    @Test
    public void testGetTravelDirn2() {
        Line line = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        Arrival arvl = new Arrival(130, "Queensway", "  East - Lancaster Gate  ");
        ArrivalBoard ab = new ArrivalBoard(line, arvl.getTravelDirn());

        assertEquals("East", ab.getTravelDirn());
    }

    @Test
    public void testGetNumArrivals() {
        Line line = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        Arrival arvl1 = new Arrival(130, "Queensway", "  East - Lancaster Gate  ");
        Arrival arvl2 = new Arrival(100, "Oxford Circus", "  East - Holborn  ");
        ArrivalBoard ab = new ArrivalBoard(line, arvl1.getTravelDirn());

        assertEquals(0, ab.getNumArrivals());

        ab.addArrival(arvl1);
        assertEquals(1, ab.getNumArrivals());

        ab.addArrival(arvl2);
        assertEquals(2, ab.getNumArrivals());

    }

    @Test
    public void testAddArrival() {
        Line line = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        Arrival arvl1 = new Arrival(130, "Queensway", "  East - Lancaster Gate  ");
        Arrival arvl2 = new Arrival(100, "Oxford Circus", "  Eastbound - Holborn  ");
        Arrival arvl3 = new Arrival(150, "Oxford Circus", "  West - Lancaster Gate  ");
        Arrival arvl4 = new Arrival(90, "Queensway", "  East - Lancaster Gate  ");
        ArrivalBoard ab = new ArrivalBoard(line, arvl1.getTravelDirn());

        assertEquals(0, ab.getNumArrivals());

        ab.addArrival(arvl1);
        ab.addArrival(arvl2);
        ab.addArrival(arvl3);
        ab.addArrival(arvl4);

        assertEquals(4, ab.getNumArrivals());

        Iterator<Arrival> it = ab.iterator();
        Arrival first = it.next();
        Arrival second = it.next();
        Arrival third = it.next();
        Arrival fourth = it.next();

        assertEquals(first, arvl4);
        assertEquals(second, arvl2);
        assertEquals(third, arvl1);
        assertEquals(fourth, arvl3);

    }

    @Test
    public void testClearArrivals() {
        Line line = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        Arrival arvl1 = new Arrival(130, "Queensway", "  East - Lancaster Gate  ");
        Arrival arvl2 = new Arrival(100, "Oxford Circus", "  Eastbound - Holborn  ");
        Arrival arvl3 = new Arrival(150, "Oxford Circus", "  West - Lancaster Gate  ");
        Arrival arvl4 = new Arrival(90, "Queensway", "  East - Lancaster Gate  ");
        ArrivalBoard ab = new ArrivalBoard(line, arvl1.getTravelDirn());

        assertEquals(0, ab.getNumArrivals());

        ab.addArrival(arvl1);
        ab.addArrival(arvl2);
        ab.addArrival(arvl3);
        ab.addArrival(arvl4);

        assertEquals(4, ab.getNumArrivals());

        ab.clearArrivals();
        assertEquals(0, ab.getNumArrivals());
    }

    /*
    Then BranchTests
     */


    /*
    Then LineTests
     */

    @Test
    public void testLineConstructor() {
        Line line = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        assertEquals(line.getStations().size(), 0);
        assertEquals(line.getBranches().size(), 0);
    }

    @Test
    public void testGetColour() {
        Line line = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        assertEquals(0xFFDC241F, line.getColour());
    }

    @Test
    public void testAddStation() {
        Line line = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        LatLon locn1 = new LatLon(10.0, 10.0);
        LatLon locn2 = new LatLon(20.0, 20.0);
        Station stn1 = new Station("01", "Oxford Circus", locn1);
        Station stn2 = new Station("02", "Holborn", locn2);

        assertEquals(0, line.getStations().size());

        line.addStation(stn1);
        line.addStation(stn2);
        line.addStation(stn2);

        assertEquals(2, line.getStations().size());

        Iterator<Station> it = line.iterator();
        Station first = it.next();
        Station second = it.next();

        assertEquals(first, stn1);
        assertEquals(second, stn2);
    }

    @Test
    public void testRemoveStation() {
        Line line = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        LatLon locn1 = new LatLon(10.0, 10.0);
        LatLon locn2 = new LatLon(20.0, 20.0);
        Station stn1 = new Station("01", "Oxford Circus", locn1);
        Station stn2 = new Station("02", "Holborn", locn2);

        assertEquals(0, line.getStations().size());

        line.addStation(stn1);
        line.addStation(stn2);
        line.removeStation(stn1);
        line.removeStation(stn1);

        assertEquals(1, line.getStations().size());
    }

    /*
    StationTests
     */
    @Test
    public void testAddLine() {
        LatLon locn = new LatLon(10.0, 10.0);
        Station stn = new Station("01", "Oxford Circus", locn);
        Line line1 = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        Line line2 = new Line(LineResourceData.BAKERLOO, "02", "bakerloo Line");

        assertEquals(0, stn.getLines().size());

        stn.addLine(line1);
        stn.addLine(line2);

        assertEquals(2, stn.getLines().size());
    }

    @Test
    public void testRemoveLine() {
        LatLon locn = new LatLon(10.0, 10.0);
        Station stn = new Station("01", "Oxford Circus", locn);
        Line line1 = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        Line line2 = new Line(LineResourceData.BAKERLOO, "02", "bakerloo Line");

        stn.addLine(line1);
        stn.addLine(line2);
        stn.removeLine(line1);
        stn.removeLine(line1);

        assertEquals(1, stn.getLines().size());
    }

    @Test
    public void testStationAddArrival() {
        LatLon locn = new LatLon(10.0, 10.0);
        Station stn = new Station("01", "Oxford Circus", locn);
        Station stn1 = new Station("00", "Epping", locn);
        Station stn2 = new Station("100", "Ealing Broadway", locn);
        Station stn3 = new Station("200", "Elephant & Castle", locn);
        Line line1 = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        Line line2 = new Line(LineResourceData.BAKERLOO, "02", "bakerloo Line");
        Line line3 = new Line(LineResourceData.NORTHERN, "03", "northern Line");
        stn.addLine(line1);
        stn.addLine(line2);
        line1.addStation(stn);
        line1.addStation(stn1);
        line1.addStation(stn2);
        line2.addStation(stn);
        line2.addStation(stn3);
        Arrival arvl1 = new Arrival(130, "Epping", "  Eastbound - Platform 1  ");  // CENTRAL  ab1
        Arrival arvl2 = new Arrival(130, "Ealing Broadway", "  Westbound - Platform 1  ");  // CENTRAL  ab2
        Arrival arvl3 = new Arrival(300, "Epping", "  Eastbound - Platform 1  ");  // CENTRAL  ab1
        Arrival arvl4 = new Arrival(100, "Elephant & Castle", "  Eastbound - Platform 1  "); // BAKERLOO  ab3
        Arrival arvl5 = new Arrival(150, "Epping", "  Eastbound - Platform 1  ");     // wrong platform do nothing

        assertEquals(0, stn.getNumArrivalBoards());

        stn.addArrival(line3, arvl1);   // wrong line do nothing
        stn.addArrival(line1, arvl5);   // wrong platform do nothing
        stn.addArrival(line1, arvl1);   // add b1(line1, east)
        stn.addArrival(line1, arvl2);   // add b2(line1, west)
        stn.addArrival(line1, arvl3);   // add arvl to b1
        stn.addArrival(line2, arvl4);   // add b3(line2, east)
        stn.addArrival(line1, arvl4);   // mismatching do nothing

        assertEquals(3, stn.getNumArrivalBoards());

        Iterator<ArrivalBoard> it = stn.iterator();
        ArrivalBoard first = it.next();
        ArrivalBoard second = it.next();
        assertEquals(2, first.getNumArrivals());
        assertEquals(1, second.getNumArrivals());
    }


    /*
    StationManagerTests
     */

    @Test
    public void testStationManagerConstructor() {
        assertEquals(stnManager.getSelected(), null);
    }




    @Test
    public void testGetStationWithId() {
        LatLon locn = new LatLon(10.0, 10.0);
        Line line1 = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        Station stn = new Station("01", "Oxford Circus", locn);
        Station stn1 = new Station("00", "Epping", locn);
        Station stn2 = new Station("100", "Ealing Broadway", locn);
        Station stn3 = new Station("200", "Elephant & Castle", locn);
        line1.addStation(stn);
        line1.addStation(stn1);
        line1.addStation(stn2);
        line1.addStation(stn3);
        stnManager.addStationsOnLine(line1);

        assertEquals(stnManager.getStationWithId("Apple"), null);
        assertEquals(stnManager.getStationWithId(""), null);
        assertEquals(stnManager.getStationWithId("100"), stn2);

    }

    @Test (expected = StationException.class)
    public void testSetSelectedUnkownStation() throws StationException{
        LatLon locn = new LatLon(10.0, 10.0);
        Line line1 = new Line(LineResourceData.CENTRAL, "01", "Central Line");
        Station stn = new Station("01", "Oxford Circus", locn);
        Station stn1 = new Station("00", "Epping", locn);
        Station stn2 = new Station("100", "Ealing Broadway", locn);
        Station stn3 = new Station("200", "Elephant & Castle", locn);
        line1.addStation(stn1);
        line1.addStation(stn2);
        line1.addStation(stn3);
        stnManager.addStationsOnLine(line1);

        stnManager.setSelected(stn);
    }

    @Test
    public void testSetSelected() {
//
        Station stn1 = linesample.getStations().get(1);


        try {
            stnManager.setSelected(stn1);
        }
        catch(StationException e) {
            fail("sth wrong");
        }

    }

//    @Test
//    public void testFindNearestTo() {
//        LatLon locn1 = new LatLon(0.06, 10.0);
//        LatLon locn2 = new LatLon(0.103097,51.6718);
//        LatLon locn3 = new LatLon(0.091015,51.5956);
//        Line line1 = new Line(LineResourceData.CENTRAL, "01", "Central Line");
//        Station stn1 = new Station("00", "Epping", locn1);
//        Station stn2 = new Station("100", "Ealing Broadway", locn2);
//        Station stn3 = new Station("200", "Elephant & Castle", locn3);
//        line1.addStation(stn1);
//        line1.addStation(stn2);
//        line1.addStation(stn3);
//        stnManager.addStationsOnLine(line1);
//
//        assertEquals(stnManager.findNearestTo(locn2), stn2);
//    }



}