package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

// Phiếu trả tài liệu, gồm bạn đọc, nhân viên nhận trả và các dòng trả.
public class ReturnReceipt7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Date returnedAt;
    private float totalFine;
    private String note;
    private Reader7 reader;
    private Librarian7 librarian;
    private ArrayList<ReturnItem7> returnItems = new ArrayList<>();

    public ReturnReceipt7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Các getter/setter kiểu Date dùng bản sao để bên ngoài không sửa trực tiếp ngày đang lưu trong đối tượng.
    public Date getReturnedAt() {
        return returnedAt == null ? null : new Date(returnedAt.getTime());
    }

    public void setReturnedAt(Date returnedAt) {
        this.returnedAt = returnedAt == null ? null : new Date(returnedAt.getTime());
    }

    public float getTotalFine() {
        return totalFine;
    }

    public void setTotalFine(float totalFine) {
        this.totalFine = totalFine;
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

    // Trả danh sách chỉ đọc; setter bên dưới sao chép danh sách đầu vào để giữ tính đóng gói.
    public List<ReturnItem7> getReturnItems() {
        return Collections.unmodifiableList(returnItems);
    }

    public void setReturnItems(List<ReturnItem7> returnItems) {
        this.returnItems = returnItems == null ? new ArrayList<>() : new ArrayList<>(returnItems);
    }
}
