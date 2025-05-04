import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useAuth0 } from '@auth0/auth0-react';

export default function AdminBookingsListPage() {
    const { getAccessTokenSilently } = useAuth0();
    const [bookings, setBookings] = useState([]);
    const [error, setError] = useState('');

    // modals
    const [showRequestModal, setShowRequestModal] = useState(false);
    const [requestDetails, setRequestDetails] = useState(null);

    const [showRoomModal, setShowRoomModal] = useState(false);
    const [roomDetails, setRoomDetails] = useState(null);

    const [showUserModal, setShowUserModal] = useState(false);
    const [userDetails, setUserDetails] = useState(null);

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

    const handleShowRequest = async (requestId) => {
        try {
            const token = await getAccessTokenSilently();
            const res = await axios.get(`/api/requests/${requestId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setRequestDetails(res.data);
            setShowRequestModal(true);
        } catch {
            alert('Не вдалося завантажити деталі запиту');
        }
    };

    const handleShowRoom = async (roomId) => {
        try {
            const token = await getAccessTokenSilently();
            const res = await axios.get(`/api/rooms/${roomId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setRoomDetails(res.data);
            setShowRoomModal(true);
        } catch {
            alert('Не вдалося завантажити деталі кімнати');
        }
    };

    const handleShowUser = async (userId) => {
        try {
            const token = await getAccessTokenSilently();
            const res = await axios.get(`/api/users/${userId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setUserDetails(res.data);
            setShowUserModal(true);
        } catch {
            alert('Не вдалося завантажити дані користувача');
        }
    };

    const handleDeleteBooking = async (id) => {
        try {
            const token = await getAccessTokenSilently();
            await axios.delete(`/api/bookings/${id}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setBookings(prev => prev.filter(b => b.id !== id));
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
                    <th>ID броні</th>
                    <th>Клієнт</th>
                    <th>ID запиту</th>
                    <th>Room ID</th>
                    <th>В'їзд</th>
                    <th>Виїзд</th>
                    <th>Ціна</th>
                    <th>Дії</th>
                </tr>
                </thead>
                <tbody>
                {bookings.map(b => (
                    <tr key={b.id}>
                        <td>{b.id}</td>
                        <td>
                            <button
                                className="btn btn-link p-0"
                                onClick={() => handleShowUser(b.clientId)}
                            >
                                {b.clientId}
                            </button>
                        </td>
                        <td>
                            <button
                                className="btn btn-link p-0"
                                onClick={() => handleShowRequest(b.requestId)}
                            >
                                {b.requestId}
                            </button>
                        </td>
                        <td>
                            <button
                                className="btn btn-link p-0"
                                onClick={() => handleShowRoom(b.roomId)}
                            >
                                {b.roomId}
                            </button>
                        </td>
                        <td>{b.checkIn}</td>
                        <td>{b.checkOut}</td>
                        <td>{b.totalPrice}₴</td>
                        <td>
                            <button
                                className="btn btn-danger btn-sm"
                                onClick={() => handleDeleteBooking(b.id)}
                            >
                                Видалити
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* Request Modal */}
            {showRequestModal && requestDetails && (
                <>
                    <div
                        className="modal-backdrop fade show"
                        onClick={() => setShowRequestModal(false)}
                    />
                    <div className="modal fade show d-block" tabIndex="-1">
                        <div className="modal-dialog">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h5 className="modal-title">
                                        Деталі запиту #{requestDetails.id}
                                    </h5>
                                    <button
                                        type="button"
                                        className="btn-close"
                                        onClick={() => setShowRequestModal(false)}
                                    />
                                </div>
                                <div className="modal-body">
                                    <p><strong>Клієнт:</strong> {requestDetails.clientId}</p>
                                    <p><strong>Тип:</strong> {requestDetails.roomType}</p>
                                    <p><strong>Гостей:</strong> {requestDetails.guests}</p>
                                    <p><strong>Статус:</strong> {requestDetails.status}</p>
                                    <p><strong>Заїзд:</strong> {requestDetails.checkIn}</p>
                                    <p><strong>Виїзд:</strong> {requestDetails.checkOut}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </>
            )}

            {/* Room Modal */}
            {showRoomModal && roomDetails && (
                <>
                    <div
                        className="modal-backdrop fade show"
                        onClick={() => setShowRoomModal(false)}
                    />
                    <div className="modal fade show d-block" tabIndex="-1">
                        <div className="modal-dialog">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h5 className="modal-title">
                                        Деталі кімнати №{roomDetails.id}
                                    </h5>
                                    <button
                                        type="button"
                                        className="btn-close"
                                        onClick={() => setShowRoomModal(false)}
                                    />
                                </div>
                                <div className="modal-body">
                                    <p><strong>Клас:</strong> {roomDetails.type}</p>
                                    <p><strong>Місць:</strong> {roomDetails.capacity}</p>
                                    <p><strong>Ціна за ніч:</strong> {roomDetails.pricePerNight}₴</p>
                                    <p><strong>Статус:</strong> {roomDetails.status}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </>
            )}

            {/* User Modal */}
            {showUserModal && userDetails && (
                <>
                    <div
                        className="modal-backdrop fade show"
                        onClick={() => setShowUserModal(false)}
                    />
                    <div className="modal fade show d-block" tabIndex="-1">
                        <div className="modal-dialog">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h5 className="modal-title">
                                        Дані користувача #{userDetails.id}
                                    </h5>
                                    <button
                                        type="button"
                                        className="btn-close"
                                        onClick={() => setShowUserModal(false)}
                                    />
                                </div>
                                <div className="modal-body">
                                    <p><strong>Email:</strong> {userDetails.email}</p>
                                    <p><strong>Ім’я:</strong> {userDetails.name}</p>
                                    <p><strong>Роль:</strong> {userDetails.role}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </>
            )}
        </div>
    );
}
