import axios from 'axios';
import { useAuth } from './auth';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use(
    (config) => {
        const { token } = useAuth();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

export const getPersons = async () => {
    const response = await api.get('/persons');
    return response.data;
};

export const getPerson = async (id) => {
    const response = await api.get(`/persons/${id}`);
    return response.data;
};

export const createPerson = async (data) => {
    const response = await api.post('/persons', data);
    return response.data;
};

export const updatePerson = async (id, data) => {
    const response = await api.put(`/persons/${id}`, data);
    return response.data;
};

export const deletePerson = async (id) => {
    await api.delete(`/persons/${id}`);
};

export const uploadImage = async (formData) => {
    const response = await api.post('/image-requests', formData);
    return response.data;
};

export const searchImages = async (query) => {
    const response = await api.get(`/image-requests/search?query=${encodeURIComponent(query)}`);
    return response.data;
};