package Util;

import Model.User;
import Model.Address;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public class UserUtil {
    public static Pair<User, Address> extractUserAndAddress(HttpServletRequest request,int roleid) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        BigDecimal phonenumber = BigDecimal.valueOf(Long.parseLong(request.getParameter("phonenumber")));
        String doorno = request.getParameter("doorno");
        String street = request.getParameter("street");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String country = request.getParameter("country");

        String random_salt = password_util.generateSalt();
        String encryptpass = password_util.hashPassword(password, random_salt);

        User user = new User(roleid, firstname, lastname, phonenumber, username, random_salt, encryptpass);
        Address address = new Address(0, doorno, street, city, state, country);

        return new Pair<>(user, address);
    }
    public static User extractUserProfileUpdate(HttpServletRequest request) throws NumberFormatException {
        int userId = (int) request.getSession().getAttribute("userId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        BigDecimal phoneNumber = BigDecimal.valueOf(Long.parseLong(request.getParameter("phoneNumber")));
        String username = request.getParameter("user_name");

        return new User(userId, firstName, lastName, phoneNumber, username);
    }
}
