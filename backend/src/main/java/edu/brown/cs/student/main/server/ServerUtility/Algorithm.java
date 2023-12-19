package edu.brown.cs.student.main.server.ServerUtility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import edu.brown.cs.student.main.server.Data.BUSRouteStopMapping;
import edu.brown.cs.student.main.server.Data.BUSStops;
import edu.brown.cs.student.main.server.Data.BUSVehicleData;
import edu.brown.cs.student.main.server.Main;
import edu.brown.cs.student.main.server.TransLocUtility.TransLocAPISource;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
// import com.google.common.collect.LinkedHashMultimap;
// import com.squareup.moshi.LinkedHashTreeMap;

public class Algorithm implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    HashMap<String, Object> fin_map = new HashMap<>();
    // try {
    // HashMap<String, Object> fin_map = new HashMap<>();
    // fin_map.put("resp", "MADE!!!");
    // return fin_map;

    String start_lat = request.queryParams("s_lat");
    String start_long = request.queryParams("s_long");
    String end_lat = request.queryParams("e_lat");
    String end_long = request.queryParams("e_long");
    String tim = request.queryParams("time");
    Integer tim1 = Integer.parseInt(tim);
    LinkedList<Double> s_stop = new LinkedList<>();
    LinkedList<Double> e_stop = new LinkedList<>();
    // Double s_laaa = Double.parseDouble(start_lat);
    // Double.parseDouble(start_long);
    double fin_val = 100000000000000.0;
    LinkedList<Double> starting = new LinkedList<>();
    starting.add(Double.parseDouble(start_lat));
    starting.add(Double.parseDouble(start_long));
    LinkedList<Double> ending = new LinkedList<>();
    ending.add(Double.parseDouble(end_lat));
    ending.add(Double.parseDouble(end_long));
    List<BUSRouteStopMapping> routeStopMap = Main.transLoc.getRouteStopMappings();
    TransLocAPISource source = new TransLocAPISource();
    LinkedList<double[]> curr_pos = new LinkedList<>();
    Map<String, List<?>> vehicle_data = source.getVehicleData();
    LinkedList<Integer> route1 = new LinkedList();
    LinkedList<Integer> route2 = new LinkedList();
    LinkedList<Integer> route3 = new LinkedList();
    LinkedList<Integer> route4 = new LinkedList();
    Map<Integer, LinkedList<Integer>> routes = new HashMap();
    route1.add(4209268);
    route1.add(4209270);
    route1.add(4209290);
    route1.add(4209292);
    route1.add(4209294);
    route1.add(4209296);
    route1.add(4209298);
    route1.add(4212918);
    route2.add(4212894);
    route2.add(4212896);
    route2.add(4212898);
    route2.add(4212900);
    route2.add(4212902);
    route2.add(4209564);
    route2.add(4212886);
    route2.add(4212888);
    route2.add(4212890);
    route2.add(4209278);
    route2.add(4209562);
    route2.add(4212892);
    route3.add(4212904);
    route3.add(4209564);
    route3.add(4209268);
    route3.add(4212906);
    route3.add(4212908);
    route3.add(4209566);
    route3.add(4212910);
    route3.add(4212912);
    route3.add(4209288);
    route3.add(4212914);
    route3.add(4212888);
    route4.add(4209306);
    route4.add(4209294);
    route4.add(4251698);
    route4.add(4209286);
    route4.add(4209288);
    route4.add(4212920);
    route4.add(4209276);
    route4.add(4209278);
    route4.add(4209280);
    route4.add(4209282);
    route4.add(4251700);
    route4.add(4209302);
    route4.add(4209304);
    routes.put(4010022, route1);
    routes.put(4010116, route2);
    routes.put(4010118, route3);
    routes.put(4010762, route4);

    List<BUSVehicleData.Vehicle> vehicleData =
        (List<BUSVehicleData.Vehicle>) (List<?>) vehicle_data.get("vehicles");
    if (vehicleData.size() == 0) {
      fin_map.put("resp", "No Shuttles Running Currently");
      return fin_map;
    } else {
      try {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(vehicleData);
        Type listType = new TypeToken<List<JsonObject>>() {}.getType();
        List<JsonObject> vehicleDataList = gson.fromJson(jsonString, listType);
        double route_id = 0.0;
        double next_stop = 5.0;
        // if next_stop is 0.0 then the shuttle is deactivated
        for (JsonObject obj : vehicleDataList) {
          if (obj.has("route_id")) {
            route_id = obj.get("route_id").getAsDouble();
            // System.out.println("Next Stop: " + nextStopValue);
          }
          if (obj.has("next_stop")) {
            next_stop = obj.get("next_stop").getAsDouble();
            // System.out.println("Next Stop: " + nextStopValue);
          }
          if (next_stop == 0.0) {
            continue;
          }
          int roundedroute_id = Math.toIntExact(Math.round(route_id));
          System.out.println(next_stop);
          int next_stop_id = Math.toIntExact(Math.round(next_stop));
          System.out.println(roundedroute_id);
          System.out.println(next_stop_id);
          // List<BUSRouteStopMapping> mapp = Main.getRouteStopMapByRouteID(roundedroute_id);
          LinkedList<Integer> stops = routes.get(roundedroute_id);
          LinkedList<double[]> location = new LinkedList<>();
          for (int h = 0; h < stops.size(); h++) {
            double[] hi = {6.0, 5.0};
            location.add(hi);
          }
          // Main.getRouteStopMapByStopID(0);
          // int[] stops = mapp.get(0).getStops();
          // List<Integer> ch_stops = new LinkedList();
          // for (int y = 0; y < stops.length; y++) {
          //   ch_stops.add(stops[y]);
          // }
          // fin_map.put("map?", stops);
          // return fin_map;
          //       // mapp.get(0).
          //       // Array<Integer> stops = mapp.get(0).getStops();
          //       // route.get(0).
          //       // route.get(0).
          //       // BUSStops stops = new

          // double[]
          int indddex = 0;
          List<BUSStops> stops3 = Main.transLoc.getStops();
          Gson gson2Gson2 = new GsonBuilder().setPrettyPrinting().create();
          String jsonString2 = gson2Gson2.toJson(stops3);
          Type listType2 = new TypeToken<List<JsonObject>>() {}.getType();
          List<JsonObject> stoppps2 = gson.fromJson(jsonString2, listType2);
          // stops3 = Main.getDefensiveStops();
          // fin_map.put("stops", stoppps2);
          // fin_map.put("data", vehicleDataList);
          // return fin_map;
          // System.out.println(stoppps2);
          for (JsonObject stopppp : stoppps2) {
            System.out.println(stopppp);
            String loc = "";
            // if (stopppp.getRoutes().get(0).getStops())
            // List<Stop> s_list_1 = stopppp.getStops();
            // for (int y = 0; y < s_list_1.size(); y++) {
            if (stopppp.has("position") && stopppp.has("id")) {
              int identi = stopppp.get("id").getAsInt();
              if (stops.contains(identi)) {
                JsonArray array = stopppp.getAsJsonArray("position");
                double[] doubleArray = new double[array.size()];
                for (int i = 0; i < array.size(); i++) {
                  doubleArray[i] = array.get(i).getAsDouble();
                }
                // loc = stopppp.get("position").getAsString();
                // System.out.println(loc);
                // String[] values = loc.substring(1, loc.length() - 1).split(",");
                // System.out.println(values);
                // // Convert string values to double and store in a double array
                // double[] doubleArray = new double[values.length];
                // for (int i = 0; i < values.length; i++) {
                //   doubleArray[i] = Double.parseDouble(values[i]);
                // }
                System.out.println(doubleArray);
                int index = stops.indexOf(identi);
                location.set(index, doubleArray);
              }

              // System.out.println("Next Stop: " + nextStopValue);
            }
            // if (stopppp.has("route_id")) {
            //   route_id = stopppp.get("route_id").getAsDouble();
            //   // System.out.println("Next Stop: " + nextStopValue);
            // }
            //   if (stops.contains(s_list_1.get(y).getId())) {
            //     int index = stops.indexOf(s_list_1.get(y).getId());
            //     location.set(index, s_list_1.get(y).getPosition());
            //   }
            // }
          }
          // fin_map.put("end", e_stop);
          // return fin_map;
          // LinkedList<double[]> lat_long = new LinkedList<>();
          // for (int k = 0; k < stops3.size(); k++) {
          //   List<BUSStops.Route> act_route = stops3.get(k).getRoutes();
          //   Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
          //   String jsonString2 = gson2.toJson(act_route);
          //   Type listType2 = new TypeToken<List<JsonObject>>() {}.getType();
          //   List<JsonObject> routes_list_fin = gson.fromJson(jsonString2, listType2);
          //   for (JsonObject obj2 : routes_list_fin){
          //     if (obj2.has("id")){
          //       double act_id = obj2.get("id").getAsDouble();

          //     }
          //   }
          //   for (int j = 0; j < act_route.size(); j++) {
          //     if (act_route.get(j).getStops().equals(stops)) {
          //       for (int u = 0; u < act_route.get(j).getStops().size(); u++) {
          //         if (stops3.get(k).getStops().get(u).getId() == 4212886) {
          //           curr_pos.add(stops3.get(k).getStops().get(u).getPosition());
          //           indddex = u;
          //         }
          //         // LinkedList<double[]> pospos = new LinkedList<>();
          //         // pospos.add(stops3.get(k).getStops().get(u).getPosition());
          //         lat_long.add(stops3.get(k).getStops().get(u).getPosition());
          //       }
          //     }
          //   }
          //   //   //   // stops3.get(k).getRoutes().get(0).getStops()
          //   //   //   // stops3.get(k).getStops().get(0).getPosition();
          // }
          // // // current position
          LinkedList<LinkedList<Double>> res =
              Dijkstra_algo(starting, ending, tim1, location, curr_pos, location.size(), indddex);
          if ((double) res.get(0).get(0) < fin_val) {
            fin_val = (double) res.get(0).get(0);
            s_stop = (LinkedList<Double>) res.get(1);
            e_stop = (LinkedList<Double>) res.get(2);
          }
          //     System.out.println("Next Stop not found.");
          // }
        }

        // // // // algorithm()
        // // // mapp.get(0).
        // // // Main.getRouteStopMapByStopID(i);
        // // // Main.
        // // // route.get(0).
        // // // //route.get(0).
        // // // //route.
        // // // }
        fin_map.put("start", s_stop);
        fin_map.put("end", e_stop);
        return fin_map;
        // fin_map.put("stop_next", nextStopValue);
        // next_
        // LinkedHashMultimap<String, Object> vehicleMap = (LinkedHashMultimap<String, Object>)
        // vehicleData.get(0);
        // convert to a string and then parse the string for the correct value
        // fin_map.put("Veh_data2", vehicleData.get(1));
        // fin_map.put("Veh_data3", vehicleData.get(2));
        // fin_map.put("Veh_data4", vehicleData.get(3));

        // fin_map.put("Next_stop", vehicleData.get(0).getNextStop());
        // return fin_map;
      } catch (Exception e) {
        fin_map.put("Error Message", e.getMessage());
        fin_map.put("Veh_data2", vehicleData);
        return fin_map;
      }
    }
  }
  // fin_map.put("resp", "Made it");
  // return fin_map;
  //   }
  // }
  // return fin_map;}}
  //     for (int i = 0; i < vehicleData.size(); i++) {
  //       Vehicle veh = vehicleData.get(i);
  //       fin_map.put("Veh_data", vehicleData.get(i));
  //       long route_id = veh.getRouteId();
  //       List<BUSRoute> route = Main.getRouteByID((int) route_id);

  //       long nex_stop = veh.getNextStop();
  //       List<BUSRouteStopMapping> mapp = Main.getRouteStopMapByStopID((int) nex_stop);
  //       int[] stops = mapp.get(0).getStops();
  //       List<Integer> ch_stops = new LinkedList();
  //       for (int y = 0; y < stops.length; y++) {
  //         ch_stops.add(stops[y]);
  //       }
  //       // mapp.get(0).
  //       // Array<Integer> stops = mapp.get(0).getStops();
  //       // route.get(0).
  //       // route.get(0).
  //       // BUSStops stops = new
  //       int indddex = 0;
  //       List<BUSStops> stops3 = Main.transLoc.getStops();
  //       stops3 = Main.getDefensiveStops();
  //       LinkedList<double[]> lat_long = new LinkedList<>();
  //       for (int k = 0; k < stops3.size(); k++) {
  //         List<BUSStops.Route> act_route = stops3.get(k).getRoutes();
  //         for (int j = 0; j < act_route.size(); j++) {

  //           if (act_route.get(j).getStops() == ch_stops) {
  //             for (int u = 0; u < act_route.get(j).getStops().size(); u++) {
  //               if (stops3.get(k).getStops().get(u).getId() == nex_stop) {
  //                 curr_pos.add(stops3.get(k).getStops().get(u).getPosition());
  //                 indddex = u;
  //               }
  //               LinkedList<double[]> pospos = new LinkedList<>();
  //               pospos.add(stops3.get(k).getStops().get(u).getPosition());
  //               lat_long.add(stops3.get(k).getStops().get(u).getPosition());
  //             }
  //           }
  //         }
  //         // stops3.get(k).getRoutes().get(0).getStops()
  //         // stops3.get(k).getStops().get(0).getPosition();
  //       }
  //       // current position
  //       LinkedList<LinkedList<Double>> res =
  //           Dijkstra_algo(starting, ending, tim1, lat_long, curr_pos, lat_long.size(), indddex);
  //       if ((double) res.get(0).get(0) < fin_val) {
  //         fin_val = (double) res.get(0).get(0);
  //         s_stop = (LinkedList<Double>) res.get(1);
  //         e_stop = (LinkedList<Double>) res.get(2);
  //       }
  //       // algorithm()
  //       // mapp.get(0).
  //       // Main.getRouteStopMapByStopID(i);
  //       // Main.
  //       // route.get(0).
  //       // //route.get(0).
  //       // //route.
  //     }
  //     fin_map.put("start", s_stop);
  //     fin_map.put("end", e_stop);
  //     return fin_map;
  //   }
  // }
  // // // TODO Auto-generated method stub
  // throw new UnsupportedOperationException("Unimplemented method 'handle'");
  // }

  public LinkedList<LinkedList<Double>> Dijkstra_algo(
      LinkedList<Double> starting,
      LinkedList<Double> ending,
      Integer time,
      LinkedList<double[]> shuttle_1,
      LinkedList<double[]> pos,
      int lenn,
      int indx) {
    if (time == 0) {
      /** Distance Minimization */
      // First go through all the stops on each shuttle and see which stop is the closest to the
      // start point
      // Then go through all the stops on each shuttle and see which stop is the closest to the end
      // point
      double d_1 = 1000000000000.0;

      LinkedList<double[]> path_1 = new LinkedList<>();

      for (int i = 0; i < lenn; i++) {
        double x_plus1 = Math.abs(shuttle_1.get(i)[0] - starting.get(0));
        double y_plus1 = Math.abs(shuttle_1.get(i)[1] - starting.get(1));
        // double x_plus2 = Math.abs(this.shuttle_2.get(i).get(0) - starting.get(0));
        // double y_plus2 = Math.abs(this.shuttle_2.get(i).get(1) - starting.get(1));
        // double x_plus3 = Math.abs(this.shuttle_3.get(i).get(0) - starting.get(0));
        // double y_plus3 = Math.abs(this.shuttle_3.get(i).get(1) - starting.get(1));
        if ((x_plus1 + y_plus1) < d_1) {
          d_1 = x_plus1 + y_plus1;
          path_1.add(shuttle_1.get(i));
        }
        // if ((x_plus2+y_plus2) < d_2){
        //     d_2 = x_plus2+y_plus2;
        //     path_2.add(this.shuttle_2.get(i));
        // }
        // if ((x_plus3+y_plus3) < d_3){
        //     d_3 = x_plus3+y_plus3;
        //     path_3.add(this.shuttle_3.get(i));
        // }
      }
      double old_d1 = d_1;
      // double old_d2 = d_2;
      // double old_d3 = d_3;
      d_1 = 10000000000000.0;
      // d_2 =10000000000000.0;
      // d_3 =10000000000000.0;
      for (int i = 0; i < lenn; i++) {
        double x_plus1 = Math.abs(shuttle_1.get(i)[0] - ending.get(0));
        double y_plus1 = Math.abs(shuttle_1.get(i)[1] - ending.get(1));
        // double x_plus2 = Math.abs(this.shuttle_2.get(i).get(0) - ending.get(0));
        // double y_plus2 = Math.abs(this.shuttle_2.get(i).get(1) - ending.get(1));
        // double x_plus3 = Math.abs(this.shuttle_3.get(i).get(0) - ending.get(0));
        // double y_plus3 = Math.abs(this.shuttle_3.get(i).get(1) - ending.get(1));
        if ((x_plus1 + y_plus1 + old_d1) < d_1) {
          d_1 = x_plus1 + y_plus1;
          path_1.add(shuttle_1.get(i));
        }
        // if ((x_plus2+y_plus2+old_d2) < d_2){
        //     d_2 = x_plus2+y_plus2;
        //     path_2.add(this.shuttle_2.get(i));
        // }
        // if ((x_plus3+y_plus3+old_d3) < d_3){
        //     d_3 = x_plus3+y_plus3;
        //     path_3.add(this.shuttle_3.get(i));
        // }
      }
      // double abs_min1 = Math.min(d_1,d_2);
      // double abs_minfin = Math.min(abs_min1,d_3);
      // if (abs_minfin == d_1){
      //     System.out.println("SHUTTLE ONE");
      //     return path_1;
      // }
      // if (abs_minfin == d_2){
      //     System.out.println("SHUTTLE TWO");
      //     return path_2;
      // }
      // if (abs_minfin == d_3){
      //     System.out.println("SHUTTLE THREE");
      //     return path_3;
      // }
      // return new LinkedList<>();
      LinkedList<LinkedList<Double>> ans = new LinkedList<>();
      LinkedList<Double> val = new LinkedList<>();
      val.add(d_1);
      ans.add(val);
      LinkedList<Double> start = new LinkedList<>();
      LinkedList<Double> end = new LinkedList<>();
      // for (int r = 0; r < 4; r++){
      start.add(path_1.get(0)[0]);
      start.add(path_1.get(0)[1]);
      end.add(path_1.get(1)[0]);
      end.add(path_1.get(1)[1]);
      // }
      ans.add(start);
      ans.add(end);
      return ans;
    } else {
      /** Time Minimization */
      // speed limit is 25 mph
      // 4 seconds per route (stop signs and traffic -> maybe???)
      // 1 unit is 100 miles
      // 0.25m/s -> shuttle transportation
      // 0.03m/s -> walking speed
      // take one node and then calculate time for each and every other node as well
      double d_1 = 1000000000000.0;
      // double d_2 = 1000000000000.0;
      // double d_3 = 1000000000000.0;
      int index_1 = indx;
      // int index_2 = 0;

      int with_indices_2 = 0;
      LinkedList<double[]> path_1 = new LinkedList<>();
      // LinkedList<LinkedList<Double>> path_2 = new LinkedList<>();
      // LinkedList<LinkedList<Double>> path_3 = new LinkedList<>();
      for (int i = 0; i < lenn; i++) {
        int index_3 = indx;
        double x_plus1 = Math.abs(shuttle_1.get(i)[0] - starting.get(0)) / 0.03;
        double y_plus1 = Math.abs(shuttle_1.get(i)[1] - starting.get(1)) / 0.03;
        if (i > index_3) {
          while (index_3 != i) {
            System.out.println("Loop1");
            with_indices_2 +=
                Math.abs(shuttle_1.get(index_3)[0] - shuttle_1.get(index_3 + 1)[0] / 0.25) + 4;
            with_indices_2 +=
                Math.abs(shuttle_1.get(index_3)[1] - shuttle_1.get(index_3 + 1)[1] / 0.25);
            index_3++;
          }
        } else if (i < index_3) { // index_1 -> 3 -> i = 0
          // int j = i;
          while (index_3 != i) {
            index_3++;
            System.out.println(index_3);
            System.out.println(i);
            System.out.println("Second Call");
            if (index_3 == lenn) {
              index_3 = 0;
            }
            int new_i = index_3 - 1;
            if (new_i == -1) {
              new_i = lenn - 1;
            }

            with_indices_2 +=
                Math.abs((shuttle_1.get(new_i)[0] - shuttle_1.get(index_3)[0]) / 0.25) + 4;
            with_indices_2 +=
                Math.abs((shuttle_1.get(new_i)[1] - shuttle_1.get(index_3)[1]) / 0.25);
          }
        }
        // double x_plus2 = Math.abs(this.shuttle_2.get(i).get(0) - starting.get(0))/0.03;
        // double y_plus2 = Math.abs(this.shuttle_2.get(i).get(1) - starting.get(1))/0.03;
        // double x_plus3 = Math.abs(this.shuttle_3.get(i).get(0) - starting.get(0))/0.03;
        // double y_plus3 = Math.abs(this.shuttle_3.get(i).get(1) - starting.get(1))/0.03;
        if ((x_plus1 + y_plus1 + with_indices_2) < d_1) {
          d_1 = x_plus1 + y_plus1;
          path_1.add(shuttle_1.get(i));
          index_1 = i;
        }
        // if ((x_plus2+y_plus2) < d_2){
        //     d_2 = x_plus2+y_plus2;
        //     path_2.add(this.shuttle_2.get(i));
        //     index_2 = i;
        // }
        // if ((x_plus3+y_plus3) < d_3){
        //     d_3 = x_plus3+y_plus3;
        //     path_3.add(this.shuttle_3.get(i));
        //     index_3 = i;
        // }
      }
      double old_d1 = d_1;
      int get_val = index_1;
      // double old_d2 = d_2;
      // double old_d3 = d_3;
      d_1 = 10000000000000.0;
      // d_2 =10000000000000.0;
      // d_3 =10000000000000.0;
      for (int i = 0; i < lenn; i++) {
        double x_plus1 = Math.abs(shuttle_1.get(i)[0] - ending.get(0)) / 0.03;
        double y_plus1 = Math.abs(shuttle_1.get(i)[1] - ending.get(1)) / 0.03;
        // double x_plus2 = Math.abs(this.shuttle_2.get(i).get(0) - ending.get(0))/0.03;
        // double y_plus2 = Math.abs(this.shuttle_2.get(i).get(1) - ending.get(1))/0.03;
        // double x_plus3 = Math.abs(this.shuttle_3.get(i).get(0) - ending.get(0))/0.03;
        // double y_plus3 = Math.abs(this.shuttle_3.get(i).get(1) - ending.get(1))/0.03;
        index_1 = get_val;
        double with_indices_1 = 0;
        // double with_indices_2 = 0;
        // double with_indices_3 = 0;
        System.out.println(index_1);
        // System.out.println(index_2);
        // System.out.println(index_3);
        if (i > index_1) {
          while (index_1 != i) {
            System.out.println("Loop1");
            with_indices_1 +=
                Math.abs(shuttle_1.get(index_1)[0] - shuttle_1.get(index_1 + 1)[0] / 0.25) + 4;
            with_indices_1 +=
                Math.abs(shuttle_1.get(index_1)[1] - shuttle_1.get(index_1 + 1)[1] / 0.25);
            index_1++;
          }
        } else if (i < index_1) { // index_1 -> 3 -> i = 0
          // int j = i;
          while (index_1 != i) {
            index_1++;
            System.out.println(index_1);
            System.out.println(i);
            System.out.println("Second Call");
            if (index_1 == lenn) {
              index_1 = 0;
            }
            int new_i = index_1 - 1;
            if (new_i == -1) {
              new_i = lenn - 1;
            }

            with_indices_1 +=
                Math.abs(shuttle_1.get(new_i)[0] - shuttle_1.get(index_1)[0] / 0.25) + 4;
            with_indices_1 += Math.abs(shuttle_1.get(new_i)[1] - shuttle_1.get(index_1)[1] / 0.25);
          }
        }
        // if (i > index_2){
        //     while (index_2 != i){
        //         System.out.println("Loop3");
        //         with_indices_2 += Math.abs(this.shuttle_2.get(index_2).get(0) -
        // this.shuttle_2.get(index_2+1).get(0)/0.25) +4;
        //         with_indices_2 += Math.abs(this.shuttle_2.get(index_2).get(1) -
        // this.shuttle_2.get(index_2+1).get(1)/0.25);
        //         index_2 ++;

        //     }
        // }
        // else if (i < index_2){
        //     while (index_2 != i){
        //         index_2++;
        //         System.out.println(index_2);
        //         System.out.println(i);
        //         System.out.println("Fourth Call");
        //         if (index_2 == 5){
        //             index_2 = 0;
        //         }
        //         int new_i = index_2-1;
        //         if (new_i == -1){
        //             new_i = 4;
        //         }

        //         with_indices_2 += Math.abs(this.shuttle_2.get(new_i).get(0) -
        // this.shuttle_2.get(index_2).get(0)/0.25) +4;
        //         with_indices_2 += Math.abs(this.shuttle_2.get(new_i).get(1) -
        // this.shuttle_2.get(index_2).get(1)/0.25);

        //     }

        // }
        // if (i > index_3){
        //     while (index_3 != i){
        //         System.out.println("Loop5");
        //         with_indices_3 += Math.abs(this.shuttle_3.get(index_3).get(0) -
        // this.shuttle_3.get(index_3+1).get(0)/0.25) +4;
        //         with_indices_3 += Math.abs(this.shuttle_3.get(index_3).get(1) -
        // this.shuttle_3.get(index_3+1).get(1)/0.25);
        //         index_3 ++;

        //     }
        // }
        // else if (i < index_3){
        //     while (index_3 != i){
        //         index_3++;
        //         System.out.println(index_3);
        //         System.out.println(i);
        //         System.out.println("Sixth Call");
        //         if (index_3 == 5){
        //             index_3 = 0;
        //         }
        //         int new_i = index_3-1;
        //         if (new_i == -1){
        //             new_i = 4;
        //         }

        //         with_indices_3 += Math.abs(this.shuttle_3.get(new_i).get(0) -
        // this.shuttle_3.get(index_3).get(0)/0.25) +4;
        //         with_indices_3 += Math.abs(this.shuttle_3.get(new_i).get(1) -
        // this.shuttle_3.get(index_3).get(1)/0.25);

        //     }

        // }

        if ((x_plus1 + y_plus1 + old_d1 + with_indices_1) < d_1) {
          d_1 = x_plus1 + y_plus1;
          path_1.add(shuttle_1.get(i));
        }
        // if ((x_plus2+y_plus2+old_d2+with_indices_2) < d_2){
        //     d_2 = x_plus2+y_plus2;
        //     path_2.add(this.shuttle_2.get(i));
        // }
        // if ((x_plus3+y_plus3+old_d3+with_indices_3) < d_3){
        //     d_3 = x_plus3+y_plus3;
        //     path_3.add(this.shuttle_3.get(i));
        // }
      }
      // double abs_min1 = Math.min(d_1,d_2);
      // double abs_minfin = Math.min(abs_min1,d_3);
      // if (abs_minfin == d_1){
      //     System.out.println("SHUTTLE ONE");
      //     return path_1;
      // }
      // if (abs_minfin == d_2){
      //     System.out.println("SHUTTLE TWO");
      //     return path_2;
      // }
      // if (abs_minfin == d_3){
      //     System.out.println("SHUTTLE THREE");
      //     return path_3;
      // }

      LinkedList<LinkedList<Double>> ans = new LinkedList<>();
      LinkedList<Double> val = new LinkedList();
      val.add(d_1);
      ans.add(val);
      LinkedList<Double> start = new LinkedList();
      LinkedList<Double> end = new LinkedList();
      // for (int r = 0; r < 4; r++){
      start.add(path_1.get(0)[0]);
      start.add(path_1.get(0)[1]);
      end.add(path_1.get(1)[0]);
      end.add(path_1.get(1)[1]);
      // }
      ans.add(start);
      ans.add(end);
      return ans;
    }
    // return null;

  }
}
