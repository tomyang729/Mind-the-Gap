package ca.ubc.cs.cpsc210.mindthegap.model;

import ca.ubc.cs.cpsc210.mindthegap.util.LatLon;

import java.util.*;

/**
 * Represents a station on the underground with an id, name, location (lat/lon)
 * set of lines with stops at this station and a list of arrival boards.
 */
public class Station implements Iterable<ArrivalBoard> {
    private List<ArrivalBoard> arrivalBoards;
    private Set<Line> lines;
    private String id;
    private String name;
    private LatLon locn;


    /**
     * Constructs a station with given id, name and location.
     * Set of lines and list of arrival boards are empty.
     *
     * @param id    the id of this station (cannot by null)
     * @param name  name of this station
     * @param locn  location of this station
     */
    public Station(String id, String name, LatLon locn) {
        this.id = id;
        this.name = name;
        this.locn = locn;
        arrivalBoards = new ArrayList<ArrivalBoard>();
        lines = new HashSet<Line>();
    }

    public String getName() {
        return name;
    }

    public LatLon getLocn() {
        return locn;
    }

    public String getID() {
        return id;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public int getNumArrivalBoards() {
        return arrivalBoards.size();
    }

    /**
     * Add line to set of lines with stops at this station.
     *
     * @param line  the line to add
     */
    public void addLine(Line line) {

        if(!hasLine(line)) {
            lines.add(line);
            line.addStation(this);
        }
    }

    /**
     * Remove line from set of lines with stops at this station
     *
     * @param line the line to remove
     */
    public void removeLine(Line line) {
        if(this.hasLine(line)) {
            lines.remove(line);
            line.removeStation(this);
        }
    }

    /**
     * Determine if this station is on a given line
     * @param line  the line
     * @return  true if this station is on given line
     */
    public boolean hasLine(Line line) {
        for(Line l : lines) {
            if(l.equals(line)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add train arrival travelling on a particular line in a particular direction to this station.
     * Arrival is added to corresponding arrival board based on the line on which it is
     * operating and the direction of travel (as indicated by platform prefix).  If the arrival
     * board for given line and travel direction does not exist, it is created and added to
     * arrival boards for this station.
     *
     * @param line    line on which train is travelling
     * @param arrival the train arrival to add to station   // assume that arrivals are all correct constructed
     */
    public void addArrival(Line line, Arrival arrival) {
        //boolean flag = false;                 // indicates that if the given arrival is on the given line
        boolean mark = true;                  // indicates the absence of a new board

//        for(Station stn : line.getStations()) {
//            if(stn.getName().equals(arrival.getDestination())) {
//                flag = true;
//                break;
//            }
//        }
        if (this.hasLine(line)) {
            while (mark) {
                for (ArrivalBoard ab : arrivalBoards) {
                    if (ab.getLine().equals(line) && ab.getTravelDirn().equals(arrival.getTravelDirn())) {
                        ab.addArrival(arrival);
                        mark = false;
                        break;
                    }
                }
                if (mark) {
                    ArrivalBoard newBoard = new ArrivalBoard(line, arrival.getTravelDirn());
                    arrivalBoards.add(newBoard);
                }
            }
        }
    }

    /**
     * Remove all arrival boards from this station
     */
    public void clearArrivalBoards() {
        arrivalBoards.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station that = (Station) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public Iterator<ArrivalBoard> iterator() {
        // Do not modify the implementation of this method!
        return arrivalBoards.iterator();
    }
}
