import React, { useState, useEffect } from "react";
import mapboxgl from "mapbox-gl";
import { ACCESS_TOKEN } from "../private/token.ts";

const AddressInput = ({ onCoordinatesSelect }) => {
  const [searchText, setSearchText] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [selectedStartCoordinates, setSelectedStartCoordinates] = useState([]);
  const [selectedEndCoordinates, setSelectedEndCoordinates] = useState([]);
  const [selectedAddresses, setSelectedAddresses] = useState({
    startingLocation: null,
    endingLocation: null,
  });
  const [selectedCoordinates, setSelectedCoordinates] = useState({
    startingCoordinates: null,
    endingCoordinates: null,
  });
  const [locationUpdateCount, setLocationUpdateCount] = useState(0);
  const [textboxPlaceholderText, setTextboxPlaceholderText] = useState(
    "Type a starting location..."
  );

  useEffect(() => {
    //get address suggestions from mapbox
    const fetchSuggestions = async () => {
      try {
        const response = await fetch(
          `https://api.mapbox.com/geocoding/v5/mapbox.places/${searchText}.json?access_token=${ACCESS_TOKEN}`
        );

        const data = await response.json();

        setSuggestions(data.features);
      } catch (error) {
        console.error("Error fetching suggestions for addresses:", error);
      }
    };

    //fetch suggestions if searchText isn't empty (so the user inputted something)
    if (searchText.trim() !== "") {
      fetchSuggestions();
    } else {
      setSuggestions([]);
    }
  }, [searchText]);

  const handleSelectAddress = (event) => {
    const selectedValue = event.target.value;
    const selectedFeature = suggestions.find(
      (feature) => feature.place_name === selectedValue
    );
    const selectedCoordinates = selectedFeature.geometry.coordinates;

    const selectedLocations = { ...selectedAddresses };

    if (locationUpdateCount % 2 === 0) {
      selectedLocations.startingLocation = selectedFeature;
      console.log("selected coordinates", selectedCoordinates);
      setSelectedCoordinates((prevCoordinates) => ({
        ...prevCoordinates,
        startingCoordinates: selectedCoordinates,
      }));
      setTextboxPlaceholderText("Type an ending location...");
    } else {
      selectedLocations.endingLocation = selectedFeature;
      setSelectedCoordinates((prevCoordinates) => ({
        ...prevCoordinates,
        endingCoordinates: selectedCoordinates,
      }));
      setTextboxPlaceholderText("Type a starting location...");

      // Pass coordinates to VehicleMap
      onCoordinatesSelect({
        startingCoordinates: selectedStartCoordinates,
        endingCoordinates: selectedEndCoordinates,
      });
    }

    setLocationUpdateCount(locationUpdateCount + 1);
    setSearchText("");
    setSelectedAddresses(selectedLocations);
  };

  return (
    <div style={{ display: "flex", flexDirection: "column" }}>
      <div style={{ display: "flex", width: "50%" }}>
        <input
          type="text"
          placeholder={textboxPlaceholderText}
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          style={{ width: "100%" }}
        />

        {suggestions.length > 0 && (
          <select
            onChange={handleSelectAddress}
            value={""}
            style={{ width: "100%" }}
          >
            <option value="" disabled>
              Select an address from below
            </option>
            {suggestions.map((feature) => (
              <option key={feature.id} value={feature.place_name}>
                {feature.place_name}
              </option>
            ))}
          </select>
        )}
      </div>

      {selectedAddresses.startingLocation && (
        <div>
          <h3>Starting Location:</h3>
          <p>{selectedAddresses.startingLocation.place_name}</p>
        </div>
      )}

      {selectedAddresses.endingLocation && (
        <div>
          <h3>Ending Location:</h3>
          <p>{selectedAddresses.endingLocation.place_name}</p>
        </div>
      )}
    </div>
  );
};

export default AddressInput;
