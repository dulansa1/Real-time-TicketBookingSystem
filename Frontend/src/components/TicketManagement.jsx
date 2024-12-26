import React, { useState, useEffect } from "react";
import axios from "axios";
import Configuration from "./Configuration";
import ControlPanel from "./ControlPanel";
import Logs from "./Logs";
import "./TicketManagement.css";

axios.defaults.baseURL = "http://localhost:8080/api/v1";

const TicketManagement = () => {
  const defaultConfig = {
    totalTickets: [null],
    ticketReleaseRate: [null],
    customerRetrievalRate: [null],
    maxTicketCapacity: [null],
  };
  const [config, setConfig] = useState(defaultConfig);

  const [logs, setLogs] = useState([]);
  const [isRunning, setIsRunning] = useState(false);
  const [error, setError] = useState("");
  const [ticketInfo, setTicketInfo] = useState({totalTickets: 0,maxTicketCapacity:0});
  

  const handleChange = (e) => {
    const { name, value } = e.target;
    const intValue = parseInt(value, 10);
    if(intValue <0){
      alert('Value should be greater than 0. ')
      return;
    }
    setConfig((prevConfig) => ({
      ...prevConfig,
      [name]: parseInt(value) || 0,
    }));
  };


  const validateInput = () => {
    // Check if any value is less than 1
    const isValid = Object.entries(config).every(([key, value]) => {
      if (value<1) {
        alert ("Value must be greater than or equal to 1. ");
      return false;
      }
      return true;
    });
    return isValid;

  };

  const startSimulation = () => {
    setError("");
    if (!validateInput()){
      return;
    }
    axios
      .post("/start", config)
      .then(() => {
        console.log("Simulation started");
        setIsRunning(true);
      })
      .catch((error) => {
        console.error("Error starting simulation:", error);
        setError("Failed to start simulation. Please check your input values.");
      });
  };

  const stopSimulation = () => {
    setError("");
    axios
      .post("/stop")
      .then(() => {
        console.log("Simulation stopped");
        setIsRunning(false);
      })
      .catch((error) => {
        console.error("Error stopping simulation:", error);
        setError("Failed to stop simulation. Please try again.");
      });
  };

  const resetConfig = () => {
    setConfig(defaultConfig);
    setLogs([]);
    setTicketInfo([]);
    setError("");
    setIsRunning(false);
    console.log("Configuration and logs reset.");
  };

  useEffect(() => {
    axios
      .get("/status")
      .then((response) => {
        setTicketInfo(response.data);
      })
      .catch((error) => {
        console.error("Error fetching ticket info:", error);
      });
  }, [isRunning]); 

  useEffect(() => {
    if (isRunning) {
      const interval = setInterval(() => {
        axios
          .get("/logs")
          .then((response) => {
            setLogs(response.data);
          })
          .catch((error) => {
            console.error("Error fetching logs:", error);
            setError("Failed to fetch logs.");
          });
      }, 1000);

      return () => clearInterval(interval);
    }
  }, [isRunning]);

  

  return (
    <>
      <h1>Ticket Management</h1>
      <div className="container">
        <div className="pannel1">
          <div className="configuration">
              <div className="controlPanel">
                <Configuration config={config} handleChange={handleChange} />
                <ControlPanel
                  startSimulation={startSimulation}
                  stopSimulation={stopSimulation}
                  resetConfig={resetConfig}
                  isRunning={isRunning}
                />
              </div>
          </div>
        </div>

        <div className="logs">
          <Logs logs={logs} error={error} 
          totalTickets={ticketInfo.totalTickets}
          maxTicketCapacity={ticketInfo.maxTicketCapacity}/>
        </div>
      </div>
    </>

  );
};

export default TicketManagement;
