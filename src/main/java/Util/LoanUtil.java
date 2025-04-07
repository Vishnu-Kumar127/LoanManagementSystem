package Util;

import Model.Loan;
import Model.LoanApplications;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class LoanUtil {
    public static LoanApplications extractLoanApplication(HttpServletRequest request, int userId) {
        int loanId = Integer.parseInt(request.getParameter("loan_id"));
        double amount = Double.parseDouble(request.getParameter("amount"));
        double interest = Double.parseDouble(request.getParameter("interest"));
        int period = Integer.parseInt(request.getParameter("period"));
        Date date = new Date();

        double monthlyInterestRate = interest / 12 / 100;
        double emi = (amount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, period)) /
                (Math.pow(1 + monthlyInterestRate, period) - 1);
        double totalInterest = (emi * period) - amount;
        double remainingInterest = totalInterest;
        double remainingPrincipal = amount;

        return new LoanApplications(userId, loanId, interest, amount, date, period, emi, totalInterest, remainingInterest, remainingPrincipal, "Pending");
    }
    public static Loan extractLoanPlan(HttpServletRequest request) throws NumberFormatException {
        Loan loan = new Loan();
        loan.setLoantype(request.getParameter("loan_type"));
        loan.setMaxperiod(Integer.parseInt(request.getParameter("max_period")));
        loan.setInterrate(Double.parseDouble(request.getParameter("interest_rate")));
        loan.setMaxamount(Double.parseDouble(request.getParameter("max_amount")));
        return loan;
    }
}
