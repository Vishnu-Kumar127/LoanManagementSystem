package DAO;

import Model.Loan;
import Model.LoanApplications;
import Model.Loanrepayement;
import Util.Db_Connector;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoanDAO {
    public static ArrayList<Loan> getAllLoanPlans() {
        ArrayList<Loan> loans = new ArrayList<>();
        try {
            Connection connection = Db_Connector.getConnection();
            String sql = "SELECT * FROM Loan_list";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                loans.add(new Loan(
                        rs.getInt("loan_id"),
                        rs.getInt("max_period"),
                        rs.getString("loan_type"),
                        rs.getDouble("interest_rate"),
                        rs.getDouble("max_amount")
                ));
            }

            return loans;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean applyForLoan(LoanApplications loan) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = Db_Connector.getConnection();
            conn.setAutoCommit(false);

            String query = "INSERT INTO LoanApplications (user_id, loan_id, interest_rate, start_date, loan_Period, " +
                    "loan_amount, EMI_amount, total_Interest, remaining_Interest, remaining_principal, " +
                    "status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, loan.getUserid());
            stmt.setInt(2, loan.getLoanid());
            stmt.setDouble(3, loan.getInterest());
            stmt.setDate(4, new Date(loan.getDate().getTime()));
            stmt.setInt(5, loan.getPeriodinmonths());
            stmt.setDouble(6, loan.getAmount());
            stmt.setDouble(7, loan.getEMIamount());
            stmt.setDouble(8, loan.getTotalinterest());
            stmt.setDouble(9, loan.getRemaininginterest());
            stmt.setDouble(10, loan.getRemainingprinicipal());
            stmt.setString(11, loan.getStatus());


            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                success = true;
            }
            System.out.println("Inserting loan: " + loan);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return success;
    }


    public static List<LoanApplications> getLoansByUser(int userId) {
        List<LoanApplications> loans = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Db_Connector.getConnection();
            String query = "SELECT * FROM LoanApplications WHERE user_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                LoanApplications loan = new LoanApplications(
                        rs.getInt("loanAppId"),
                        rs.getInt("user_id"),
                        rs.getInt("loan_id"),
                        rs.getDouble("interest_rate"),
                        rs.getDate("start_date"),
                        rs.getInt("loan_Period"),
                        rs.getDouble("loan_amount"),
                        rs.getDouble("EMI_amount"),
                        rs.getDouble("total_Interest"),
                        rs.getDouble("remaining_Interest"),
                        rs.getDouble("remaining_principle"),
                        rs.getString("status")
                );
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return loans;
    }

    public  static LoanApplications getloanapplication(int loanappid)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = Db_Connector.getConnection();
            String query = "SELECT * FROM LoanApplications WHERE loanAppId = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, loanappid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return ( new LoanApplications(
                        rs.getInt("loanAppId"),
                        rs.getInt("user_id"),
                        rs.getInt("loan_id"),
                        rs.getDouble("interest_rate"),
                        rs.getDate("start_date"),
                        rs.getInt("loan_Period"),
                        rs.getDouble("loan_amount"),
                        rs.getDouble("EMI_amount"),
                        rs.getDouble("total_Interest"),
                        rs.getDouble("remaining_Interest"),
                        rs.getDouble("remaining_principle"),
                        rs.getString("status")
                ));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static boolean updateLoanStatus(int loanAppId, String status) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean updated = false;

        try {
            conn = Db_Connector.getConnection();
            String query = "UPDATE LoanApplications SET status = ? WHERE loanAppId = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, loanAppId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                updated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return updated;
    }

    public static List<Map<String, Object>> getUserMonthlyPayable(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> payables = new ArrayList<>();

        try {
            conn = Db_Connector.getConnection();
            String query = "SELECT loanAppId, EMI_amount, status FROM LoanApplications WHERE user_id = ? AND status = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setString(2, "Pending");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("loanAppId", rs.getInt("loanAppId"));
                record.put("EMI_amount", rs.getDouble("EMI_amount"));
                record.put("status", rs.getString("status"));
                payables.add(record);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }

        return payables;
    }

    public static List<Loanrepayement> getRepaymentsByLoanAppId(int loanAppId) {
        List<Loanrepayement> repayments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Db_Connector.getConnection();
            String query = "SELECT * FROM Loan_repayment WHERE loanAppId = ? And status = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, loanAppId);
            stmt.setString(2,"Paid");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Loanrepayement repayment = new Loanrepayement(
                        rs.getInt("loan_payment_id"),
                        rs.getInt("loanAppId"),
                        rs.getDate("due_date"),
                        rs.getString("status"),
                        rs.getDouble("EMI_amount"),
                        rs.getDouble("interest_paid"),
                        rs.getDouble("principal_paid"),
                        rs.getDouble("penalty_paid")
                );
                repayments.add(repayment);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return repayments;
    }

    public static String processLoanRepayment(int loanAppId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Db_Connector.getConnection();
            conn.setAutoCommit(false);

            double emiAmount = 0;

            String fetchQuery = "SELECT EMI_amount, status FROM LoanApplications WHERE loanAppId = ?";
            stmt = conn.prepareStatement(fetchQuery);
            stmt.setInt(1, loanAppId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                emiAmount = rs.getDouble("EMI_amount");
                String status = rs.getString("status");

                if ("Pending".equalsIgnoreCase(status)) {
                    return "Loan status is still pending. Cannot process repayment.";
                } else if ("Rejected".equalsIgnoreCase(status)) {
                    return "Loan status is Rejected. Cannot process repayment.";
                }
            } else {
                return "Loan application not found with ID: " + loanAppId;
            }

            rs.close();
            stmt.close();

            String insertRepaymentQuery = "INSERT INTO Loan_repayment (loanAppId, EMI_amount, interest_paid, principal_paid, penalty_paid, date, status) " +
                    "VALUES (?, ?, 0, 0, 0, NOW(), 'Pending')";
            stmt = conn.prepareStatement(insertRepaymentQuery);
            stmt.setInt(1, loanAppId);
            stmt.setDouble(2, emiAmount);
            stmt.executeUpdate();

            conn.commit();
            return null; // Success
        } catch (SQLException | ClassNotFoundException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return "Database error: " + e.getMessage();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }



    public boolean processLoanRepaymentAfterpayment(Connection conn, int loanAppId, int loanrepaymentid) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {

            String fetchQuery = "SELECT EMI_amount, remaining_Interest, remaining_principal, interest_rate FROM LoanApplications WHERE loanAppId = ?";
            stmt = conn.prepareStatement(fetchQuery);
            stmt.setInt(1, loanAppId);
            rs = stmt.executeQuery();

            double emiAmount = 0, remainingInterest = 0, remainingPrincipal = 0, interestRate = 0;

            if (rs.next()) {
                emiAmount = rs.getDouble("EMI_amount");
                remainingInterest = rs.getDouble("remaining_Interest");
                remainingPrincipal = rs.getDouble("remaining_principal");
                interestRate = rs.getDouble("interest_rate");
            } else {
                throw new RuntimeException("LoanApplication not found for ID: " + loanAppId);
            }

            rs.close();
            stmt.close();

            double monthlyInterestRate = interestRate / 12.0 / 100.0;
            double interestPaid = Math.min(remainingInterest, emiAmount * monthlyInterestRate);
            double principalPaid = Math.min(remainingPrincipal, emiAmount - interestPaid);

            String updateRepaymentQuery = "UPDATE Loan_repayment SET interest_paid = ?, principal_paid = ?, status = 'Paid' WHERE loan_payment_id = ?";
            stmt = conn.prepareStatement(updateRepaymentQuery);
            stmt.setDouble(1, interestPaid);
            stmt.setDouble(2, principalPaid);
            stmt.setInt(3, loanrepaymentid);
            stmt.executeUpdate();
            stmt.close();

            double updatedRemainingInterest = Math.max(0, remainingInterest - interestPaid);
            double updatedRemainingPrincipal = Math.max(0, remainingPrincipal - principalPaid);

            String updateLoanQuery = "UPDATE LoanApplications SET remaining_Interest = ?, remaining_principal = ? WHERE loanAppId = ?";
            stmt = conn.prepareStatement(updateLoanQuery);
            stmt.setDouble(1, updatedRemainingInterest);
            stmt.setDouble(2, updatedRemainingPrincipal);
            stmt.setInt(3, loanAppId);
            stmt.executeUpdate();
            stmt.close();

            if (updatedRemainingInterest <= 0.01 && updatedRemainingPrincipal <= 0.01) {
                String updateStatusQuery = "UPDATE LoanApplications SET status = 'Completed' WHERE loanAppId = ?";
                stmt = conn.prepareStatement(updateStatusQuery);
                stmt.setInt(1, loanAppId);
                stmt.executeUpdate();
                stmt.close();
            }

            success = true;

        } catch (SQLException e) {
            throw new RuntimeException("Error processing loan repayment: " + e.getMessage(), e);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
        }

        return success;
    }





}
