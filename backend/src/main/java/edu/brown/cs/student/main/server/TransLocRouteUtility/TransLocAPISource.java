package edu.brown.cs.student.main.server.TransLocRouteUtility;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.Data.BUSRoute;
import edu.brown.cs.student.main.server.Data.BUSRouteStopMapping;
import edu.brown.cs.student.main.server.Data.BUSStops;
import edu.brown.cs.student.main.server.Exceptions.ShuttleDataException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import okio.Buffer;

public class TransLocAPISource implements APISource {
  private static double[] convertToDoubleArray(List<Double> list) {
    double[] array = new double[list.size()];
    for (int i = 0; i < list.size(); i++) {
      array[i] = list.get(i);
    }
    return array;
  }

  public static List<BUSRoute> convertToBUSRouteList(List<Map<String, Object>> dataList) {
    List<BUSRoute> busRoutes = new ArrayList<>();

    for (Map<String, Object> data : dataList) {
      int agencyId = ((Number) data.get("agency_id")).intValue();
      double[] bounds = convertToDoubleArray((List<Double>) data.get("bounds"));
      String color = (String) data.get("color");
      String description = (String) data.get("description");
      int id = ((Number) data.get("id")).intValue();
      boolean isActive = (Boolean) data.get("is_active");
      String longName = (String) data.get("long_name");
      String shortName = (String) data.get("short_name");
      String textColor = (String) data.get("text_color");
      String type = (String) data.get("type");
      String url = (String) data.get("url");

      BUSRoute busRoute =
          new BUSRoute(
              agencyId,
              bounds,
              color,
              description,
              id,
              isActive,
              longName,
              shortName,
              textColor,
              type,
              url);
      busRoutes.add(busRoute);
    }

    return busRoutes;
  }

  @Override
  public List<BUSRoute> getRoutes() throws ShuttleDataException {
    try {
      Object response = deserializeTransLocData("https://feeds.transloc.com/3/routes?agencies=635");

      if (response instanceof Map) {
        Map<String, Object> jsonResponse = (Map<String, Object>) response;
        List<Map<String, Object>> routesDataList =
            (List<Map<String, Object>>) jsonResponse.get("routes");

        return convertToBUSRouteList(routesDataList);
      }
    } catch (IOException e) {
      throw new ShuttleDataException(e.getMessage());
    }

    return Collections.emptyList();
  }

  @Override
  public List<BUSStops> getStops() throws ShuttleDataException {
    try {
      Object response =
          deserializeTransLocData(
              "https://feeds.transloc.com/3/stops?agencies=635&include_routes=true");

      if (response instanceof Map) {
        Map<String, Object> jsonResponse = (Map<String, Object>) response;
        List<Map<String, Object>> stopsDataList =
            (List<Map<String, Object>>) jsonResponse.get("stops");

        // Convert 'id' field from double to int directly in the list
        for (Map<String, Object> stopData : stopsDataList) {
          double originalId = (Double) stopData.get("id");
          int newId = (int) originalId;
          stopData.put("id", newId);
        }

        // Cast the list to List<BUSStops> if needed
        List<BUSStops> stopsList = (List<BUSStops>) (List<?>) stopsDataList;

        return stopsList;
      }
    } catch (IOException e) {
      throw new ShuttleDataException(e.getMessage());
    }

    return Collections.emptyList();
  }

  @Override
  public List<BUSRouteStopMapping> getRouteStopMappings() throws ShuttleDataException {
    try {
      Object response =
          deserializeTransLocData(
              "https://feeds.transloc.com/3/stops?agencies=635&include_routes=true");

      if (response instanceof Map) {
        Map<String, Object> jsonResponse = (Map<String, Object>) response;
        List<Map<String, Object>> routesDataList =
            (List<Map<String, Object>>) jsonResponse.get("routes");

        // Convert 'id' field from double to int and 'stops' list
        for (Map<String, Object> routeData : routesDataList) {
          double originalId = (Double) routeData.get("id");
          int newId = (int) originalId;
          routeData.put("id", newId);

          // Assuming there's a 'stops' field in BUSRouteStopMapping
          List<Double> stopsList = (List<Double>) routeData.get("stops");
          List<Integer> newStopsList =
              stopsList.stream().map(Double::intValue).collect(Collectors.toList());
          routeData.put("stops", newStopsList);
        }

        // Cast the list to List<BUSRouteStopMapping> if needed
        List<BUSRouteStopMapping> routeStopMappingsList =
            (List<BUSRouteStopMapping>) (List<?>) routesDataList;

        return routeStopMappingsList;
      }
    } catch (IOException e) {
      throw new ShuttleDataException(e.getMessage());
    }

    return Collections.emptyList();
  }

  private Object deserializeTransLocData(String url) throws IOException, ShuttleDataException {
    URL requestURL = new URL(url);
    HttpURLConnection connection = connect(requestURL);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Object> adapter = moshi.adapter(Object.class);
    Object responseObj = adapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
    connection.disconnect();
    return responseObj;
  }

  private HttpURLConnection connect(URL requestURL) throws ShuttleDataException, IOException {
    URLConnection urlConnection = requestURL.openConnection();
    if (!(urlConnection instanceof HttpURLConnection)) {
      throw new ShuttleDataException("Unexpected: result of connection wasn't HTTP");
    }
    HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
    clientConnection.connect();
    if (clientConnection.getResponseCode() != 200) {
      throw new ShuttleDataException(
          "Unexpected: API connection not successful, status "
              + clientConnection.getResponseMessage());
    }
    return clientConnection;
  }
}