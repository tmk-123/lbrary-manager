package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Document7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String publisher;
    private int publicationYear;
    private float coverPrice;
    private String description;
    private DocumentType7 documentType;
    private ArrayList<DocumentAuthor7> documentAuthors = new ArrayList<>();

    public Document7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public float getCoverPrice() {
        return coverPrice;
    }

    public void setCoverPrice(float coverPrice) {
        this.coverPrice = coverPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentType7 getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType7 documentType) {
        this.documentType = documentType;
    }

    public List<DocumentAuthor7> getDocumentAuthors() {
        return Collections.unmodifiableList(documentAuthors);
    }

    public void setDocumentAuthors(List<DocumentAuthor7> documentAuthors) {
        this.documentAuthors = documentAuthors == null ? new ArrayList<>() : new ArrayList<>(documentAuthors);
    }
    public void addDocumentAuthor(DocumentAuthor7 author) {
        if (author == null) throw new IllegalArgumentException("Tác giả không được null");
        documentAuthors.add(author);
    }
}
