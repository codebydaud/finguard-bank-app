const nock = require("nock");

Feature("Login");

Scenario("should handle successful login and redirect", async ({ I }) => {

  nock("http://localhost:8080")
    .post("/api/v1/users/login")
    .reply(200, { token: "mocked-token" });

  I.amOnPage("/"); 

  I.see("Login to your account");

  I.fillField("Enter email or account number", "daudrizvi@gmail.com");
  I.fillField("Enter password", "Admin@123");

  I.click('button[type="submit"]');

  I.waitForNavigation();

  I.executeScript(() => {
    return localStorage.getItem("userAuthToken");
  }).then((token) => {
    I.assertEqual(
      token,
      "mocked-token",
      "User authentication token is not set."
    );
  });

  I.seeInCurrentUrl("/users/dashboard");
});

Scenario(
  "should display error message on invalid credentials",
  async ({ I }) => {
   
    nock("http://localhost:8080")
      .post("/api/v1/users/login")
      .reply(401, { message: "Invalid credentials!" });

    I.amOnPage("/"); 

    I.see("Login to your account");

    I.fillField("Enter email or account number", "wrong@example.com");
    I.fillField("Enter password", "wrongpassword");

    I.click('button[type="submit"]');

    I.waitForText("Invalid credentials!", 5);

    I.see("Invalid credentials!");
  }
);
