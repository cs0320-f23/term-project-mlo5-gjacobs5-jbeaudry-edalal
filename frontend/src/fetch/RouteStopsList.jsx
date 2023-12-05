import { fetchURL } from './Fetch.jsx';

export function getStopsByRouteID(routeId) {
    return fetchURL(`stops?routeID=${routeId}`);
}

export function getStopsByStopID(stopId) {
    return fetchURL(`stops?stopID=${stopId}`);
}
