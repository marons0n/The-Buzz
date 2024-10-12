
import axios from 'axios';

const API_URL = 'http://your-backend-url.com/api';

export const getIdeas = () => axios.get(`${API_URL}/ideas`);
export const createIdea = (idea) => axios.post(`${API_URL}/ideas`, idea);
export const likeIdea = (id) => axios.put(`${API_URL}/ideas/${id}/like`);
export const unlikeIdea = (id) => axios.delete(`${API_URL}/ideas/${id}/like`);