package model;

// Liên kết một đầu tài liệu với một tác giả, kèm vai trò như chủ biên hoặc đồng tác giả.
public class DocumentAuthor7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String role;
    private Author7 author;

    public DocumentAuthor7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Author7 getAuthor() {
        return author;
    }

    public void setAuthor(Author7 author) {
        this.author = author;
    }
}
