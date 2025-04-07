package Controller;

import DAO.UserDAO;
import DAO.LoanDAO;
import Model.Loan;
import Model.LoanApplications;
import Model.User;
import Util.UserUtil;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static Util.LoanUtil.extractLoanApplication;

public class UserController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getPathInfo();

        switch (action) {
            case "/editProfile":
                editProfile(request, response);
                break;
            case "/submitLoanRequest":
                submitLoanRequest(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getPathInfo();

        switch (action) {
            case "/viewPlans":
                viewPlans(response);
                break;
            case "/viewPayables":
                viewPayables(request, response);
                break;
            case "/getallloansbyuser":
                getallloanbyuser(request,response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    private void getallloanbyuser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            int userId = (int) request.getSession().getAttribute("userId");
            List<LoanApplications> loans = LoanDAO.getLoansByUser(userId);

            StringBuilder json = new StringBuilder();
            json.append("[");

            for (int i = 0; i < loans.size(); i++) {
                LoanApplications loan = loans.get(i);
                    json.append("{")
                        .append("\"loanAppId\":").append(loan.getLoanAppId()).append(",")
                        .append("\"user_id\":").append(loan.getUserid()).append(",")
                        .append("\"loan_id\":").append(loan.getLoanid()).append(",")
                        .append("\"interest_rate\":").append(loan.getInterest()).append(",")
                        .append("\"start_date\":\"").append(loan.getDate()).append("\",")
                        .append("\"loan_Period\":").append(loan.getPeriodinmonths()).append(",")
                        .append("\"loan_amount\":").append(loan.getAmount()).append(",")
                        .append("\"EMI_amount\":").append(loan.getEMIamount()).append(",")
                        .append("\"total_Interest\":").append(loan.getTotalinterest()).append(",")
                        .append("\"remaining_Interest\":").append(loan.getRemaininginterest()).append(",")
                        .append("\"remaining_principle\":").append(loan.getRemainingprinicipal()).append(",")
                        .append("\"status\":\"").append(loan.getStatus()).append("\"")
                        .append("}");

                if (i < loans.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");
            out.print(json.toString());
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to fetch loans.");
        }
    }

    private void editProfile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            User user = UserUtil.extractUserProfileUpdate(request);
            boolean updated = UserDAO.editUser(user, UserDAO.getaddressid(user.getUserId()));

            if (updated) {
                out.println("{\"message\": \"Profile updated successfully\"}");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Update failed");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format: " + e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong: " + e.getMessage());
        }
    }

    private void viewPlans(HttpServletResponse response) throws IOException {
        List<Loan> loans = LoanDAO.getAllLoanPlans();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println(loans.toString());
    }

    private void viewPayables(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int userId = (int) request.getSession().getAttribute("userId");
        List<Map<String, Object>> payables = LoanDAO.getUserMonthlyPayable(userId);

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");

        for (int i = 0; i < payables.size(); i++) {
            Map<String, Object> payable = payables.get(i);
            jsonBuilder.append("{");
            jsonBuilder.append("\"loanAppId\":").append(payable.get("loanAppId")).append(",");
            jsonBuilder.append("\"EMI_amount\":").append(payable.get("EMI_amount")).append(",");
            jsonBuilder.append("\"status\":\"").append(payable.get("status")).append("\"");
            jsonBuilder.append("}");

            if (i != payables.size() - 1) {
                jsonBuilder.append(",");
            }
        }

        jsonBuilder.append("]");

        response.setContentType("application/json");
        response.getWriter().write(jsonBuilder.toString());
    }



    private void submitLoanRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        LoanApplications loanApplications = extractLoanApplication(request, userId);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (LoanDAO.applyForLoan(loanApplications)) {
            out.println("{\"message\": \"Loan request submitted\"}");
            out.println(loanApplications.toString());
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to submit loan request");
        }

    }
}
