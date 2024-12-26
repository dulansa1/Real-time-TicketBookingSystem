import React from "react";
import "./TicketManagement.css";


const Logs = ({ logs, error, totalTickets, maxTicketCapacity }) => {
  return (
    <div className="logs-section">
      <div className="ticket-status">
      <h2>Ticket Status:</h2>
      <h4>Total Tickets: {totalTickets} </h4>
      <h4>Max Ticket Capacity: {maxTicketCapacity}</h4>
    </div>
    <h2>Simulation Logs</h2>
      {error && <p className="error-message">{error}</p>}
      <ul>
        {logs.map((log, index) => (
          <li key={index}>{log}</li>
        ))}
      </ul>
    </div>
  );
};

export default Logs;
