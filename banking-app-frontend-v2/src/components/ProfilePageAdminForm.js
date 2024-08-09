import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";
import { useAuth } from "../contexts/AuthContext";
import TextInput from "./TextInput";
import Form from "./Form";
import Button from "./Button";

export default function ProfilePageAdmin() {
  const { accountNumber } = useParams();
  const { currentAdmin } = useAuth();
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [address, setAddress] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [countryCode] = useState("PK");
  const [balance, setBalance] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [isEditMode, setIsEditMode] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    if (!currentAdmin) {
      navigate("/admins/login", { replace: true });
      return;
    }

    const fetchProfileData = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/v1/accounts/${accountNumber}`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("adminAuthToken")}`,
            },
          }
        );

        const data = response.data;
        setName(data.name);
        setEmail(data.email);
        setAddress(data.address);
        setPhoneNumber(data.phoneNumber);
        setBalance(data.balance);
      } catch (err) {
        setError("Failed to fetch profile data.");
      } finally {
        setLoading(false);
      }
    };

    fetchProfileData();
  }, [accountNumber, currentAdmin, navigate]);

  const handleEditClick = () => {
    setIsEditMode(!isEditMode);
  };

  const validatePhoneNumber = (phoneNumber) => {
    const phoneRegex = /^[0-9]{10}$/;
    return phoneRegex.test(phoneNumber);
  };

  const validatePassword = (password) => {
    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,127}$/;
    return passwordRegex.test(password);
  };

  const handleSaveClick = async (e) => {
    e.preventDefault();

    if (!email || !address || !phoneNumber) {
      setError("Please fill out all required fields.");
      return;
    }

    if (!validatePhoneNumber(phoneNumber)) {
      setError("Phone number must be 10 digits.");
      return;
    }

    if (newPassword && !validatePassword(newPassword)) {
      setError(
        "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long."
      );
      return;
    }

    try {
      const updateData = {
        email,
        address,
        phoneNumber,
      };
      if (newPassword) {
        updateData.password = newPassword;
      }
      await axios.patch(
        `http://localhost:8080/api/v1/accounts/${accountNumber}`,
        updateData,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("adminAuthToken")}`,
          },
        }
      );
      setIsEditMode(false);
      setSuccessMessage("Profile updated successfully.");
      setTimeout(() => {
        setSuccessMessage("");
      }, 2000);
    } catch (err) {
      if (err.response) {
        if (typeof err.response.data === "string") {
          setError(err.response.data);
        } else if (err.response.data && err.response.data.message) {
          setError(err.response.data.message);
        } else {
          setError("Failed to save profile data!");
        }
      } else {
        setError("Failed to save profile data!");
      }
    }
  };

  if (loading) return <p>Loading...</p>;

  return (
    <Form style={{ height: "500px" }} onSubmit={handleSaveClick}>
      {successMessage && (
        <p style={{ color: "green", marginBottom: "10px" }}>{successMessage}</p>
      )}

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
        disabled={!isEditMode}
        type="email"
        placeholder="Email"
        icon="alternate_email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required={isEditMode}
      />

      <label
        htmlFor="address"
        style={{ display: "block", marginBottom: "5px" }}
      >
        Address
      </label>
      <TextInput
        disabled={!isEditMode}
        type="text"
        placeholder="Address"
        icon="home"
        value={address}
        onChange={(e) => setAddress(e.target.value)}
        required={isEditMode}
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
      <TextInput
        type="text"
        icon="smartphone"
        value={phoneNumber}
        disabled={!isEditMode}
        onChange={(e) => setPhoneNumber(e.target.value)}
        required={isEditMode}
      />

      {isEditMode && (
        <>
          <label
            htmlFor="newPassword"
            style={{ display: "block", marginBottom: "5px" }}
          >
            New Password
          </label>
          <div style={{ position: "relative" }}>
            <TextInput
              type={showPassword ? "text" : "password"}
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              style={{
                position: "absolute",
                top: "50%",
                right: "10px",
                transform: "translateY(-50%)",
                border: "none",
                background: "transparent",
                cursor: "pointer",
                padding: "0",
              }}
            >
              <FontAwesomeIcon icon={showPassword ? faEyeSlash : faEye} />
            </button>
          </div>
        </>
      )}

      {isEditMode ? (
        <>
          <Button disabled={loading} type="submit">
            <span>Save Profile</span>
          </Button>
          <Button disabled={loading} onClick={handleEditClick} type="button">
            <span>Cancel</span>
          </Button>
        </>
      ) : (
        <Button disabled={loading} onClick={handleEditClick} type="button">
          <span>Edit Profile</span>
        </Button>
      )}
      {error && <p className="error">{error}</p>}
    </Form>
  );
}
