import React, { useEffect, useState } from "react";
import { useAuth } from "../../contexts/AuthContext";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../styles/AdminDashboard.css";

export default function AdminDashboard() {
  const [loading, setLoading] = useState(true);
  const [accounts, setAccounts] = useState([]);
  const [successMessage, setSuccessMessage] = useState("");
  const [deletedAccountNumber, setDeletedAccountNumber] = useState(null);
  const [searchQuery, setSearchQuery] = useState("");
  const { currentAdmin, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const adminToken = localStorage.getItem("adminAuthToken");
    if (!adminToken) {
      navigate("/admins/login", { replace: true });
      return;
    }

    async function fetchAccounts() {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/v1/accounts",
          {
            headers: {
              Authorization: `Bearer ${adminToken}`,
            },
          }
        );
        setAccounts(response.data);
      } catch (err) {
        console.error("Failed to fetch accounts:", err);
      } finally {
        setLoading(false);
      }
    }

    fetchAccounts();
  }, [navigate]);

  const handleViewClick = (accountNumber) => {
    navigate(`/${accountNumber}/profile`);
  };

  const handleViewTransactionsClick = (accountNumber) => {
    navigate(`/${accountNumber}/transactions`);
  };

  const handleDeleteClick = async (accountNumber) => {
    const adminToken = localStorage.getItem("adminAuthToken");
    try {
      await axios.delete(
        `http://localhost:8080/api/v1/accounts/${accountNumber}`,
        {
          headers: {
            Authorization: `Bearer ${adminToken}`,
          },
        }
      );
      setSuccessMessage("Account deleted successfully.");
      setDeletedAccountNumber(accountNumber);
      setTimeout(() => {
        setAccounts(
          accounts.filter((account) => account.accountNumber !== accountNumber)
        );
        setSuccessMessage("");
        setDeletedAccountNumber(null);
      }, 2000);
    } catch (err) {
      console.error("Failed to delete account:", err);
    }
  };

  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
  };

  const filteredAccounts = accounts.filter((account) =>
    account.accountHolderName.toLowerCase().includes(searchQuery.toLowerCase())
  );

  if (loading) return <div>Loading...</div>;

  return (
    <div className="admin-dashboard-container">
      {successMessage && (
        <div className="success-message">{successMessage}</div>
      )}
      <div className="search-bar-container">
        <input
          type="text"
          
          placeholder="Search by account holder name"
          value={searchQuery}
          onChange={handleSearchChange}
          className="search-bar"
        />
      </div>
      {filteredAccounts.length === 0 ? (
        <p>No accounts found</p>
      ) : (
        <ul className="accounts-list">
          {filteredAccounts.map((account) => (
            <li
              key={account.accountNumber}
              className={`account-item ${
                deletedAccountNumber === account.accountNumber ? "deleting" : ""
              }`}
            >
              <div className="account-details">
                <div>
                  <strong>Account Number:</strong> {account.accountNumber}
                </div>
                <div>
                  <strong>Account Holder Name:</strong>{" "}
                  {account.accountHolderName}
                </div>
                <div>
                  <strong>Balance:</strong> ${account.balance.toFixed(2)}
                </div>
                <div>
                  <strong>Created At:</strong>{" "}
                  {new Date(account.createdAt).toLocaleDateString()}
                </div>
              </div>
              <div className="button-container">
                <button
                  className="view-button"
                  onClick={() => handleViewClick(account.accountNumber)}
                >
                  View Profile
                </button>
                <button
                  className="view-transactions-button"
                  onClick={() =>
                    handleViewTransactionsClick(account.accountNumber)
                  }
                >
                  View Transactions
                </button>
                <button
                  className="delete-button"
                  onClick={() => handleDeleteClick(account.accountNumber)}
                >
                  Delete Profile
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
