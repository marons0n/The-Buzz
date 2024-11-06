import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import './Header.css';

const Header = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const menuItems = [
    { key: "features", label: "Features", to: "/features" },
    { key: "about", label: "About Us", to: "/about" },
    { key: "ideas", label: "Ideas", to: "/ideas" },
  ];

  const handleSignUp = () => {
    navigate('/signup'); // Redirect to signup page
  };
  const handleLogin = () => { navigate('/login'); };

  return (
    <header className="buzz-header">
      <div className="buzz-header-container">
        <Link to="/" className="buzz-logo">
          <h1>The Buzz</h1>
        </Link>

        <nav className="buzz-desktop-menu">
          {menuItems.map((item) => (
            <Link
              key={item.key}
              to={item.to}
              className={`buzz-menu-item ${location.pathname === item.to ? 'active' : ''}`}
            >
              {item.label}
            </Link>
          ))}
        </nav>

        <div className="buzz-header-actions">
          
          <button onClick={handleLogin} className="buzz-try-free-button">Sign In</button>
          <button onClick={handleSignUp} className="buzz-try-free-button">Try for Free</button>
        </div>
      </div>
    </header>
  );
};

export default Header;