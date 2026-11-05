-- CSDL LibMan (de 1) - dung theo muc 12 "Thiet ke co so du lieu" (19 bang, Hinh 12.1)
DROP DATABASE IF EXISTS libman7;
CREATE DATABASE libman7 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE libman7;

CREATE TABLE tblAddress7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  houseNumber VARCHAR(20) NULL,
  street VARCHAR(100) NULL,
  ward VARCHAR(100) NOT NULL,
  province VARCHAR(100) NOT NULL
);
CREATE TABLE tblMember7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  lastName VARCHAR(50) NOT NULL,
  middleName VARCHAR(50) NULL,
  firstName VARCHAR(50) NOT NULL,
  dateOfBirth DATE NOT NULL,
  phone VARCHAR(15) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  tblAddress7id INT NULL,
  FOREIGN KEY (tblAddress7id) REFERENCES tblAddress7(id)
);
CREATE TABLE tblReaderType7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL UNIQUE,
  maxBooks INT NOT NULL,
  loanDays INT NOT NULL,
  description VARCHAR(255) NULL
);
CREATE TABLE tblReader7 (
  tblMember7id INT PRIMARY KEY,
  tblReaderType7id INT NOT NULL,
  FOREIGN KEY (tblMember7id) REFERENCES tblMember7(id),
  FOREIGN KEY (tblReaderType7id) REFERENCES tblReaderType7(id)
);
CREATE TABLE tblStaff7 (
  tblMember7id INT PRIMARY KEY,
  position VARCHAR(50) NOT NULL,
  FOREIGN KEY (tblMember7id) REFERENCES tblMember7(id)
);
CREATE TABLE tblReaderCard7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  cardNumber VARCHAR(20) NOT NULL UNIQUE,
  issuedAt DATE NULL,
  expiredAt DATE NULL,
  status VARCHAR(20) NOT NULL,
  tblReader7id INT NOT NULL,
  FOREIGN KEY (tblReader7id) REFERENCES tblReader7(tblMember7id)
);
CREATE TABLE tblDocumentType7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL UNIQUE,
  description VARCHAR(255) NULL
);
CREATE TABLE tblDocument7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  publisher VARCHAR(150) NULL,
  publicationYear INT NULL,
  coverPrice DECIMAL(12,2) NULL,
  description TEXT NULL,
  tblDocumentType7id INT NOT NULL,
  FOREIGN KEY (tblDocumentType7id) REFERENCES tblDocumentType7(id)
);
CREATE TABLE tblAuthor7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(150) NOT NULL,
  description VARCHAR(255) NULL
);
CREATE TABLE tblDocumentAuthor7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  role VARCHAR(30) NOT NULL,
  tblDocument7id INT NOT NULL,
  tblAuthor7id INT NOT NULL,
  UNIQUE (tblDocument7id, tblAuthor7id),
  FOREIGN KEY (tblDocument7id) REFERENCES tblDocument7(id),
  FOREIGN KEY (tblAuthor7id) REFERENCES tblAuthor7(id)
);
CREATE TABLE tblShelf7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  room VARCHAR(100) NOT NULL,
  floor INT NOT NULL,
  description VARCHAR(255) NULL
);
CREATE TABLE tblDocumentCopy7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  barcode VARCHAR(30) NOT NULL UNIQUE,
  `condition` VARCHAR(30) NOT NULL,
  status VARCHAR(30) NOT NULL,
  tblDocument7id INT NOT NULL,
  tblShelf7id INT NULL,
  FOREIGN KEY (tblDocument7id) REFERENCES tblDocument7(id),
  FOREIGN KEY (tblShelf7id) REFERENCES tblShelf7(id)
);
CREATE TABLE tblLoan7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  borrowedAt DATETIME NOT NULL,
  note VARCHAR(255) NULL,
  tblReader7id INT NOT NULL,
  tblStaff7id INT NOT NULL,
  FOREIGN KEY (tblReader7id) REFERENCES tblReader7(tblMember7id),
  FOREIGN KEY (tblStaff7id) REFERENCES tblStaff7(tblMember7id)
);
CREATE TABLE tblLoanItem7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  dueDate DATE NOT NULL,
  tblLoan7id INT NOT NULL,
  tblDocumentCopy7id INT NOT NULL,
  UNIQUE (tblLoan7id, tblDocumentCopy7id),
  FOREIGN KEY (tblLoan7id) REFERENCES tblLoan7(id),
  FOREIGN KEY (tblDocumentCopy7id) REFERENCES tblDocumentCopy7(id)
);
CREATE TABLE tblReturnReceipt7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  returnedAt DATETIME NOT NULL,
  totalFine DECIMAL(12,2) NOT NULL,
  note VARCHAR(255) NULL,
  tblReader7id INT NOT NULL,
  tblStaff7id INT NOT NULL,
  FOREIGN KEY (tblReader7id) REFERENCES tblReader7(tblMember7id),
  FOREIGN KEY (tblStaff7id) REFERENCES tblStaff7(tblMember7id)
);
CREATE TABLE tblReturnItem7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  conditionOnReturn VARCHAR(30) NOT NULL,
  fine DECIMAL(12,2) NOT NULL,
  tblReturnReceipt7id INT NOT NULL,
  tblLoanItem7id INT NOT NULL UNIQUE,
  FOREIGN KEY (tblReturnReceipt7id) REFERENCES tblReturnReceipt7(id),
  FOREIGN KEY (tblLoanItem7id) REFERENCES tblLoanItem7(id)
);
CREATE TABLE tblSupplier7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(150) NOT NULL,
  phone VARCHAR(15) NOT NULL,
  email VARCHAR(100) NOT NULL,
  description VARCHAR(255) NULL,
  tblAddress7id INT NULL,
  FOREIGN KEY (tblAddress7id) REFERENCES tblAddress7(id)
);
CREATE TABLE tblImportReceipt7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  importedAt DATETIME NOT NULL,
  totalAmount DECIMAL(12,2) NOT NULL,
  note VARCHAR(255) NULL,
  tblSupplier7id INT NOT NULL,
  tblStaff7id INT NOT NULL,
  FOREIGN KEY (tblSupplier7id) REFERENCES tblSupplier7(id),
  FOREIGN KEY (tblStaff7id) REFERENCES tblStaff7(tblMember7id)
);
CREATE TABLE tblImportItem7 (
  id INT PRIMARY KEY AUTO_INCREMENT,
  quantity INT NOT NULL,
  unitPrice DECIMAL(12,2) NOT NULL,
  tblImportReceipt7id INT NOT NULL,
  tblDocument7id INT NOT NULL,
  UNIQUE (tblImportReceipt7id, tblDocument7id),
  FOREIGN KEY (tblImportReceipt7id) REFERENCES tblImportReceipt7(id),
  FOREIGN KEY (tblDocument7id) REFERENCES tblDocument7(id)
);

