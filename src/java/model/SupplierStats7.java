package model;

import java.util.Date;

public class SupplierStats7 extends Supplier7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Date periodStart;
    private Date periodEnd;
    private int importQuantity;
    private float importAmount;

    public SupplierStats7() {
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

    public int getImportQuantity() {
        return importQuantity;
    }

    public void setImportQuantity(int importQuantity) {
        this.importQuantity = importQuantity;
    }

    public float getImportAmount() {
        return importAmount;
    }

    public void setImportAmount(float importAmount) {
        this.importAmount = importAmount;
    }
}
