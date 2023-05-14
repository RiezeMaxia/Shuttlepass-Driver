package com.panburikat.shuttlepass_driver;

public class TripHistoryList {

    private String tripID;
    private String origin;
    private String destination;
    private String via;
    private String timestamp;
    private String departureTime;

    public TripHistoryList(String tripID, String origin, String destination, String via, String timestamp, String departureTime) {
        this.tripID = tripID;
        this.origin = origin;
        this.destination = destination;
        this.via = via;
        this.timestamp = timestamp;
        this.departureTime = departureTime;
    }

    public String getTripID() {
        return tripID;
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

    public String getTimestamp() {
        return timestamp;
    }

    public String getDepartureTime() {
        return departureTime;
    }
}
