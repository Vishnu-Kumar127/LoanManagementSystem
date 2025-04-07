package Model;

import java.util.Date;

public class Loanrepayement {
    private int loanpaymentid;
    private int loanappid;
    private Date date;
    private String Status;
    private Double EMIAmount;
    private Double interestpaid;
    private Double prinicipalpaid;
    private Double penaltypaid;

    public Loanrepayement(int loanpaymentid, int loanappid, Date date, String status, Double EMIAmount, Double interestpaid, Double prinicipalpaid, Double penaltypaid) {
        this.loanpaymentid = loanpaymentid;
        this.loanappid = loanappid;
        this.date = date;
        Status = status;
        this.EMIAmount = EMIAmount;
        this.interestpaid = interestpaid;
        this.prinicipalpaid = prinicipalpaid;
        this.penaltypaid = penaltypaid;
    }

    public int getLoanpaymentid() {
        return loanpaymentid;
    }

    public void setLoanpaymentid(int loanpaymentid) {
        this.loanpaymentid = loanpaymentid;
    }

    public int getLoanappid() {
        return loanappid;
    }

    public void setLoanappid(int loanappid) {
        this.loanappid = loanappid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Double getEMIAmount() {
        return EMIAmount;
    }

    public void setEMIAmount(Double EMIAmount) {
        this.EMIAmount = EMIAmount;
    }

    public Double getInterestpaid() {
        return interestpaid;
    }

    public void setInterestpaid(Double interestpaid) {
        this.interestpaid = interestpaid;
    }

    public Double getPrinicipalpaid() {
        return prinicipalpaid;
    }

    public void setPrinicipalpaid(Double prinicipalpaid) {
        this.prinicipalpaid = prinicipalpaid;
    }

    public Double getPenaltypaid() {
        return penaltypaid;
    }

    public void setPenaltypaid(Double penaltypaid) {
        this.penaltypaid = penaltypaid;
    }

    @Override
    public String toString() {
        return "Loanrepayement{" +
                "loanpaymentid=" + loanpaymentid +
                ", loanappid=" + loanappid +
                ", date=" + date +
                ", Status='" + Status + '\'' +
                ", EMIAmount=" + EMIAmount +
                ", interestpaid=" + interestpaid +
                ", prinicipalpaid=" + prinicipalpaid +
                ", penaltypaid=" + penaltypaid +
                '}';
    }
}
