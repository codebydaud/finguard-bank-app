import React, { createContext, useState, useContext, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);
  const [currentAdmin, setCurrentAdmin] = useState(null);
  const [needsProfileUpdate, setNeedsProfileUpdate] = useState(false);
  const navigate = useNavigate();

  const fetchUserProfile = async () => {
    const token = localStorage.getItem("userAuthToken");
    if (token) {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/v1/users/profile",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setCurrentUser(response.data);
        setNeedsProfileUpdate(false);
      } catch {
        localStorage.removeItem("userAuthToken");
        setCurrentUser(null);
        navigate("/users/login", { replace: true });
      }
    }
  };

  const fetchUserBalance = async () => {
    const token = localStorage.getItem("userAuthToken");
    if (token) {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/v1/users/balance",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        updateCurrentUserField("balance", response.data.balance);
      } catch (err) {
        console.error("Failed to fetch balance:", err);
      }
    }
  };

  useEffect(() => {
    if (needsProfileUpdate) {
      fetchUserProfile();
    }
  }, [needsProfileUpdate]);

  useEffect(() => {
    fetchUserProfile();
  }, []);

  const login = async (identifier, password) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/v1/users/login",
        { identifier, password },
        { headers: { "Content-Type": "application/json" } }
      );
      const token = response.headers["authorization"].split(" ")[1];
      localStorage.setItem("userAuthToken", token);
      await fetchUserProfile();
      console.log("Navigating to /users/dashboard");
      navigate("/users/dashboard", { replace: true });
    } catch {
      throw new Error("Invalid credentials");
    }
  };

  const adminLogin = async (identifier, password) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/v1/admins/login",
        { identifier, password },
        { headers: { "Content-Type": "application/json" } }
      );
      const token = response.headers["authorization"].split(" ")[1];
      localStorage.setItem("adminAuthToken", token);
      setCurrentAdmin(response.data);
      navigate("/admins/dashboard", { replace: true });
    } catch {
      throw new Error("Invalid credentials");
    }
  };

  const logout = async () => {
    const userToken = localStorage.getItem("userAuthToken");
    if (userToken) {
      try {
        await axios.get("http://localhost:8080/api/v1/users/logout", {
          headers: { Authorization: `Bearer ${userToken}` },
        });
      } catch {
        console.error("Failed to log out user");
      }
      localStorage.removeItem("userAuthToken");
      setCurrentUser(null);
    }

    const adminToken = localStorage.getItem("adminAuthToken");
    if (adminToken) {
      try {
        await axios.get("http://localhost:8080/api/v1/admins/logout", {
          headers: { Authorization: `Bearer ${adminToken}` },
        });
      } catch {
        console.error("Failed to log out admin");
      }
      localStorage.removeItem("adminAuthToken");
      setCurrentAdmin(null);
    }

    navigate("/users/login", { replace: true });
  };

  const triggerProfileUpdate = () => {
    setNeedsProfileUpdate(true);
  };

  const updateCurrentUserField = (field, value) => {
    setCurrentUser((prevUser) => ({
      ...prevUser,
      [field]: value,
    }));
  };

  return (
    <AuthContext.Provider
      value={{
        currentUser,
        currentAdmin,
        login,
        adminLogin,
        logout,
        triggerProfileUpdate,
        updateCurrentUserField,
        fetchUserBalance, // Expose the new function
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
