import React, { useEffect, useState } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import axios from 'axios';

export default function ClientRequestsPage() {
    const { getAccessTokenSilently } = useAuth0();
    const [requests, setRequests] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    const fetchRequests = async () => {
        try {
            const token = await getAccessTokenSilently();
            const res = await axios.get('/api/requests', {
                headers: { Authorization: `Bearer ${token}` }
            });
            setRequests(res.data);
        } catch (e) {
            console.error(e);
            setError('Не вдалося завантажити ваші запити');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchRequests();
    }, [getAccessTokenSilently]);

    // Cancel a pending request
    const handleCancel = async (id) => {
        try {
            const token = await getAccessTokenSilently();
            await axios.delete(`/api/requests/${id}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            // Remove canceled request from UI
            setRequests(prev => prev.filter(r => r.id !== id));
        } catch (e) {
            console.error(e);
            alert('Не вдалося скасувати запит');
        }
    };

    if (loading) return <p>Завантаження...</p>;
    if (error)   return <p className="text-danger">{error}</p>;

    return (
        <div className="container mt-5">
            <h2>Мої запити</h2>
            {requests.length === 0 ? (
                <p>У вас немає активних запитів.</p>
            ) : (
                <table className="table table-striped">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Тип кімнати</th>
                        <th>Гостей</th>
                        <th>Заїзд</th>
                        <th>Виїзд</th>
                        <th>Статус</th>
                        <th>Дії</th>
                    </tr>
                    </thead>
                    <tbody>
                    {requests.map(r => (
                        <tr key={r.id}>
                            <td>{r.id}</td>
                            <td>{r.roomType}</td>
                            <td>{r.guests}</td>
                            <td>{r.checkIn}</td>
                            <td>{r.checkOut}</td>
                            <td><strong>{r.status}</strong></td>
                            <td>
                                {r.status === 'PENDING' && (
                                    <button
                                        className="btn btn-danger btn-sm"
                                        onClick={() => handleCancel(r.id)}
                                    >
                                        Скасувати
                                    </button>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
