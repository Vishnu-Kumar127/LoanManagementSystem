package DAO;

import Model.Address;
import Model.User;
import Util.Db_Connector;
import Util.password_util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static User validateUser(String username, String password) {
        String query = "SELECT user_id, role_id, first_name, last_name, phone_Number, user_name, password, random_salt, address_id " +
                "FROM User WHERE user_name = ?";

        try (Connection conn = Db_Connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String randomSalt = rs.getString("random_salt");
                String encryptedPass = rs.getString("password");

                if (password_util.verifyPassword(encryptedPass,randomSalt,password)) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getInt("role_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getBigDecimal("phone_Number"),
                            rs.getString("user_name"),
                            encryptedPass,
                            rs.getInt("address_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;

    }

    public static String getRole(int roleId) {
        String query = "SELECT role_name FROM Role WHERE role_id = ?";
        String role = "";
        Connection conn = null;
        try {
            conn = Db_Connector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) role = rs.getString("role_name");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return role;
    }



    public static User getUserById(int userId) {
        String query = "SELECT * FROM User WHERE user_id = ?";

        try (Connection conn = Db_Connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getInt("role_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getBigDecimal("phone_Number"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getInt("address_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static User getUserByUsername(String username) {
        String query = "SELECT * FROM User WHERE user_name = ?";
        try (Connection conn = Db_Connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getInt("role_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getBigDecimal("phone_Number"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getInt("address_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public static boolean AddUser(User user, Address address) {
        Connection conn = null;
        PreparedStatement userStmt = null;
        PreparedStatement addressStmt = null;
        PreparedStatement updateUserStmt = null;
        ResultSet rs = null;
        int userId = -1;
        int addressId = -1;

        try {
            conn = Db_Connector.getConnection();
            conn.setAutoCommit(false);

            // 1. Insert into User table
            String userQuery = "INSERT INTO User (role_id, first_name, last_name, phone_Number, user_name, random_salt, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            userStmt = conn.prepareStatement(userQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            userStmt.setInt(1, user.getRoleId());
            userStmt.setString(2, user.getFirstName());
            userStmt.setString(3, user.getLastName());
            userStmt.setBigDecimal(4, user.getPhoneNumber()); // Assuming phone number is stored as BIGINT in DB
            userStmt.setString(5, user.getUsername());
            userStmt.setString(6, user.getRandomSalt());
            userStmt.setString(7, user.getPassword());
            userStmt.executeUpdate();

            rs = userStmt.getGeneratedKeys();
            if (rs.next()) {
                userId = rs.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve generated user ID.");
            }

            // 2. Insert into Address table
            String addressQuery = "INSERT INTO Address (user_id, door_no, street, city, state, country) VALUES (?, ?, ?, ?, ?, ?)";
            addressStmt = conn.prepareStatement(addressQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            addressStmt.setInt(1, userId);
            addressStmt.setString(2, address.getDoorno());
            addressStmt.setString(3, address.getStreet());
            addressStmt.setString(4, address.getCity());
            addressStmt.setString(5, address.getState());
            addressStmt.setString(6, address.getCountry());
            addressStmt.executeUpdate();

            rs = addressStmt.getGeneratedKeys();
            if (rs.next()) {
                addressId = rs.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve generated address ID.");
            }

            // 3. Update User table with address_id
            String updateUserQuery = "UPDATE User SET address_id = ? WHERE user_id = ?";
            updateUserStmt = conn.prepareStatement(updateUserQuery);
            updateUserStmt.setInt(1, addressId);
            updateUserStmt.setInt(2, userId);
            updateUserStmt.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException | ClassNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (userStmt != null) userStmt.close(); } catch (SQLException ignored) {}
            try { if (addressStmt != null) addressStmt.close(); } catch (SQLException ignored) {}
            try { if (updateUserStmt != null) updateUserStmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }

        return false;
    }


    public static boolean addAddress(int userid, Address address) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int addressId = 0;
        boolean success = false;
        try {
            conn = Db_Connector.getConnection();
            conn.setAutoCommit(false);
            String addressQuery = "INSERT INTO Address (user_id, door_no, street, city, state, country) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(addressQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, userid);
            stmt.setString(2, address.getDoorno());
            stmt.setString(3, address.getStreet());
            stmt.setString(4, address.getCity());
            stmt.setString(5, address.getState());
            stmt.setString(6, address.getCountry());
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                addressId = rs.getInt(1);
            }
            stmt.close();

            String updateUserQuery = "UPDATE User SET address_id = ? WHERE user_id = ?";
            stmt = conn.prepareStatement(updateUserQuery);
            stmt.setInt(1, addressId);
            stmt.setInt(2, userid);
            stmt.executeUpdate();
            conn.commit();
            success = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return success;
    }
    public static int getaddressid(int userid)
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int addressid = -1;
        try {
            conn = Db_Connector.getConnection();

            String query = "SELECT address_id FROM User WHERE user_name = ?";
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, userid);
            rs = stmt.executeQuery();

            if (rs.next()){
                addressid = rs.getInt("address_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return addressid;
    }

    public static boolean editAddress(Address address) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = Db_Connector.getConnection();
            conn.setAutoCommit(false);

            String query = "UPDATE Address SET door_no = ?, street= ?, city=? , state=?,country=? WHERE address_id = " + address.getAddressid();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, address.getDoorno());
            stmt.setString(2, address.getStreet());
            stmt.setString(3, address.getCity());
            stmt.setString(4, address.getState());
            stmt.setString(5, address.getCountry());
            stmt.executeUpdate();

            conn.commit();
            success = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return success;
    }

    public static boolean editUser(User user, int addressid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = Db_Connector.getConnection();
            conn.setAutoCommit(false);

            String query = "UPDATE User SET first_name = ?, last_name = ?, phone_Number = ?, user_name = ?, address_id = ? WHERE user_id = ?";
            stmt = conn.prepareStatement(query);

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setBigDecimal(3, user.getPhoneNumber());
            stmt.setString(4, user.getUsername());
            stmt.setInt(5, addressid);
            stmt.setInt(6, user.getUserId());

            stmt.executeUpdate();
            conn.commit();
            success = true;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
        return success;
    }

}

