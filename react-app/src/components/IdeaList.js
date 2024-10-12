import React, { useEffect, useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { FaThumbsUp, FaSpinner, FaExclamationTriangle } from 'react-icons/fa';
import api from '../services/api';
import './IdeaList.css';

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

  const handleLike = async (id) => {
    try {
      await api.ideas.like(id);
      setIdeas(ideas.map(idea => 
        idea.mId === id ? { ...idea, mLikes: idea.mLikes + 1 } : idea
      ));
    } catch (err) {
      console.error('Error liking idea:', err);
    }
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
            <motion.div
              key={idea.mId}
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
                  className="like-button"
                  onClick={() => handleLike(idea.mId)}
                >
                  <FaThumbsUp /> {idea.mLikes}
                </button>
              </div>
            </motion.div>
          ))}
        </motion.div>
      )}
    </AnimatePresence>
  );
};

export default IdeaList;