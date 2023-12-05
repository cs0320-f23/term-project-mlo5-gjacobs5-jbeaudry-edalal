const server = 'http://localhost:3232';

export function fetchURL(url) {
    const serverUrl = `${server}/${url}`;

    return fetch(serverUrl).then((response) => {
        if (!response.ok) {
            throw new Error('Failed to get response from backend');
        }
        return response.json();
    }).catch((error) => {
        console.error('Error:', error);
        throw error;
    });
}
