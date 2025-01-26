# Real-time Simulation Ticket Booking System
This project is mainly based on OOP concepts frontend development that allows users to view real-time updates, 
and control operations.It is build with Reat.js for tha front and Spring Boot for the backend.

**Features**
* Real-time ticket pool status display.
* Configuration of ticket release and retrieval rates.
* Control panel for starting, stopping, and resetting opertions.
* Error alerts for invalid input or unexpected issues.
* useState management for seamless updates between frontend and backend.
* Periodic polling for backend communication.

**Pre-Requirements**
-Java Development Kit (JDK) (version 17 or later)
-Spring Boot Initializer (Maven)
-React.js CLI
-Postman
-Git (for cloning the repository)

**Setup Instructions**
_**Backend Setup(SpringBoot)**_

_Step 1.Use Spring Initializer__
1.Visit Spring Initializr.
2.Configure the project:
   Project: Maven
   Language: Java
   Spring Boot Version: (Choose the latest stable version)
   Dependencies:
*       Spring Web
*       Spring Starter Test
*       Spring JDBC

3.Download the generated project as a ZIP file and extract it.

__Step 2.Import into IDE_ 
1. Open your IDE(e.g.;IntellJ) and import the extracted Maven Project.
2. Verify the pom.xml file includes the dependancies selected above.
Frontend Setup(React)

_Step 3.Setup Controller and Service_
Create a basic Rest API to verify backend functionality.
Example Controller:
@RestController
@RequestMapping("/api/v1")
    public class TicketSimulationController {
    @GetMapping
    public ResponseEntity<String> getTickets() {
        return ResponseEntity.ok("Backend is working!");
    }
}

_Step 4: Run Backend_
1.Run the Spring Boot application.
2.Test API using Postman at http://localhost:8080/api/v1.


_**Frontend Setup(React)**_
_Step 1:Initialize Vite React Project_
Run the following commands in your terminal:
   npm create vite@latest frontend --template react
   cd frontend
   npm install

_Step 2: Configure Vite for Proxy_
To communicate with the backend, set up a proxy in vite.config.js:

import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
    plugins: [react()],
        server: {
            proxy: {
        '/api': 'http://localhost:8080', 
        },
    },
});

_Step 3: Create a Basic React Component_
Update APP.jsx to fetch data from the backend

_Step 4:Run the frontend_

1.Start the Vite development server-npm run dev
2.Open the app in a browser at the URL provided (e.g., http://localhost:3003).
3.Start both the Spring Boot backend and Vite React frontend.
Open the frontend application and ensure it displays the backend message (Backend is working!).


Troubleshooting
* Backend not starting: 
Ensure your JDK is properly configured and all dependencies are resolved.

* Frontend/backend communication failure:
Verify the backend URL in the frontend configuration matches the running backend server URL.

Logs and Debugging
* Check the backend logs in the console or log file for errors.
* Use browser developer tools to debug frontend issues.

