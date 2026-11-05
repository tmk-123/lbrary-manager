package model;

import java.util.Date;

public class LoanItem7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Date dueDate;
    private DocumentCopy7 documentCopy;

    public LoanItem7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDueDate() {
        return dueDate == null ? null : new Date(dueDate.getTime());
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate == null ? null : new Date(dueDate.getTime());
    }

    public DocumentCopy7 getDocumentCopy() {
        return documentCopy;
    }

    public void setDocumentCopy(DocumentCopy7 documentCopy) {
        this.documentCopy = documentCopy;
    }
}
