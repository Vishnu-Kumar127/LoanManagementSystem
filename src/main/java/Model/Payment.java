package Model;

import java.util.Date;

public class Payment {
    private int paymentid;
    private double amount;
    private Date date;
    private int loanpaymentid;

    public Payment(int paymentid, double amount, Date date, int loanpaymentid) {
        this.paymentid = paymentid;
        this.amount = amount;
        this.date = date;
        this.loanpaymentid = loanpaymentid;
    }

    public Payment() {

    }

    public int getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(int paymentid) {
        this.paymentid = paymentid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLoanpaymentid() {
        return loanpaymentid;
    }

    public void setLoanpaymentid(int loanpaymentid) {
        this.loanpaymentid = loanpaymentid;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentid=" + paymentid +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                ", loanpaymentid=" + loanpaymentid +
                '}';
    }
}
