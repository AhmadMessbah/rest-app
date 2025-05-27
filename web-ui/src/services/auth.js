import { createContext, useContext, useState } from 'react';
import axios from 'axios';

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [token, setToken] = useState(localStorage.getItem('token'));

    const login = async ({ username, password }) => {
        try {
            const response = await axios.post('http://localhost:8080/api/login', { username, password });
            const newToken = response.data.token;
            setToken(newToken);
            localStorage.setItem('token', newToken);
        } catch (err) {
            throw new Error('Login failed');
        }
    };

    const logout = () => {
        setToken(null);
        localStorage.removeItem('token');
    };

    return (
        <AuthContext.Provider value={{ token, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}