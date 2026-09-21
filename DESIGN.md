# Phân tích và cách làm

## 1. Cách chuyển yêu cầu thành class

Đọc đề và tách ba nhóm: dữ liệu chung, dữ liệu riêng, thao tác quản lý nhiều đối tượng. Dữ liệu chung đưa vào lớp cha; các hành vi thay đổi theo loại đặt ở lớp con; thêm/tìm/xóa/thống kê đặt trong lớp quản lý.

Không phải mọi quan hệ đều dùng kế thừa: `Car` **là một** `Vehicle`, còn `Vehicle` **có một** `Owner`. Vì vậy Car dùng `extends Vehicle`, còn Vehicle có field `Owner owner`.

Các file đặt tên tiếng Anh. Mỗi bài độc lập, không dùng package để người mới có thể biên dịch với `javac src/*.java`. Khi ghép nhiều bài vào một ứng dụng lớn mới cần tổ chức lại package và build tool.

## 2. Bài hình học

Thứ tự đọc: `Shape.java` → `Rectangle.java` → `Circle.java` → `Main.java`.

`private final` giữ kích thước không bị thay đổi tùy tiện. Constructor cha kiểm tra số hữu hạn và > 0. `super(...)` khởi tạo phần trạng thái cha trong cùng đối tượng, không tạo một đối tượng cha độc lập. Hình tròn chuyển bán kính sang đường kính trước khi gọi cha.

Hai lớp con có `getArea()` riêng; đây không phải override vì Shape không khai báo phương thức đó. `toString()` mới là ví dụ override và đa hình: khi in một Shape tham chiếu tới Circle, Java gọi `Circle.toString()`.

`Shape` không abstract vì đề cần `new Shape(...)`. Không thêm công thức diện tích chung cho Shape vì chỉ có width/height chưa xác định được loại hình.

## 3. Bài kho siêu thị

Thứ tự triển khai: `Product` → ba lớp hàng → `Inventory` → `SelfCheck` → menu `Main`.

`Product` là abstract vì không cần tạo “hàng chung chung”, và chưa biết VAT/đánh giá khi chưa xác định loại. Ba lớp con triển khai `getVatRate()` và `evaluateConsumption(today)`.

Luồng thêm hàng:

```text
Chọn loại → đọc dữ liệu → constructor kiểm tra → Inventory.add kiểm tra mã trùng
         → tăng mảng nếu đầy → lưu vào products[size] → tăng size
```

Chỉ thêm sau khi mọi validation thành công. Nếu lỗi ngày, số hoặc trùng mã, kho không bị thay đổi một phần. `size` là số phần tử đã dùng, không phải độ dài mảng. `getProducts()` trả mảng sao chép để code bên ngoài không thể gán trực tiếp vào vùng lưu trữ của kho.

Ví dụ đa hình:

```java
for (Product product : inventory.getProducts()) {
    System.out.println(product.evaluateConsumption(today));
}
```

Biến có kiểu Product nhưng JVM chọn phương thức của Food, Electronics hoặc Crockery tùy đối tượng thực tế. `Main` không cần tự viết lại các điều kiện tiêu thụ.

VAT chung tính một lần ở cha, còn tỷ lệ được lấy qua phương thức lớp con. Tiền dùng `new BigDecimal("0.05")`, không dùng `new BigDecimal(0.05)` để tránh đưa sai số biểu diễn `double` vào giá trị tiền.

Truyền `today` vào phương thức đánh giá giúp kiểm tra được “hôm qua/hôm nay/10 ngày” bằng ngày cố định; menu lấy `LocalDate.now()` một lần cho mỗi báo cáo.

## 4. Bài quản lý phương tiện

Thứ tự triển khai: `Owner`, `Vehicle` → ba lớp xe → `VehicleRegistry` → `SelfCheck` → menu `Main`.

`Owner` dùng Java record vì đây là nhóm dữ liệu bất biến. Record tự sinh constructor theo các thành phần, accessor như `identityNumber()`, cùng `equals`, `hashCode`, `toString`. Compact constructor thêm kiểm tra định danh, tên và email.

`VehicleRegistry` dùng `ArrayList<Vehicle>` vì bài 3 không bắt buộc mảng. Tìm chủ và xe bằng duyệt danh sách là đủ cho bài console nhỏ. Không thêm một Map lưu chủ riêng vì chưa có yêu cầu quản lý chủ độc lập với xe.

Một chủ có thể xuất hiện trên nhiều xe. “Chủ có số định danh duy nhất” nghĩa là cùng số định danh không được mô tả hai người khác nhau; không có nghĩa mỗi chủ chỉ được có một xe. Menu tìm chủ trước khi tạo xe, và `add()` kiểm tra thông tin nhất quán một lần nữa.

Xóa theo hãng dùng `removeIf`, tránh vừa lặp bằng for-each vừa xóa gây `ConcurrentModificationException`. Sắp xếp trả một danh sách mới theo Comparator đảo chiều; dữ liệu trong danh sách quản lý không bị đổi thứ tự. Thống kê khởi tạo cả loại/hãng có 0 xe và trả tất cả hãng đồng hạng cao nhất. Danh sách rỗng trả không có hãng thắng.

## 5. Kiểm tra gì và vì sao?

| Bài | Kiểm tra chính |
| --- | --- |
| Shape | Diện tích/chu vi hình chữ nhật, diện tích/chu vi hình tròn với sai số nhỏ |
| Inventory | VAT 5%/10%, tổng tồn, hết hạn hôm qua so với hôm nay, hết hàng, ngưỡng 2/3 và 50/51, lưu kho 10/11 ngày |
| Inventory | Trùng mã khác hoa/thường, ngày sai, số âm, NaN, mảng tăng dung lượng, mảng trả về không sửa được kho |
| Vehicles | Trùng mã, chủ có nhiều xe, cùng định danh khác thông tin, tìm không thấy, mã/định danh sai độ dài, email sai, hãng ngoài danh sách |
| Vehicles | Năm 2000 bị từ chối, 2001 và năm hiện tại hợp lệ, năm tương lai bị từ chối, thứ tự mã, đồng hạng, xóa toàn bộ theo hãng, thống kê rỗng |

Validation dữ liệu dùng `throw IllegalArgumentException`, luôn có hiệu lực. `assert` chỉ dùng cho kiểm tra và cần `-ea`; không dùng assert thay cho validation đầu vào.

Các phép kiểm tra này tập trung vào quy tắc đề bài, không phải chứng minh mọi khả năng nhập liệu. Email chỉ kiểm tra mẫu cơ bản; dữ liệu không lưu bền; không có đồng thời hoặc giao diện web vì đề không yêu cầu.

## 6. Tài liệu Java tham khảo

- [JDK 21: LocalDate](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/LocalDate.html): ngày không kèm giờ, so sánh bằng `isBefore`, tính mốc bằng `minusDays`.
- [JDK 21: BigDecimal](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/math/BigDecimal.html): số thập phân, phép nhân/cộng, lưu ý constructor nhận double.
- [Java 21: Record classes](https://docs.oracle.com/en/java/javase/21/language/records.html): dữ liệu bất biến, accessor tự sinh và compact constructor.

Nên học theo thứ tự: chạy ví dụ → đọc model → đổi một dữ liệu → đoán kết quả → chạy check → đọc lớp quản lý → đọc menu. Sau đó tự thêm một loại hàng hoặc một loại xe để thấy phần nào dùng lại được qua kế thừa/đa hình.
