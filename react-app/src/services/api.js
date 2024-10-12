import axios from 'axios';

const API_URL = "http://localhost:8080";


// Create an axios instance with default config
const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for API calls
apiClient.interceptors.request.use(
  (config) => {
    // You can add auth headers here if needed
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for API calls
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
    create: (idea) => apiClient.post('/ideas', idea),
    update: (id, idea) => apiClient.put(`/ideas/${id}`, idea),
    delete: (id) => apiClient.delete(`/ideas/${id}`),
    like: (id) => apiClient.put(`/ideas/${id}`, { mLikes: 1 }),
    unlike: (id) => apiClient.put(`/ideas/${id}`, { mLikes: -1 }),
  },
  // You can add more API endpoints here as needed
};

export default api;