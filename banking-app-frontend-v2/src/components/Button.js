import React from "react";
import classes from "../styles/Button.module.css";

export default function Button({ className, children, ...rest }) {
  return (
    <button
      className={`${classes.button} ${className ? classes[className] : ""}`}
      {...rest}
    >
      {children}
    </button>
  );
}
