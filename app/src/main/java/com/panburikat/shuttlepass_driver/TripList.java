package com.panburikat.shuttlepass_driver;

public class TripList {

    private String tripCode;
    private String origin;
    private String destination;
    private String via;

    public TripList(String tripCode, String origin, String destination, String via) {
        this.tripCode = tripCode;
        this.origin = origin;
        this.destination = destination;
        this.via = via;
    }

    public String getTripCode() {
        return tripCode;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getVia() {
        return via;
    }
}
