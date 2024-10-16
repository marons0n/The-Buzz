import axios from 'axios';

const API_URL = "http://localhost:8080";

const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error.response);
    return Promise.reject(error);
  }
);

const api = {
  ideas: {
    getAll: () => apiClient.get('/ideas'),
    getById: (id) => apiClient.get(`/ideas/${id}`),
    create: (message) => apiClient.post('/ideas', { mMessage: message }),
    update: (id, likes) => apiClient.put(`/ideas/${id}`, { mLikes: likes }),
    delete: (id) => apiClient.delete(`/ideas/${id}`),
    like: (id) => apiClient.put(`/ideas/${id}`, { mLikes: 1 }),
    unlike: (id) => apiClient.put(`/ideas/${id}`, { mLikes: -1 }),
  },
};

export default api;