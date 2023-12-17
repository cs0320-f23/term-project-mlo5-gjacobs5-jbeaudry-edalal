package edu.brown.cs.student.main.server.Data;

import java.net.URL;
import java.security.DrbgParameters.Reseed;
import java.util.LinkedList;

import spark.Request;
import spark.Response;
import spark.Route;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class Algorithm implements Route{
    private LinkedList<BUSVehicleData> shuttle;
    public Algorithm(LinkedList<BUSVehicleData> shuttles, LinkedList<Double> start, LinkedList<Double> end){
        this.shuttle = shuttles;
    }

    public LinkedList<LinkedList<Double>> Dijkstra_algo(LinkedList<Double> starting, LinkedList<Double> ending, int time){
        
        
        // traverse through the shuttle list to find the active ones 
        // if none then output none 
        // call to the api, then 
        
        if (time == 0){
            /**
             * Distance Minimization
             */
            //First go through all the stops on each shuttle and see which stop is the closest to the start point
            //Then go through all the stops on each shuttle and see which stop is the closest to the end point
            double d_1 = 1000000000000.0;
            double d_2 = 1000000000000.0;
            double d_3 = 1000000000000.0;
            LinkedList<LinkedList<Double>> path_1 = new LinkedList<>();
            LinkedList<LinkedList<Double>> path_2 = new LinkedList<>();
            LinkedList<LinkedList<Double>> path_3 = new LinkedList<>();
            for (int i = 0; i < 5; i++){
                double x_plus1 = Math.abs(this.shuttle_1.get(i).get(0) - starting.get(0));
                double y_plus1 = Math.abs(this.shuttle_1.get(i).get(1) - starting.get(1));
                double x_plus2 = Math.abs(this.shuttle_2.get(i).get(0) - starting.get(0));
                double y_plus2 = Math.abs(this.shuttle_2.get(i).get(1) - starting.get(1));
                double x_plus3 = Math.abs(this.shuttle_3.get(i).get(0) - starting.get(0));
                double y_plus3 = Math.abs(this.shuttle_3.get(i).get(1) - starting.get(1));
                if ((x_plus1+y_plus1) < d_1){
                    d_1 = x_plus1+y_plus1;
                    path_1.add(this.shuttle_1.get(i));
                }
                if ((x_plus2+y_plus2) < d_2){
                    d_2 = x_plus2+y_plus2;
                    path_2.add(this.shuttle_2.get(i));
                }
                if ((x_plus3+y_plus3) < d_3){
                    d_3 = x_plus3+y_plus3;
                    path_3.add(this.shuttle_3.get(i));
                }
            }
            double old_d1 = d_1;
            double old_d2 = d_2;
            double old_d3 = d_3;
            d_1 =10000000000000.0;
            d_2 =10000000000000.0;
            d_3 =10000000000000.0;
            for (int i = 0; i < 5; i++){
                double x_plus1 = Math.abs(this.shuttle_1.get(i).get(0) - ending.get(0));
                double y_plus1 = Math.abs(this.shuttle_1.get(i).get(1) - ending.get(1));
                double x_plus2 = Math.abs(this.shuttle_2.get(i).get(0) - ending.get(0));
                double y_plus2 = Math.abs(this.shuttle_2.get(i).get(1) - ending.get(1));
                double x_plus3 = Math.abs(this.shuttle_3.get(i).get(0) - ending.get(0));
                double y_plus3 = Math.abs(this.shuttle_3.get(i).get(1) - ending.get(1));
                if ((x_plus1+y_plus1+old_d1) < d_1){
                    d_1 = x_plus1+y_plus1;
                    path_1.add(this.shuttle_1.get(i));
                }
                if ((x_plus2+y_plus2+old_d2) < d_2){
                    d_2 = x_plus2+y_plus2;
                    path_2.add(this.shuttle_2.get(i));
                }
                if ((x_plus3+y_plus3+old_d3) < d_3){
                    d_3 = x_plus3+y_plus3;
                    path_3.add(this.shuttle_3.get(i));
                }
            }
            double abs_min1 = Math.min(d_1,d_2);
            double abs_minfin = Math.min(abs_min1,d_3);
            if (abs_minfin == d_1){
                System.out.println("SHUTTLE ONE");
                return path_1;
            }
            if (abs_minfin == d_2){
                System.out.println("SHUTTLE TWO");
                return path_2;
            }
            if (abs_minfin == d_3){
                System.out.println("SHUTTLE THREE");
                return path_3;
            }
            return new LinkedList<>();
        } else {
            /**
             * Time Minimization
             */
            // speed limit is 25 mph
            // 4 seconds per route (stop signs and traffic -> maybe???)
            // 1 unit is 100 miles
            // 0.25m/s -> shuttle transportation
            // 0.03m/s -> walking speed
            // take one node and then calculate time for each and every other node as well
            double d_1 = 1000000000000.0;
            double d_2 = 1000000000000.0;
            double d_3 = 1000000000000.0;
            int index_1 = 0;
            int index_2 = 0;
            int index_3 = 0;
            LinkedList<LinkedList<Double>> path_1 = new LinkedList<>();
            LinkedList<LinkedList<Double>> path_2 = new LinkedList<>();
            LinkedList<LinkedList<Double>> path_3 = new LinkedList<>();
            for (int i = 0; i < 5; i++){
                double x_plus1 = Math.abs(this.shuttle_1.get(i).get(0) - starting.get(0))/0.03;
                double y_plus1 = Math.abs(this.shuttle_1.get(i).get(1) - starting.get(1))/0.03;
                double x_plus2 = Math.abs(this.shuttle_2.get(i).get(0) - starting.get(0))/0.03;
                double y_plus2 = Math.abs(this.shuttle_2.get(i).get(1) - starting.get(1))/0.03;
                double x_plus3 = Math.abs(this.shuttle_3.get(i).get(0) - starting.get(0))/0.03;
                double y_plus3 = Math.abs(this.shuttle_3.get(i).get(1) - starting.get(1))/0.03;
                if ((x_plus1+y_plus1) < d_1){
                    d_1 = x_plus1+y_plus1;
                    path_1.add(this.shuttle_1.get(i));
                    index_1 = i;
                }
                if ((x_plus2+y_plus2) < d_2){
                    d_2 = x_plus2+y_plus2;
                    path_2.add(this.shuttle_2.get(i));
                    index_2 = i;
                }
                if ((x_plus3+y_plus3) < d_3){
                    d_3 = x_plus3+y_plus3;
                    path_3.add(this.shuttle_3.get(i));
                    index_3 = i;
                }
            }
            double old_d1 = d_1;
            double old_d2 = d_2;
            double old_d3 = d_3;
            d_1 =10000000000000.0;
            d_2 =10000000000000.0;
            d_3 =10000000000000.0;
            for (int i = 0; i < 5; i++){
                double x_plus1 = Math.abs(this.shuttle_1.get(i).get(0) - ending.get(0))/0.03;
                double y_plus1 = Math.abs(this.shuttle_1.get(i).get(1) - ending.get(1))/0.03;
                double x_plus2 = Math.abs(this.shuttle_2.get(i).get(0) - ending.get(0))/0.03;
                double y_plus2 = Math.abs(this.shuttle_2.get(i).get(1) - ending.get(1))/0.03;
                double x_plus3 = Math.abs(this.shuttle_3.get(i).get(0) - ending.get(0))/0.03;
                double y_plus3 = Math.abs(this.shuttle_3.get(i).get(1) - ending.get(1))/0.03;
                double with_indices_1 = 0;
                double with_indices_2 = 0;
                double with_indices_3 = 0;
                System.out.println(index_1);
                System.out.println(index_2);
                System.out.println(index_3);
                if (i > index_1){
                    while (index_1 != i){
                        System.out.println("Loop1");
                        with_indices_1 += Math.abs(this.shuttle_1.get(index_1).get(0) - this.shuttle_1.get(index_1+1).get(0)/0.25) +4;
                        with_indices_1 += Math.abs(this.shuttle_1.get(index_1).get(1) - this.shuttle_1.get(index_1+1).get(1)/0.25);
                        index_1 ++;
                    }
                }
                else if (i < index_1){ //index_1 -> 3 -> i = 0
                    //int j = i;
                    while (index_1 != i){
                        index_1++;
                        System.out.println(index_1);
                        System.out.println(i);
                        System.out.println("Second Call");
                        if (index_1 == 5){
                            index_1 = 0;
                        }
                        int new_i = index_1-1;
                        if (new_i == -1){
                            new_i = 4;
                        }

                        with_indices_1 += Math.abs(this.shuttle_1.get(new_i).get(0) - this.shuttle_1.get(index_1).get(0)/0.25) +4;
                        with_indices_1 += Math.abs(this.shuttle_1.get(new_i).get(1) - this.shuttle_1.get(index_1).get(1)/0.25);


                    }

                }
                if (i > index_2){
                    while (index_2 != i){
                        System.out.println("Loop3");
                        with_indices_2 += Math.abs(this.shuttle_2.get(index_2).get(0) - this.shuttle_2.get(index_2+1).get(0)/0.25) +4;
                        with_indices_2 += Math.abs(this.shuttle_2.get(index_2).get(1) - this.shuttle_2.get(index_2+1).get(1)/0.25);
                        index_2 ++;

                    }
                }
                else if (i < index_2){
                    while (index_2 != i){
                        index_2++;
                        System.out.println(index_2);
                        System.out.println(i);
                        System.out.println("Fourth Call");
                        if (index_2 == 5){
                            index_2 = 0;
                        }
                        int new_i = index_2-1;
                        if (new_i == -1){
                            new_i = 4;
                        }

                        with_indices_2 += Math.abs(this.shuttle_2.get(new_i).get(0) - this.shuttle_2.get(index_2).get(0)/0.25) +4;
                        with_indices_2 += Math.abs(this.shuttle_2.get(new_i).get(1) - this.shuttle_2.get(index_2).get(1)/0.25);

                    }

                }
                if (i > index_3){
                    while (index_3 != i){
                        System.out.println("Loop5");
                        with_indices_3 += Math.abs(this.shuttle_3.get(index_3).get(0) - this.shuttle_3.get(index_3+1).get(0)/0.25) +4;
                        with_indices_3 += Math.abs(this.shuttle_3.get(index_3).get(1) - this.shuttle_3.get(index_3+1).get(1)/0.25);
                        index_3 ++;

                    }
                }
                else if (i < index_3){
                    while (index_3 != i){
                        index_3++;
                        System.out.println(index_3);
                        System.out.println(i);
                        System.out.println("Sixth Call");
                        if (index_3 == 5){
                            index_3 = 0;
                        }
                        int new_i = index_3-1;
                        if (new_i == -1){
                            new_i = 4;
                        }

                        with_indices_3 += Math.abs(this.shuttle_3.get(new_i).get(0) - this.shuttle_3.get(index_3).get(0)/0.25) +4;
                        with_indices_3 += Math.abs(this.shuttle_3.get(new_i).get(1) - this.shuttle_3.get(index_3).get(1)/0.25);

                    }

                }

                if ((x_plus1+y_plus1+old_d1+with_indices_1) < d_1){
                    d_1 = x_plus1+y_plus1;
                    path_1.add(this.shuttle_1.get(i));
                }
                if ((x_plus2+y_plus2+old_d2+with_indices_2) < d_2){
                    d_2 = x_plus2+y_plus2;
                    path_2.add(this.shuttle_2.get(i));
                }
                if ((x_plus3+y_plus3+old_d3+with_indices_3) < d_3){
                    d_3 = x_plus3+y_plus3;
                    path_3.add(this.shuttle_3.get(i));
                }
            }
            double abs_min1 = Math.min(d_1,d_2);
            double abs_minfin = Math.min(abs_min1,d_3);
            if (abs_minfin == d_1){
                System.out.println("SHUTTLE ONE");
                return path_1;
            }
            if (abs_minfin == d_2){
                System.out.println("SHUTTLE TWO");
                return path_2;
            }
            if (abs_minfin == d_3){
                System.out.println("SHUTTLE THREE");
                return path_3;
            }

            return new LinkedList<>();
        }

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        try {
            
            String urlString = "https://api.example.com/data"; // Replace with your URL

            
            URL url = new URL(urlString);

            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            
            connection.setRequestMethod("GET");

            
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder res = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    res.append(line);
                }
                reader.close();

                
                String jsonString = res.toString();

                // Access 'vehicles' subsection manually (replace this with your logic)
                int vehiclesStartIndex = jsonString.indexOf("\"vehicles\":[");
                if (vehiclesStartIndex != -1) {
                    vehiclesStartIndex += "\"vehicles\":[".length();
                    int vehiclesEndIndex = jsonString.indexOf("]", vehiclesStartIndex);
                    if (vehiclesEndIndex != -1) {
                        String vehiclesJson = jsonString.substring(vehiclesStartIndex, vehiclesEndIndex + 1);

                        // Process the 'vehicles' JSON array
                        String[] vehiclesArray = vehiclesJson.split("\\},\\{");

                        // Access each vehicle in the 'vehicles' array
                        for (int i = 0; i < vehiclesArray.length; i++) {
                            String vehicle = vehiclesArray[i];
                            if (i == 0) {
                                vehicle += "}";
                            } else if (i == vehiclesArray.length - 1) {
                                vehicle = "{" + vehicle;
                            } else {
                                vehicle = "{" + vehicle + "}";
                            }

                            System.out.println("Vehicle " + i + ": " + vehicle);
                            // Perform operations with each vehicle data
                            // You may further process the vehicle string as needed
                        }
                    }
                }
                return jsonString;
            } else {
                System.out.println("Failed to fetch data. Response code: " + responseCode);
            }

            // Disconnect the connection
            connection.disconnect();
            
        } catch (IOException e) {
            e.printStackTrace();
            return e;
        }
        return response;
    }
    
}

