import React, { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../../contexts/AuthContext";
import "../../styles/TransactionsPage.css";

export default function UserTransactionsPage() {
  const { currentUser } = useAuth();
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function fetchTransactions() {
      console.log("Fetching transactions...");
      try {
        const token = localStorage.getItem("userAuthToken");
        const endpoint = "http://localhost:8080/api/v1/users/transactions";

        const { data } = await axios.get(endpoint, {
          headers: { Authorization: `Bearer ${token}` },
        });

        setTransactions(data);
      } catch (err) {
        console.error(err);
        setError("Failed to fetch transactions. Please try again later.");
      } finally {
        setLoading(false);
      }
    }

    if (currentUser && currentUser.accountNumber) {
      fetchTransactions();
    } else {
      setError("Account number not provided.");
      setLoading(false);
    }
  }, []);

  if (loading) return <p className="loading">Loading transactions...</p>;

  return (
    <div className="transactions-container">
      {error && <p className="error">{error}</p>}
      {transactions.length === 0 ? (
        <p>No previous transactions.</p>
      ) : (
        <ul className="transactions-list">
          {transactions.map((transaction) => {
            const isSource =
              transaction.sourceAccountNumber === currentUser.accountNumber;
            const isTarget =
              transaction.targetAccountNumber === currentUser.accountNumber;

            return (
              <li key={transaction.transactionId} className="transaction-item">
                <div>
                  <strong>
                    {isSource ? "To" : isTarget ? "From" : "From"} Account
                    Number:
                  </strong>{" "}
                  {isSource
                    ? transaction.targetAccountNumber
                    : isTarget
                    ? transaction.sourceAccountNumber
                    : `${transaction.sourceAccountNumber} to ${transaction.targetAccountNumber}`}
                </div>
                <div>
                  <strong>
                    {isSource ? "To" : isTarget ? "From" : "From/To"}:
                  </strong>{" "}
                  {isSource
                    ? transaction.targetUserName
                    : isTarget
                    ? transaction.sourceUserName
                    : `${transaction.sourceUserName} to ${transaction.targetUserName}`}
                </div>
                <div>
                  <strong>Amount:</strong> ${transaction.amount}
                </div>
                <div>
                  <strong>Type:</strong>{" "}
                  {isSource
                    ? "Sent"
                    : isTarget
                    ? "Received"
                    : transaction.transactionType}
                </div>
                <div>
                  <strong>Description:</strong> {transaction.description}
                </div>
                <div>
                  <strong>Date:</strong>{" "}
                  {new Date(transaction.transactionDate).toLocaleDateString()}
                </div>
              </li>
            );
          })}
        </ul>
      )}
    </div>
  );
}
