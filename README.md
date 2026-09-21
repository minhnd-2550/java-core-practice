# Java Core Practice

Ba bài thực hành Java Core, chạy độc lập bằng **JDK 21**, không cần Maven, Spring Boot hay database.

Nguồn yêu cầu: [S*Learn — Bài thực hành Java Core, attachment 9035](https://sun-asterisk.wsm.vn/learn/vi/learning/3824/content/4394/attachment/9035/), đọc ngày 2026-09-21. Tài liệu có 13 trang: mục tiêu ở trang 2, Practice 1 ở trang 3, Practice 2 ở trang 4–7, Practice 3 ở trang 8–10; trang 11–13 kết thúc/Q&A. Trang 8 và 9 lặp lại phần mô tả bài 3.

Project này tự chứa toàn bộ mã nguồn và tài liệu để có thể tách sang repo mới. Bài 1 được sao chép từ bài Shape đã có trong workspace; bài gốc vẫn giữ nguyên. Bài 2 và 3 có menu console. Dữ liệu chỉ nằm trong RAM và mất khi thoát chương trình.

## Chạy chương trình

Mở terminal tại thư mục chứa README này:

```sh
java -version
javac -version
bash run.sh shape
bash run.sh inventory
bash run.sh vehicles
```

Chọn một lệnh để chạy từng bài. Với menu console, nhập `0` để thoát. Nhập ngày theo `yyyy-MM-dd`, số thập phân dùng dấu chấm. Nếu nhập sai, chương trình báo lỗi và trở về menu; thao tác thêm chưa hoàn tất sẽ không được lưu.

Chạy toàn bộ kiểm tra:

```sh
bash run.sh check
```

Script biên dịch với `--release 21`, bật assertions bằng `-ea` và dừng ngay nếu lỗi. Kết quả gồm diện tích/chu vi bài 1, `Inventory checks passed.` và `Vehicle registry checks passed.`. Các kiểm tra ngày của bài kho dùng ngày cố định nên không phụ thuộc ngày chạy.

Không có Bash thì chạy trực tiếp từng bài, ví dụ:

```sh
cd 02-supermarket-inventory
mkdir -p out
javac --release 21 -encoding UTF-8 -d out src/*.java
java -cp out Main
java -ea -cp out Main --check
```

Mỗi bài có `Main` riêng, do đó biên dịch riêng từng thư mục, không gom toàn bộ file Java của ba bài vào một lần biên dịch. `out/` và `*.class` đã được bỏ qua trong Git.

## Cấu trúc

```text
java-core-practice/
├── README.md
├── DESIGN.md
├── .gitignore
├── run.sh
├── 01-shape-inheritance/src/
│   ├── Shape.java
│   ├── Rectangle.java
│   ├── Circle.java
│   └── Main.java
├── 02-supermarket-inventory/src/
│   ├── Product.java
│   ├── Food.java
│   ├── Electronics.java
│   ├── Crockery.java
│   ├── Inventory.java
│   ├── Main.java
│   └── SelfCheck.java
└── 03-vehicle-management/src/
    ├── Owner.java
    ├── Vehicle.java
    ├── Car.java
    ├── Motorcycle.java
    ├── Truck.java
    ├── VehicleRegistry.java
    ├── Main.java
    └── SelfCheck.java
```

## Practice 1 — Shape inheritance

`Shape` chứa `width`, `height`. `Rectangle` và `Circle` kế thừa trực tiếp `Shape`.

| Đối tượng mẫu | Kết quả |
| --- | --- |
| `Shape(3, 4)` | In kích thước |
| `Rectangle(5, 8)` | Diện tích 40, chu vi 26 |
| `Circle(3)` | Diện tích khoảng 28.27, chu vi khoảng 18.85 |

Giữ `Shape` là lớp thường vì đề yêu cầu khởi tạo cả ba lớp. Hình tròn dùng chiều rộng/chiều cao bằng đường kính, bán kính suy ra từ chiều rộng. Dùng `Math.PI` thay vì hằng số rút gọn `3.14` trong slide; nếu chấm đúng theo 3.14 thì kết quả chu vi bán kính 3 sẽ là 18.84 thay vì 18.85. Constructor từ chối kích thước không dương hoặc không hữu hạn; ví dụ chỉ dành cho kích thước thông thường, chưa xử lý tràn kết quả với số cực lớn.

## Practice 2 — Supermarket inventory

| Lớp | Thuộc tính riêng | VAT theo đề | Đánh giá tiêu thụ |
| --- | --- | --- | --- |
| `Food` | Ngày sản xuất, hạn dùng, nhà cung cấp | 5% | Còn hàng và đã hết hạn → `HARD_TO_SELL` |
| `Electronics` | Bảo hành theo tháng, công suất kW | 10% | Tồn kho < 3 → `SELLING_WELL` |
| `Crockery` | Nhà sản xuất, ngày nhập kho | 10% | Tồn kho > 50 và lưu kho > 10 ngày → `SLOW_SELLING` |

Mọi loại hàng có mã, tên, tồn kho và đơn giá. `Inventory` tương ứng lớp DSHH trong đề, lưu `Product[]` cùng biến `size`. Mảng tự tăng kích thước khi đầy; thêm hàng sẽ từ chối mã trùng, không phân biệt hoa/thường. Menu cho chọn loại hàng trước khi nhập thuộc tính.

Các quy ước triển khai:

- Tồn kho, đơn giá, bảo hành và công suất không âm; tên/mã/nhà cung cấp/nhà sản xuất không rỗng. Không chấp nhận `NaN` hoặc vô cực cho công suất.
- Hạn dùng được bằng ngày sản xuất. Hết hạn nghĩa là `expiresOn < today`; đúng ngày hết hạn chưa bị coi là quá hạn trong bài này.
- Tồn kho bằng 3, gốm sứ bằng 50 hoặc lưu đúng 10 ngày đều chưa thỏa điều kiện đánh giá tương ứng.
- Slide dùng từ “sold” ở hàng điện tử; chương trình đặt tên `SELLING_WELL` để biểu diễn tiêu thụ tốt theo ngưỡng tồn kho, không tự giảm tồn kho.
- Các trường hợp còn lại trả `NOT_EVALUATED`.
- Đơn giá được coi là giá chưa VAT. `unitVat = unitPrice × vatRate`; `inventoryVat = unitVat × quantity`. Báo cáo có cả VAT đơn vị và tổng VAT tồn kho, cộng riêng theo loại. Đây là tỷ lệ phục vụ bài tập, không phải tra cứu quy định thuế thực tế.
- Dùng `BigDecimal` từ chuỗi cho tiền; chưa làm tròn vì đề không quy định đơn vị tiền hay chính sách làm tròn. `LocalDate` biểu diễn ngày không kèm giờ.

Thử nhanh: thêm Electronics mã `E01`, tên `Fan`, số lượng `2`, đơn giá `100`, bảo hành `12`, công suất `0.05`. Chọn Report: VAT đơn vị `10.00`, tổng VAT `20.00`, trạng thái `SELLING_WELL`. Thêm mã `e01` lần nữa sẽ bị từ chối.

## Practice 3 — Vehicle management

`Vehicle` là cha của `Car`, `Motorcycle`, `Truck`. Mỗi xe giữ một `Owner`; một chủ có thể sở hữu nhiều xe.

| Menu | Chức năng |
| --- | --- |
| 1 | Thêm ô tô, xe máy hoặc xe tải |
| 2 | Tìm theo mã xe |
| 3 | Liệt kê xe theo số định danh chủ sở hữu |
| 4 | Xóa tất cả xe của hãng được nhập |
| 5 | Liệt kê hãng có nhiều xe nhất, kể cả đồng hạng, cùng số lượng từng hãng |
| 6 | Liệt kê xe theo mã giảm dần |
| 7 | Thống kê số lượng Car, Motorcycle, Truck, kể cả loại có 0 xe |

Validation theo đề: mã xe duy nhất, dài đúng 5 ký tự; hãng chỉ có Honda, Yamaha, Toyota, Suzuki; `2000 < year <= năm hiện tại`; số định danh đúng 12 chữ số; email có định dạng hợp lệ cơ bản. Dùng chuỗi cho số định danh để giữ số 0 ở đầu. Khi nhập số định danh đã tồn tại, menu dùng lại thông tin chủ sở hữu. API `add()` cũng từ chối cùng số định danh nhưng khác tên/email.

Quy ước bổ sung: bỏ khoảng trắng đầu/cuối khi nhập, mã xe không chứa khoảng trắng và không phân biệt hoa/thường; số ghế, dung tích xe máy và tải trọng phải dương, các số thực phải hữu hạn. Bài chưa yêu cầu lưu chủ không còn xe, nên thông tin chủ được tìm từ các xe hiện có. Email được kiểm tra ở mức bài tập, không xác minh hộp thư tồn tại.

**Điểm cần xác nhận khi nộp bài:** slide viết “Sort vehicles by number of vehicles in descending order”, chưa rõ muốn sắp theo mã xe hay nhóm theo số lượng. Bản này hiểu là **mã xe giảm dần theo thứ tự chuỗi**; menu 5 đã có thống kê số xe theo hãng. Thuộc tính “capacity” của xe máy được hiểu là dung tích xi-lanh (cc). Nếu giảng viên quy định nghĩa khác, cần điều chỉnh hai điểm này.

Thử với dữ liệu giả: Car `A0001`, Toyota, năm `2024`, White, chủ `000000000001`, Demo Owner, `demo@example.com`, 4 chỗ, Petrol. Thêm Motorcycle `B0001`, Honda, cùng chủ: không phải nhập lại tên/email. Menu 3 trả về cả hai xe.

## Repository

Repository riêng: [minhnd-2550/java-core-practice](https://github.com/minhnd-2550/java-core-practice).

Clone và kiểm tra trên máy có JDK 21:

```sh
git clone https://github.com/minhnd-2550/java-core-practice.git
cd java-core-practice
bash run.sh check
```

Trong workspace học tập, project này có Git riêng ngay tại `01-java-core/projects/java-core-practice`. Khi commit/push, mở terminal tại thư mục này; repo chỉ chứa ba bài Java Core, không chứa project Spring Boot.

Không đưa bản slide nội bộ vào repo; link nguồn nằm ở đầu README. Mã nguồn và hướng dẫn trong project đủ để chạy độc lập.

Đọc [DESIGN.md](DESIGN.md) để hiểu thứ tự triển khai, kế thừa, đa hình, validation và các kiểm tra.
