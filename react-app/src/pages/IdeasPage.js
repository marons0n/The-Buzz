// IdeasPage.js
import React, { useState, useCallback } from 'react';
import IdeaList from '../components/IdeaList';
import IdeaForm from '../components/IdeaForm';
import { FaLightbulb, FaExclamationTriangle } from 'react-icons/fa';
import './IdeasPage.css';

const IdeasPage = () => {
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [isFormVisible, setIsFormVisible] = useState(true);
  const [error, setError] = useState(null);

  const refreshIdeas = useCallback(() => {
    setRefreshTrigger(prev => prev + 1);
  }, []);

  const toggleForm = () => {
    setIsFormVisible(!isFormVisible);
  };

  const handleIdeaCreated = () => {
    refreshIdeas();
    setIsFormVisible(false);
    setError(null);
  };

  const handleError = (errorMessage) => {
    setError(errorMessage);
    // Optionally, auto-hide the error after 5 seconds
    // setTimeout(() => setError(null), 5000);
  };

  return (
    <div className="ideas-page">
      <header className="ideas-header">
        <h1><FaLightbulb /> The Buzz: Ideas Board</h1>
        <p>Share your innovative ideas and collaborate with colleagues!</p>
        <button className="toggle-form-btn" onClick={toggleForm}>
          {isFormVisible ? 'Hide Form' : 'Share an Idea'}
        </button>
      </header>
      
      {isFormVisible && (
        <div className="idea-form-container">
          <IdeaForm onIdeaCreated={handleIdeaCreated} />
        </div>
      )}
      
      <div className="ideas-container">
        <h2>Latest Ideas</h2>
        <IdeaList
          refreshTrigger={refreshTrigger}
          onError={handleError}
        />
      </div>

      {error && (
        <div className="error-message">
          <FaExclamationTriangle /> {error}
        </div>
      )}
    </div>
  );
};

export default IdeasPage;