import React, { useState } from 'react';
import { AlertCircle } from 'lucide-react';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { motion } from 'framer-motion';
import './Idea.css';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// API configuration
const api = {
  ideas: {
    like: async (id) => {
      try {
        const response = await fetch(`${API_URL}/ideas/${id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
          },
          body: JSON.stringify({
            mLikes: 1
          })
        });

        if (!response.ok) {
          let errorMessage = 'Failed to like idea';
          try {
            const errorData = await response.json();
            errorMessage = errorData.mMessage || errorMessage;
          } catch (e) {
            errorMessage = `Server error: ${response.status} ${response.statusText}`;
          }
          throw new Error(errorMessage);
        }

        const data = await response.json();
        
        if (data.mStatus === 'error') {
          throw new Error(data.mMessage || 'Server reported an error');
        }

        return data;
      } catch (error) {
        console.error('API Error:', error);
        throw error;
      }
    },
    unlike: async (id) => {
      try {
        const response = await fetch(`${API_URL}/ideas/${id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
          },
          body: JSON.stringify({
            mLikes: -1
          })
        });

        if (!response.ok) {
          let errorMessage = 'Failed to unlike idea';
          try {
            const errorData = await response.json();
            errorMessage = errorData.mMessage || errorMessage;
          } catch (e) {
            errorMessage = `Server error: ${response.status} ${response.statusText}`;
          }
          throw new Error(errorMessage);
        }

        const data = await response.json();
        
        if (data.mStatus === 'error') {
          throw new Error(data.mMessage || 'Server reported an error');
        }

        return data;
      } catch (error) {
        console.error('API Error:', error);
        throw error;
      }
    }
  }
};

const Idea = ({ idea, onUpdate }) => {
  const [isLiking, setIsLiking] = useState(false);
  const [likes, setLikes] = useState(idea.mLikes);
  const [error, setError] = useState(null);

  const handleLikeToggle = async () => {
    setIsLiking(true);
    setError(null);

    try {
      const isUnliking = likes > idea.mLikes;
      const response = await (isUnliking ? 
        api.ideas.unlike(idea.mId) : 
        api.ideas.like(idea.mId)
      );

      if (response.mStatus === 'ok') {
        const updatedLikes = isUnliking ? likes - 1 : likes + 1;
        setLikes(updatedLikes);
        onUpdate?.();
      } else {
        throw new Error(response.mMessage || 'Failed to update likes');
      }
    } catch (err) {
      // Revert likes to original state
      setLikes(idea.mLikes);

      // Determine appropriate error message
      let errorMessage;
      if (!window.navigator.onLine) {
        errorMessage = 'You are offline. Please check your internet connection.';
      } else if (err.message.includes('Failed to fetch')) {
        errorMessage = 'Server connection failed. Please ensure the server is running.';
      } else {
        // Handle specific HTTP status codes
        switch (err.status) {
          case 400:
            errorMessage = 'Invalid request. Please try again.';
            break;
          case 401:
            errorMessage = 'Please log in to like ideas.';
            break;
          case 404:
            errorMessage = 'This idea no longer exists.';
            break;
          case 500:
            errorMessage = 'Server error. Please try again later.';
            break;
          default:
            errorMessage = err.message || 'Failed to update like. Please try again.';
        }
      }

      setError(errorMessage);
      console.error('Error toggling like:', err);
    } finally {
      setIsLiking(false);
    }
  };

  return (
    <motion.div 
      className="idea-card"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, y: -20 }}
      transition={{ type: "spring", stiffness: 200, damping: 20 }}
    >
      <h3 className="idea-title">{idea.mMessage}</h3>
      <p className="idea-likes">Likes: {likes}</p>

      {error && (
        <Alert variant="destructive" className="mb-4">
          <AlertCircle className="h-4 w-4" />
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      )}

      <button
        className={`like-button ${likes > idea.mLikes ? 'liked' : ''}`}
        onClick={handleLikeToggle}
        disabled={isLiking}
        aria-label={isLiking ? 'Processing like' : (likes > idea.mLikes ? 'Unlike idea' : 'Like idea')}
      >
        {isLiking ? (
          <span className="loading-text">
            Processing...
          </span>
        ) : (
          <span>
            {likes > idea.mLikes ? 'Unlike' : 'Like'}
          </span>
        )}
      </button>
    </motion.div>
  );
};

// Add prop types validation
Idea.defaultProps = {
  onUpdate: () => {},
};

export default Idea;