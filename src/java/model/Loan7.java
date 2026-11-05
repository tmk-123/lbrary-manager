package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

public class Loan7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Date borrowedAt;
    private String note;
    private Reader7 reader;
    private Librarian7 librarian;
    private ArrayList<LoanItem7> loanItems = new ArrayList<>();

    public Loan7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getBorrowedAt() {
        return borrowedAt == null ? null : new Date(borrowedAt.getTime());
    }

    public void setBorrowedAt(Date borrowedAt) {
        this.borrowedAt = borrowedAt == null ? null : new Date(borrowedAt.getTime());
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Reader7 getReader() {
        return reader;
    }

    public void setReader(Reader7 reader) {
        this.reader = reader;
    }

    public Librarian7 getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian7 librarian) {
        this.librarian = librarian;
    }

    public List<LoanItem7> getLoanItems() {
        return Collections.unmodifiableList(loanItems);
    }

    public void setLoanItems(List<LoanItem7> loanItems) {
        ensureDraft();
        this.loanItems = loanItems == null ? new ArrayList<>() : new ArrayList<>(loanItems);
    }
    public boolean isSaved() { return id > 0; }

    public void addLoanItem(LoanItem7 item) {
        ensureDraft();
        if (item == null || item.getDocumentCopy() == null) throw new IllegalArgumentException("Dòng mượn không hợp lệ");
        for (LoanItem7 existing : loanItems)
            if (existing.getDocumentCopy().getId() == item.getDocumentCopy().getId())
                throw new IllegalArgumentException("Bản tài liệu đã có trong danh sách mượn");
        loanItems.add(item);
    }

    public boolean removeLoanItem(int idCopy) {
        ensureDraft();
        return loanItems.removeIf(item -> item.getDocumentCopy().getId() == idCopy);
    }

    private void ensureDraft() {
        if (isSaved()) throw new IllegalStateException("Phiếu đã lưu không được thay đổi danh sách mượn");
    }
}
