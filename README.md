# Ứng dụng Quản lý Cửa hàng Trà Sữa

Đây là một ứng dụng được phát triển bằng Java nhằm quản lý hoạt động của cửa hàng trà sữa. Ứng dụng cung cấp các chức năng như xử lý đơn hàng, quản lý kho, lập hóa đơn và báo cáo hoạt động kinh doanh.

## Tính năng

- **Quản lý đơn hàng:** Tạo và quản lý đơn hàng của khách hàng một cách dễ dàng.
- **Quản lý nguyên liệu:** Theo dõi nguyên liệu và số lượng tồn kho chính xác.
- **Lập hóa đơn:** Tạo và in hóa đơn cho khách hàng.
- **Quản lý nhà cung cấp:** Quản lý thông tin nhà cung cấp và phiếu nhập hàng.
- **Quản lý nhân viên:** Lưu trữ hồ sơ nhân viên và phân quyền truy cập.
- **Báo cáo:** Tạo các báo cáo về doanh số và tồn kho để phân tích.

## Công nghệ sử dụng

- **Java:** Ngôn ngữ lập trình chính cho ứng dụng.
- **JavaFX:** Xây dựng giao diện đồ họa cho người dùng.
- **Lombok:** Giảm thiểu mã lặp lại bằng cách sử dụng các chú thích.
- **MySQL:** Hệ quản trị cơ sở dữ liệu để lưu trữ dữ liệu.
- **iText PDF:** Để tạo tài liệu PDF như hóa đơn.
- **IntelliJ IDEA:** Môi trường phát triển tích hợp (IDE) được khuyến nghị.

## Hướng dẫn cài đặt

### Yêu cầu trước khi cài đặt

- **Java Development Kit (JDK) 8 hoặc cao hơn**
- **Máy chủ Cơ sở Dữ liệu MySQL**
- **IntelliJ IDEA**
- **Plugin Lombok cho IntelliJ IDEA**

### Các bước cài đặt

1. **Sao chép kho lưu trữ:**

   ```bash
   git clone https://github.com/magiauy/Milktea.git
   ```

2. **Mở dự án trong IntelliJ IDEA:**

   - Mở IntelliJ IDEA.
   - Chọn **File > Open** và điều hướng đến thư mục dự án vừa sao chép.

3. **Cài đặt plugin Lombok trong IntelliJ IDEA:**

   - Vào **File > Settings** (hoặc **IntelliJ IDEA > Preferences** trên macOS).
   - Chọn **Plugins** từ thanh bên.
   - Nhấp vào **Marketplace** và tìm kiếm **Lombok**.
   - Nhấp **Install** để thêm plugin Lombok.
   - Khởi động lại IntelliJ IDEA để áp dụng thay đổi.

4. **Kích hoạt Annotation Processing:**

   - Vào **File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors**.
   - Đánh dấu vào ô **Enable annotation processing**.
   - Nhấp **Apply** và sau đó **OK**.

5. **Thiết lập cơ sở dữ liệu:**

   - Cài đặt MySQL và khởi động máy chủ MySQL.
   - Tạo một cơ sở dữ liệu mới cho ứng dụng.
   - Nhập tệp SQL cung cấp sẵn để tạo bảng và dữ liệu mẫu.
   - Cập nhật cài đặt kết nối cơ sở dữ liệu trong tệp cấu hình của ứng dụng (thường là `connect.json` hoặc `config.yml`).

6. **Xây dựng và chạy ứng dụng:**

   - Trong IntelliJ IDEA, chọn **Build > Build Project** để biên dịch ứng dụng.
   - Chạy lớp chính (main class) để khởi động ứng dụng.

## Sử dụng ứng dụng

- **Đăng nhập:** Sử dụng thông tin đăng nhập được cung cấp để truy cập hệ thống.
- **Di chuyển trong ứng dụng:** Sử dụng menu để truy cập các chức năng khác nhau.
- **Xử lý đơn hàng:** Tạo đơn hàng mới và xử lý các đơn hàng hiện có.
- **Quản lý nguyên liệu:** Thêm, cập nhật hoặc xóa nguyên liệu và theo dõi số lượng tồn kho.
- **Lập hóa đơn:** Tạo hóa đơn và in phiếu trực tiếp.
- **Báo cáo:** Truy cập các báo cáo về doanh số và tồn kho để có cái nhìn sâu sắc.

## Giải quyết sự cố

- **Vấn đề với Lombok:** Nếu bạn gặp lỗi liên quan đến các chú thích của Lombok:
  - Đảm bảo rằng plugin Lombok đã được cài đặt.
  - Kiểm tra rằng bạn đã kích hoạt Annotation Processing trong cài đặt của IntelliJ IDEA.

- **Lỗi kết nối cơ sở dữ liệu:**
  - Kiểm tra rằng máy chủ MySQL đang chạy.
  - Xác nhận rằng thông tin đăng nhập và chi tiết kết nối cơ sở dữ liệu là chính xác.

- **Lỗi font khi xuất PDF:**
  - Đảm bảo rằng file font `arial-unicode-ms.ttf` nằm trong thư mục `fonts` của dự án.
  - Kiểm tra đường dẫn đến font trong mã nguồn.
  - Xác định rằng `FontProvider` đã được cấu hình đúng để sử dụng font tùy chỉnh.
  - Nếu vẫn gặp vấn đề, thử sử dụng font khác như **Noto Sans** để hỗ trợ tiếng Việt.

## Giấy phép

Dự án này được cấp phép theo Giấy phép MIT.

---

Nếu bạn cần thêm thông tin cụ thể hoặc hỗ trợ, vui lòng liên hệ với chúng tôi.