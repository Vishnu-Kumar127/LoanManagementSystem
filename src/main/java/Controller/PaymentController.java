package Controller;

import DAO.LoanDAO;
import DAO.PaymentDAO;
import Model.Loanrepayement;
import Model.Payment;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

public class PaymentController extends HttpServlet {


    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        response.setContentType("application/json");
        switch (action) {
            case "/getpaymentbyloan":
                getPaymentsByLoan(request, response);
                break;
            case "/gettotalpaidamount":
                getTotalPaidAmount(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown GET action: " + action);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        response.setContentType("application/json");

        switch (action) {
            case "/makePayment":
                makePayment(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown POST action: " + action);
                break;
        }
    }

    private void makePayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        try {
            int loanpaymentid = Integer.parseInt(request.getParameter("loanpaymentid"));
            double amount = Double.parseDouble(request.getParameter("amount"));
            Date date = new Date();

            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setDate(date);
            payment.setLoanpaymentid(loanpaymentid);

            String resultJson = PaymentDAO.addPayment(payment);
            out.write(resultJson);

        } catch (NumberFormatException e) {
            out.write("{\"success\": false, \"message\": \"Invalid loan payment ID or amount.\"}");
        }
    }

    private void getPaymentsByLoan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            int loanAppId = Integer.parseInt(request.getParameter("loanAppId"));
            List<Payment> payments = PaymentDAO.getPaymentsByLoan(loanAppId);

            StringBuilder json = new StringBuilder();
            json.append("[");

            for (int i = 0; i < payments.size(); i++) {
                Payment p = payments.get(i);
                json.append("{")
                        .append("\"payment_id\":").append(p.getPaymentid()).append(",")
                        .append("\"amount\":").append(p.getAmount()).append(",")
                        .append("\"payment_date\":\"").append(p.getDate()).append("\",")
                        .append("\"loan_payment_id\":").append(p.getLoanpaymentid())
                        .append("}");

                if (i < payments.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");
            out.print(json.toString());

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve payments.");
        }
    }

    private void getTotalPaidAmount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            int loanAppId = Integer.parseInt(request.getParameter("loanAppId"));

            List<Loanrepayement> repayments = LoanDAO.getRepaymentsByLoanAppId(loanAppId);
            double totalPaid = PaymentDAO.getTotalPaidAmount(loanAppId);

            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"totalPaid\": ").append(totalPaid).append(",");
            json.append("\"repayments\": [");

            for (int i = 0; i < repayments.size(); i++) {
                Loanrepayement r = repayments.get(i);
                json.append("{")
                        .append("\"loan_payment_id\": ").append(r.getLoanpaymentid()).append(",")
                        .append("\"loanAppId\": ").append(r.getLoanappid()).append(",")
                        .append("\"amount\": ").append(r.getEMIAmount()).append(",")
                        .append("\"due_date\": \"").append(r.getDate()).append("\",")
                        .append("\"paid_status\": ").append(r.getStatus())
                        .append("}");

                if (i < repayments.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]}");

            out.write(json.toString());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"error\": \"Invalid input or server error\"}");
        }
    }

}
