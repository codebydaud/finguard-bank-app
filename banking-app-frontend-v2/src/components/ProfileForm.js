import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext"; // Import AuthContext
import TextInput from "./TextInput"; // Import TextInput component
import Form from "./Form";

export default function ProfilePage() {
  const { currentUser } = useAuth(); // Get currentUser from AuthContext
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [address, setAddress] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [countryCode] = useState("PK"); // Assuming this is a static value
  const [accountNumber, setAccountNumber] = useState("");
  const [balance, setBalance] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    // Redirect to login if no currentUser data
    if (!currentUser) {
      navigate("/users/login", { replace: true });
      return;
    }

    // Populate fields with currentUser data if available
    setName(currentUser.name);
    setEmail(currentUser.email);
    setAddress(currentUser.address);
    setPhoneNumber(currentUser.phoneNumber);
    setAccountNumber(currentUser.accountNumber);
    setBalance(currentUser.balance);
  }, [currentUser, navigate]);

  return (
    <Form style={{ height: "500px" }}>
      <label
        htmlFor="accountNumber"
        style={{ display: "block", marginBottom: "5px" }}
      >
        Account Number
      </label>
      <TextInput
        disabled
        type="text"
        placeholder="Account Number"
        icon="attribution"
        value={accountNumber}
      />

      <label
        htmlFor="balance"
        style={{ display: "block", marginBottom: "5px" }}
      >
        Account Balance
      </label>
      <TextInput
        disabled
        type="text"
        placeholder="Account Balance"
        icon="currency_pound"
        value={`$${balance}`}
      />

      <label htmlFor="name" style={{ display: "block", marginBottom: "5px" }}>
        Name
      </label>
      <TextInput
        disabled
        type="text"
        placeholder="Name"
        icon="person"
        value={name}
      />
      <label htmlFor="email" style={{ display: "block", marginBottom: "5px" }}>
        Email
      </label>
      <TextInput
        disabled
        type="email"
        placeholder="Email"
        icon="alternate_email"
        value={email}
      />
      <label
        htmlFor="Address"
        style={{ display: "block", marginBottom: "5px" }}
      >
        Address
      </label>
      <TextInput
        disabled
        type="text"
        placeholder="Address"
        icon="home"
        value={address}
      />
      <label
        htmlFor="country"
        style={{ display: "block", marginBottom: "5px" }}
      >
        Country
      </label>
      <TextInput type="text" icon="flag" value={countryCode} disabled />
      <label
        htmlFor="phoneNumber"
        style={{ display: "block", marginBottom: "5px" }}
      >
        Phone Number
      </label>
      <TextInput type="text" icon="smartphone" value={phoneNumber} disabled />
    </Form>
  );
}
