import React, { useState } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function CreateRequestPage() {
    const { getAccessTokenSilently} = useAuth0();
    const navigate = useNavigate();

    const [form, setForm] = useState({
        roomType: '',
        guests: 1,
        checkIn: '',
        checkOut: ''
    });
    const [error, setError] = useState('');

    const handleChange = e => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async e => {
        e.preventDefault();
        try {
            const token = await getAccessTokenSilently();
            await axios.post(
                '/api/requests',
                form, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            navigate('/my-requests');
        } catch (e) {
            console.error(e);
            setError('Не вдалося створити запит');
        }
    };

    return (
        <div className="container mt-5" style={{ maxWidth: '600px' }}>
            <h2 className="mb-4 text-center">➕ Створити запит</h2>
            {error && <p className="text-danger">{error}</p>}
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Клас кімнати</label>
                    <select
                        name="roomType"
                        className="form-select"
                        value={form.roomType}
                        onChange={handleChange}
                        required
                    >
                        <option value="">-- Виберіть --</option>
                        <option value="Economy">Economy</option>
                        <option value="Standard">Standard</option>
                        <option value="Luxury">Luxury</option>
                    </select>
                </div>

                <div className="mb-3">
                    <label className="form-label">Кількість гостей</label>
                    <input
                        name="guests"
                        type="number"
                        min="1"
                        className="form-control"
                        value={form.guests}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label className="form-label">Дата заїзду</label>
                    <input
                        name="checkIn"
                        type="date"
                        className="form-control"
                        value={form.checkIn}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="mb-3">
                    <label className="form-label">Дата виїзду</label>
                    <input
                        name="checkOut"
                        type="date"
                        className="form-control"
                        value={form.checkOut}
                        onChange={handleChange}
                        required
                    />
                </div>

                <button type="submit" className="btn btn-primary w-100">
                    Створити запит
                </button>
            </form>
        </div>
    );
}
