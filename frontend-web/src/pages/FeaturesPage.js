import React from 'react';
import './FeaturesPage.css';

const FeatureCard = ({ icon, title, description }) => (
  <div className="feature-card">
    <div className="feature-icon">{icon}</div>
    <h3 className="feature-title">{title}</h3>
    <p className="feature-description">{description}</p>
  </div>
);

const FeaturesPage = () => {
  const features = [
    {
      icon: '💡',
      title: 'Share Ideas',
      description: 'Post short messages to share your innovative ideas with colleagues across the globe.'
    },
    {
      icon: '👍',
      title: 'Like Ideas',
      description: 'Show support for great ideas by liking posts from your coworkers.'
    },
    {
      icon: '🔄',
      title: 'Flexible Interaction',
      description: 'Easily add or remove likes from ideas as you refine your thoughts.'
    },
    {
      icon: '🌐',
      title: 'Global Collaboration',
      description: 'Connect with NakedMoleRat employees from around the world in one secure platform.'
    },
    {
      icon: '📱',
      title: 'Mobile Access',
      description: 'Stay connected on-the-go with our mobile app, available for iOS and Android.'
    },
    {
      icon: '🔒',
      title: 'Secure Environment',
      description: 'Share ideas confidently in a secure, professional social interaction space.'
    }
  ];

  return (
    <div className="features-page">
      <header className="features-header">
        <h1>The Buzz: Connecting NakedMoleRat Globally</h1>
        <p>Empowering employees to share and collaborate on innovative ideas</p>
      </header>
      <div className="features-grid">
        {features.map((feature, index) => (
          <FeatureCard key={index} {...feature} />
        ))}
      </div>
    </div>
  );
};

export default FeaturesPage;