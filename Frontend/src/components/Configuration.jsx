import React from "react";
import "./TicketManagement.css";


const Configuration = ({ config, handleChange }) => {
  return (
    <div className="config-section">
      <h2>Configurations</h2>
      <label>
        Total Tickets:
        <input
          type="number"
          name="totalTickets"
          value={config.totalTickets}
          onChange={handleChange}
        />
      </label>
      <label>
        Release Rate:
        <input
          type="number"
          name="ticketReleaseRate"
          value={config.ticketReleaseRate}
          onChange={handleChange}
        />
      </label>
      <label>
        Retrieval Rate:
        <input
          type="number"
          name="customerRetrievalRate"
          value={config.customerRetrievalRate}
          onChange={handleChange}
        />
      </label>
      <label>
        Max Capacity:
        <input
          type="number"
          name="maxTicketCapacity"
          value={config.maxTicketCapacity}
          onChange={handleChange}
        />
      </label>
    </div>
  );
};

export default Configuration;
