package edu.brown.cs.student.main.server.ServerUtility;

import edu.brown.cs.student.main.server.Data.BUSRouteStopMapping;
import edu.brown.cs.student.main.server.Data.BUSStops;
import edu.brown.cs.student.main.server.Exceptions.ShuttleDataException;
import edu.brown.cs.student.main.server.Main;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/** The SearchCSVHandler class handles HTTP requests for searching within loaded CSV data. */
public class Stops implements Route {

  /** Constructs a SearchCSVHandler instance. */
  public Stops() {}

  /**
   * Handles an HTTP request to search within loaded CSV data.
   *
   * @param request The HTTP request object.
   * @param response The HTTP response object.
   * @return A serialized response containing the search results or an error message.
   * @throws ShuttleDataException
   */
  @Override
  public Object handle(Request request, Response response) throws ShuttleDataException {
    Map<String, Object> result = new HashMap<>();
    Set<String> params = request.queryParams();

    if (params.contains("stopID")) {
      String stopID = request.queryParams("stopID");
      List<BUSRouteStopMapping> routeStopMap = Main.transLoc.getRouteStopMappings();
      Main.setRouteStopMap(routeStopMap);
      routeStopMap = Main.getRouteStopMapByStopID(Integer.parseInt(stopID));
      result.put("routes", routeStopMap);
    } else if (params.contains("routeID")) {
      String routeID = request.queryParams("routeID");
      List<BUSRouteStopMapping> routeStopMap = Main.transLoc.getRouteStopMappings();
      Main.setRouteStopMap(routeStopMap);
      routeStopMap = Main.getRouteStopMapByRouteID(Integer.parseInt(routeID));
      result.put("routes", routeStopMap);
    } else {
      List<BUSRouteStopMapping> routeStopMap = Main.transLoc.getRouteStopMappings();
      Main.setRouteStopMap(routeStopMap);
      routeStopMap = Main.getDefensiveRouteStopMap();
      result.put("routes", routeStopMap);
      List<BUSStops> stops = Main.transLoc.getStops();
      Main.setStops(stops);
      stops = Main.getDefensiveStops();
      result.put("stops", stops);
    }

    ServerResponse res = new ServerResponse(result);
    return res.serialize();
  }
}
