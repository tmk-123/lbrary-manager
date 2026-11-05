package model;

public class ReturnItem7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String conditionOnReturn;
    private float fine;
    private LoanItem7 loanItem;

    public ReturnItem7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConditionOnReturn() {
        return conditionOnReturn;
    }

    public void setConditionOnReturn(String conditionOnReturn) {
        this.conditionOnReturn = conditionOnReturn;
    }

    public float getFine() {
        return fine;
    }

    public void setFine(float fine) {
        this.fine = fine;
    }

    public LoanItem7 getLoanItem() {
        return loanItem;
    }

    public void setLoanItem(LoanItem7 loanItem) {
        this.loanItem = loanItem;
    }
}
