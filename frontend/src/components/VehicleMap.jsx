import React, { useState, useEffect } from 'react';
import colorPalette from './colorPalette';

function VehicleMap() {
  const [vehicleData, setVehicleData] = useState([]);
  const defaultCenter = {
    lat: 41.825331,
    lng: -71.402523,
  };
  let map;
  let customMarkers = [];
  let socket;

  const handleWebSocketMessage = (event) => {
    console.log(event);

    const newVehicleData = JSON.parse(event.data);

    newVehicleData.forEach((vehicle, index) => {
      const position = {
        lat: vehicle.position[0],
        lng: vehicle.position[1],
      };
      const heading = vehicle.heading;

      console.log(`Vehicle ${vehicle.call_name} lat: ${position.lat} lng: ${position.lng} heading: ${heading}˚`);
    });
    console.log(`Total vehicles: ${newVehicleData.length}`);

    setVehicleData(newVehicleData);

    // Update custom markers
    updateCustomMarkers(newVehicleData);
  };

  const updateCustomMarkers = (newVehicleData) => {
    customMarkers.forEach((marker) => marker.setMap(null));
    customMarkers = [];

    newVehicleData.forEach((vehicle) => {
      const position = {
        lat: vehicle.position[0],
        lng: vehicle.position[1],
      };
      const heading = vehicle.heading;
      const call_name = vehicle.call_name;

      const customMarker = new CustomMarker(
        map,
        position,
        heading,
        call_name
      );

      customMarker.setMap(map);
      customMarkers.push(customMarker);
    });
  };

  useEffect(() => {
    map = new window.google.maps.Map(document.getElementById('map'), {
      center: defaultCenter,
      zoom: 16,
    });

    // Connect to WebSocket
    socket = new WebSocket('ws://localhost:3200');
    socket.addEventListener('message', handleWebSocketMessage);

    return () => {
      socket.close();
    };
  }, []);

  return (
    <div style={{ height: '100vh', width: '100%' }}>
      <div id="map" style={{ height: '100%', width: '100%' }}></div>
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
  const div = document.createElement('div');
  div.style.position = 'absolute';
  div.style.transform = `translate(-50%, -50%) rotate(${this.heading}deg)`;
  div.style.left = '0';
  div.style.top = '0';
  div.style.width = '0';
  div.style.height = '0';
  div.style.borderLeft = '15px solid transparent';
  div.style.borderRight = '15px solid transparent';
  div.style.borderBottom = '30px solid ' + colorPalette.primary.brown;

  // Create and style a div element for displaying the call name
  const call_name_div = document.createElement('div');
  call_name_div.style.position = 'absolute';
  call_name_div.style.bottom = '-29px';
  call_name_div.style.left = '50%';
  call_name_div.style.transform = 'translateX(-50%)';
  call_name_div.style.fontSize = '10px';
  call_name_div.style.fontWeight = 'bold';
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
  this.div.style.left = point.x + 'px';
  this.div.style.top = point.y + 'px';
};

CustomMarker.prototype.onRemove = function () {
  this.div.parentNode.removeChild(this.div);
  this.div = null;
};
