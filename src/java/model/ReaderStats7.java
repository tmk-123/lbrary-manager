package model;

import java.util.Date;

// Kế thừa thông tin bạn đọc và bổ sung số lượt mượn trong một khoảng thống kê.
public class ReaderStats7 extends Reader7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Date periodStart;
    private Date periodEnd;
    private int borrowCount;

    public ReaderStats7() {
        super();
    }

    // Các getter/setter kiểu Date dùng bản sao để bên ngoài không sửa trực tiếp ngày đang lưu trong đối tượng.
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
