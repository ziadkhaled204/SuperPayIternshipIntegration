package com.superpay.tamweely.service;

import com.superpay.tamweely.data.MockDataStore;
import com.superpay.tamweely.model.Bill;
import com.superpay.tamweely.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class TamweelyMockService {

    private final MockDataStore dataStore;

    public TamweelyMockService(MockDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Bill inquiry(Map<String, Object> request) {
        String msg = str(request, "Msg");
        if (!isValidRequest(request, "Inquiry")) {
            return error(msg, "Invalid request");
        }
        Bill bill = dataStore.findBill(str(request, "IDNO"), intVal(request, "Num_Funding"));
        if (bill == null) {
            return error(msg, "Client not found");
        }
        record(request, "Inquiry", null, "successful");
        bill.setStatus(new Bill.Status(200, "successful"));
        return bill;
    }

    public Bill payment(Map<String, Object> request) {
        String msg = str(request, "Msg");
        if (!isValidRequest(request, "Payment")) {
            return error(msg, "Invalid request");
        }
        BigDecimal amount = decimal(request, "Amount");
        if (amount == null || amount.signum() <= 0) {
            return error(msg, "Invalid amount");
        }
        Bill bill = dataStore.findBill(str(request, "IDNO"), intVal(request, "Num_Funding"));
        if (bill == null) {
            return error(msg, "Client not found");
        }

        bill.setPAIDAMOUNT(bill.getPAIDAMOUNT().add(amount));
        bill.setTOTALOUTSTANDING(bill.getTOTALOUTSTANDING().subtract(amount).max(BigDecimal.ZERO));
        bill.setNUMBEROFREMAININGINSTALLMENTS(Math.max(0, bill.getNUMBEROFREMAININGINSTALLMENTS() - 1));
        bill.setDUEDATE(bill.getNEXTMONTHLYDUEDATE());
        bill.setNEXTMONTHLYDUEDATE(bill.getNEXTMONTHLYDUEDATE() == null
                ? null : bill.getNEXTMONTHLYDUEDATE().plusMonths(1));
        bill.setPRIMIUMNO(bill.getPRIMIUMNO() + 1);
        bill.setNEXTMONTHLYDUEINSTALLMENT(bill.getNUMBEROFREMAININGINSTALLMENTS() == 0
                ? BigDecimal.ZERO
                : bill.getTOTALOUTSTANDING().min(bill.getMonthlyInstallment()));

        dataStore.updateBill(bill);
        record(request, "Payment", amount, "successful");

        bill.setStatus(new Bill.Status(200, "successful"));
        return bill;
    }

    public Bill checkStatus(Map<String, Object> request) {
        String msg = str(request, "Msg");
        if (!isValidRequest(request, "CheckStatus")) {
            return error(msg, "Invalid request");
        }
        Bill bill = dataStore.findBill(str(request, "IDNO"), intVal(request, "Num_Funding"));
        if (bill == null) {
            return error(msg, "Client not found");
        }
        record(request, "CheckStatus", null, "successful");

        boolean paid = bill.getNUMBEROFREMAININGINSTALLMENTS() == 0
                && bill.getTOTALOUTSTANDING().signum() == 0;
        bill.setStatus(paid ? new Bill.Status(200, "successful") : new Bill.Status(201, "Not Paid"));
        return bill;
    }

    private boolean isValidRequest(Map<String, Object> request, String expectedMsg) {
        return str(request, "SenderName") != null
                && expectedMsg.equals(str(request, "Msg"))
                && str(request, "IDNO") != null
                && str(request, "RequestId") != null
                && str(request, "Signature") != null;
    }

    private Bill error(String msg, String message) {
        Bill bill = new Bill();
        bill.setStatus(new Bill.Status(400, message));
        return bill;
    }

    private void record(Map<String, Object> request, String msg, BigDecimal amount, String result) {
        Transaction transaction = new Transaction(
                str(request, "SenderName"),
                msg,
                str(request, "IDNO"),
                intVal(request, "Num_Funding"),
                amount,
                str(request, "RequestId"),
                str(request, "RequestDate"),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                result);
        dataStore.addTransaction(transaction);
    }

    private String str(Map<String, Object> request, String key) {
        Object value = request.get(key);
        return value == null ? null : value.toString();
    }

    private int intVal(Map<String, Object> request, String key) {
        Object value = request.get(key);
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString());
    }

    private BigDecimal decimal(Map<String, Object> request, String key) {
        Object value = request.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return new BigDecimal(value.toString());
    }
}
