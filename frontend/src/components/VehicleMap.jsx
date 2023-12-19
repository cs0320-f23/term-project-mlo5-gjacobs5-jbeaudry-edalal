import React, { useState, useEffect } from "react";
import colorPalette from "./colorPalette";
import { getAllRoutes, getRouteByID } from "../fetch/Routes";
import { getAllStops, getShuttlesByStopID } from "../fetch/Stops";
import { getStopsByStopID, getStopsByRouteID } from "../fetch/RouteStopsList";
//import { ACCESS_TOKEN } from "../private/token.ts";
import mapboxgl from "mapbox-gl";
import AddressInput from "./AddressInput.jsx";
/* 
The first argument is the start lat and start long -> starting location 
Second argument is the end lat and end long -> ending location 
Leave 0 there 
const apiUrl = 'http://localhost:3232/algorithm?s_lat=10.0&s_long=50.0&e_lat=-10.0&e_long=100.0&time=0'; 

// Fetch data from the URL
fetch(apiUrl)
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok.');
        }
        return response.json(); // Parse the JSON response
    })
    .then(data => {
        // Assuming the received data is in the format: { "start": [41.818824, -71.408827], "end": [41.818015, -71.406897] }
        
        // Retrieve the start and end coordinates from the fetched JSON data
        const startCoordinates: number[] = data.start || [];
        const endCoordinates: number[] = data.end || [];

        // Process or use the retrieved coordinates as needed
        console.log("Start Coordinates:");
        printCoordinates(startCoordinates);

        console.log("\nEnd Coordinates:");
        printCoordinates(endCoordinates);
    })
    .catch(error => {
        console.error('There was a problem fetching the data:', error);
    });
*/
function VehicleMap() {
  const [vehicleData, setVehicleData] = useState([]);
  const [stopsData, setStopsData] = useState([]);
  const [selectedStop, setSelectedStop] = useState([]);
  const [shuttlesAtStop, setShuttlesAtStop] = useState([]);
  const { DirectionsService, DirectionsRenderer } = window.google.maps;

  const defaultCenter = {
    lat: 41.825331,
    lng: -71.402523,
  };

  const [startCoordinates, setStartCoordinates] = useState([
    41.825331, -71.402523,
  ]);
  const [endCoordinates, setEndCoordinates] = useState([41.825331, -71.402523]);
  const [showRoute, setShowRoute] = useState(false);

  const onCoordinatesSelect = ({ startingCoordinates, endingCoordinates }) => {
    setStartCoordinates(startingCoordinates);
    setEndCoordinates(endingCoordinates);

    // showRouteOnMap(startCoords, endCoords);
    setShowRoute(true);
  };

  let map;
  let customMarkers = [];
  let socket;

  function showRouteOnMap(startCoords, endCoords) {
    const directionsService = new DirectionsService();
    const directionsRenderer = new DirectionsRenderer();

    // Set the directions renderer to the map
    directionsRenderer.setMap(map);

    // Define the route request
    const request = {
      origin: new google.maps.LatLng(startCoords.lat, startCoords.lng),
      destination: new google.maps.LatLng(endCoords.lat, endCoords.lng),
      travelMode: google.maps.TravelMode.DRIVING,
    };

    // Make the directions request
    directionsService.route(request, (result, status) => {
      if (status === google.maps.DirectionsStatus.OK) {
        // Display the route on the map
        directionsRenderer.setDirections(result);
        console.log("SUCCESS");
      } else {
        console.error("Error displaying route:", status);
      }
    });
  }

  const fetchFromBackend = async () => {
    getAllRoutes().then((result) => {
      console.log("All routes:");
      console.log(result.routes);
    });

    getRouteByID("4010118").then((result) => {
      console.log("Routes by route ID:");
      console.log(result.routes[0]);
    });

    getAllStops().then((result) => {
      console.log("All stops:");
      console.log(result.stops[0].position);
      setStopsData(result.stops);
    });

    getStopsByStopID("4209268").then((result) => {
      console.log("Stops by stop ID:");
      console.log(result.routes);
    });

    getStopsByRouteID("4010118").then((result) => {
      console.log("Stops by route ID:");
      console.log(result.routes);
    });
  };

  const handleStopClick = (stop) => {
    getShuttlesByStopID(stop.id)
      .then((result) => {
        console.log(`Shuttles at Stop ${stop.id}:`);
        console.log(result.routes);
        const idList = result.routes.map((route) => route.id);
        setShuttlesAtStop(idList);
        console.log(idList);
      })
      .catch((error) => {
        console.error("Error fetching shuttles at stop:", error);
      });

    getAllStops()
      .then((result) => {
        const getStopNameById = (stopId) => {
          const stop = result.stops.find((s) => s.id === stopId);
          console.log("STOPPPPP");
          console.log(stop);
          return stop ? stop.name : null;
        };

        const selectedStopName = getStopNameById(stop.id);
        console.log("SELECTED STOP");
        setSelectedStop([selectedStopName]);
        console.log(selectedStopName);
      })
      .catch((error) => {
        console.error("Error fetching shuttles at stop:", error);
      });
  };

  const handleWebSocketMessage = (event) => {
    const data = JSON.parse(event.data);
    //console.log("heyyyyy")

    // Update the state with the new coordinates
    const newStartCoordinates = [data.startLatitude, data.startLongitude];
    const newEndCoordinates = [data.endLatitude, data.endLongitude];

    // Update the state with the new coordinates
    setStartCoordinates(newStartCoordinates);
    setEndCoordinates(newEndCoordinates);

    // Call the function to display the route
    // showRouteOnMap(newStartCoordinates, newEndCoordinates);

    if (Array.isArray(data.vehicles)) {
      data.vehicles.forEach((vehicle, index) => {
        const position = {
          lat: parseFloat(vehicle.position[0]),
          lng: parseFloat(vehicle.position[1]),
        };
        const heading = vehicle.heading;

        //console.log(`Vehicle ${vehicle.call_name} lat: ${position.lat} lng: ${position.lng} heading: ${heading}˚`);
      });

      //console.log(`Total vehicles: ${data.vehicles.length}`);

      setVehicleData(data.vehicles);

      updateCustomMarkers(data.vehicles);
    } else {
      console.error(
        "Invalid data format. Expected an array under the 'vehicles' property."
      );
    }

    if (Array.isArray(data.arrivals)) {
      var count = 0;
      var arrivalsByShuttle = {};

      data.arrivals.forEach((arrival, index) => {
        const shuttleCallName = arrival.call_name;
        const shuttleStopID = arrival.stop_id;
        const shuttleRouteID = arrival.route_id;
        const distance = arrival.distance;
        const arrivalTime = arrival.timestamp;

        const currentTimeInSeconds = Math.floor(Date.now() / 1000);
        const differenceInSeconds = arrivalTime - currentTimeInSeconds;
        const minutes = Math.floor(differenceInSeconds / 60);
        const seconds = differenceInSeconds % 60;

        // only print the arrival if it is less than 5 minutes away
        if (minutes < 5 || (minutes === 0 && seconds < 60)) {
          if (!arrivalsByShuttle[shuttleCallName]) {
            arrivalsByShuttle[shuttleCallName] = [];
          }

          arrivalsByShuttle[shuttleCallName].push({
            shuttleStopID,
            shuttleRouteID,
            distance,
            arrivalTime,
            minutes,
            seconds,
          });

          count++;
        }
      });

      // sort arrivals for each shuttle by the shortest time to longest
      for (const shuttleCallName in arrivalsByShuttle) {
        arrivalsByShuttle[shuttleCallName].sort((a, b) => {
          const timeDifferenceA = a.minutes * 60 + a.seconds;
          const timeDifferenceB = b.minutes * 60 + b.seconds;
          return timeDifferenceA - timeDifferenceB;
        });
      }

      // print arrivals for each shuttle
      for (const shuttleCallName in arrivalsByShuttle) {
        //console.log(`Shuttle ${shuttleCallName}:`);
        arrivalsByShuttle[shuttleCallName].forEach((arrival) => {
          //console.log(`  - Shuttle is ${arrival.distance} meters away from stop ${arrival.shuttleStopID} on route ${arrival.shuttleRouteID}, arriving in ${arrival.minutes} minutes and ${arrival.seconds} seconds.`);
        });
      }

      //console.log(`Total arrivals: ${count}`);
    } else {
      console.error(
        "Invalid data format. Expected an array under the 'arrivals' property."
      );
    }
  };

  const updateCustomMarkers = (newVehicleData) => {
    customMarkers.forEach((marker) => marker.setMap(null));
    customMarkers = [];

    newVehicleData.forEach((vehicle) => {
      const position = {
        lat: parseFloat(vehicle.position[0]),
        lng: parseFloat(vehicle.position[1]),
      };
      const heading = vehicle.heading;
      const callName = vehicle.call_name;

      const customMarker = new CustomMarker(map, position, heading, callName);

      customMarker.setMap(map);
      customMarkers.push(customMarker);
    });
  };

  useEffect(() => {
    map = new window.google.maps.Map(document.getElementById("map"), {
      center: defaultCenter,
      zoom: 16,
    });

    // stopsData.forEach((stop) => {
    //   const stopPosition = {
    //     lat: stop.position[0],
    //     lng: stop.position[1],
    //   };

    //   const stopMarker = new google.maps.Marker({
    //     position: stopPosition,
    //     map,
    //     title: stop.name,
    //   });
    //   stopMarker.addListener("click", () => handleStopClick(stop));
    // });

    // connect to WebSocket
    socket = new WebSocket("ws://localhost:3200");

    // listen for messages (new vehicle data sent from server)
    socket.addEventListener("message", handleWebSocketMessage);

    // sample fetching data from backend
    fetchFromBackend();
  }, []);

  useEffect(() => {
    stopsData.forEach((stop) => {
      const stopPosition = {
        lat: stop.position[0],
        lng: stop.position[1],
      };

      const stopMarker = new google.maps.Marker({
        position: stopPosition,
        map,
        title: stop.name,
      });
      stopMarker.addListener("click", () => handleStopClick(stop));
    });

    if (showRoute) {
      console.log("Showing route with Google Maps API");
      const startLatValue = startCoordinates[1];
      const startLngValue = startCoordinates[0];
      console.log("Start Coordinates:", startLatValue, startLngValue);

      const endLatValue = endCoordinates[1];
      const endLngValue = endCoordinates[0];
      console.log("End Coordinates:", endLatValue, endLngValue);

      const startCoords = {
        lat: parseFloat(startLatValue),
        lng: parseFloat(startLngValue),
      };

      const endCoords = {
        lat: parseFloat(endLatValue),
        lng: parseFloat(endLngValue),
      };

      console.log("Parsed Start Coordinates:", startCoords);
      console.log("Parsed End Coordinates:", endCoords);
      showRouteOnMap(startCoords, endCoords);
    }
  }, [stopsData]);

  /*useEffect(() => {
    // Call the function to display the route
    showRouteOnMap(startCoordinates, endCoordinates);
  }, [startCoordinates, endCoordinates]);*/

  // useEffect(() => {
  //   map = new window.google.maps.Map(document.getElementById("map"), {
  //     center: defaultCenter,
  //     zoom: 16,
  //   });

  //   if (stopsData.length > 0) {
  //     updateCustomMarkers([]);
  //   }
  //   stopsData.forEach((stop) => {
  //     const stopPosition = {
  //       lat: stop.position[0],
  //       lng: stop.position[1],
  //     };

  //     const stopMarker = new google.maps.Marker({
  //       position: stopPosition,
  //       map,
  //       title: stop.name,
  //     });
  //     stopMarker.addListener("click", () => handleStopClick(stop));
  //   });
  // }, [stopsData]);

  return (
    <div
      style={{
        height: "100vh",
        width: "100%",
        display: "flex",
        flexDirection: "column",
      }}
    >
      <div id="map" style={{ height: "100%", flex: 1 }}></div>

      <AddressInput
        onCoordinatesSelect={onCoordinatesSelect}
        setSelectedStop={setSelectedStop}
        setShuttlesAtStop={setShuttlesAtStop}
        style={{ width: "300px", marginBottom: "10px" }}
      />

      <div
        style={{
          backgroundColor: "white",
          padding: "20px",
          margin: "20px",
          borderRadius: "5px",
          boxShadow: "0 0 10px rgba(0, 0, 0, 0.1)",
          width: "300px",
        }}
      >
        <h3>
          <strong>Shuttle Information</strong>
        </h3>
        <p>Stop: {selectedStop}</p>
        <p>Number of shuttles at stop: {shuttlesAtStop.length}</p>
      </div>
    </div>
  );
}

