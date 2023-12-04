package edu.brown.cs.student.main.server.TransLocUtility;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.Data.BUSRoute;
import edu.brown.cs.student.main.server.Data.BUSRouteStopMapping;
import edu.brown.cs.student.main.server.Data.BUSStops;
import edu.brown.cs.student.main.server.Data.BUSVehicleData;
import edu.brown.cs.student.main.server.Exceptions.ShuttleDataException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

  public Map<String, List<?>> getVehicleData() throws ShuttleDataException {
    try {
      Object response =
          deserializeTransLocData("https://feeds.transloc.com/3/vehicle_statuses?agencies=635&include_arrivals=true");

      if (response instanceof Map) {
        Map<String, Object> jsonResponse = (Map<String, Object>) response;
        List<Map<String, Object>> vehiclesDataList =
            (List<Map<String, Object>>) jsonResponse.get("vehicles");
        List<Map<String, Object>> arrivalsDataList =
            (List<Map<String, Object>>) jsonResponse.get("arrivals");

        List<BUSVehicleData.Vehicle> vehicleData =
            (List<BUSVehicleData.Vehicle>) (List<?>) vehiclesDataList;

        Map<String, List<?>> result = new HashMap<>();
        result.put("vehicles", vehicleData);
        result.put("arrivals", arrivalsDataList);

        return result;
      }
    } catch (IOException e) {
      throw new ShuttleDataException(e.getMessage());
    }

    return (Map<String, List<?>>) Collections.emptyList();
  }

  public Map<String, Object> parseVehicleData(Map<String, List<?>> data) {
    Map<String, Object> result = new HashMap<>();
    List<Map<String, Object>> resultList = new ArrayList<>();
    List<Map<String, Object>> arrivalsList = new ArrayList<>();

    List<?> vehiclesList = data.get("vehicles");
    List<?> arrivalsDataList = data.get("arrivals");

    for (int i = 0; i < vehiclesList.size(); i++) {
        resultList.add(removeDecimalFromMap((Map<String, Object>) vehiclesList.get(i)));
    }

    for (int i = 0; i < arrivalsDataList.size(); i++) {
        arrivalsList.add(removeDecimalFromMap((Map<String, Object>) arrivalsDataList.get(i)));
    }

    result.put("success", true);
    result.put("vehicles", resultList);
    result.put("arrivals", arrivalsList);

    return result;
  }

  public String mapToJson(Map<String, Object> map) {
    StringBuilder json = new StringBuilder("{");

    for (Map.Entry<String, Object> entry : map.entrySet()) {
        json.append("\"").append(entry.getKey()).append("\":");

        if (entry.getValue() instanceof List) {
            List<?> list = (List<?>) entry.getValue();
            json.append(listToJson(list));
        } else {
            json.append("\"").append(removeDecimal(entry.getValue())).append("\"");
        }

        json.append(",");
    }

    if (json.charAt(json.length() - 1) == ',') {
        json.deleteCharAt(json.length() - 1);
    }

    json.append("}");

    return json.toString();
  }

  public String listToJson(List<?> list) {
    StringBuilder json = new StringBuilder("[");

    for (Object item : list) {
        if (item instanceof Map) {
            json.append(mapToJson((Map<String, Object>) item));
        } else {
            json.append("\"").append(removeDecimal(item)).append("\"");
        }

        json.append(",");
    }

    if (json.charAt(json.length() - 1) == ',') {
        json.deleteCharAt(json.length() - 1);
    }

    json.append("]");

    return json.toString();
  }

  private Object removeDecimal(Object value) {
    if (value instanceof Double) {
        String stringValue = String.valueOf(value);
        if (stringValue.endsWith(".0")) {
            return Integer.parseInt(stringValue.substring(0, stringValue.length() - 2));
        } else if (stringValue.contains("E")) {
            return (int) Double.parseDouble(stringValue);
        } else {
            return value;
        }
    } else {
        return value;
    }
  }

  private Map<String, Object> removeDecimalFromMap(Map<String, Object> map) {
    Map<String, Object> newMap = new HashMap<>();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
        newMap.put(entry.getKey(), removeDecimal(entry.getValue()));
    }
    return newMap;
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
