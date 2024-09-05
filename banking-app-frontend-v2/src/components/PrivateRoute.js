import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

function PrivateRoute({ children, adminOnly = false }) {
  const { currentUser, currentAdmin } = useAuth();
  const location = useLocation();

  if (adminOnly) {
    if (!currentAdmin) {
     
      return <Navigate to="/admins/login" replace state={{ from: location }} />;
    }
  } else {
    if (!currentUser) {

      return <Navigate to="/users/login" replace state={{ from: location }} />;
    }
  }

  return children;
}

export default PrivateRoute;
