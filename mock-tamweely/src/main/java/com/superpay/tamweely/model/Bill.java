package com.superpay.tamweely.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bill {

    private Status status;
    private String CLIENTNAME;
    private String NATIONALID;
    private LocalDateTime DUEDATE;
    private LocalDateTime NEXTMONTHLYDUEDATE;
    private BigDecimal CUSTOMERDUEAMOUNT;
    private BigDecimal TOTALOUTSTANDING;
    private BigDecimal PAIDAMOUNT;
    private int NUMBEROFREMAININGINSTALLMENTS;
    private BigDecimal NEXTMONTHLYDUEINSTALLMENT;
    private int PRIMIUMNO;
    private BigDecimal PENALITIES;

    @JsonIgnore
    private int fundingNumber;
    @JsonIgnore
    private BigDecimal monthlyInstallment;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCLIENTNAME() {
        return CLIENTNAME;
    }

    public void setCLIENTNAME(String CLIENTNAME) {
        this.CLIENTNAME = CLIENTNAME;
    }

    public String getNATIONALID() {
        return NATIONALID;
    }

    public void setNATIONALID(String NATIONALID) {
        this.NATIONALID = NATIONALID;
    }

    public LocalDateTime getDUEDATE() {
        return DUEDATE;
    }

    public void setDUEDATE(LocalDateTime DUEDATE) {
        this.DUEDATE = DUEDATE;
    }

    public LocalDateTime getNEXTMONTHLYDUEDATE() {
        return NEXTMONTHLYDUEDATE;
    }

    public void setNEXTMONTHLYDUEDATE(LocalDateTime NEXTMONTHLYDUEDATE) {
        this.NEXTMONTHLYDUEDATE = NEXTMONTHLYDUEDATE;
    }

    public BigDecimal getCUSTOMERDUEAMOUNT() {
        return CUSTOMERDUEAMOUNT;
    }

    public void setCUSTOMERDUEAMOUNT(BigDecimal CUSTOMERDUEAMOUNT) {
        this.CUSTOMERDUEAMOUNT = CUSTOMERDUEAMOUNT;
    }

    public BigDecimal getTOTALOUTSTANDING() {
        return TOTALOUTSTANDING;
    }

    public void setTOTALOUTSTANDING(BigDecimal TOTALOUTSTANDING) {
        this.TOTALOUTSTANDING = TOTALOUTSTANDING;
    }

    public BigDecimal getPAIDAMOUNT() {
        return PAIDAMOUNT;
    }

    public void setPAIDAMOUNT(BigDecimal PAIDAMOUNT) {
        this.PAIDAMOUNT = PAIDAMOUNT;
    }

    public int getNUMBEROFREMAININGINSTALLMENTS() {
        return NUMBEROFREMAININGINSTALLMENTS;
    }

    public void setNUMBEROFREMAININGINSTALLMENTS(int NUMBEROFREMAININGINSTALLMENTS) {
        this.NUMBEROFREMAININGINSTALLMENTS = NUMBEROFREMAININGINSTALLMENTS;
    }

    public BigDecimal getNEXTMONTHLYDUEINSTALLMENT() {
        return NEXTMONTHLYDUEINSTALLMENT;
    }

    public void setNEXTMONTHLYDUEINSTALLMENT(BigDecimal NEXTMONTHLYDUEINSTALLMENT) {
        this.NEXTMONTHLYDUEINSTALLMENT = NEXTMONTHLYDUEINSTALLMENT;
    }

    public int getPRIMIUMNO() {
        return PRIMIUMNO;
    }

    public void setPRIMIUMNO(int PRIMIUMNO) {
        this.PRIMIUMNO = PRIMIUMNO;
    }

    public BigDecimal getPENALITIES() {
        return PENALITIES;
    }

    public void setPENALITIES(BigDecimal PENALITIES) {
        this.PENALITIES = PENALITIES;
    }

    public int getFundingNumber() {
        return fundingNumber;
    }

    public void setFundingNumber(int fundingNumber) {
        this.fundingNumber = fundingNumber;
    }

    public BigDecimal getMonthlyInstallment() {
        return monthlyInstallment;
    }

    public void setMonthlyInstallment(BigDecimal monthlyInstallment) {
        this.monthlyInstallment = monthlyInstallment;
    }

    public static class Status {
        private int code;
        private String message;

        public Status() {
        }

        public Status(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
