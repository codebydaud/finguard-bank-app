import "../styles/App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "../contexts/AuthContext";
import Layout from "./Layout";
import Signup from "./pages/Signup";
import Login from "./pages/Login";
import PrivateRoute from "./PrivateRoute";
import UserDashboard from "./pages/UserDashboard";
import AdminLogin from "./pages/AdminLogin";
import FundTrasferPage from "./pages/FundTransferPage";
import ProfilePage from "./pages/ProfilePage";
import TransactionsPage from "./pages/TransactionsPage";
import AdminDashboard from "./pages/AdminDashboard";
import ProfilePageAdmin from "./pages/ProfilePageAdmin";
import AdminTransactionsPageWrapper from "./pages/AdminTransactionsPageWrapper";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Layout>
          <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/users/signup" element={<Signup />} />
            <Route path="/users/login" element={<Login />} />
            <Route path="/admins/login" element={<AdminLogin />} />
            <Route
              path="/fundtransfer"
              element={
                <PrivateRoute>
                  <FundTrasferPage />
                </PrivateRoute>
              }
            />
            <Route
              path="/profile"
              element={
                <PrivateRoute>
                  <ProfilePage />
                </PrivateRoute>
              }
            />
            <Route
              path="/transactions"
              element={
                <PrivateRoute>
                  <TransactionsPage />
                </PrivateRoute>
              }
            />
            <Route
              path="/users/dashboard"
              element={
                <PrivateRoute>
                  <UserDashboard />
                </PrivateRoute>
              }
            />
            <Route
              path="/admins/dashboard"
              element={
                <PrivateRoute adminOnly>
                  <AdminDashboard />
                </PrivateRoute>
              }
            />
            <Route
              path="/:accountNumber/profile"
              element={
                <PrivateRoute adminOnly>
                  <ProfilePageAdmin />
                </PrivateRoute>
              }
            />
            <Route
              path="/:accountNumber/transactions"
              element={
                <PrivateRoute adminOnly>
                  <AdminTransactionsPageWrapper />
                </PrivateRoute>
              }
            />
          </Routes>
        </Layout>
      </AuthProvider>
    </BrowserRouter>
  );
}
