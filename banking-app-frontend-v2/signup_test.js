// tests/signupForm.test.js
const nock = require("nock");

Feature("Signup Form");

Scenario(
  "should display the signup form and unsuccessful signup",
  async ({ I }) => {
    nock("http://localhost:8080")
      .post("/api/v1/users")
      .reply(400, { message: "Email already exists" });

    I.amOnPage("/users/signup");

    I.executeScript(() => {
      const nameInput = document.querySelector(
        'input[placeholder="Enter name"]'
      );
      const emailInput = document.querySelector(
        'input[placeholder="Enter email"]'
      );
      const passwordInput = document.querySelector(
        'input[placeholder="Enter password"]'
      );
      const confirmPasswordInput = document.querySelector(
        'input[placeholder="Confirm password"]'
      );
      const addressInput = document.querySelector(
        'input[placeholder="Enter address"]'
      );
      const phoneNumberInput = document.querySelector(
        'input[placeholder="Enter phone number"]'
      );

      return {
        namePlaceholder: nameInput ? nameInput.placeholder : null,
        emailPlaceholder: emailInput ? emailInput.placeholder : null,
        passwordPlaceholder: passwordInput ? passwordInput.placeholder : null,
        confirmPasswordPlaceholder: confirmPasswordInput
          ? confirmPasswordInput.placeholder
          : null,
        addressPlaceholder: addressInput ? addressInput.placeholder : null,
        phoneNumberPlaceholder: phoneNumberInput
          ? phoneNumberInput.placeholder
          : null,
      };
    }).then((placeholders) => {
      I.assertEqual(placeholders.namePlaceholder, "Enter name");
      I.assertEqual(placeholders.emailPlaceholder, "Enter email");
      I.assertEqual(placeholders.passwordPlaceholder, "Enter password");
      I.assertEqual(
        placeholders.confirmPasswordPlaceholder,
        "Confirm password"
      );
      I.assertEqual(placeholders.addressPlaceholder, "Enter address");
      I.assertEqual(placeholders.phoneNumberPlaceholder, "Enter phone number");
    });

    I.fillField("Enter name", "John Doe");
    I.fillField("Enter email", "testing@example.com");
    I.fillField("Enter password", "Password@123");
    I.fillField("Confirm password", "Password@123");
    I.fillField("Enter address", "123 Main St");
    I.fillField("Enter phone number", "3498018899");
    I.checkOption("I agree to the Terms & Conditions");
    I.click("Submit now");

    I.waitForText("Email already exists", 5);
    I.dontSeeInCurrentUrl("/users/login");
  }
);

Scenario("should show invalid name and not submit form", async ({ I }) => {
  I.amOnPage("/users/signup");

  I.fillField("Enter name", "123");
  I.fillField("Enter email", "john.doe@example.com");
  I.fillField("Enter password", "Password");
  I.fillField("Confirm password", "Password");
  I.fillField("Enter address", "123 Main St");
  I.fillField("Enter phone number", "123456789");
  I.checkOption("I agree to the Terms & Conditions");

  I.click("Submit now");

  I.waitForText("Name must contain only alphabets.", 5);
  I.dontSeeInCurrentUrl("/users/dashboard");
});

Scenario(
  "should show password mismatched and not submit form",
  async ({ I }) => {
    I.amOnPage("/users/signup");

    I.fillField("Enter name", "John Doe");
    I.fillField("Enter email", "john.doe@example.com");
    I.fillField("Enter password", "Password");
    I.fillField("Confirm password", "DifferentPassword");
    I.fillField("Enter address", "123 Main St");
    I.fillField("Enter phone number", "1234567890");
    I.checkOption("I agree to the Terms & Conditions");

    I.click("Submit now");

    I.waitForText("Password mismatched!", 5);
    I.dontSeeInCurrentUrl("/users/dashboard");
  }
);

Scenario("should show invalid password and not submit form", async ({ I }) => {
  I.amOnPage("/users/signup");

  I.fillField("Enter name", "John Doe");
  I.fillField("Enter email", "john.doe@example.com");
  I.fillField("Enter password", "Password");
  I.fillField("Confirm password", "Password");
  I.fillField("Enter address", "123 Main St");
  I.fillField("Enter phone number", "1234567890");
  I.checkOption("I agree to the Terms & Conditions");

  I.click("Submit now");

  I.waitForText(
    "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be atleast 8 characters long.",
    5
  );
  I.dontSeeInCurrentUrl("/users/dashboard");
});

Scenario(
  "should show invalid phone number and not submit form",
  async ({ I }) => {
    I.amOnPage("/users/signup");

    I.fillField("Enter name", "John Doe");
    I.fillField("Enter email", "john.doe@example.com");
    I.fillField("Enter password", "Password@123");
    I.fillField("Confirm password", "Password@123");
    I.fillField("Enter address", "123 Main St");
    I.fillField("Enter phone number", "123456789");
    I.checkOption("I agree to the Terms & Conditions");

    I.click("Submit now");

    I.waitForText("Phone number must be exactly 10 digits.", 5);
    I.dontSeeInCurrentUrl("/users/dashboard");
  }
);
