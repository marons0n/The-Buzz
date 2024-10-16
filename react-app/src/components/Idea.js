import React, { useState } from 'react';
import api from '../services/api';
import './Idea.css';

const Idea = ({ idea, onUpdate }) => {
  const [isLiking, setIsLiking] = useState(false);
  const [likes, setLikes] = useState(idea.mLikes);

  const handleLikeToggle = async () => {
    setIsLiking(true);
    try {
      let updatedLikes;
      if (likes > idea.mLikes) {
        // User is unliking
        await api.ideas.unlike(idea.mId);
        updatedLikes = likes - 1;
      } else {
        // User is liking
        await api.ideas.like(idea.mId);
        updatedLikes = likes + 1;
      }
      setLikes(updatedLikes);
      onUpdate();
    } catch (error) {
      console.error('Error toggling like:', error);
      // Revert the like state if the API call fails
      setLikes(idea.mLikes);
      // You might want to show an error message to the user here
    } finally {
      setIsLiking(false);
    }
  };

  return (
    <div className="idea-card">
      <h3 className="idea-title">{idea.mMessage}</h3>
      <p className="idea-likes">Likes: {likes}</p>
      <button
        className={`like-button ${likes > idea.mLikes ? 'liked' : ''}`}
        onClick={handleLikeToggle}
        disabled={isLiking}
      >
        {isLiking ? 'Processing...' : (likes > idea.mLikes ? 'Unlike' : 'Like')}
      </button>
    </div>
  );
};

export default Idea;