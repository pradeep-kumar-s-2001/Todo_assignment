import axios from "axios";
const server_url = "http://localhost:8080/api"

const apiClient = axios.create({
    baseURL:server_url
})

apiClient.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('todo-token'); 
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;  
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

export const axiosInstance = apiClient 