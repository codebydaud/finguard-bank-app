import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import { useNavigate } from "react-router-dom";
import paymentImage from "../../assets/images/payment.png";
import profileImage from "../../assets/images/man.png";
import historyImage from "../../assets/images/invoice.png";
import "../../styles/UserDashboard.css";

export default function UserDashboard() {
  const { currentUser, triggerProfileUpdate } = useAuth();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    async function checkUser() {
      const token = localStorage.getItem("userAuthToken");
      if (!token) {
        navigate("/users/login", { replace: true });
        return;
      }

      if (!currentUser) {
        try {
          await triggerProfileUpdate();
        } catch (err) {
          setError("Failed to fetch user data.");
        } finally {
          setLoading(false);
        }
      } else {
        setLoading(false);
      }
    }

    checkUser();
  }, [currentUser, navigate, triggerProfileUpdate]);

  if (loading) return <div>Loading...</div>;

  if (error) return <div>{error}</div>;

  return (
    <div>
      <h1>Welcome, {currentUser.name}</h1>
      <section className="dashboardGrid">
        <article className="dashboardItem">
          <img src={paymentImage} alt="Fund Transfer" />
          <Link to="/fundtransfer" className="link">
            Transfer Funds
          </Link>
        </article>
        <article className="dashboardItem">
          <img src={historyImage} alt="Recent Transactions" />
          <Link to="/transactions" className="link">
            Transactions
          </Link>
        </article>
        <article className="dashboardItem">
          <img src={profileImage} alt="Profile" />
          <Link to="/profile" className="link">
            Profile
          </Link>
        </article>
      </section>
    </div>
  );
}
