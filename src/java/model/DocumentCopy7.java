package model;

public class DocumentCopy7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String barcode;
    private String condition;
    private String status;
    private Document7 document;
    private Shelf7 shelf;

    public DocumentCopy7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Document7 getDocument() {
        return document;
    }

    public void setDocument(Document7 document) {
        this.document = document;
    }

    public Shelf7 getShelf() {
        return shelf;
    }

    public void setShelf(Shelf7 shelf) {
        this.shelf = shelf;
    }
}
