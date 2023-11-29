package edu.brown.cs.student.main.server.Data;

import java.util.List;

public class BUSStops {
  private List<Route> routes;
  private List<Stop> stops;
  private boolean success;

  public BUSStops(List<Route> routes, List<Stop> stops, boolean success) {
    this.routes = routes;
    this.stops = stops;
    this.success = success;
  }

  public List<Route> getRoutes() {
    return routes;
  }

  public List<Stop> getStops() {
    return stops;
  }

  public boolean isSuccess() {
    return success;
  }

  public static class Route {
    private int id;
    private List<Integer> stops;

    public Route(int id, List<Integer> stops) {
      this.id = id;
      this.stops = stops;
    }

    public int getId() {
      return id;
    }

    public List<Integer> getStops() {
      return stops;
    }
  }

  public static class Stop {
    private String code;
    private String description;
    private int id;
    private String locationType;
    private String name;
    private Integer parentStationId;
    private double[] position;
    private String url;

    public Stop(
        String code,
        String description,
        int id,
        String locationType,
        String name,
        Integer parentStationId,
        double[] position,
        String url) {
      this.code = code;
      this.description = description;
      this.id = id;
      this.locationType = locationType;
      this.name = name;
      this.parentStationId = parentStationId;
      this.position = position;
      this.url = url;
    }

    public String getCode() {
      return code;
    }

    public String getDescription() {
      return description;
    }

    public int getId() {
      return id;
    }

    public String getLocationType() {
      return locationType;
    }

    public String getName() {
      return name;
    }

    public Integer getParentStationId() {
      return parentStationId;
    }

    public double[] getPosition() {
      return position;
    }

    public String getUrl() {
      return url;
    }
  }
}
