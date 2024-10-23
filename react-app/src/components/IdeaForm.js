import React, { useState, useEffect } from 'react';
import { FaPaperPlane, FaSpinner, FaExclamationCircle, FaCheckCircle } from 'react-icons/fa';
import './IdeaForm.css';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';
const MAX_CHARS = 500;
const CHAR_WARNING_THRESHOLD = 450;
const SUCCESS_MESSAGE_DURATION = 3000;

// API configuration
const api = {
  ideas: {
    create: async (message) => {
      try {
        const response = await fetch(`${API_URL}/ideas`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
          },
          body: JSON.stringify({
            mMessage: message
          })
        });

        // Log response headers for debugging
        console.log('Response headers:', {
          cors: response.headers.get('Access-Control-Allow-Origin'),
          contentType: response.headers.get('Content-Type')
        });

        if (!response.ok) {
          let errorMessage = 'Failed to create idea';
          try {
            const errorData = await response.json();
            errorMessage = errorData.mMessage || errorMessage;
          } catch (e) {
            // If parsing fails, use status text
            errorMessage = `Server error: ${response.status} ${response.statusText}`;
          }
          throw new Error(errorMessage);
        }

        const data = await response.json();
        
        if (data.mStatus === 'error') {
          throw new Error(data.mMessage || 'Server reported an error');
        }

        return data;
      } catch (error) {
        console.error('API Error:', error);
        throw error;
      }
    }
  }
};

const IdeaForm = ({ onIdeaCreated, className = '' }) => {
  const [formState, setFormState] = useState({
    message: '',
    loading: false,
    error: null,
    success: false,
    charCount: 0,
    isValid: false
  });

  // Clean up success message after timeout
  useEffect(() => {
    let timeoutId;
    if (formState.success) {
      timeoutId = setTimeout(() => {
        setFormState(prev => ({ ...prev, success: false }));
      }, SUCCESS_MESSAGE_DURATION);
    }
    return () => clearTimeout(timeoutId);
  }, [formState.success]);

  const validateMessage = (message) => {
    const trimmedMessage = message.trim();
    return {
      isValid: trimmedMessage.length > 0 && trimmedMessage.length <= MAX_CHARS,
      error: trimmedMessage.length > MAX_CHARS 
        ? `Character limit exceeded: ${trimmedMessage.length}/${MAX_CHARS}`
        : null
    };
  };

  const handleMessageChange = (e) => {
    const newMessage = e.target.value;
    const validation = validateMessage(newMessage);
    
    setFormState(prev => ({
      ...prev,
      message: newMessage,
      charCount: newMessage.length,
      isValid: validation.isValid,
      error: validation.error
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const trimmedMessage = formState.message.trim();
    
    // Final validation before submission
    const validation = validateMessage(trimmedMessage);
    if (!validation.isValid) {
      setFormState(prev => ({
        ...prev,
        error: validation.error || 'Please enter a valid idea'
      }));
      return;
    }

    setFormState(prev => ({ ...prev, loading: true, error: null }));

    try {
      const response = await api.ideas.create(trimmedMessage);
      
      if (response.mStatus === 'ok') {
        setFormState(prev => ({
          ...prev,
          message: '',
          success: true,
          charCount: 0,
          isValid: false
        }));
        
        if (onIdeaCreated) {
          onIdeaCreated();
        }
      } else {
        throw new Error(response.mMessage || 'Failed to create idea');
      }
    } catch (err) {
      let errorMessage = 'Failed to submit your idea. Please try again.';
      
      if (!window.navigator.onLine) {
        errorMessage = 'You are offline. Please check your internet connection.';
      } else if (err.message.includes('Failed to fetch')) {
        errorMessage = 'Server connection failed. Please ensure the server is running.';
      } else if (err.message) {
        errorMessage = err.message;
      }

      setFormState(prev => ({
        ...prev,
        error: errorMessage
      }));

      console.error('Submission error:', err);
    } finally {
      setFormState(prev => ({ ...prev, loading: false }));
    }
  };

  const getCharCountColor = () => {
    if (formState.charCount > MAX_CHARS) return 'text-red-500';
    if (formState.charCount > CHAR_WARNING_THRESHOLD) return 'text-yellow-500';
    return 'text-gray-500';
  };

  return (
    <form 
      className={`idea-form ${className}`}
      onSubmit={handleSubmit}
      noValidate
    >
      <h2 className="text-2xl font-bold mb-4">Share Your Brilliant Idea</h2>
      
      <div className="form-group">
        <label htmlFor="message" className="block text-sm font-medium mb-2">
          Your Idea
        </label>
        
        <textarea
          id="message"
          value={formState.message}
          onChange={handleMessageChange}
          placeholder="What's your innovative idea? Be creative!"
          rows="4"
          disabled={formState.loading}
          className={`w-full p-3 border rounded-lg resize-none transition-colors
            ${formState.error && !formState.success ? 'border-red-500' : 'border-gray-300'}
            ${formState.loading ? 'bg-gray-100' : 'bg-white'}
            focus:outline-none focus:ring-2 focus:ring-blue-500
            disabled:opacity-50 disabled:cursor-not-allowed`}
          aria-label="Your idea"
          aria-invalid={!!formState.error}
          aria-describedby={formState.error ? 'message-error' : undefined}
        />
        
        <div className={`text-sm mt-1 flex justify-between items-center`}>
          <span className={getCharCountColor()}>
            {formState.charCount}/{MAX_CHARS} characters
          </span>
          {formState.charCount > CHAR_WARNING_THRESHOLD && (
            <span className="text-yellow-500 text-sm">
              {MAX_CHARS - formState.charCount} characters remaining
            </span>
          )}
        </div>
      </div>

      {formState.error && !formState.success && (
        <div 
          id="message-error"
          role="alert"
          className="error-message flex items-center text-red-500 mt-2"
        >
          <FaExclamationCircle className="mr-2" />
          <span>{formState.error}</span>
        </div>
      )}

      {formState.success && (
        <div 
          role="alert"
          className="success-message flex items-center text-green-500 mt-2"
        >
          <FaCheckCircle className="mr-2" />
          Your idea has been shared successfully!
        </div>
      )}

      <button
        type="submit"
        disabled={formState.loading || !formState.isValid}
        className={`submit-button mt-4 w-full flex items-center justify-center
          py-2 px-4 border border-transparent rounded-md shadow-sm text-white
          ${formState.loading || !formState.isValid
            ? 'bg-gray-400 cursor-not-allowed'
            : 'bg-blue-600 hover:bg-blue-700'
          } transition-colors duration-200`}
        aria-disabled={formState.loading || !formState.isValid}
      >
        {formState.loading ? (
          <>
            <FaSpinner className="animate-spin mr-2" />
            Submitting...
          </>
        ) : (
          <>
            <FaPaperPlane className="mr-2" />
            Share Idea
          </>
        )}
      </button>
    </form>
  );
};

export default IdeaForm;