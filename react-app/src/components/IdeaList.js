import React, { useEffect, useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { FaThumbsUp, FaSpinner, FaExclamationTriangle, FaHeart } from 'react-icons/fa';
import './IdeaList.css';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// API configuration
const api = {
  ideas: {
    getAll: async () => {
      try {
        const response = await fetch(`${API_URL}/ideas`, {
          method: 'GET',
          headers: {
            'Accept': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
          }
        });

        if (!response.ok) {
          let errorMessage = 'Failed to fetch ideas';
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
    like: async (id) => {
      try {
        const response = await fetch(`${API_URL}/ideas/${id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
          },
          body: JSON.stringify({ mLikes: 1 })
        });

        if (!response.ok) {
          throw new Error(`Failed to like idea: ${response.status}`);
        }

        return await response.json();
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
          body: JSON.stringify({ mLikes: -1 })
        });

        if (!response.ok) {
          throw new Error(`Failed to unlike idea: ${response.status}`);
        }

        return await response.json();
      } catch (error) {
        console.error('API Error:', error);
        throw error;
      }
    }
  }
};

const IdeaCard = ({ idea, onLike }) => {
  const [likes, setLikes] = useState(idea.mLikes);
  const [isLiking, setIsLiking] = useState(false);
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
        onLike(idea.mId, updatedLikes);
      } else {
        throw new Error('Failed to update like');
      }
    } catch (error) {
      console.error('Error toggling like:', error);
      setLikes(idea.mLikes);
      setError(
        error instanceof Error 
          ? error.message 
          : 'Failed to update like. Please try again.'
      );
    } finally {
      setIsLiking(false);
    }
  };

  const cardVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0 },
    exit: { opacity: 0, y: -20 }
  };

  return (
    <motion.div
      className="idea-card"
      variants={cardVariants}
      initial="hidden"
      animate="visible"
      exit="exit"
      layout
      layoutId={`idea-${idea.mId}`}
      transition={{ 
        type: "spring",
        stiffness: 200,
        damping: 20
      }}
    >
      <div className="idea-content">
        <p className="idea-message">{idea.mMessage}</p>
        {error && (
          <div className="idea-error">
            <FaExclamationTriangle /> {error}
          </div>
        )}
      </div>
      
      <div className="idea-footer">
        <button
          className={`like-button ${likes > idea.mLikes ? 'liked' : ''}`}
          onClick={handleLikeToggle}
          disabled={isLiking}
          aria-label={isLiking ? "Processing" : (likes > idea.mLikes ? "Unlike" : "Like")}
        >
          {isLiking ? (
            <FaSpinner className="spinner" />
          ) : likes > idea.mLikes ? (
            <FaHeart />
          ) : (
            <FaThumbsUp />
          )}
          <span className="like-count">{likes}</span>
        </button>
      </div>
    </motion.div>
  );
};

const IdeaList = ({ refreshTrigger }) => {
  const [ideas, setIdeas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchIdeas = async () => {
      setLoading(true);
      setError(null);

      try {
        const response = await api.ideas.getAll();
        
        if (response.mStatus === 'ok') {
          setIdeas(response.mData || []);
        } else {
          throw new Error(response.mMessage || 'Failed to fetch ideas');
        }
      } catch (err) {
        let errorMessage = 'Failed to fetch ideas. Please try again later.';
        
        if (!window.navigator.onLine) {
          errorMessage = 'You are offline. Please check your internet connection.';
        } else if (err.message.includes('Failed to fetch')) {
          errorMessage = 'Server connection failed. Please ensure the server is running.';
        } else if (err.message) {
          errorMessage = err.message;
        }
        
        setError(errorMessage);
        console.error('Error fetching ideas:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchIdeas();
  }, [refreshTrigger]);

  const handleLike = (id, newLikes) => {
    setIdeas(ideas.map(idea =>
      idea.mId === id ? { ...idea, mLikes: newLikes } : idea
    ));
  };

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        when: "beforeChildren",
        staggerChildren: 0.1
      }
    }
  };

  if (loading) {
    return (
      <div className="ideas-status loading">
        <FaSpinner className="spinner" />
        <p>Loading brilliant ideas...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="ideas-status error">
        <FaExclamationTriangle />
        <p>{error}</p>
        <button 
          onClick={() => window.location.reload()}
          className="retry-button"
        >
          Try Again
        </button>
      </div>
    );
  }

  return (
    <AnimatePresence mode="wait">
      {ideas.length === 0 ? (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          className="ideas-status empty"
        >
          <p>No ideas yet. Be the first to share one!</p>
        </motion.div>
      ) : (
        <motion.div
          className="idea-grid"
          variants={containerVariants}
          initial="hidden"
          animate="visible"
          layout
        >
          {ideas.map((idea) => (
            <IdeaCard
              key={idea.mId}
              idea={idea}
              onLike={handleLike}
            />
          ))}
        </motion.div>
      )}
    </AnimatePresence>
  );
};

export default IdeaList;