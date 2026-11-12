package util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import model.LoanItem7;
import model.ReaderCard7;
import model.ReaderType7;

/** The same borrowing rules are used during identification, selection and save. */
public final class BorrowingRules7 {
    public static final ZoneId ZONE = ZoneId.of("Asia/Bangkok");
    private BorrowingRules7() { }
    // Dùng cùng múi giờ cho ngày hiện tại và các phép kiểm tra hạn trả.
    public static LocalDate today() { return LocalDate.now(ZONE); }
    public static LocalDate date(Date value) {
        return Instant.ofEpochMilli(value.getTime()).atZone(ZONE).toLocalDate();
    }
    // Hạn trả là ngày mượn cộng số ngày được phép theo loại bạn đọc.
    public static Date dueDate(Date borrowedAt, int loanDays) {
        return Date.from(date(borrowedAt).plusDays(loanDays).atStartOfDay(ZONE).toInstant());
    }
    /** null means eligible; a non-null value is a user-facing validation error. */
    // Trả null nếu được mượn; kiểm tra lần lượt thẻ, quy định, quá hạn và tổng số bản sau khi chọn.
    public static String validate(ReaderCard7 card, List<LoanItem7> current, int selected, LocalDate today) {
        if (card == null || card.getReader() == null || current == null) return "Chưa kiểm tra được điều kiện mượn";
        if (!"Đang hiệu lực".equals(card.getStatus())) return "Thẻ không ở trạng thái đang hiệu lực";
        if (card.getIssuedAt() != null && date(card.getIssuedAt()).isAfter(today)) return "Thẻ chưa đến ngày có hiệu lực";
        if (card.getExpiredAt() != null && date(card.getExpiredAt()).isBefore(today)) return "Thẻ bạn đọc đã hết hạn";
        ReaderType7 type = card.getReader().getReaderType();
        if (type == null || type.getMaxBooks() <= 0 || type.getLoanDays() <= 0) return "Quy định mượn chưa hợp lệ";
        for (LoanItem7 item : current) {
            if (item.getDueDate() == null) return "Chưa xác định được hạn trả của tài liệu đang mượn";
            if (date(item.getDueDate()).isBefore(today)) return "Bạn đọc phải trả tài liệu quá hạn trước khi mượn thêm";
        }
        // selected là số bản của lượt mượn mới, chưa nằm trong danh sách current đang mượn.
        if (selected < 0 || current.size() + selected > type.getMaxBooks())
            return "Vượt số tài liệu tối đa: " + type.getMaxBooks() + " cuốn (đang mượn " + current.size() + ")";
        return null;
    }
}
