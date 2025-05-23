ToDo Summary Assistant

A full-stack ToDo Summary Assistant application built with *Spring Boot* for the backend API and *React.js* for the frontend. This app allows users to create, manage, and summarize their daily tasks efficiently.



 Features

- Create, update, delete, and mark todos as complete
- Summarize tasks (completed, pending) with openAi and pushed to slack
  = RESTful API integration between frontend and backend
- Persistent storage with database integration (PostgreSQL)

---

 Tech Stack

 Frontend
- [React.js](http://localhost:5173/)
- Axios (for HTTP requests)
- Tailwind CSS / Bootstrap (optional styling)
- React Router

 Backend
- [Spring Boot](http://localhost:8080)
- Spring Data JPA
- Spring Web
- Spring Security (optional authentication)
- PostgreSQL 

---

Steps to Run:

1. Clone the Repository:
  => https://github.com/pradeep-kumar-s-2001/Todo_assignment.git

2. Backend (Spring Boot):
   =>Open in STS/IntelliJ.
   =>Configure application.properties with postgresql credentials.
   =>Run TodoApi.java as a Spring Boot App.

3. Frontend (Vite + React):
   =>Navigate to  frontend folder.
   =>Run npm install.
   =>Start with npm run dev.
   =>Open http://localhost:5173/ in the browser.

   
