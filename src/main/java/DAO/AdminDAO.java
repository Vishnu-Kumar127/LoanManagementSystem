package DAO;

import Model.User;
import Model.Loan;
import Model.LoanApplications;
import Util.Db_Connector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public boolean addLoanPlan(Loan loan) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = Db_Connector.getConnection();
            String query = "INSERT INTO Loan_list (loan_type, max_period, interest_rate, max_amount) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, loan.getLoantype());
            stmt.setInt(2, loan.getMaxperiod());
            stmt.setDouble(3, loan.getInterrate());
            stmt.setDouble(4, loan.getMaxamount());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return success;
    }

    public static List<LoanApplications> getPendingLoanApplications() {
        List<LoanApplications> loanApplications = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Db_Connector.getConnection();
            String query = "SELECT * FROM LoanApplications WHERE status = 'Pending'";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                LoanApplications loanApp = new LoanApplications(
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
                        rs.getDouble("remaining_principal"),
                        rs.getString("status")
                );
                loanApplications.add(loanApp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return loanApplications;
    }

    public static User getAdminByUsername(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = Db_Connector.getConnection();
            String query = "SELECT * FROM User WHERE user_name = ? and role_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setInt(2, 2);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getInt("role_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getBigDecimal("phone_Number"),
                        rs.getString("user_name"),
                        rs.getString("random_salt"),
                        rs.getString("password"),
                        rs.getInt("address_id")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
