import React, { useState, useEffect } from "react";
import { Link, Navigate } from "react-router-dom";
import { useAuth0 } from "@auth0/auth0-react";
import { useUserRole } from "../hooks/useUserRole.js";
import axios from "axios";

export default function DashboardPage() {
  const {
    isAuthenticated,
    isLoading: authLoading,
    loginWithRedirect,
    getAccessTokenSilently
  } = useAuth0();
  const { userRole, isLoading: roleLoading } = useUserRole();

  const [rooms, setRooms] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!isAuthenticated || userRole !== 'admin') return;
    const fetchRooms = async () => {
      try {
        const token = await getAccessTokenSilently();
        const res = await axios.get("/api/rooms/all", {
          headers: { Authorization: `Bearer ${token}` }
        });
        setRooms(res.data);
      } catch (e) {
        console.error("Failed to load rooms:", e);
        setError("Не вдалося завантажити номери");
      }
    };
    fetchRooms();
  }, [isAuthenticated, getAccessTokenSilently, userRole]);

  if (authLoading || roleLoading) {
    return <div>Завантаження...</div>;
  }
  if (!isAuthenticated) {
    return (
        <div className="container mt-5 text-center">
          <p>Будь ласка, увійдіть, щоб побачити сторінку Адміністратора</p>
          <button className="btn btn-primary" onClick={() => loginWithRedirect()}>
            Log In
          </button>
        </div>
    );
  }
  if (userRole !== 'admin') {
    return <Navigate to="/" />;
  }

  const handleDelete = async (roomId) => {
    const room = rooms.find(r => r.id === roomId);
    if (room?.status !== "FREE") {
      alert("The room doesn't have status (FREE)");
      return;
    }
    try {
      const token = await getAccessTokenSilently();
      await axios.delete(`/api/rooms/${roomId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setRooms(prev => prev.filter(r => r.id !== roomId));
    } catch (e) {
      console.error("Failed to delete room:", e);
      setError("Не вдалося видалити номер");
    }
  };

  return (
      <div className="container mt-5">
        <h1 className="mb-4 text-center">📊 Admin Dashboard</h1>
        {error && <p className="text-danger">{error}</p>}
        <div className="mb-3">
          <Link to="/add-room" className="btn btn-success me-2">➕ Додати номер</Link>
          <Link to="/create-booking" className="btn btn-warning me-2">➕ Нове бронювання</Link>
          <Link to="/bookings" className="btn btn-info">📖 Список бронювань</Link>
          <Link to="/admin/requests" className="btn btn-primary ms-2">📝 Запити</Link>
        </div>
        <div className="row">
          {rooms.map(room => (
              <div key={room.id} className="col-md-4 mb-3">
                <div className="card">
                  <div className="card-body">
                    <h5 className="card-title">
                      {room.type} №{room.id}
                    </h5>
                    <p className="card-text">
                      Місць: {room.capacity}<br/>
                      Ціна: {room.pricePerNight}₴<br/>
                      Статус: <strong>{room.status}</strong>
                    </p>
                    <button
                        className="btn btn-danger btn-sm"
                        onClick={() => handleDelete(room.id)}
                    >
                      Видалити
                    </button>
                  </div>
                </div>
              </div>
          ))}
        </div>
      </div>
  );
}
