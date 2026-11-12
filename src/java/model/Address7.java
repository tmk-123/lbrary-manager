package model;

// Địa chỉ của thành viên hoặc nhà cung cấp, được tách thành các thành phần để lưu và hiển thị.
public class Address7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String houseNumber;
    private String street;
    private String ward;
    private String province;

    public Address7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
