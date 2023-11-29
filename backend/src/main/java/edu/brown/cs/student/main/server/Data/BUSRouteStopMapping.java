package edu.brown.cs.student.main.server.Data;

public class BUSRouteStopMapping {
  private int id;
  private int[] stops;

  public BUSRouteStopMapping(int id, int[] stops) {
    this.id = id;
    this.stops = stops;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int[] getStops() {
    return stops;
  }

  public void setStops(int[] stops) {
    this.stops = stops;
  }
}
