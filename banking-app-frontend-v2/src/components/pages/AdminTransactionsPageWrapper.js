import React from "react";
import { useParams } from "react-router-dom";
import AdminTransactionsPage from "./AdminTrasanctionPage";

export default function AdminTransactionsPageWrapper() {
  const { accountNumber } = useParams();

  return <AdminTransactionsPage accountNumber={accountNumber} />;
}
