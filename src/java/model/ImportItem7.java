package model;

// Một dòng phiếu nhập: đầu tài liệu, số lượng nhập và đơn giá tại thời điểm nhập.
public class ImportItem7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int quantity;
    private float unitPrice;
    private Document7 document;

    public ImportItem7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Document7 getDocument() {
        return document;
    }

    public void setDocument(Document7 document) {
        this.document = document;
    }
}
