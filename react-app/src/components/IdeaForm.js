import React, { useState } from 'react';
import { FaPaperPlane, FaSpinner } from 'react-icons/fa';
import api from '../services/api';
import './IdeaForm.css';

const IdeaForm = ({ onIdeaCreated }) => {
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!message.trim()) {
      setError('Please enter your idea before submitting.');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      await api.ideas.create({ mMessage: message });
      onIdeaCreated();
      setMessage('');
      setError(null);
    } catch (err) {
      setError('Failed to submit your idea. Please try again.');
      console.error('Error creating idea:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="idea-form" onSubmit={handleSubmit}>
      <h2>Share Your Brilliant Idea</h2>
      <div className="form-group">
        <label htmlFor="message">Your Idea</label>
        <textarea
          id="message"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="What's your innovative idea? Be creative!"
          rows="4"
          required
        />
      </div>
      <button type="submit" disabled={loading} className="submit-button">
        {loading ? (
          <>
            <FaSpinner className="spinner" /> Submitting...
          </>
        ) : (
          <>
            <FaPaperPlane /> Share Idea
          </>
        )}
      </button>
      {error && <p className="error-message">{error}</p>}
    </form>
  );
};

export default IdeaForm;