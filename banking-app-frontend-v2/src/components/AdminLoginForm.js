import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "../contexts/AuthContext";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";
import Button from "./Button";
import Form from "./Form";
import TextInput from "./TextInput";
import { encryptPassword } from "./EncryptionUtil";

export default function AdminLoginForm() {
  const [identifier, setIdentifier] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const { adminLogin } = useAuth();
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();

    try {
      setError("");
      setLoading(true);
      const encryptedPassword = encryptPassword(password);
      await adminLogin(identifier, encryptedPassword);
    } catch (err) {
      setLoading(false);
      setError("Invalid credentials!");
    }
  }

  return (
    <Form style={{ height: "330px" }} onSubmit={handleSubmit}>
      <TextInput
        required
        type="email"
        placeholder="Enter email"
        icon="alternate_email"
        value={identifier}
        onChange={(e) => setIdentifier(e.target.value)}
      />
      <div style={{ position: "relative" }}>
        <TextInput
          type={showPassword ? "text" : "password"}
          placeholder="Enter password"
          required
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button
          type="button"
          onClick={() => setShowPassword(!showPassword)}
          style={{
            position: "absolute",
            top: "30%",
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
      <Button disabled={loading} type="submit">
        <span>Login</span>
      </Button>
      {error && <p className="error">{error}</p>}
    </Form>
  );
}
