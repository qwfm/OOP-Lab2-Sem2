import React, { useState } from "react";
import { useAuth0 } from "@auth0/auth0-react";

function AddRoomPage() {
  const { getAccessTokenSilently } = useAuth0();
  const [room, setRoom] = useState({
    type: "",
    capacity: 1,
    pricePerNight: ""
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setRoom((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = await getAccessTokenSilently();

      const response = await fetch("/hotel-booking/api/rooms", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
          type: room.type,
          capacity: Number(room.capacity),
          pricePerNight: parseFloat(room.pricePerNight)
        })
      });

      if (!response.ok) {
        throw new Error("Request failed");
      }

      alert("Room added!");
      setRoom({ type: "", capacity: 1, pricePerNight: "" });
    } catch (error) {
      console.error("Error adding room:", error);
      alert("Failed to add room");
    }
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "600px" }}>
      <h2 className="mb-4 text-center">➕ Add Room</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Room Type</label>
          <select
              name="type"
              className="form-select"
              value={room.type}
              onChange={handleChange}
              required
          >
            <option value="">Select Class</option>
            <option value="Economy">Economy</option>
            <option value="Standard">Standard</option>
            <option value="Luxury">Luxury</option>
          </select>
        </div>

        <div className="mb-3">
          <label className="form-label">Capacity</label>
          <input
            name="capacity"
            type="number"
            className="form-control"
            value={room.capacity}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Price per Night</label>
          <input
            name="pricePerNight"
            type="number"
            step="0.01"
            className="form-control"
            value={room.pricePerNight}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit" className="btn btn-success w-100">
          ➕ Add Room
        </button>
      </form>
    </div>
  );
}

export default AddRoomPage;
