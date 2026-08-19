package com.superpay.tamweely.data;

import com.superpay.tamweely.model.Bill;
import com.superpay.tamweely.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockDataStore {

    private final List<Bill> bills = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();

    public MockDataStore() {
        seed();
    }

    private void seed() {
        Bill b1 = new Bill();
        b1.setNATIONALID("29009252700311");
        b1.setFundingNumber(1);
        b1.setCLIENTNAME("نادر احمد محمد احمد");
        b1.setDUEDATE(null);
        b1.setNEXTMONTHLYDUEDATE(LocalDateTime.of(2020, 6, 25, 0, 0));
        b1.setCUSTOMERDUEAMOUNT(BigDecimal.ZERO);
        b1.setTOTALOUTSTANDING(new BigDecimal("2960"));
        b1.setPAIDAMOUNT(BigDecimal.ZERO);
        b1.setNUMBEROFREMAININGINSTALLMENTS(3);
        b1.setMonthlyInstallment(new BigDecimal("555"));
        b1.setNEXTMONTHLYDUEINSTALLMENT(new BigDecimal("555"));
        b1.setPRIMIUMNO(2);
        b1.setPENALITIES(BigDecimal.ZERO);
        bills.add(b1);

        Bill b2 = new Bill();
        b2.setNATIONALID("29009252700311");
        b2.setFundingNumber(2);
        b2.setCLIENTNAME("نادر احمد محمد احمد");
        b2.setDUEDATE(LocalDateTime.of(2020, 5, 25, 0, 0));
        b2.setNEXTMONTHLYDUEDATE(LocalDateTime.of(2020, 6, 25, 0, 0));
        b2.setCUSTOMERDUEAMOUNT(new BigDecimal("400"));
        b2.setTOTALOUTSTANDING(new BigDecimal("1200"));
        b2.setPAIDAMOUNT(new BigDecimal("2800"));
        b2.setNUMBEROFREMAININGINSTALLMENTS(2);
        b2.setMonthlyInstallment(new BigDecimal("600"));
        b2.setNEXTMONTHLYDUEINSTALLMENT(new BigDecimal("600"));
        b2.setPRIMIUMNO(4);
        b2.setPENALITIES(new BigDecimal("50"));
        bills.add(b2);

        Bill b3 = new Bill();
        b3.setNATIONALID("30002316458974");
        b3.setFundingNumber(1);
        b3.setCLIENTNAME("نادر احمد محمد احمد");
        b3.setDUEDATE(null);
        b3.setNEXTMONTHLYDUEDATE(LocalDateTime.of(2020, 6, 25, 0, 0));
        b3.setCUSTOMERDUEAMOUNT(BigDecimal.ZERO);
        b3.setTOTALOUTSTANDING(new BigDecimal("2960"));
        b3.setPAIDAMOUNT(BigDecimal.ZERO);
        b3.setNUMBEROFREMAININGINSTALLMENTS(3);
        b3.setMonthlyInstallment(new BigDecimal("555"));
        b3.setNEXTMONTHLYDUEINSTALLMENT(new BigDecimal("555"));
        b3.setPRIMIUMNO(2);
        b3.setPENALITIES(BigDecimal.ZERO);
        bills.add(b3);

        Bill b5 = new Bill();
        b5.setNATIONALID("50009876543210");
        b5.setFundingNumber(1);
        b5.setCLIENTNAME("محمد علي السيد");
        b5.setDUEDATE(LocalDateTime.of(2020, 4, 10, 0, 0));
        b5.setNEXTMONTHLYDUEDATE(LocalDateTime.of(2020, 5, 10, 0, 0));
        b5.setCUSTOMERDUEAMOUNT(BigDecimal.ZERO);
        b5.setTOTALOUTSTANDING(BigDecimal.ZERO);
        b5.setPAIDAMOUNT(new BigDecimal("2000"));
        b5.setNUMBEROFREMAININGINSTALLMENTS(0);
        b5.setMonthlyInstallment(new BigDecimal("500"));
        b5.setNEXTMONTHLYDUEINSTALLMENT(BigDecimal.ZERO);
        b5.setPRIMIUMNO(4);
        b5.setPENALITIES(BigDecimal.ZERO);
        bills.add(b5);

        Bill b4 = new Bill();
        b4.setNATIONALID("40001234567890");
        b4.setFundingNumber(1);
        b4.setCLIENTNAME("أحمد محمود عبد الله");
        b4.setDUEDATE(LocalDateTime.of(2020, 7, 1, 0, 0));
        b4.setNEXTMONTHLYDUEDATE(LocalDateTime.of(2020, 8, 1, 0, 0));
        b4.setCUSTOMERDUEAMOUNT(new BigDecimal("1000"));
        b4.setTOTALOUTSTANDING(new BigDecimal("5000"));
        b4.setPAIDAMOUNT(new BigDecimal("1500"));
        b4.setNUMBEROFREMAININGINSTALLMENTS(6);
        b4.setMonthlyInstallment(new BigDecimal("1000"));
        b4.setNEXTMONTHLYDUEINSTALLMENT(new BigDecimal("1000"));
        b4.setPRIMIUMNO(2);
        b4.setPENALITIES(new BigDecimal("75.5"));
        bills.add(b4);
    }

    public synchronized Bill findBill(String nationalId, int fundingNumber) {
        for (Bill bill : bills) {
            if (nationalId.equals(bill.getNATIONALID()) && fundingNumber == bill.getFundingNumber()) {
                return bill;
            }
        }
        return null;
    }

    public synchronized void updateBill(Bill bill) {
        for (int i = 0; i < bills.size(); i++) {
            Bill existing = bills.get(i);
            if (nationalIdMatches(bill, existing)) {
                bills.set(i, bill);
                return;
            }
        }
        bills.add(bill);
    }

    private boolean nationalIdMatches(Bill a, Bill b) {
        return a.getNATIONALID().equals(b.getNATIONALID())
                && a.getFundingNumber() == b.getFundingNumber();
    }

    public synchronized void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
