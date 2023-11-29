package edu.brown.cs.student.main.server.TransLocRouteUtility;

import edu.brown.cs.student.main.server.Data.BUSRoute;
import edu.brown.cs.student.main.server.Data.BUSRouteStopMapping;
import edu.brown.cs.student.main.server.Data.BUSStops;
import edu.brown.cs.student.main.server.Exceptions.ShuttleDataException;
import java.util.List;

/**
 * The ACSAPISource interface defines a contract for data sources that provide information from the
 * TransLoc API.
 */
public interface APISource {
  List<BUSRoute> getRoutes() throws ShuttleDataException;

  List<BUSStops> getStops() throws ShuttleDataException;

  List<BUSRouteStopMapping> getRouteStopMappings() throws ShuttleDataException;
}
