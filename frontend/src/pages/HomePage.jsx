import React from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";

function HomePage() {
  return (
    <>
      <Navbar />

      <div className="container mt-5 text-center">
        <h1>🏨 Hotel Booking</h1>
        <p>Welcome to the hotel system!</p>
      </div>
    </>
  );
}

export default HomePage;
