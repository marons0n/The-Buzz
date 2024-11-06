import React, { useState, useCallback } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  FaLightbulb, 
  FaExclamationTriangle, 
  FaPlusCircle, 
  FaMinusCircle 
} from 'react-icons/fa';
import IdeaList from '../components/IdeaList';
import IdeaForm from '../components/IdeaForm';
import './IdeasPage.css';

const IdeasPage = () => {
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [isFormVisible, setIsFormVisible] = useState(true);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);

  const refreshIdeas = useCallback(() => {
    setRefreshTrigger(prev => prev + 1);
  }, []);

  const toggleForm = () => {
    setIsFormVisible(prev => !prev);
    // Clear any existing error messages when toggling form
    setError(null);
  };

  const handleIdeaCreated = () => {
    refreshIdeas();
    setSuccessMessage('Your idea was successfully shared!');
    setError(null);
    
    // Auto-hide success message after 3 seconds
    setTimeout(() => {
      setSuccessMessage(null);
      setIsFormVisible(false);
    }, 3000);
  };

  const handleError = useCallback((errorMessage) => {
    setError(errorMessage);
    // Clear error after 5 seconds
    setTimeout(() => setError(null), 5000);
  }, []);

  // Animation variants
  const containerVariants = {
    hidden: { opacity: 0 },
    visible: { 
      opacity: 1,
      transition: { duration: 0.3 }
    },
    exit: { 
      opacity: 0,
      transition: { duration: 0.2 }
    }
  };

  const messageVariants = {
    hidden: { 
      opacity: 0, 
      y: -20 
    },
    visible: { 
      opacity: 1, 
      y: 0,
      transition: { 
        type: "spring", 
        stiffness: 300, 
        damping: 25 
      }
    },
    exit: { 
      opacity: 0, 
      y: 20,
      transition: { duration: 0.2 }
    }
  };

  return (
    <motion.div 
      className="ideas-page"
      initial="hidden"
      animate="visible"
      variants={containerVariants}
    >
      <header className="ideas-header">
        <motion.h1
          initial={{ y: -20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.2 }}
        >
          <FaLightbulb className="header-icon" />
          The Buzz: Ideas Board
        </motion.h1>
        <motion.p
          initial={{ y: -20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.3 }}
        >
          Share your innovative ideas and collaborate with colleagues!
        </motion.p>
        <motion.button 
          className={`toggle-form-btn ${isFormVisible ? 'active' : ''}`}
          onClick={toggleForm}
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
        >
          {isFormVisible ? (
            <>
              <FaMinusCircle className="btn-icon" /> Hide Form
            </>
          ) : (
            <>
              <FaPlusCircle className="btn-icon" /> Share an Idea
            </>
          )}
        </motion.button>
      </header>

      <AnimatePresence mode="wait">
        {isFormVisible && (
          <motion.div
            key="form"
            className="idea-form-container"
            initial="hidden"
            animate="visible"
            exit="exit"
            variants={containerVariants}
          >
            <IdeaForm 
              onIdeaCreated={handleIdeaCreated}
              onError={handleError}
            />
          </motion.div>
        )}
      </AnimatePresence>

      <div className="ideas-container">
        <motion.h2
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.4 }}
        >
          Latest Ideas
        </motion.h2>

        <IdeaList
          refreshTrigger={refreshTrigger}
          onError={handleError}
        />
      </div>

      <AnimatePresence>
        {error && (
          <motion.div 
            className="message error-message"
            variants={messageVariants}
            initial="hidden"
            animate="visible"
            exit="exit"
          >
            <FaExclamationTriangle className="message-icon" />
            {error}
          </motion.div>
        )}

        {successMessage && (
          <motion.div 
            className="message success-message"
            variants={messageVariants}
            initial="hidden"
            animate="visible"
            exit="exit"
          >
            <FaLightbulb className="message-icon" />
            {successMessage}
          </motion.div>
        )}
      </AnimatePresence>
    </motion.div>
  );
};

export default IdeasPage;