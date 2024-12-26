import React from 'react';
import { Link } from 'react-router-dom';
import './HomePage.css';
import  ticketImage from './assets/ticket.png'

const HomePage = () => {
  const buttonStyle = {
    padding: '10px 20px',
    backgroundColor: '#007BFF',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    textDecoration: 'none', // Ensures the text inside Link doesn't look like a hyperlink
  };

  return (
    <div className="container-1">
      <div className="topic">
        <h1>Real-Time Ticket Simulation System</h1>
        <p>Welcome to the ultimate ticket management experience! Configure settings, monitor real-time ticket pool status, and simulate ticket release and retrieval rates seamlessly. Start and stop your simulation with ease—bringing innovative solutions to ticketing challenges!</p>
      </div>
      <div className="image-section">
        <img
          src={ticketImage}
          alt="ticket"
          className="image"
        />
      </div>
      <Link to="/ticketmanagement" style={buttonStyle}>
        Get Started
      </Link>
    </div>
  );
};

export default HomePage;
