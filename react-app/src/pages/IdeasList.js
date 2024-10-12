import React, { useState, useEffect } from 'react';
import { getIdeas } from '../services/api';
import Idea from './Idea';

const IdeaList = ({ refreshTrigger }) => {
  const [ideas, setIdeas] = useState([]);

  useEffect(() => {
    fetchIdeas();
  }, [refreshTrigger]);

  const fetchIdeas = async () => {
    try {
      const response = await getIdeas();
      setIdeas(response.data);
    } catch (error) {
      console.error('Error fetching ideas:', error);
    }
  };

  return (
    <div className="idea-list">
      <h2>Latest Ideas</h2>
      {ideas.map(idea => (
        <Idea key={idea.id} idea={idea} onUpdate={fetchIdeas} />
      ))}
    </div>
  );
};

export default IdeaList;