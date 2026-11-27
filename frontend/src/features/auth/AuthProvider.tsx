import { useState, type ReactNode } from "react";
import { clearToken, getToken, setToken } from "../../services/api/tokenStorage";
import { AuthContext } from "./AuthContext";

export default function AuthProvider({ children }: { children: ReactNode }) {
    const [isAuthenticated, setIsAuthenticated] = useState(getToken() !== null);

    const login = (token: string) => {
        setToken(token);
        setIsAuthenticated(true);
    };

    const logout = () => {
        clearToken();
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}
