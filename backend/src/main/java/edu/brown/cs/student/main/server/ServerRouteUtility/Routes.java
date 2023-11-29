package edu.brown.cs.student.main.server.ServerRouteUtility;

import edu.brown.cs.student.main.server.Data.BUSRoute;
import edu.brown.cs.student.main.server.Exceptions.InvalidArgsException;
import edu.brown.cs.student.main.server.Exceptions.ShuttleDataException;
import edu.brown.cs.student.main.server.Main;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/** The Routes class handles HTTP requests for getting shuttle routes. */
public class Routes implements Route {

  public Routes() {}

  /**
   * Handles an HTTP request to get shuttle routes.
   *
   * @param request The HTTP request object.
   * @param response The HTTP response object.
   * @return A serialized response indicating the success or failure of the operation.
   * @throws ShuttleDataException
   * @throws Exception If there is an issue with the request.
   */
  @Override
  public Object handle(Request request, Response response)
      throws InvalidArgsException, ShuttleDataException {
    Map<String, Object> result = new HashMap<>();
    Set<String> params = request.queryParams();

    if (params.contains("routeID")) {
      List<BUSRoute> routes = Main.transLoc.getRoutes();
      Main.setRoutes(routes);
      String routeID = request.queryParams("routeID");
      routes = Main.getRouteByID(Integer.parseInt(routeID));
      result.put("routes", routes);
    } else {
      List<BUSRoute> routes = Main.transLoc.getRoutes();
      Main.setRoutes(routes);
      routes = Main.getDefensiveRoutes();
      result.put("routes", routes);
    }

    ServerResponse res = new ServerResponse(result);
    return res.serialize();
  }
}
