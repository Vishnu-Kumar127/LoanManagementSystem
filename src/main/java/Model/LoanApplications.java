package Model;

import java.util.Date;

public class LoanApplications {
    private int LoanAppId;
    private int userid;
    private int loanid;
    private double interest;
    private Date Date;
    private int periodinmonths;
    private double amount;
    private double EMIamount;
    private double totalinterest;
    private double remaininginterest;

    public LoanApplications() {

    }

    public LoanApplications(int userid, int loanid, double interest, double amount, Date date, int periodinmonths, double EMIamount, double totalinterest, double remaininginterest, double remainingprinicipal, String status) {
        this.userid = userid;
        this.loanid = loanid;
        this.interest = interest;
        Date = date;
        this.periodinmonths = periodinmonths;
        this.amount = amount;
        this.EMIamount = EMIamount;
        this.totalinterest = totalinterest;
        this.remaininginterest = remaininginterest;
        this.remainingprinicipal = remainingprinicipal;
        this.status = status;
    }

    @Override
    public String toString() {
        return "LoanApplications{" +
                "LoanAppId=" + LoanAppId +
                ", userid=" + userid +
                ", loanid=" + loanid +
                ", interest=" + interest +
                ", Date='" + Date + '\'' +
                ", periodinmonths=" + periodinmonths +
                ", amount=" + amount +
                ", EMIamount=" + EMIamount +
                ", totalinterest=" + totalinterest +
                ", remaininginterest=" + remaininginterest +
                ", remainingprinicipal=" + remainingprinicipal +
                ", status='" + status + '\'' +
                '}';
    }

    public LoanApplications(int loanAppId, int userid, int loanid, double interest, Date date, int periodinmonths, double amount, double EMIamount, double totalinterest, double remaininginterest, double remainingprinicipal, String status) {
        LoanAppId = loanAppId;
        this.userid = userid;
        this.loanid = loanid;
        this.interest = interest;
        Date = date;
        this.periodinmonths = periodinmonths;
        this.amount = amount;
        this.EMIamount = EMIamount;
        this.totalinterest = totalinterest;
        this.remaininginterest = remaininginterest;
        this.remainingprinicipal = remainingprinicipal;
        this.status = status;
    }

    public int getLoanAppId() {
        return LoanAppId;
    }

    public void setLoanAppId(int loanAppId) {
        LoanAppId = loanAppId;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getLoanid() {
        return loanid;
    }

    public void setLoanid(int loanid) {
        this.loanid = loanid;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public int getPeriodinmonths() {
        return periodinmonths;
    }

    public void setPeriodinmonths(int periodinmonths) {
        this.periodinmonths = periodinmonths;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getEMIamount() {
        return EMIamount;
    }

    public void setEMIamount(double EMIamount) {
        this.EMIamount = EMIamount;
    }

    public double getTotalinterest() {
        return totalinterest;
    }

    public void setTotalinterest(double totalinterest) {
        this.totalinterest = totalinterest;
    }

    public double getRemaininginterest() {
        return remaininginterest;
    }

    public void setRemaininginterest(double remaininginterest) {
        this.remaininginterest = remaininginterest;
    }

    public double getRemainingprinicipal() {
        return remainingprinicipal;
    }

    public void setRemainingprinicipal(double remainingprinicipal) {
        this.remainingprinicipal = remainingprinicipal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private double remainingprinicipal;
    private String status;
}
