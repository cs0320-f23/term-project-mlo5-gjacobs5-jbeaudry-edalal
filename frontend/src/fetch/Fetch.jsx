const server = 'http://localhost:3232';

export async function fetchURL(url) {
    const serverUrl = `${server}/${url}`;

    try {
        const response = await fetch(serverUrl);
        if (!response.ok) {
            throw new Error('Failed to get response from backend');
        }
        return await response.json();
    } catch (error) {
        console.error('Error:', error);
        throw error;
    }
}
