package edu.brown.cs.student.main.server.ServerRouteUtility;

import spark.Request;
import spark.Response;
import spark.Route;

/** The Vehicles class handles HTTP requests for realtime vehicle data. */
public class Vehicles implements Route {

  public Vehicles() {}

  @Override
  public Object handle(Request request, Response response) {
    // WebSocket with realtime vehicle data

    return null;
  }
}
