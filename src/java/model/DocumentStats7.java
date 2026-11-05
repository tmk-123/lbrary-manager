package model;

import java.util.Date;

public class DocumentStats7 extends Document7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Date periodStart;
    private Date periodEnd;
    private int borrowCount;

    public DocumentStats7() {
        super();
    }

    public Date getPeriodStart() {
        return periodStart == null ? null : new Date(periodStart.getTime());
    }

    public void setPeriodStart(Date periodStart) {
        this.periodStart = periodStart == null ? null : new Date(periodStart.getTime());
    }

    public Date getPeriodEnd() {
        return periodEnd == null ? null : new Date(periodEnd.getTime());
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd == null ? null : new Date(periodEnd.getTime());
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }
}
