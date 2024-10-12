import React, { useEffect, useState } from 'react';
import './IdeaList.css';

const IdeaList = ({ refreshTrigger }) => {
  const [ideas, setIdeas] = useState([]);

  useEffect(() => {
    // Fetch ideas (placeholder)
    setIdeas([
      {
        id: 1,
        title: 'First Idea',
        description: 'This is the first idea.',
        author: 'Wa',
        date: '2024-10-01',
        likes: 0,
      },
      // Add more ideas here
    ]);
  }, [refreshTrigger]);

  const handleLike = (id) => {
    setIdeas((prevIdeas) =>
      prevIdeas.map((idea) =>
        idea.id === id ? { ...idea, likes: idea.likes + 1 } : idea
      )
    );
  };

  return (
    <div className="idea-list">
      {ideas.map((idea) => (
        <div key={idea.id} className="idea-card">
          <h2>{idea.title}</h2>
          <p>{idea.description}</p>
          <div className="idea-meta">
            <span>By {idea.author}</span> | <span>{idea.date}</span>
          </div>
          <button className="like-button" onClick={() => handleLike(idea.id)}>
            👍 {idea.likes}
          </button>
        </div>
      ))}
    </div>
  );
};

export default IdeaList;