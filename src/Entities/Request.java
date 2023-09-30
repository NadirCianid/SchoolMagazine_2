package Entities;

public class Request {
    private User student;
    private String confDocLink;
    private String faCategory;
    private String applicationStatus;
    private String adminComment;
    private int paymentAmount;
    private User admin;

    public User getStudent() {
        return student;
    }

    public String getConfDocLink() {
        return confDocLink;
    }

    public String getFaCategory() {
        return faCategory;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public String getAdminComment() {
        return adminComment;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public User getAdmin() {
        return admin;
    }

    public Request(User student, String confDocLink, String faCategory, int paymentAmount) {
        this.student = student;
        this.confDocLink = confDocLink;
        this.faCategory = faCategory;
        this.paymentAmount = paymentAmount;
    }
}
