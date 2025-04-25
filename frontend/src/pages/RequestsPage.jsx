import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth0 } from '@auth0/auth0-react';

const RequestsPage = () => {
    const [requests, setRequests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { getAccessTokenSilently } = useAuth0();

    useEffect(() => {
        const fetchRequests = async () => {
            try {
                const token = await getAccessTokenSilently();
                const response = await axios.get('/api/requests', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setRequests(response.data);
            } catch (err) {
                setError('Error fetching requests');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchRequests();
    }, [getAccessTokenSilently]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div>
            <h1>Requests</h1>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Room Type</th>
                    <th>Guests</th>
                    <th>Status</th>
                    <th>Check In</th>
                    <th>Check Out</th>
                </tr>
                </thead>
                <tbody>
                {requests.map((request) => (
                    <tr key={request.id}>
                        <td>{request.id}</td>
                        <td>{request.roomType}</td>
                        <td>{request.guests}</td>
                        <td>{request.status}</td>
                        <td>{request.checkIn}</td>
                        <td>{request.checkOut}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default RequestsPage;