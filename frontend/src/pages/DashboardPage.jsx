import {Link, Navigate} from "react-router-dom";
import {useEffect, useState} from "react";
import {useUserRole} from "../hooks/useUserRole.js";
import {useAuth0} from "@auth0/auth0-react";
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
        const { data } = await axios.get("/hotel-booking/api/rooms", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setRooms(data);
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

  if (userRole !== 'admin') {
    return <Navigate to="/" />;
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

  return (
      <div className="container mt-5">
        <h1 className="mb-4 text-center">📊 Admin Dashboard</h1>
        {error && <p className="text-danger">{error}</p>}
        <div className="row">
          <div className="col-12 mb-4">
            <Link to="/add-room" className="btn btn-success">
              ➕ Додати номер
            </Link>
            <Link to="/create-booking" className="btn btn-warning">
              ➕ Нове бронювання
            </Link>
            <Link to="/bookings" className="btn btn-info ms-2">
              📖 Список бронювань
            </Link>
          </div>
          {rooms.map((room) => (
              <div key={room.id} className="col-md-4 mb-3">
                <div className="card">
                  <div className="card-body">
                    <h5 className="card-title">
                      {room.type} — №{room.roomNumber}
                    </h5>
                    <p className="card-text">
                      Місць: {room.capacity}
                      <br />
                      Ціна за ніч: {room.pricePerNight}₴
                    </p>
                  </div>
                </div>
              </div>
          ))}
        </div>
      </div>
  );
}
