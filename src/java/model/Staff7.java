package model;

// Lớp nhân viên kế thừa thông tin thành viên và bổ sung chức vụ.
public class Staff7 extends Member7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String position;

    public Staff7() {
        super();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
