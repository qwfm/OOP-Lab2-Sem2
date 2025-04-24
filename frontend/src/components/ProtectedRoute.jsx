import React from "react";
import { useAuth0 } from "@auth0/auth0-react";
import { Navigate } from "react-router-dom";
import { useUserRole } from "../hooks/useUserRole";

export default function ProtectedRoute({ children, requiredRole }) {
    const { isAuthenticated, isLoading: authLoading } = useAuth0();
    const { userRole, isLoading: roleLoading } = useUserRole();

    if (authLoading || roleLoading) return <div>Loading...</div>;

    if (!isAuthenticated) {
        return <Navigate to="/" />;
    }

    if (requiredRole && userRole !== requiredRole) {
        return <Navigate to="/" />;
    }

    return children;
}
