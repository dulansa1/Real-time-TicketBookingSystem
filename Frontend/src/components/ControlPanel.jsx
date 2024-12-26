import React from "react";
import "./TicketManagement.css";


const ControlPanel = ({ startSimulation, stopSimulation, resetConfig, isRunning }) => {
  return (
    <div className="configuration-panel">
      <button onClick={startSimulation} disabled={isRunning}>
        Start 
      </button>
      <button onClick={stopSimulation} disabled={!isRunning}>
        Stop 
      </button>
      <button onClick={resetConfig}>Reset</button>
    </div>
  );
};

export default ControlPanel;
