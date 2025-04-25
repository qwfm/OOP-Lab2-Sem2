import React, { useState, useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import axios from 'axios';

export default function BookingsListPage() {
    const { getAccessTokenSilently } = useAuth0();
    const [bookings, setBookings] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchBookings = async () => {
            try {
                const token = await getAccessTokenSilently();
                const res = await axios.get('/api/bookings', {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setBookings(res.data);
            } catch {
                setError('Не вдалося завантажити бронювання');
            }
        };
        fetchBookings();
    }, [getAccessTokenSilently]);

    const handleDeleteBooking = async (id) => {
        try {
            const token = await getAccessTokenSilently();
            await axios.delete(`/api/bookings/${id}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setBookings(bookings.filter(b => b.id !== id));
        } catch {
            setError('Не вдалося видалити бронювання');
        }
    };

    return (
        <div className="container mt-5">
            <h2>Список бронювань</h2>
            {error && <p className="text-danger">{error}</p>}
            <table className="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Клієнт</th>
                    <th>Request ID</th>
                    <th>Room ID</th>
                    <th>Ціна</th>
                    <th>Дії</th>
                </tr>
                </thead>
                <tbody>
                {bookings.map(booking => (
                    <tr key={booking.id}>
                        <td>{booking.id}</td>
                        <td>{booking.clientName}</td>
                        <td>{booking.requestId}</td>
                        <td>{booking.roomId}</td>
                        <td>{booking.totalPrice}₴</td>
                        <td>
                            <button
                                onClick={() => handleDeleteBooking(booking.id)}
                                className="btn btn-danger"
                            >
                                Видалити
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
