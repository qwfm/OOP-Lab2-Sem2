import React from "react";
import { Link } from "react-router-dom";
import { useAuth0 } from "@auth0/auth0-react";
import { useUserRole } from "../hooks/useUserRole";

const Navbar = () => {
    const {
        isAuthenticated,
        loginWithRedirect,
        logout,
        user
    } = useAuth0();

    const { userRole, isLoading } = useUserRole();

    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark px-3">
            <Link className="navbar-brand" to="/">🏨 Hotel</Link>

            <button
                className="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarNav"
            >
                <span className="navbar-toggler-icon"></span>
            </button>

            <div className="collapse navbar-collapse" id="navbarNav">
                <ul className="navbar-nav ms-auto">
                    {!isAuthenticated && (
                        <li className="nav-item">
                            <button
                                className="btn btn-link nav-link"
                                onClick={() => loginWithRedirect()}
                            >
                                Log In
                            </button>
                        </li>
                    )}

                    {isAuthenticated && (
                        <>
                            <li className="nav-item">
                                <span className="nav-link">
                                    Hi, {user?.name || user?.email}
                                </span>
                            </li>

                            {!isLoading && userRole === 'admin' && (
                                <>
                                    <li className="nav-item">
                                        <Link className="nav-link" to="/admin">
                                            Admin Panel
                                        </Link>
                                    </li>
                                </>
                            )}

                            {!isLoading && (
                                <li className="nav-item">
                                    <Link className="nav-link" to="/my-bookings">
                                        My Bookings
                                    </Link>
                                </li>
                            )}

                            { !isLoading && (
                                <li className="nav-item">
                                    <Link className="nav-link" to="/create-request">
                                         Make a request
                                    </Link>
                                </li>
                            ) }

                            {!isLoading && (
                                <li className="nav-item">
                                    <Link className="nav-link" to="/my-requests">
                                        My Requests
                                    </Link>
                                </li>
                            )}
                            <li className="nav-item">
                                <button
                                    className="btn btn-link nav-link"
                                    onClick={() => logout({
                                        logoutParams: {
                                            returnTo: window.location.origin
                                        }
                                    })}
                                >
                                    Log Out
                                </button>
                            </li>
                        </>
                    )}
                </ul>
            </div>
        </nav>
    );
};

export default Navbar;
