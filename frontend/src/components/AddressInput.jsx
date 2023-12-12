import React, { useState, useEffect } from "react";
import mapboxgl from "mapbox-gl";
import { ACCESS_TOKEN } from "../private/token.ts";

const AddressInput = () => {
  const [searchText, setSearchText] = useState("");
  const [suggestions, setSuggestions] = useState([]);
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

  //handles selecting an address from the suggestions in the dropdown
  const handleSelectAddress = (event) => {
    const selectedValue = event.target.value;
    const selectedFeature = suggestions.find(
      (feature) => feature.place_name === selectedValue
    );
    const selectedCoordinates = selectedFeature.geometry.coordinates;

    //making copy of addresses object so we can update it
    const selectedLocations = { ...selectedAddresses };

    //updates ending location every other time
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
    }

    setLocationUpdateCount(locationUpdateCount + 1);
    //for some reason the below line is logging 1 less than the actual count
    console.log(
      "Number of times location has been updated:",
      locationUpdateCount
    );
    //clear the input after selecting an address so ending location can be selected
    setSearchText("");
    setSelectedAddresses(selectedLocations);
    // TODO: route calculation algo here if both starting and ending locations are selected?
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

        {/*display suggestions in a dropdown */}
        {suggestions.length > 0 && (
          <select
            onChange={handleSelectAddress}
            value={""}
            style={{ width: "100%" }}
          >
            {/*disables dropdown if there is no address typed (contained in value var) */}
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
          {/*TODO: route calculation algo here if both starting and ending locations are selected*/}
        </div>
      )}
    </div>
  );
};

export default AddressInput;
