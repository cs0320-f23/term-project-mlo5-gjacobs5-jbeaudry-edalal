import { fetchURL } from './Fetch.jsx';

export function getAllStops() {
    return fetchURL('stops');
}
