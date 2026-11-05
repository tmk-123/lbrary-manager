# LibMan Search Document 7 và Borrow Document 7

MVC: JSP hiển thị → Servlet điều phối → DAO truy cập CSDL; model đóng gói dữ liệu. Thiết kế toàn hệ thống có 25 lớp model và 19 bảng. Code hiện cài hai module cùng đăng nhập/đăng xuất hỗ trợ.

## Môi trường đã kiểm tra

- JDK 26.0.2.1, biên dịch --release 17.
- Apache Tomcat 11.0.26; jakarta.servlet.
- MySQL Server 8.0; Connector/J 9.6.0.
- Project NetBeans: thư mục hiện tại (demo), source/target Java 17. Chạy thử đã xác nhận trên JDK 26.

Chỉ chỉnh sửa mã Java trong src/java và giao diện JSP trong web. Đây là cây mã nguồn duy nhất của dự án. SQL tạo CSDL nằm trong sql; cấu hình mẫu nằm trong config. WAR: demo.war, context /demo.

## Cấu hình và chạy

1. Với CSDL mới, dùng sql/LibMan_DB.sql để tạo cấu trúc và dữ liệu mẫu.
2. Sao chép config/libman-db.properties.example vào thư mục user.home/.libman/libman-db.properties. Trên Windows thường là %USERPROFILE%/.libman/libman-db.properties. Điền db.url, db.user, db.password. Thông tin kết nối của máy hiện tại đã được chuyển vào file riêng này.
3. Chọn file khác bằng -Dlibman.config=<đường dẫn>. Các system property libman.db.url/user/password, rồi biến môi trường LIBMAN_DB_URL/USER/PASSWORD, được ưu tiên hơn nội dung file.
4. Đặt Connector/J trong WEB-INF/lib. Project NetBeans tham chiếu JAR trên máy và đóng gói thư viện đó vào WAR.
5. Build/run demo trên Tomcat; mở http://localhost:8080/demo/gdLogin7.jsp.

Mật khẩu MySQL nằm ngoài source/WAR. Mỗi thao tác mở/đóng Connection riêng; mọi câu lệnh lưu một phiếu dùng chung một Connection của transaction đó.

Tài khoản mẫu, mật khẩu 123456:

| Tài khoản | Vai trò | Dữ liệu thử |
|---|---|---|
| thuha05 | Bạn đọc Trần Thu Hà | Tìm cơ sở dữ liệu; xem TL0123 |
| ducpm | Nhân viên Phạm Minh Đức | Thẻ TV23-00457; DC0123-03, DC0456-01 |

## Đóng gói và luồng xử lý

- Thuộc tính model private. Date được sao chép khi đọc/ghi. Collection khởi tạo rỗng; setter sao chép đầu vào, getter trả List chỉ đọc.
- Loan7.addLoanItem()/removeLoanItem() kiểm soát thay đổi danh sách. Phiếu đã lưu chặn sửa danh sách.
- DAO trả model đã đóng gói, dùng try-with-resources, ném SQLException khi lỗi. Danh sách rỗng biểu thị truy vấn thành công không có dòng; không thay lỗi bằng danh sách rỗng.
- BorrowingRules7 thống nhất kiểm tra thẻ, quá hạn, giới hạn mượn và hạn trả. Thẻ còn hiệu lực hết ngày ghi trên thẻ.
- LoanDAO.addLoan(Loan7 loan, ReaderCard7 readerCard) khóa bạn đọc/thẻ, đọc lại điều kiện và ghi phiếu/dòng/trạng thái trong một transaction. Chỉ công bố ID/ngày/trạng thái vào model sau commit; rollback giữ nguyên phiếu nháp.
- loanToken trong session và các form xác định lượt mượn. readerCardId kiểm tra thẻ của form Tiếp tục. Từ chối form cũ khi lượt mượn thay đổi.
- POST thêm/bỏ/hủy/lưu/hoàn tất chuyển hướng sang GET hiển thị. Đăng xuất dùng POST. JSP trang chủ không tự xóa lượt mượn.
- View7.html() escape HTML; View7.list() đọc collection có kiểm tra kiểu.

## Servlet

| Servlet | GET | POST |
|---|---|---|
| /LoginServlet7 | Trang đăng nhập | Xác thực và tạo session |
| /LogoutServlet7 | Không hỗ trợ | Hủy session |
| /reader/SearchDocumentServlet7 | Tìm/hiển thị | Dùng cùng phần tìm kiếm |
| /reader/DocumentDetailServlet7 | Chi tiết theo idDocument | Không hỗ trợ |
| /librarian/IdentifyReaderServlet7 | Trang xác định | Tìm thẻ, kiểm tra điều kiện |
| /librarian/BorrowDocumentServlet7 | Phiếu nháp | new, add, remove, cancel, finish |
| /librarian/SaveLoanServlet7 | Không hỗ trợ | Ghi nhận phiếu với loanToken |

## Kiểm tra

Java/JSP biên dịch thành công. Đã chạy 30 kiểm tra HTTP/DAO trên Tomcat và schema MySQL tạm riêng: luồng chính, form cũ, gửi lại form, sửa phiếu đã lưu, lỗi dữ liệu, rollback, Date/collection defensive copy và hai giao dịch đồng thời giữ đúng giới hạn mượn. Không ghi dữ liệu thử vào CSDL libman7 đang dùng.

Báo cáo: ../word_nop/de1_toan_bo_chuan_hoa_code.docx. Sơ đồ/scenario thiết kế phản ánh source đã chuẩn hóa; các chức năng ngoài hai module vẫn thuộc phạm vi thiết kế.
