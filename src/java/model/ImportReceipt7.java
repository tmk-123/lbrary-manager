package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

public class ImportReceipt7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Date importedAt;
    private float totalAmount;
    private String note;
    private Supplier7 supplier;
    private Librarian7 librarian;
    private ArrayList<ImportItem7> importItems = new ArrayList<>();

    public ImportReceipt7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getImportedAt() {
        return importedAt == null ? null : new Date(importedAt.getTime());
    }

    public void setImportedAt(Date importedAt) {
        this.importedAt = importedAt == null ? null : new Date(importedAt.getTime());
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Supplier7 getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier7 supplier) {
        this.supplier = supplier;
    }

    public Librarian7 getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian7 librarian) {
        this.librarian = librarian;
    }

    public List<ImportItem7> getImportItems() {
        return Collections.unmodifiableList(importItems);
    }

    public void setImportItems(List<ImportItem7> importItems) {
        this.importItems = importItems == null ? new ArrayList<>() : new ArrayList<>(importItems);
    }
}
