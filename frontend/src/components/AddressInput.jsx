import React, { useState, useEffect } from "react";
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

  //currently keyboard shortcut is not working but does not impact functionality
  const handleKeyDown = (event) => {
    console.log("Key pressed:");
    //using control J to click on "type in starting location" textbox
    if (event.ctrlKey && event.key === "j") {
      console.log("Ctrl + J pressed");
      // Focus on the input when Ctrl + J is pressed
      document.getElementById("addressTextbox").click();
    }
  };

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

    //alternates between updating starting and ending locations
    if (locationUpdateCount % 2 === 0) {
      selectedLocations.startingLocation = selectedFeature;
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
        startingCoordinates:
          selectedLocations.startingLocation.geometry.coordinates,
        endingCoordinates:
          selectedLocations.endingLocation.geometry.coordinates,
      });
    }

    setLocationUpdateCount(locationUpdateCount + 1);
    setSearchText("");
    setSelectedAddresses(selectedLocations);
  };

  return (
    <div style={{ display: "flex", flexDirection: "column" }}>
      <div
        style={{
          display: "flex",
          width: "99%",
          marginTop: "10px",
          marginLeft: "20px",
          marginRight: "20px",
        }}
      >
        <input
          id="addressTextbox"
          type="text"
          placeholder={textboxPlaceholderText}
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          style={{
            width: "100%",
            font: "inherit",
            fontSize: "12px",
            padding: "5px",
            boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
            backgroundColor: "#ebebeb",
            border: "1px solid #ccc",
            borderRadius: "8px",
          }}
          onKeyDown={handleKeyDown}
        />

        {suggestions.length > 0 && (
          <select
            onChange={handleSelectAddress}
            value={""}
            style={{
              width: "100%",
              font: "inherit",
              fontSize: "12px",
              padding: "5px",
              boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
              backgroundColor: "#fcfafa",
              border: "1px solid #ccc",
              borderRadius: "8px",
              marginLeft: "10px",
            }}
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

      <div style={{ display: "flex" }}>
        {selectedAddresses.startingLocation && (
          <div
            style={{
              marginRight: "20px",
              fontSize: "12px",
              marginLeft: "20px",
            }}
          >
            <h3>Starting Location:</h3>
            <p>{selectedAddresses.startingLocation.place_name}</p>
          </div>
        )}

        {selectedAddresses.endingLocation && (
          <div
            style={{
              fontSize: "12px",
            }}
          >
            <h3>Ending Location:</h3>
            <p>{selectedAddresses.endingLocation.place_name}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default AddressInput;
