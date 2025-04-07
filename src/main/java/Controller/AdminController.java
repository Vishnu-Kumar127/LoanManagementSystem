package Controller;

import DAO.AdminDAO;
import DAO.LoanDAO;
import DAO.UserDAO;

import Model.Address;
import Model.LoanApplications;
import Model.Loan;
import Model.User;

import Util.UserUtil;
import Util.LoanUtil;
import Util.Pair;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static Util.LoanUtil.extractLoanPlan;

public class AdminController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AdminDAO adminDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        adminDAO = new AdminDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        switch (action) {
            case "/createUser":
                createUser(request, response);
                break;
            case "/createAdmin":
                createAdmin(request, response);
                break;
            case "/addLoanPlan":
                addLoanPlan(request, response);
                break;
            case "/approveLoan":
                approveLoan(request, response);
                break;
            case "/rejectLoan":
                rejectLoan(request, response);
                break;
            case "/applyLoan":
                applyForLoan(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid action");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if ("/pendingloanApplications".equals(action)) {
            getAllpendingLoanApplications(request, response);
        } else if ("/loanrepayment".equals(action)) {
            processloanrepayment(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid action");
        }
    }

    private void processloanrepayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int loanappid = Integer.parseInt(request.getParameter("loanappid"));
        String error = LoanDAO.processLoanRepayment(loanappid);

        if (error == null) {
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Loan repayment created successfully.\"}");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, error);
        }
    }


    private void createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Pair<User, Address> userData = UserUtil.extractUserAndAddress(request, 1);
        User user = userData.getKey();
        Address address = userData.getValue();

        if (userDAO.AddUser(user, address)) {
            response.getWriter().write("User created successfully.");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create user.");
        }
    }

    private void createAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Pair<User, Address> userData = UserUtil.extractUserAndAddress(request, 2);
        User user = userData.getKey();
        Address address = userData.getValue();

        if (userDAO.AddUser(user, address)) {
            response.getWriter().write("Admin created successfully.");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create user.");
        }
    }

    private void addLoanPlan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Loan loan = extractLoanPlan(request);

            if (adminDAO.addLoanPlan(loan)) {
                response.getWriter().write("Loan plan added successfully.\n");
                response.getWriter().write(loan.toString());
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add loan plan.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input format: " + e.getMessage());
        }
    }

    private void applyForLoan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = UserDAO.getUserByUsername(request.getParameter("username"));
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found.");
            return;
        }

        LoanApplications loanApplications = LoanUtil.extractLoanApplication(request, user.getUserId());

        if (LoanDAO.applyForLoan(loanApplications)) {
            response.getWriter().write("Loan application submitted.\n");
            response.getWriter().write(loanApplications.toString());
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to apply for loan.");
        }
    }

    private void approveLoan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int loanAppId = Integer.parseInt(request.getParameter("loanAppId"));

        if (LoanDAO.updateLoanStatus(loanAppId, "Approved")) {
            response.getWriter().write("Loan application approved.");
            response.getWriter().write(Objects.requireNonNull(LoanDAO.getloanapplication(loanAppId)).toString());
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to approve loan application.");
        }
    }

    private void rejectLoan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int loanAppId = Integer.parseInt(request.getParameter("loanAppId"));

        if (LoanDAO.updateLoanStatus(loanAppId, "Rejected")) {
            response.getWriter().write("Loan application rejected.");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to reject loan application.");
        }
    }

    private void getAllpendingLoanApplications(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<LoanApplications> loanApplications = adminDAO.getPendingLoanApplications();
        response.setContentType("application/json");
        response.getWriter().write(loanApplications.toString());
    }
}
