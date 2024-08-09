import { Link } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import classes from "../styles/Account.module.css";

export default function Account() {
  const { currentUser, currentAdmin, logout } = useAuth();

  return (
    <div className={classes.account}>
      {currentUser ? (
        <>
          <Link to="/users/dashboard" className={classes.link}>
            Dashboard
          </Link>
          <Link to="/fundtransfer" className={classes.link}>
            Transfer Funds
          </Link>
          <Link to="/profile" className={classes.link}>
            Profile
          </Link>
          <Link to="/transactions" className={classes.link}>
            Transactions
          </Link>
          <span className="material-icons-outlined" title="Account">
            account_circle
          </span>
          <span>{currentUser.name}</span>
          <span>${currentUser.balance}</span>
          <span
            className="material-icons-outlined"
            title="Logout"
            onClick={logout}
          >
            logout
          </span>
        </>
      ) : currentAdmin ? (
        <>
          <Link to="/admins/dashboard" className={classes.link}>
            Dashboard
          </Link>
          <Link to="/users/signup" className={classes.link}>
            Create an account
          </Link>
          <span className="material-icons-outlined" title="Account">
            account_circle
          </span>
          <span>Admin</span>
          <span
            className="material-icons-outlined"
            title="Logout"
            onClick={logout}
          >
            logout
          </span>
        </>
      ) : (
        <>
          <Link to="/users/signup" className={classes.link}>
            Signup
          </Link>
          <Link to="/users/login" className={classes.link}>
            Login
          </Link>
          <Link to="/admins/login" className={classes.link}>
            Admin Portal
          </Link>
        </>
      )}
    </div>
  );
}