-- ===================== DU LIEU MAU (theo kich ban muc 6) =====================
INSERT INTO tblAddress7(id, houseNumber, street, ward, province) VALUES
 (2051, '96', 'Trần Phú', 'Phường Hà Đông', 'Hà Nội'),
 (2052, '12', 'Nguyễn Trãi', 'Phường Thanh Xuân', 'Hà Nội');
INSERT INTO tblMember7(id, username, password, lastName, middleName, firstName, dateOfBirth, phone, email, tblAddress7id) VALUES
 (1023, 'thuha05', '123456', 'Trần', 'Thu', 'Hà', '2005-06-21', '0987654321', 'ha.tt@stu.edu.vn', 2051),
 (1024, 'ducpm', '123456', 'Phạm', 'Minh', 'Đức', '1990-03-12', '0912000005', 'ducpm@lib.edu.vn', 2052),
 (1030, 'lananh', '123456', 'Nguyễn', 'Lan', 'Anh', '1985-11-02', '0912000030', 'anhnl@lib.edu.vn', NULL);
-- quy dinh muon cua Hoc vien
INSERT INTO tblReaderType7(id, name, maxBooks, loanDays, description) VALUES
 (1, 'Cán bộ, giảng viên', 7, 60, 'Cán bộ, giảng viên của Học viện'),
 (2, 'Sinh viên chính quy', 8, 150, 'Sinh viên cao đẳng, đại học hệ chính quy; mượn theo môn học của học kỳ'),
 (3, 'Sinh viên hệ khác, cao học, nghiên cứu sinh', 5, 150, 'Sinh viên các hệ đào tạo khác, học viên cao học, nghiên cứu sinh');
INSERT INTO tblReader7(tblMember7id, tblReaderType7id) VALUES (1023, 2);
INSERT INTO tblStaff7(tblMember7id, position) VALUES (1024, 'Nhân viên thư viện'), (1030, 'Quản lí');
INSERT INTO tblReaderCard7(id, cardNumber, issuedAt, expiredAt, status, tblReader7id) VALUES
 (457, 'TV23-00457', '2023-09-01', '2027-08-31', 'Đang hiệu lực', 1023);

INSERT INTO tblDocumentType7(id, name, description) VALUES
 (1, 'Sách', 'Sách tham khảo'), (2, 'Giáo trình', 'Tài liệu giảng dạy chính thức của học phần'), (3, 'Tạp chí', 'Tạp chí khoa học');
