package Model;

import java.util.function.DoubleToLongFunction;

public class Loan {
    private int loanid;
    private int maxperiod;
    private String loantype;
    private Double interrate;
    private Double maxamount;

    @Override
    public String toString() {
        return "Loan{" +
                "loanid=" + loanid +
                ", maxperiod=" + maxperiod +
                ", loantype='" + loantype + '\'' +
                ", interrate=" + interrate +
                ", maxamount=" + maxamount +
                '}';
    }

    public Loan(int loanid, int maxperiod, String loantype, Double interrate, Double maxamount) {
        this.loanid = loanid;
        this.maxperiod = maxperiod;
        this.loantype = loantype;
        this.interrate = interrate;
        this.maxamount = maxamount;
    }

    public Loan() {

    }

    public int getLoanid() {
        return loanid;
    }

    public void setLoanid(int loanid) {
        this.loanid = loanid;
    }

    public int getMaxperiod() {
        return maxperiod;
    }

    public void setMaxperiod(int maxperiod) {
        this.maxperiod = maxperiod;
    }

    public String getLoantype() {
        return loantype;
    }

    public void setLoantype(String loantype) {
        this.loantype = loantype;
    }

    public Double getInterrate() {
        return interrate;
    }

    public void setInterrate(Double interrate) {
        this.interrate = interrate;
    }

    public Double getMaxamount() {
        return maxamount;
    }

    public void setMaxamount(Double maxamount) {
        this.maxamount = maxamount;
    }
}
