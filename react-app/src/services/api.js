// services/api.js

import axios from 'axios';

// Custom error class for API-specific errors
export class ApiError extends Error {
  constructor(message, status) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
  }
}

const API_URL = "https://team-untitled-23.dokku.cse.lehigh.edu";

const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Response interceptor for error handling
apiClient.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response) {
      // The request was made and the server responded with a status code
      // that falls out of the range of 2xx
      throw new ApiError(
        error.response.data?.mMessage || 'Server error',
        error.response.status
      );
    } else if (error.request) {
      // The request was made but no response was received
      throw new ApiError('Network error', 0);
    } else {
      // Something happened in setting up the request that triggered an Error
      throw new ApiError('Request configuration error', -1);
    }
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