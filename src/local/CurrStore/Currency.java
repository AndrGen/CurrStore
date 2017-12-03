package local.CurrStore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Currency {

    private List<String> paysHistory = new ArrayList<>();

    private BigDecimal bill;

    public void addPaysHistory(String pay) {
        this.paysHistory.add(pay);
    }

    public BigDecimal getBill() {
        return bill;
    }

    public void addBill(BigDecimal bill) {
        this.bill = this.bill.add(bill);
    }

    public Currency(String pay, BigDecimal bill) {
        this.paysHistory.add(pay);
        this.bill = bill;
    }
}
