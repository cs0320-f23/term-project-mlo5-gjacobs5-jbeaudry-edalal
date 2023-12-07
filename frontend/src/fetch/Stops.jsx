import { fetchURL } from './Fetch.jsx';

export function getAllStops() {
    return fetchURL('stops');
}

export function getShuttlesByStopID(stopId) {
    return fetchURL(`stops?stopID=${stopId}`);
}
