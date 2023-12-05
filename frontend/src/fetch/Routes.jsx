import { fetchURL } from './Fetch.jsx';

export function getAllRoutes() {
    return fetchURL('routes');
}

export function getRouteByID(routeId) {
    return fetchURL(`routes?routeID=${routeId}`);
}
