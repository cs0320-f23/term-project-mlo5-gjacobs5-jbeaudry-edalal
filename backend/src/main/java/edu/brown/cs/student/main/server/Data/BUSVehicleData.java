package edu.brown.cs.student.main.server.Data;

import java.util.List;

public class BUSVehicleData {
  public static class Vehicle {
    private long id;
    private Integer numCars;
    private String serviceStatus;
    private int agencyId;
    private long routeId;
    private long tripId;
    private String tripStart;
    private String tripEnd;
    private String gtfsTripId;
    private boolean direction;
    private long stopPatternId;
    private String callName;
    private Long currentStopId;
    private Long nextStop;
    private String arrivalStatus;
    private List<Double> position;
    private int heading;
    private double speed;
    private Long segmentId;
    private boolean offRoute;
    private long timestamp;
    private double load;
    private String apcStatus;

    public Vehicle(
        long id,
        Integer numCars,
        String serviceStatus,
        int agencyId,
        long routeId,
        long tripId,
        String tripStart,
        String tripEnd,
        String gtfsTripId,
        boolean direction,
        long stopPatternId,
        String callName,
        Long currentStopId,
        Long nextStop,
        String arrivalStatus,
        List<Double> position,
        int heading,
        double speed,
        Long segmentId,
        boolean offRoute,
        long timestamp,
        double load,
        String apcStatus) {
      this.id = id;
      this.numCars = numCars;
      this.serviceStatus = serviceStatus;
      this.agencyId = agencyId;
      this.routeId = routeId;
      this.tripId = tripId;
      this.tripStart = tripStart;
      this.tripEnd = tripEnd;
      this.gtfsTripId = gtfsTripId;
      this.direction = direction;
      this.stopPatternId = stopPatternId;
      this.callName = callName;
      this.currentStopId = currentStopId;
      this.nextStop = nextStop;
      this.arrivalStatus = arrivalStatus;
      this.position = position;
      this.heading = heading;
      this.speed = speed;
      this.segmentId = segmentId;
      this.offRoute = offRoute;
      this.timestamp = timestamp;
      this.load = load;
      this.apcStatus = apcStatus;
    }

    public long getId() {
      return id;
    }

    public Integer getNumCars() {
      return numCars;
    }

    public String getServiceStatus() {
      return serviceStatus;
    }

    public int getAgencyId() {
      return agencyId;
    }

    public long getRouteId() {
      return routeId;
    }

    public long getTripId() {
      return tripId;
    }

    public String getTripStart() {
      return tripStart;
    }

    public String getTripEnd() {
      return tripEnd;
    }

    public String getGtfsTripId() {
      return gtfsTripId;
    }

    public boolean isDirection() {
      return direction;
    }

    public long getStopPatternId() {
      return stopPatternId;
    }

    public String getCallName() {
      return callName;
    }

    public Long getCurrentStopId() {
      return currentStopId;
    }

    public Long getNextStop() {
      return nextStop;
    }

    public String getArrivalStatus() {
      return arrivalStatus;
    }

    public List<Double> getPosition() {
      return position;
    }

    public int getHeading() {
      return heading;
    }

    public double getSpeed() {
      return speed;
    }

    public Long getSegmentId() {
      return segmentId;
    }

    public boolean isOffRoute() {
      return offRoute;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public double getLoad() {
      return load;
    }

    public String getApcStatus() {
      return apcStatus;
    }
  }

  private boolean success;
  private List<Vehicle> vehicles;

  public boolean isSuccess() {
    return success;
  }

  public List<Vehicle> getVehicles() {
    return vehicles;
  }
}
