import React, { useEffect, useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { FaThumbsUp, FaSpinner, FaExclamationTriangle } from 'react-icons/fa';
import api from '../services/api';
import './IdeaList.css';

const IdeaCard = ({ idea, onLike }) => {
  const [likes, setLikes] = useState(idea.mLikes);
  const [isLiking, setIsLiking] = useState(false);

  const handleLikeToggle = async () => {
    setIsLiking(true);
    try {
      if (likes > idea.mLikes) {
        await api.ideas.unlike(idea.mId);
        setLikes(likes - 1);
      } else {
        await api.ideas.like(idea.mId);
        setLikes(likes + 1);
      }
      onLike(idea.mId, likes);
    } catch (error) {
      console.error('Error toggling like:', error);
      setLikes(idea.mLikes);
    } finally {
      setIsLiking(false);
    }
  };

  return (
    <motion.div
      className="idea-card"
      layout
      initial={{ opacity: 0, y: 50 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, y: 50 }}
      transition={{ duration: 0.3 }}
    >
      <p className="idea-message">{idea.mMessage}</p>
      <div className="idea-footer">
        <button
          className={`like-button ${likes > idea.mLikes ? 'liked' : ''}`}
          onClick={handleLikeToggle}
          disabled={isLiking}
        >
          <FaThumbsUp /> {likes}
          {isLiking && <FaSpinner className="spinner" />}
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
        setIdeas(response.data.mData || []);
      } catch (err) {
        setError('Failed to fetch ideas. Please try again later.');
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

  if (loading) {
    return (
      <div className="loading">
        <FaSpinner className="spinner" />
        <p>Loading brilliant ideas...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error">
        <FaExclamationTriangle />
        <p>{error}</p>
      </div>
    );
  }

  return (
    <AnimatePresence>
      {ideas.length === 0 ? (
        <motion.p
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          className="no-ideas"
        >
          No ideas yet. Be the first to share one!
        </motion.p>
      ) : (
        <motion.div className="idea-grid" layout>
          {ideas.map((idea) => (
            <IdeaCard key={idea.mId} idea={idea} onLike={handleLike} />
          ))}
        </motion.div>
      )}
    </AnimatePresence>
  );
};

export default IdeaList;