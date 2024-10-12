import React, { useState, useCallback } from 'react';
import IdeaList from '../components/IdeaList';
import IdeaForm from '../components/IdeaForm';
import './IdeasPage.css';

const IdeasPage = () => {
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const refreshIdeas = useCallback(() => {
    setRefreshTrigger(prev => prev + 1);
  }, []);

  return (
    <div className="ideas-page">
      <header className="ideas-header">
        <h1>Ideas</h1>
        <p>Share your innovative ideas and see what others think!</p>
      </header>
      <IdeaForm onIdeaCreated={refreshIdeas} />
      <IdeaList refreshTrigger={refreshTrigger} />
    </div>
  );
};

export default IdeasPage;