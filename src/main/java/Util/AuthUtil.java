package Util;

import Model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthUtil {

    public static boolean loginUser(
            HttpServletRequest request,
            HttpServletResponse response,
            User user
    ) throws IOException {
        if (user != null && password_util.verifyPassword(user.getPassword(), user.getRandomSalt(), request.getParameter("password"))) {
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedInUser", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("role", user.getRoleId());
            return true;
        }
        return false;
    }
}
