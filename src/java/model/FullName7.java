package model;

// Họ tên thành viên, tách riêng họ, tên đệm và tên.
public class FullName7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String lastName;
    private String middleName;
    private String firstName;

    public FullName7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
