package DAO;

import Model.Payment;
import Util.Db_Connector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {


    public static String addPayment(Payment payment) {
        Connection conn = null;
        PreparedStatement insertStmt = null;
        PreparedStatement selectStmt = null;
        ResultSet rs = null;
        String resultMessage;

        try {
            conn = Db_Connector.getConnection();
            conn.setAutoCommit(false);

            String checkStatusQuery = "SELECT status, loanAppId FROM Loan_repayment WHERE loan_payment_id = ?";
            selectStmt = conn.prepareStatement(checkStatusQuery);
            selectStmt.setInt(1, payment.getLoanpaymentid());
            rs = selectStmt.executeQuery();

            int loanAppId = 0;
            if (rs.next()) {
                String status = rs.getString("status");
                loanAppId = rs.getInt("loanAppId");

                if ("Paid".equalsIgnoreCase(status)) {
                    return "{\"success\": false, \"message\": \"This EMI is already paid. No further payment is required.\"}";
                }
            } else {
                return "{\"success\": false, \"message\": \"Invalid loan payment ID. Please check the details and try again.\"}";
            }

            rs.close();
            selectStmt.close();

            // Insert payment
            String insertQuery = "INSERT INTO Payment (amount, payment_date, loan_payment_id) VALUES (?, ?, ?)";
            insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setDouble(1, payment.getAmount());
            insertStmt.setDate(2, new Date(payment.getDate().getTime()));
            insertStmt.setInt(3, payment.getLoanpaymentid());

            int rowsInserted = insertStmt.executeUpdate();
            insertStmt.close();

            if (rowsInserted > 0) {
                LoanDAO loan = new LoanDAO();
                boolean updated = loan.processLoanRepaymentAfterpayment(conn, loanAppId, payment.getLoanpaymentid());

                if (updated) {
                    conn.commit();
                    resultMessage = "{\"success\": true, \"message\": \"Payment processed successfully.\"}";
                } else {
                    conn.rollback();
                    resultMessage = "{\"success\": false, \"message\": \"Payment inserted but failed to update repayment status.\"}";
                }
            } else {
                conn.rollback();
                resultMessage = "{\"success\": false, \"message\": \"Payment could not be processed.\"}";
            }

        } catch (SQLException | ClassNotFoundException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
            resultMessage = "{\"success\": false, \"message\": \"Error occurred: " + e.getMessage().replace("\"", "'") + "\"}";
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (insertStmt != null) insertStmt.close(); } catch (SQLException ignored) {}
            try { if (selectStmt != null) selectStmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }

        return resultMessage;
    }

    public static List<Payment> getPaymentsByLoan(int loanAppId) {
        List<Payment> payments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Db_Connector.getConnection();
            String query = "SELECT p.* FROM Payment p JOIN Loan_repayment lr ON p.loan_payment_id = lr.loan_payment_id " +
                    "WHERE lr.loanAppId = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, loanAppId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("payment_id"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date"),
                        rs.getInt("loan_payment_id")
                );
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return payments;
    }


    public static double getTotalPaidAmount(int loanAppId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double totalPaid = 0;

        try {
            conn = Db_Connector.getConnection();
            String query = "SELECT SUM(p.amount) AS totalPaid FROM Payment p " +
                    "JOIN Loan_repayment lr ON p.loan_payment_id = lr.loan_payment_id " +
                    "WHERE lr.loanAppId = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, loanAppId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                totalPaid = rs.getDouble("totalPaid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return totalPaid;
    }
}
