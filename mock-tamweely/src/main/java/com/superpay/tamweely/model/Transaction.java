package com.superpay.tamweely.model;

import java.math.BigDecimal;

public class Transaction {

    private String senderName;
    private String msg;
    private String idno;
    private int fundingNumber;
    private BigDecimal amount;
    private String requestId;
    private String requestDate;
    private String timestamp;
    private String result;

    public Transaction() {
    }

    public Transaction(String senderName, String msg, String idno, int fundingNumber, BigDecimal amount,
                       String requestId, String requestDate, String timestamp, String result) {
        this.senderName = senderName;
        this.msg = msg;
        this.idno = idno;
        this.fundingNumber = fundingNumber;
        this.amount = amount;
        this.requestId = requestId;
        this.requestDate = requestDate;
        this.timestamp = timestamp;
        this.result = result;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public int getFundingNumber() {
        return fundingNumber;
    }

    public void setFundingNumber(int fundingNumber) {
        this.fundingNumber = fundingNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
