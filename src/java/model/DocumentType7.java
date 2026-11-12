package model;

// Loại tài liệu dùng để phân loại các đầu tài liệu trong thư viện.
public class DocumentType7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String description;

    public DocumentType7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