export default VehicleMap;

// CustomMarker class to create custom markers with orientation
function CustomMarker(map, position, heading, call_name) {
  this.position = position;
  this.heading = heading;
  this.call_name = call_name;
  this.div = null;
  this.setMap(map);
}

const google = window.google;

CustomMarker.prototype = new google.maps.OverlayView();

CustomMarker.prototype.onAdd = function () {
  const div = document.createElement("div");
  div.style.position = "absolute";
  div.style.transform = `translate(-50%, -50%) rotate(${this.heading}deg)`;
  div.style.left = "0";
  div.style.top = "0";
  div.style.width = "0";
  div.style.height = "0";
  div.style.borderLeft = "15px solid transparent";
  div.style.borderRight = "15px solid transparent";
  div.style.borderBottom = "30px solid " + colorPalette.primary.brown;

  // Create and style a div element for displaying the call name
  const call_name_div = document.createElement("div");
  call_name_div.style.position = "absolute";
  call_name_div.style.bottom = "-29px";
  call_name_div.style.left = "50%";
  call_name_div.style.transform = "translateX(-50%)";
  call_name_div.style.fontSize = "10px";
  call_name_div.style.fontWeight = "bold";
  call_name_div.style.color = colorPalette.primary.red;
  call_name_div.innerText = this.call_name;

  div.appendChild(call_name_div);

  this.div = div;
  const panes = this.getPanes();
  panes.overlayMouseTarget.appendChild(div);
};

CustomMarker.prototype.draw = function () {
  const overlayProjection = this.getProjection();
  const point = overlayProjection.fromLatLngToDivPixel(this.position);
  this.div.style.left = point.x + "px";
  this.div.style.top = point.y + "px";
};

CustomMarker.prototype.onRemove = function () {
  this.div.parentNode.removeChild(this.div);
  this.div = null;
};
