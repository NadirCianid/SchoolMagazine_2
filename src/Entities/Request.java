package Entities;

public class Request {
    private int id;
    private String studentName;
    private String studentInstitute;
    private String studentSpecialty;
    private String confDocLink;
    private String faCategory;
    private String requestStatus;
    private String fillingDate;
    private String responseDate;
    private String adminComment;
    private String paymentAmount;
    private User admin;

    public String getStudentName() {
        return studentName;
    }

    public String getStudentInstitute() {
        return studentInstitute;
    }

    public String getStudentSpecialty() {
        return studentSpecialty;
    }

    public String getConfDocLink() {
        return confDocLink;
    }

    public String getFaCategory() {
        return faCategory;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public String getFillingDate() {
        return fillingDate;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public String getAdminComment() {
        return adminComment;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public User getAdmin() {
        return admin;
    }

    public int getId() {
        return id;
    }

    public Request(int id, String confDocLink, String faCategory, String fillingDate) {
        this.id = id;
        this.confDocLink = confDocLink;
        this.faCategory = faCategory;
        this.fillingDate = fillingDate;
    }

    public Request(String faCategory, String adminComment, String requestStatus, String fillingDate, String responseDate, String paymentAmount) {
        this.faCategory = faCategory;
        this.requestStatus = requestStatus;
        this.fillingDate = fillingDate;
        this.responseDate = responseDate;
        this.adminComment = adminComment;
        this.paymentAmount = paymentAmount;
    }

    public void setStudentParams(String studentName, String studentInstitute, String studentSpecialty) {
        this.studentName = studentName;
        this.studentInstitute = studentInstitute;
        this.studentSpecialty = studentSpecialty;
    }
}
