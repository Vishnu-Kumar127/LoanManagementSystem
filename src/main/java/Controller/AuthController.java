package Controller;

import DAO.UserDAO;
import Model.Address;
import Model.User;
import Util.AuthUtil;
import Util.Pair;
import Util.UserUtil;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getPathInfo();

        if ("/login".equals(action)) {
            login(request, response);
        } else if ("/signup".equals(action)) {
            signup(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");

        User user = UserDAO.getUserByUsername(username);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (AuthUtil.loginUser(request, response, user)) {
            out.println("{\"message\": \"Login successful\", \"role\": \"" + UserDAO.getRole(user.getRoleId()) + "\"}");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
        }
    }

    private void signup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Pair<User, Address> userData = UserUtil.extractUserAndAddress(request,1);
        User user = userData.getKey();
        Address address = userData.getValue();

        boolean success = UserDAO.AddUser(user, address);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (success) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("role", user.getRoleId());
            out.println("{\"message\": \"Signup successful\"}");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Signup failed");
        }
    }
}
