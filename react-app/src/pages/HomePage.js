import React from 'react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import './HomePage.css';

const HomePage = () => {
  return (
    <div className="home-page">
      <section className="hero">
        <motion.div 
          className="hero-content"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
        >
          <h1>Welcome to The Buzz</h1>
          <p>Empowering NakedMoleRat's global workforce to share ideas and innovate together</p>
          <Link to="/signup" className="cta-button">Get Started</Link>
        </motion.div>
        <motion.img 
          src="/the uzz.png" 
          alt="The Buzz Platform" 
          className="hero-image"
          initial={{ opacity: 0, x: 10 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.5, delay: 0.2 }}
        />
      </section>

      <section className="features-overview">
        <h2>Why Choose The Buzz?</h2>
        <div className="feature-grid">
          {[
            { icon: '💡', title: 'Share Ideas', description: 'Easily post and share your innovative thoughts' },
            { icon: '👍', title: 'Engage', description: 'Like and interact with ideas from colleagues' },
            { icon: '🌐', title: 'Global Reach', description: 'Connect with team members across the world' },
            { icon: '🔒', title: 'Secure', description: 'Enterprise-grade security for your peace of mind' }
          ].map((feature, index) => (
            <motion.div 
              key={index} 
              className="feature-card"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.5, delay: index * 0.1 }}
            >
              <div className="feature-icon">{feature.icon}</div>
              <h3>{feature.title}</h3>
              <p>{feature.description}</p>
            </motion.div>
          ))}
        </div>
      </section>

      <section className="how-it-works">
        <h2>How It Works</h2>
        <div className="steps">
          {[
            { number: '1', text: 'Sign up for an account' },
            { number: '2', text: 'Share your ideas with the team' },
            { number: '3', text: 'Engage with others\' posts' },
            { number: '4', text: 'Collaborate and innovate together' }
          ].map((step, index) => (
            <motion.div 
              key={index} 
              className="step"
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.5, delay: index * 0.1 }}
            >
              <div className="step-number">{step.number}</div>
              <p>{step.text}</p>
            </motion.div>
          ))}
        </div>
      </section>

      <section className="cta">
        <h2>Ready to join the conversation?</h2>
        <Link to="/signup" className="cta-button">Sign Up Now</Link>
      </section>
    </div>
  );
};

export default HomePage;