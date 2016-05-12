package ca.ubc.cs.cpsc210.mindthegap.model;

import java.util.*;

/**
 * Represents an arrivals board for a particular station, on a particular line,
 * for trains traveling in a particular direction (as indicated by platform prefix).
 *
 * Invariant: maintains arrivals in order of time to station
 * (first train to arrive will be listed first).
 */
public class ArrivalBoard implements Iterable<Arrival> {
    private List<Arrival> arrivals;
    private Line line;
    private String travelDirn;


    /**
     * Constructs an arrival board for the given line with an empty list of arrivals
     * and given travel direction.
     *
     * @param line        line on which arrivals listed on this board operate (cannot be null)
     * @param travelDirn  the direction of travel
     */
    public ArrivalBoard(Line line, String travelDirn) {
        arrivals = new ArrayList<Arrival>();
        this.line = line;
        this.travelDirn = travelDirn;
    }

    public Line getLine() {
        return line;
    }

    public String getTravelDirn() {
        return travelDirn;
    }


    /**
     * Get total number of arrivals posted on this arrival board
     *
     * @return  total number of arrivals
     */
    public int getNumArrivals() {
        return arrivals.size();
    }

    /**
     * Add a train arrival this arrivals board.
     * Assume that there is no requirement for the travelDirn of the arrival for now. I can add arrival with different drin's.
     *
     * @param arrival  the arrival to add to this arrivals board
     */
    public void addArrival(Arrival arrival) {
        int index = 0;
        for(Arrival a : arrivals) {
            if(a.compareTo(arrival) < 0) {
                index++;
            } else {
                break;
            }
        }
        arrivals.add(index, arrival);
    }

    /**
     * Clear all arrivals from this arrival board
     */
    public void clearArrivals() {
        arrivals.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrivalBoard arrivals = (ArrivalBoard) o;

        if (getLine() != null ? !getLine().equals(arrivals.getLine()) : arrivals.getLine() != null) return false;
        return !(getTravelDirn() != null ? !getTravelDirn().equals(arrivals.getTravelDirn()) : arrivals.getTravelDirn() != null);

    }

    @Override
    public int hashCode() {
        int result = getLine() != null ? getLine().hashCode() : 0;
        result = 31 * result + (getTravelDirn() != null ? getTravelDirn().hashCode() : 0);
        return result;
    }

    @Override
    /**
     * Produces an iterator over the arrivals on this arrival board
     * ordered by time to arrival (first train to arrive is produced
     * first).
     */
    public Iterator<Arrival> iterator() {
        // Do not modify the implementation of this method!
        return arrivals.iterator();
    }
}
