package model;

import java.util.Date;

// Thẻ bạn đọc, chứa số thẻ, thời gian hiệu lực, trạng thái và chủ thẻ.
public class ReaderCard7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String cardNumber;
    private Date issuedAt;
    private Date expiredAt;
    private String status;
    private Reader7 reader;

    public ReaderCard7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    // Các getter/setter kiểu Date dùng bản sao để bên ngoài không sửa trực tiếp ngày đang lưu trong đối tượng.
    public Date getIssuedAt() {
        return issuedAt == null ? null : new Date(issuedAt.getTime());
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt == null ? null : new Date(issuedAt.getTime());
    }

    public Date getExpiredAt() {
        return expiredAt == null ? null : new Date(expiredAt.getTime());
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt == null ? null : new Date(expiredAt.getTime());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Reader7 getReader() {
        return reader;
    }

    public void setReader(Reader7 reader) {
        this.reader = reader;
    }
}
