import React, { useState, useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';

export default function CreateBookingPage() {
    const { getAccessTokenSilently } = useAuth0();
    const navigate = useNavigate();
    const location = useLocation();

    // якщо прийшла навігація з AdminRequestsPage:
    const preselectedRequest = location.state?.requestId ?? '';

    const [requests, setRequests] = useState([]);
    const [rooms, setRooms] = useState([]);
    const [selectedRequestId, setSelectedRequestId] = useState(preselectedRequest);
    const [selectedRoomId, setSelectedRoomId] = useState('');
    const [totalPrice, setTotalPrice] = useState(0);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const token = await getAccessTokenSilently();
                const [reqRes, roomRes] = await Promise.all([
                    axios.get('/api/requests', {
                        headers: { Authorization: `Bearer ${token}` }
                    }),
                    axios.get('/api/rooms', {
                        headers: { Authorization: `Bearer ${token}` }
                    })
                ]);

                const allRequests = Array.isArray(reqRes.data) ? reqRes.data : [];
                setRequests(allRequests.filter(r => r.status === 'PENDING'));
                setRooms(Array.isArray(roomRes.data) ? roomRes.data : []);
            } catch (e) {
                console.error('Failed to load data:', e);
                setError('Не вдалося завантажити дані');
            }
        };
        fetchData();
    }, [getAccessTokenSilently]);

    useEffect(() => {
        if (preselectedRequest && requests.some(r => r.id === preselectedRequest)) {
            setSelectedRequestId(preselectedRequest);
        }
    }, [preselectedRequest, requests]);

    useEffect(() => {
        const req = requests.find(r => r.id === Number(selectedRequestId));
        const room = rooms.find(r => r.id === Number(selectedRoomId));
        if (req && room) {
            const nights = Math.ceil(
                (new Date(req.checkOut) - new Date(req.checkIn)) /
                (1000 * 60 * 60 * 24)
            );
            setTotalPrice(nights * room.pricePerNight);
        } else {
            setTotalPrice(0);
        }
    }, [selectedRequestId, selectedRoomId, requests, rooms]);

    const handleSubmit = async e => {
        e.preventDefault();
        try {
            const token = await getAccessTokenSilently();
            await axios.post(
                '/api/bookings',
                {
                    requestId: Number(selectedRequestId),
                    roomId: Number(selectedRoomId),
                    totalPrice,
                    checkIn:    requests.find(r => r.id === +selectedRequestId)?.checkIn,
                    checkOut:   requests.find(r => r.id === +selectedRequestId)?.checkOut,
                },
                {
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`
                    }
                }
            );
            navigate('/admin');
        } catch (e) {
            console.error('Failed to create booking:', e);
            setError('Не вдалося створити бронювання');
        }
    };

    return (
        <div className="container mt-5" style={{ maxWidth: '600px' }}>
            <h2 className="mb-4 text-center">➕ Створити бронювання</h2>
            {error && <p className="text-danger">{error}</p>}
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Обрати запит</label>
                    <select
                        className="form-select"
                        value={selectedRequestId}
                        onChange={e => setSelectedRequestId(e.target.value)}
                        required
                    >
                        <option value="">-- Виберіть запит --</option>
                        {requests.map(req => (
                            <option key={req.id} value={req.id}>
                                #{req.id} — {req.roomType}, {req.checkIn} → {req.checkOut}, ClientId: {req.clientId}, Guests: {req.guests}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mb-3">
                    <label className="form-label">Обрати кімнату</label>
                    <select
                        className="form-select"
                        value={selectedRoomId}
                        onChange={e => setSelectedRoomId(e.target.value)}
                        required
                    >
                        <option value="">-- Виберіть кімнату --</option>
                        {rooms.map(room => (
                            <option key={room.id} value={room.id}>
                                {room.type} — №{room.id} (ціна за ніч: {room.pricePerNight}₴)
                            </option>
                        ))}
                    </select>
                </div>
                <div className="mb-3">
                    <p>Загальна ціна: <strong>{totalPrice}₴</strong></p>
                </div>
                <button type="submit" className="btn btn-primary w-100">
                    Створити бронювання
                </button>
            </form>
        </div>
    );
}
