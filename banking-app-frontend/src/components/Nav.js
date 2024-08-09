import { Link } from "react-router-dom";
import logo from "../assets/images/coding.png";
import classes from "../styles/Nav.module.css";
import Account from "./Account";

export default function Nav() {
  return (
    <nav className={classes.nav}>
      <ul>
        <li>
          <Link to="/" className={classes.brand}>
            <img src={logo} alt="FinGuard" />
            <h2>FinGuard</h2>
          </Link>
        </li>
      </ul>
      <Account />
    </nav>
  );
}
