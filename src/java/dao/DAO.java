package dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Connection factory; a Connection belongs to one operation/transaction only. */
public class DAO {
    public DAO() { }

    // Mở kết nối riêng cho mỗi lần thao tác; bên gọi chịu trách nhiệm đóng bằng try-with-resources.
    protected Connection getConnection() throws SQLException {
        Properties config = new Properties();
        Path file = Path.of(System.getProperty("libman.config",
                Path.of(System.getProperty("user.home"), ".libman", "libman-db.properties").toString()));
        if (Files.exists(file)) {
            try (InputStream input = Files.newInputStream(file)) {
                config.load(input);
            } catch (IOException e) {
                throw new SQLException("Không đọc được cấu hình kết nối LibMan", e);
            }
        }
        // Đọc thông tin kết nối; mật khẩu phải được cung cấp qua cấu hình thay vì ghi trong mã nguồn.
        String url = setting(config, "url", "jdbc:mysql://localhost:3306/libman7?sslMode=PREFERRED&characterEncoding=UTF-8");
        String user = setting(config, "user", "root");
        String password = setting(config, "password", null);
        if (password == null) throw new SQLException("Chưa cấu hình db.password hoặc LIBMAN_DB_PASSWORD");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Thiếu thư viện MySQL Connector/J trong WEB-INF/lib", e);
        }
        return DriverManager.getConnection(url, user, password);
    }

    // Ưu tiên thuộc tính JVM, rồi biến môi trường, rồi tệp cấu hình; cuối cùng dùng giá trị mặc định.
    private String setting(Properties config, String key, String fallback) {
        String value = System.getProperty("libman.db." + key);
        if (value == null) value = System.getenv("LIBMAN_DB_" + key.toUpperCase(java.util.Locale.ROOT));
        return value == null ? config.getProperty("db." + key, fallback) : value;
    }
}
