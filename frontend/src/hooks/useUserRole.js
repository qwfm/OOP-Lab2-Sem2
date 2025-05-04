import { useEffect, useState } from 'react';
import { useAuth0 } from '@auth0/auth0-react';

export function useUserRole() {
    const [userRole, setUserRole] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const { isAuthenticated, getAccessTokenSilently } = useAuth0();

    useEffect(() => {
        const fetchRole = async () => {
            if (!isAuthenticated) {
                setIsLoading(false);
                return; 
            }

            try {
                const token = await getAccessTokenSilently();
                const response = await fetch('/api/users/me', {
                    headers: { Authorization: `Bearer ${token}` },
                });
                const data = await response.json();
                setUserRole(data.role);
            } catch (error) {
                console.error('Error fetching user role:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchRole();
    }, [isAuthenticated, getAccessTokenSilently]);

    return { userRole, isLoading };
}
