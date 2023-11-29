package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.Data.BUSRoute;
import edu.brown.cs.student.main.server.Data.BUSRouteStopMapping;
import edu.brown.cs.student.main.server.Data.BUSStops;
import edu.brown.cs.student.main.server.Exceptions.ShuttleDataException;
import edu.brown.cs.student.main.server.ServerUtility.Routes;
import edu.brown.cs.student.main.server.ServerUtility.Stops;
import edu.brown.cs.student.main.server.ServerUtility.Vehicles;
import edu.brown.cs.student.main.server.TransLocUtility.CachedTransLocAPISource;
import edu.brown.cs.student.main.server.TransLocUtility.TransLocAPISource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import spark.Route;
import spark.Spark;

/** The Server class represents a server application */
public class Main {
  public static CachedTransLocAPISource transLoc;

  private static List<BUSRoute> routes = new ArrayList<>();
  // Defensive copy of routes to ensure immutability
  private static List<BUSRoute> defensiveRoutes = null;

  private static List<BUSRouteStopMapping> routeStopMap = new ArrayList<>();
  // Defensive copy of routeStopMap to ensure immutability
  private static List<BUSRouteStopMapping> defensiveRouteStopMap = null;

  private static List<BUSStops> stops = new ArrayList<>();
  // Defensive copy of stops to ensure immutability
  private static List<BUSStops> defensiveStops = null;

  /**
   * Get the defensive copy of routes.
   *
   * @return An unmodifiable list containing routes.
   */
  public static List<BUSRoute> getDefensiveRoutes() {
    if (defensiveRoutes == null) {
      defensiveRoutes = Collections.unmodifiableList(routes);
    }
    return defensiveRoutes;
  }

  public static List<BUSRoute> getRouteByID(int routeID) {
    List<BUSRoute> result = new ArrayList<>();
    for (BUSRoute route : getDefensiveRoutes()) {
      if (route.getId() == routeID) {
        result.add(route);
      }
    }
    return result;
  }

  /**
   * Set the routes with updated data.
   *
   * @param newRoutes The new route data to set.
   */
  public static void setRoutes(List<BUSRoute> newRoutes) {
    routes = newRoutes;
  }

  /**
   * Get the defensive copy of route stop mappings.
   *
   * @return An unmodifiable list containing route stop mappings.
   */
  public static List<BUSRouteStopMapping> getDefensiveRouteStopMap() {
    if (defensiveRouteStopMap == null) {
      defensiveRouteStopMap = Collections.unmodifiableList(routeStopMap);
    }
    return defensiveRouteStopMap;
  }

  public static List<BUSRouteStopMapping> getRouteStopMapByRouteID(int routeID) {
    List<BUSRouteStopMapping> result = new ArrayList<>();
    try {
      for (Object item : getDefensiveRouteStopMap()) {
        if (item instanceof BUSRouteStopMapping) {
            BUSRouteStopMapping routeStopMapping = (BUSRouteStopMapping) item;
            if (routeStopMapping.getId() == routeID) {
                result.add(routeStopMapping);
            }
        } else if (item instanceof Map) {
          Map<?, ?> mapItem = (Map<?, ?>) item;

          if (mapItem.containsKey("id") && mapItem.containsKey("stops")) {
            Object idObject = mapItem.get("id");
            Object stopsObject = mapItem.get("stops");

            if (idObject instanceof Number && stopsObject instanceof List<?>) {
              int id = ((Number) idObject).intValue();
              int[] stops = ((List<?>) stopsObject)
                      .stream()
                      .filter(stop -> stop instanceof Number)
                      .mapToInt(stop -> ((Number) stop).intValue())
                      .toArray();

              BUSRouteStopMapping routeStopMapping = new BUSRouteStopMapping(id, stops);
              if (routeStopMapping.getId() == routeID) {
                  result.add(routeStopMapping);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Error in getRouteStopMapByRouteID: " + e.getMessage());
    }
    return result;
  }

  public static List<BUSRouteStopMapping> getRouteStopMapByStopID(int stopID) {
    List<BUSRouteStopMapping> result = new ArrayList<>();
    try {
      for (Object item : routeStopMap) {
        if (item instanceof Map) {
          Map<?, ?> mapItem = (Map<?, ?>) item;

          if (mapItem.containsKey("id") && mapItem.containsKey("stops")) {
            Object idObject = mapItem.get("id");
            Object stopsObject = mapItem.get("stops");

            if (idObject instanceof Number && stopsObject instanceof List<?>) {
              int id = ((Number) idObject).intValue();
              int[] stops = ((List<?>) stopsObject)
                      .stream()
                      .filter(stop -> stop instanceof Number)
                      .mapToInt(stop -> ((Number) stop).intValue())
                      .toArray();

              BUSRouteStopMapping routeStopMapping = new BUSRouteStopMapping(id, stops);
              for (int stop : routeStopMapping.getStops()) {
                if (stop == stopID) {
                  result.add(routeStopMapping);
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Error in getRouteStopMapByStopID: " + e.getMessage());
    }
    return result;
  }

  /**
   * Set the route stop map with updated data.
   *
   * @param newRouteStopMap The new route stop map data to set.
   */
  public static void setRouteStopMap(List<BUSRouteStopMapping> newRouteStopMap) {
    routeStopMap = newRouteStopMap;
  }

  /**
   * Get the defensive copy of stops.
   *
   * @return An unmodifiable list containing stops.
   */
  public static List<BUSStops> getDefensiveStops() {
    if (defensiveStops == null) {
      defensiveStops = Collections.unmodifiableList(stops);
    }
    return defensiveStops;
  }

  /**
   * Set the stops with updated data.
   *
   * @param newStops The new stop data to set.
   */
  public static void setStops(List<BUSStops> newStops) {
    stops = newStops;
  }

  /**
   * The main method to start the server application.
   *
   * @param args Command-line arguments (not used).
   * @throws ShuttleDataException If there is an issue with the data source.
   */
  public static void main(String[] args) throws ShuttleDataException {
    int port = 3232;

    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    Spark.get("routes", (Route) new Routes());
    Spark.get("stops", (Route) new Stops());
    Spark.get("vehicles", (Route) new Vehicles());

    transLoc = new CachedTransLocAPISource(new TransLocAPISource(), 100, 5);

    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }
}
