# bank-app-v2
# Overview
The Bank App is a simple online banking platform built using modern web technologies. It supports two types of users: Admin and Customer. Customers can create accounts, transfer funds, view their transactions, and manage their profiles, while admins can manage customer accounts and view transactions. The app uses JWT (JSON Web Token) for secure authentication and authorization.

Features
Customer Functionalities:
Create an Account: Sign up for a new account.
Login: Authenticate using credentials (with JWT token handling).
Fund Transfer: Transfer funds between accounts.
View Transaction History: View the transaction history of your account.
View Profile: View and manage your personal information.
View Balance: Check your account balance.
Logout: Securely log out from the application.
Admin Functionalities:
Login: Authenticate as an admin using credentials (with JWT token handling).
Create an Account for Customer: Add new customer accounts.
View All Accounts: View details of all customer accounts.
View Transactions of Customers: Access the transaction history of any customer.
Update Customer Profile: Edit personal information of any customer.
Delete an Account: Remove a customer account.
Logout: Securely log out from the admin panel.
Technology Stack
Frontend:

React.js (with Material-UI for styling)
Axios for API calls
React Router DOM for routing
Backend:

Spring Boot (REST API)
Spring Security for JWT-based authentication and authorization
Database:

MySQL
Security
JWT Authentication: The app uses JSON Web Tokens for user authentication. After logging in, the backend issues a JWT, which is stored in the frontend (e.g., localStorage). Each subsequent request to the server includes this token for user identification.
Role-based Access Control: There are two roles: Admin and Customer. Specific routes and functionalities are protected based on the user’s role.



Customer Login

![User_Login](https://github.com/user-attachments/assets/b929779f-993d-402f-87bd-855a0652aa65)



Customer Signup

![User_Signup](https://github.com/user-attachments/assets/5e4c2dd0-1cbc-464a-92dc-ec7fe0e9216f)


Admin Login

![Admin_Login](https://github.com/user-attachments/assets/36a36913-4d36-490f-8ac5-a832efcdbc1b)


Customer Dashboard

![User_Dashboard](https://github.com/user-attachments/assets/60fc8b83-47e6-4fad-9d86-241fd1e7535d)


Customer Fund Transfer

![User_FundTransfer](https://github.com/user-attachments/assets/5b05ee17-a1fe-456b-84de-ddbb714dfa3e)


Customer Transactions History

![User_Transactions](https://github.com/user-attachments/assets/31d4bf5b-81a5-4761-8925-073f5c2e1a7e)


Customer Profile

![User_Profile](https://github.com/user-attachments/assets/59a7d03a-9651-4965-bf83-82d610d0fec4)


Admin Dashbboard

![Admin_Dashboard](https://github.com/user-attachments/assets/59389e47-4165-48c7-a0bc-1b7aaaad65f8)


Admin Search

![Admin_Search](https://github.com/user-attachments/assets/b5a64c59-7c1b-45f3-b974-8aa7b0384139)


Admin Edit Customer Profile

![Admin_Edit_User_Profile](https://github.com/user-attachments/assets/495999d2-6742-44b0-bf58-69e3e210bb04)

