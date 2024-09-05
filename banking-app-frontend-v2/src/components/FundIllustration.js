import fundTransfer from "../assets/images/payment.png";
import classes from "../styles/Illustration.module.css";

export default function FundIllustration() {
  return (
    <div className={classes.illustration}>
      <img src={fundTransfer} alt="Fund Transfer" />
    </div>
  );
}
