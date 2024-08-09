import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";
import Button from "./Button";
import Checkbox from "./Checkbox";
import Form from "./Form";
import TextInput from "./TextInput";

export default function SignupForm() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [address, setAddress] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [agree, setAgree] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState("");
  const [countryCode] = useState("PK");
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const navigate = useNavigate();

  let successTimeout;
  useEffect(() => {
    return () => {
      if (successTimeout) {
        clearTimeout(successTimeout);
      }
    };
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();

    if (password !== confirmPassword) {
      return setError("Password mismatched!");
    }
    if (!agree) {
      return setError("You must agree to the Terms & Conditions.");
    }

    const nameRegex = /^[A-Za-z\s]+$/;
    if (!nameRegex.test(name)) {
      return setError("Name must contain only alphabets.");
    }

    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (!passwordRegex.test(password)) {
      return setError(
        "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be atleast 8 characters long."
      );
    }

    const phoneRegex = /^\d{10}$/;
    if (!phoneRegex.test(phoneNumber)) {
      return setError("Phone number must be exactly 10 digits.");
    }

    try {
      setError("");
      setSuccess("");
      setLoading(true);

      const signupDetails = {
        name,
        email,
        password,
        address,
        phoneNumber,
        countryCode,
      };

      await axios.post(
        "http://localhost:8080/api/v1/users",
        signupDetails,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      setLoading(false);
      setSuccess("Account created successfully");

      successTimeout = setTimeout(() => {
        setSuccess("");
        if (localStorage.getItem("adminAuthToken")) {
          navigate("/admins/dashboard");
        } else {
          navigate("/users/login");
        }
      }, 3000);

      setAgree(false);
    } catch (err) {
      setLoading(false);

      if (err.response) {
        if (typeof err.response.data === "string") {
          setError(err.response.data);
        } else if (err.response.data && err.response.data.message) {
          setError(err.response.data.message);
        } else {
          setError("Failed to create an account!");
        }
      } else {
        setError("Failed to create an account!");
      }
    }
  }

  return (
    <Form style={{ height: "500px" }} onSubmit={handleSubmit}>
      <TextInput
        required
        type="text"
        placeholder="Enter name"
        icon="person"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <TextInput
        required
        type="email"
        placeholder="Enter email"
        icon="alternate_email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <div style={{ position: "relative" }}>
        <TextInput
          required
          type={showPassword ? "text" : "password"}
          placeholder="Enter password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
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
          }}
        >
          <FontAwesomeIcon icon={showPassword ? faEyeSlash : faEye} />
        </button>
      </div>
      <div style={{ position: "relative" }}>
        <TextInput
          required
          type={showConfirmPassword ? "text" : "password"}
          placeholder="Confirm password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
        <button
          type="button"
          onClick={() => setShowConfirmPassword(!showConfirmPassword)}
          style={{
            position: "absolute",
            top: "50%",
            right: "10px",
            transform: "translateY(-50%)",
            border: "none",
            background: "transparent",
            cursor: "pointer",
          }}
        >
          <FontAwesomeIcon icon={showConfirmPassword ? faEyeSlash : faEye} />
        </button>
      </div>
      <TextInput
        required
        type="text"
        placeholder="Enter address"
        icon="home"
        value={address}
        onChange={(e) => setAddress(e.target.value)}
      />
      <TextInput type="text" icon="flag" value={countryCode} disabled />
      <TextInput
        required
        type="text"
        placeholder="Enter phone number"
        icon="smartphone"
        value={phoneNumber}
        onChange={(e) => setPhoneNumber(e.target.value)}
      />
      <Checkbox
        required
        type="checkbox"
        text=" I agree to the Terms &amp; Conditions"
        checked={agree}
        onChange={(e) => setAgree(e.target.checked)}
      />
      <Button disabled={loading} type="submit">
        <span>Submit now</span>
      </Button>
      {error && <p className="error">{error}</p>}
      {success && <p className="success">{success}</p>}
      <div className="container">
        {!localStorage.getItem("adminAuthToken") && (
          <div className="info">
            Already have an account? <Link to="/users/login">Login</Link>{" "}
            instead.
          </div>
        )}
      </div>
    </Form>
  );
}
