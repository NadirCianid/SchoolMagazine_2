package Entities;

import java.sql.SQLException;

public class FACategory {
    private String title;
    private String reqDoc;
    private String paymentAmount;

    public String getTitle() {
        return title;
    }

    public String getReqDocs() {
        return reqDoc;
    }

    public String getPaymentAmount() {
        if(paymentAmount.equals("")) {
            return "";
        }
        return "до " + paymentAmount + "р";
    }

    public FACategory(String title, String recDoc, String paymentAmount) throws SQLException {
        this.title = title;
        this.reqDoc = recDoc;
        this.paymentAmount = paymentAmount;
    }
}
