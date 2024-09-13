# Bank App
## Overview
The Bank App is a simple online banking platform built using modern web technologies. It supports two types of users: Admin and Customer. Customers can create accounts, transfer funds, view their transactions, and manage their profiles, while admins can manage customer accounts and view transactions. The app uses JWT (JSON Web Token) for secure authentication and authorization.

## Features
### Customer Functionalities:
* Create an Account: Sign up for a new account <br/>
* Login: Authenticate using credentials (with JWT token handling). <br/>
* Fund Transfer: Transfer funds between accounts. <br/>
* View Transaction History: View the transaction history of your account. <br/>
* View Profile: View and manage your personal information. <br/>
* View Balance: Check your account balance. <br/>
* Logout: Securely log out from the application. <br/>
### Admin Functionalities:
* Login: Authenticate as an admin using credentials (with JWT token handling). <br/>
* Create an Account for Customer: Add new customer accounts. <br/>
* View All Accounts: View details of all customer accounts. <br/>
* View Transactions of Customers: Access the transaction history of any customer. <br/>
* Update Customer Profile: Edit personal information of any customer. <br/>
* Delete an Account: Remove a customer account. <br/>
* Logout: Securely log out from the admin panel. <br/>
## Technology Stack
### Frontend:

* React.js (with Material-UI for styling) <br/>
* Axios for API calls <br/>
* React Router DOM for routing <br/>
### Backend:

* Spring Boot (REST API) <br/>
* Spring Security for JWT-based authentication and authorization <br/>
### Database:

* MySQL
### Security:


* JWT Authentication: The app uses JSON Web Tokens for user authentication. After logging in, the backend issues a JWT, which is stored in the frontend (e.g., localStorage). Each subsequent request to the server includes this token for user identification. <br/>
* Role-based Access Control: There are two roles: Admin and Customer. Specific routes and functionalities are protected based on the user’s role. <br/>

## VIDEO DEMO

[Bank-App.webm](https://github.com/user-attachments/assets/a8ad14b6-70ef-4f7b-a815-b2322358022a)

## Screenshots

### Customer Login

![User_Login](https://github.com/user-attachments/assets/b929779f-993d-402f-87bd-855a0652aa65 | width=100)



### Customer Signup

![User_Signup](https://github.com/user-attachments/assets/5e4c2dd0-1cbc-464a-92dc-ec7fe0e9216f)


### Admin Login

![Admin_Login](https://github.com/user-attachments/assets/36a36913-4d36-490f-8ac5-a832efcdbc1b)


### Customer Dashboard

![User_Dashboard](https://github.com/user-attachments/assets/60fc8b83-47e6-4fad-9d86-241fd1e7535d)


## Customer Fund Transfer

![User_FundTransfer](https://github.com/user-attachments/assets/5b05ee17-a1fe-456b-84de-ddbb714dfa3e)


### Customer Transactions History

![User_Transactions](https://github.com/user-attachments/assets/31d4bf5b-81a5-4761-8925-073f5c2e1a7e)


### Customer Profile

![User_Profile](https://github.com/user-attachments/assets/59a7d03a-9651-4965-bf83-82d610d0fec4)


### Admin Dashbboard

![Admin_Dashboard](https://github.com/user-attachments/assets/59389e47-4165-48c7-a0bc-1b7aaaad65f8)


### Admin Search

![Admin_Search](https://github.com/user-attachments/assets/b5a64c59-7c1b-45f3-b974-8aa7b0384139)


### Admin Edit Customer Profile

![Admin_Edit_User_Profile](https://github.com/user-attachments/assets/495999d2-6742-44b0-bf58-69e3e210bb04)

