import Illustration from "../Illustration";
import AdminLoginForm from "../AdminLoginForm";

export default function AdminLogin() {
  return (
    <>
      <h1>Login to your admin account</h1>
      <div className="column">
        <Illustration />
        <AdminLoginForm />
      </div>
    </>
  );
}