INSERT INTO tblDocument7(id, title, publisher, publicationYear, coverPrice, description, tblDocumentType7id) VALUES
 (123, 'Giáo trình Cơ sở dữ liệu', 'NXB Đại học Quốc gia Hà Nội', 2019, 85000, 'Mô hình quan hệ, đại số quan hệ, SQL, chuẩn hóa dữ liệu', 2),
 (124, 'Bài tập Cơ sở dữ liệu', 'NXB Đại học Quốc gia Hà Nội', 2020, 60000, 'Bài tập SQL và chuẩn hóa', 2),
 (310, 'Hệ quản trị cơ sở dữ liệu SQL Server', 'NXB Thống kê', 2018, 120000, 'Quản trị SQL Server', 1),
 (456, 'Cấu trúc dữ liệu và giải thuật', 'NXB Khoa học và Kỹ thuật', 2015, 70000, 'Danh sách, cây, đồ thị, sắp xếp, tìm kiếm', 2),
 (877, 'Lập trình hướng đối tượng với Java', 'NXB Bách khoa Hà Nội', 2021, 95000, 'Lớp, đối tượng, kế thừa, đa hình trong Java', 1);
INSERT INTO tblAuthor7(id, name, description) VALUES
 (5, 'Nguyễn Kim Anh', 'Giảng viên khoa Công nghệ thông tin'), (6, 'Đỗ Xuân Lôi', 'Giảng viên'), (7, 'Phạm Hữu Khang', 'Tác giả sách tin học'), (8, 'Trần Văn B', 'Giảng viên');
INSERT INTO tblDocumentAuthor7(id, role, tblDocument7id, tblAuthor7id) VALUES
 (101, 'Chủ biên', 123, 5), (103, 'Tác giả', 124, 5), (104, 'Tác giả', 310, 7), (105, 'Tác giả', 456, 6), (106, 'Tác giả', 877, 8);
INSERT INTO tblShelf7(id, code, room, floor, description) VALUES
 (12, 'B2', 'Phòng mượn giáo trình', 3, 'Giáo trình Công nghệ thông tin'),
 (13, 'C1', 'Phòng mượn sách tham khảo', 2, 'Sách tham khảo Công nghệ thông tin');
INSERT INTO tblDocumentCopy7(id, barcode, `condition`, status, tblDocument7id, tblShelf7id) VALUES
 (12301, 'DC0123-01', 'Tốt', 'Sẵn có', 123, 12),
 (12302, 'DC0123-02', 'Tốt', 'Đang mượn', 123, 12),
 (12303, 'DC0123-03', 'Tốt', 'Sẵn có', 123, 12),
 (12304, 'DC0123-04', 'Cũ', 'Sẵn có', 123, 12),
 (12305, 'DC0123-05', 'Tốt', 'Đang mượn', 123, 12),
 (12401, 'DC0124-01', 'Tốt', 'Sẵn có', 124, 12),
 (31001, 'DC0310-01', 'Tốt', 'Đang mượn', 310, 13),
 (45601, 'DC0456-01', 'Tốt', 'Sẵn có', 456, 12),
 (45602, 'DC0456-02', 'Tốt', 'Sẵn có', 456, 12),
 (87702, 'DC0877-02', 'Tốt', 'Đang mượn', 877, 13);
-- ban doc dang muon 1 cuon (hien o trang xac dinh ban doc)
INSERT INTO tblLoan7(id, borrowedAt, note, tblReader7id, tblStaff7id) VALUES (14, '2026-10-02 09:15:00', NULL, 1023, 1024);
INSERT INTO tblLoanItem7(id, dueDate, tblLoan7id, tblDocumentCopy7id) VALUES (290, '2027-03-01', 14, 87702); -- 02/10/2026 + 150 ngay
-- ban doc thu hai dang muon 3 ban (de trang chi tiet hien "sẵn có 3/5")
INSERT INTO tblMember7(id, username, password, lastName, middleName, firstName, dateOfBirth, phone, email, tblAddress7id) VALUES
 (1025, 'namlv', '123456', 'Lê', 'Văn', 'Nam', '2004-01-15', '0988111222', 'nam.lv@stu.edu.vn', NULL);
INSERT INTO tblReader7(tblMember7id, tblReaderType7id) VALUES (1025, 3);
INSERT INTO tblReaderCard7(id, cardNumber, issuedAt, expiredAt, status, tblReader7id) VALUES
 (458, 'TV22-00458', '2022-09-01', '2026-08-31', 'Hết hạn', 1025);
INSERT INTO tblLoan7(id, borrowedAt, note, tblReader7id, tblStaff7id) VALUES (13, '2026-04-20 14:00:00', NULL, 1025, 1024);
INSERT INTO tblLoanItem7(id, dueDate, tblLoan7id, tblDocumentCopy7id) VALUES
 (287, '2026-09-17', 13, 12302), (288, '2026-09-17', 13, 12305), (289, '2026-09-17', 13, 31001); -- 20/04/2026 + 150 ngay, da qua han
