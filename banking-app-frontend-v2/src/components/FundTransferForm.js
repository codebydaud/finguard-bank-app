import { useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import Button from "./Button";
import Form from "./Form";
import TextInput from "./TextInput";
import Checkbox from "./Checkbox";

const getToken = () => {
  return localStorage.getItem("userAuthToken");
};

export default function FundTransferForm() {
  const [targetAccountNumber, setTargetAccountNumber] = useState("");
  const [description, setDescription] = useState("");
  const [amount, setAmount] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);
  const [agree, setAgree] = useState(false);
  const { currentUser, updateCurrentUserField, fetchUserBalance } = useAuth();

  async function handleSubmit(e) {
    e.preventDefault();

    if (!currentUser) {
      setError("You must be logged in to perform a fund transfer.");
      return;
    }

    if (!agree) {
      setError("You must confirm the provided details.");
      return;
    }

    if (!/^\d{10}$/.test(targetAccountNumber)) {
      setError("Account number must be exactly 10 digits.");
      return;
    }

    if (parseFloat(amount) <= 0) {
      setError("Amount must be greater than zero.");
      return;
    }

    if (parseFloat(amount) % 100 !== 0) {
      setError("Amount must be a multiple of 100.");
      return;
    }

    try {
      setError("");
      setSuccess("");
      setLoading(true);

      const token = getToken();

      const response = await fetch(
        "http://localhost:8080/api/v1/users/transactions",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            targetAccountNumber,
            amount,
            description,
          }),
        }
      );

      if (response.ok) {
        setSuccess("Fund transferred successfully");
        setTargetAccountNumber("");
        setAmount("");
        setDescription("");
        setAgree(false);

        await fetchUserBalance();
        setTimeout(() => {
          setSuccess("");
        }, 2000);
      } else {
        const contentType = response.headers.get("Content-Type");
        let result;
        if (contentType && contentType.includes("application/json")) {
          result = await response.json();
        } else {
          const text = await response.text();
          throw new Error(
            text || "Failed to transfer funds. Please try again."
          );
        }

        if (!response.ok) {
          throw new Error(
            result.message || "Failed to transfer funds. Please try again."
          );
        }
      }
    } catch (err) {
      console.error(err);
      setError(err.message || "Failed to transfer funds. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <Form style={{ height: "400px" }} onSubmit={handleSubmit}>
      <TextInput
        type="text"
        placeholder="Enter target account number"
        icon="account_balance"
        required
        value={targetAccountNumber}
        onChange={(e) => setTargetAccountNumber(e.target.value)}
      />
      <TextInput
        type="text"
        placeholder="Enter description"
        icon="description"
        required
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />
      <TextInput
        type="number"
        placeholder="Enter amount"
        step="0.01"
        min="0"
        icon="attach_money"
        required
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
      />
      <Checkbox
        required
        type="checkbox"
        text="I confirm the provided details"
        checked={agree}
        onChange={(e) => setAgree(e.target.checked)}
      />
      <Button disabled={loading} type="submit">
        <span>{loading ? "Processing..." : "Fund Transfer"}</span>
      </Button>
      {error && <p className="error">{error}</p>}
      {success && <p className="success">{success}</p>}
    </Form>
  );
}
