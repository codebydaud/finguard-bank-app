import profile from "../assets/images/man.png";
import classes from "../styles/Illustration.module.css";

export default function ProfileIllustration() {
  return (
    <div className={classes.illustration}>
      <img src={profile} alt="Profile" />
    </div>
  );
}
