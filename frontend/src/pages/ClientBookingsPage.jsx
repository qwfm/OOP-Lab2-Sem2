import React, { useState, useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import axios from 'axios';

export default function ClientBookingsPage() {
    const { getAccessTokenSilently, isAuthenticated } = useAuth0();
    const [bookings, setBookings] = useState([]);
    const [error, setError] = useState('');


    useEffect(() => {
        if (!isAuthenticated) return;

        const fetchMyBookings = async () => {
            try {
                const token = await getAccessTokenSilently();
                const res = await axios.get('/api/bookings', {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setBookings(res.data);
            } catch (e) {
                console.error(e);
                setError('Не вдалося завантажити ваші бронювання');
            }
        };

        fetchMyBookings();
    }, [getAccessTokenSilently, isAuthenticated]);

    const handleCancel = async (id) => {
        try {
            const token = await getAccessTokenSilently();
            await axios.delete(`/api/bookings/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            // Після успішного видалення прибираємо бронювання з локального стану
            setBookings((prev) => prev.filter((b) => b.id !== id));
        } catch (e) {
            console.error(e);
            setError('Не вдалося скасувати бронювання');
        }
    };

    return (
        <div className="container mt-5">
            <h2 className="mb-4">Мої бронювання</h2>

            {error && <p className="text-danger">{error}</p>}

            {bookings.length === 0 ? (
                <p>У вас немає активних бронювань.</p>
            ) : (
                <table className="table table-striped">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Request ID</th>
                        <th>Room ID</th>
                        <th>Ціна</th>
                        <th>Дії</th>
                    </tr>
                    </thead>
                    <tbody>
                    {bookings.map((b) => (
                        <tr key={b.id}>
                            <td>{b.id}</td>
                            <td>{b.requestId}</td>
                            <td>{b.roomId}</td>
                            <td>{b.totalPrice}₴</td>
                            <td>
                                <button
                                    className="btn btn-danger btn-sm"
                                    onClick={() => handleCancel(b.id)}
                                >
                                    Скасувати
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
