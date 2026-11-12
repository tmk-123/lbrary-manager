package model;

// Bạn đọc kế thừa thông tin thành viên và có loại bạn đọc quyết định hạn mức mượn.
public class Reader7 extends Member7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private ReaderType7 readerType;

    public Reader7() {
        super();
    }

    public ReaderType7 getReaderType() {
        return readerType;
    }

    public void setReaderType(ReaderType7 readerType) {
        this.readerType = readerType;
    }
}
