import React, { useState } from 'react';
import api from '../services/api';
import './Idea.css'; 

const Idea = ({ idea, onUpdate }) => {
  const [isLiking, setIsLiking] = useState(false);

  const handleLikeToggle = async () => {
    setIsLiking(true);
    try {
      if (idea.userHasLiked) {
        await api.ideas.unlike(idea.id);
      } else {
        await api.ideas.like(idea.id);
      }
      onUpdate();
    } catch (error) {
      console.error('Error toggling like:', error);
      // You might want to show an error message to the user here
    } finally {
      setIsLiking(false);
    }
  };

  return (
    <div className="idea-card">
      <h3 className="idea-title">{idea.mMessage}</h3>
      <p className="idea-likes">Likes: {idea.mLikes}</p>
      <button 
        className={`like-button ${idea.userHasLiked ? 'liked' : ''}`}
        onClick={handleLikeToggle}
        disabled={isLiking}
      >
        {isLiking ? 'Processing...' : (idea.userHasLiked ? 'Unlike' : 'Like')}
      </button>
    </div>
  );
};

export default Idea;