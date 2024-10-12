import React from 'react';
import { likeIdea, unlikeIdea } from '../services/api';

const Idea = ({ idea, onUpdate }) => {
  const handleLike = async () => {
    try {
      await likeIdea(idea.id);
      onUpdate();
    } catch (error) {
      console.error('Error liking idea:', error);
    }
  };

  const handleUnlike = async () => {
    try {
      await unlikeIdea(idea.id);
      onUpdate();
    } catch (error) {
      console.error('Error unliking idea:', error);
    }
  };

  return (
    <div>
      <h3>{idea.title}</h3>
      <p>{idea.description}</p>
      <p>Likes: {idea.likes}</p>
      <button onClick={handleLike}>Like</button>
      <button onClick={handleUnlike}>Unlike</button>
    </div>
  );
};

export default Idea;