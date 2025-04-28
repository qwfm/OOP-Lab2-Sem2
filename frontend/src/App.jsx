import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import DashboardPage from './pages/DashboardPage';
import AddRoomPage from './pages/AddRoom';
import ProtectedRoute from './components/ProtectedRoute';
import CreateBookingPage from "./pages/CreateBookingPage.jsx";
import AdminBookingsListPage from "./pages/AdminBookingsListPage.jsx";
import ClientBookingsPage from "./pages/ClientBookingsPage.jsx";
import CreateRequestPage from "./pages/CreateRequestPage.jsx";
import RequestsPage from "./pages/ClientRequestsPage.jsx";
import AdminRequestsPage from "./pages/AdminRequestsPage.jsx";

function App() {

    return (
        <Routes>
            <Route path="/" element={<HomePage />} />
            <Route
                path="/admin"
                element={
                    <ProtectedRoute requiredRole="admin">
                        <DashboardPage />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/add-room"
                element={
                    <ProtectedRoute requiredRole="admin">
                        <AddRoomPage />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/create-booking"
                element={
                    <ProtectedRoute requiredRole="admin">
                        <CreateBookingPage />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/bookings"
                element={
                    <ProtectedRoute requiredRole="admin">
                        <AdminBookingsListPage />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/my-bookings"
                element={
                    <ProtectedRoute>
                        <ClientBookingsPage />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/create-request"
                element={
                    <ProtectedRoute>
                        <CreateRequestPage />
                    </ProtectedRoute>
                }
            />
            <Route path="/my-requests"
                   element={
                       <ProtectedRoute>
                           <RequestsPage />
                       </ProtectedRoute>
                   }
            />
            <Route
                path="/admin/requests"
                element={
                    <ProtectedRoute requiredRole="admin">
                        <AdminRequestsPage />
                    </ProtectedRoute>
                }
            />
        </Routes>
    );
}

export default App;
