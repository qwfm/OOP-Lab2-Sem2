import React, { useEffect, useState } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export default function AdminRequestsPage() {
    const { getAccessTokenSilently } = useAuth0();
    const navigate = useNavigate();
    const [requests, setRequests] = useState([]);
    const [error, setError] = useState(null);

    const fetchRequests = async () => {
        try {
            const token = await getAccessTokenSilently();
            const res = await axios.get('/api/requests/all', {
                headers: { Authorization: `Bearer ${token}` }
            });
            setRequests(res.data);
        } catch (e) {
            console.error(e);
            setError('Не вдалося завантажити запити');
        }
    };

    useEffect(() => {
        fetchRequests();
    }, [getAccessTokenSilently]);

    const handleAction = async (id, action) => {
        if (action === 'confirm') {
            // Перехід на сторінку створення бронювання із preselect
            navigate('/create-booking', { state: { requestId: id } });
        } else {
            // Відхилення запиту
            try {
                const token = await getAccessTokenSilently();
                await axios.put(`/api/requests/${id}/reject`, null, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setRequests(prev =>
                    prev.map(r =>
                        r.id === id ? { ...r, status: 'REJECTED' } : r
                    )
                );
            } catch (e) {
                console.error(e);
                alert('Не вдалося відхилити запит');
            }
        }
    };

    if (error) return <p className="text-danger">{error}</p>;

    return (
        <div className="container mt-5">
            <h2>Управління запитами</h2>
            <table className="table table-striped">
                <thead>
                <tr>
                    <th>ID</th><th>Клієнт</th><th>Тип кімнати</th><th>Гостей</th>
                    <th>Заїзд</th><th>Виїзд</th><th>Статус</th><th>Дії</th>
                </tr>
                </thead>
                <tbody>
                {requests.map(r => (
                    <tr key={r.id}>
                        <td>{r.id}</td>
                        <td>{r.clientId}</td>
                        <td>{r.roomType}</td>
                        <td>{r.guests}</td>
                        <td>{r.checkIn}</td>
                        <td>{r.checkOut}</td>
                        <td><strong>{r.status}</strong></td>
                        <td>
                            {r.status === 'PENDING' && (
                                <>
                                    <button
                                        className="btn btn-success btn-sm me-2"
                                        onClick={() => handleAction(r.id, 'confirm')}
                                    >
                                        Підтвердити
                                    </button>
                                    <button
                                        className="btn btn-danger btn-sm"
                                        onClick={() => handleAction(r.id, 'reject')}
                                    >
                                        Відхилити
                                    </button>
                                </>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
