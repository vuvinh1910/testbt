# Ứng Dụng Quản Lý Nhà Trọ - Hướng Dẫn Cấu Trúc MVC

## 📁 Cấu Trúc Dự Án

### Model (Mô hình)
- **Room.java** - Lớp đại diện cho phòng trọ
  - Thuộc tính: roomCode, roomName, rentalPrice, status, tenantName, phoneNumber
  - Getter/Setter cho tất cả thuộc tính

### View (Giao Diện)
- **activity_main.xml** - Màn hình chính
  - Header với tiêu đề và nút "Thêm"
  - RecyclerView để hiển thị danh sách phòng

- **item_room.xml** - Layout cho mỗi item trong RecyclerView
  - Hiển thị: Tên phòng, Giá thuê, Tình trạng
  - Badge màu: Xanh (Còn trống), Đỏ (Đã thuê)

- **dialog_room.xml** - Dialog để thêm/sửa phòng
  - EditText: Mã phòng, Tên phòng, Giá thuê, Tên người thuê, Số điện thoại
  - Spinner: Chọn tình trạng (Còn trống / Đã thuê)

### Controller (Điều Khiển)
- **MainActivity.java** - Activity chính, xử lý tất cả logic
  - initializeRoomList(): Khởi tạo danh sách phòng mẫu
  - setupRecyclerView(): Thiết lập adapter và listeners
  - showAddDialog(): Hiển thị dialog thêm phòng
  - showEditDialog(): Hiển thị dialog sửa phòng
  - showDeleteConfirmDialog(): Hiển thị xác nhận xóa
  - validateInput(): Kiểm tra dữ liệu nhập

### Adapter (Bộ Điều Hợp)
- **RoomAdapter.java** - RecyclerView Adapter
  - onCreateViewHolder(): Tạo ViewHolder
  - onBindViewHolder(): Ràng buộc dữ liệu vào UI
  - Xử lý click và long-click trên item

## 🔄 Quy Trình CRUD

### 1. CREATE (Thêm)
```
Bấm nút "+ Thêm" 
→ Hiển thị dialog
→ Nhập dữ liệu
→ Validate
→ Thêm vào List
→ Cập nhật RecyclerView
```

### 2. READ (Đọc)
```
Hiển thị danh sách phòng trong RecyclerView
→ Tô màu badge theo tình trạng
→ Xanh: Còn trống
→ Đỏ: Đã thuê
```

### 3. UPDATE (Sửa)
```
Click vào item 
→ Hiển thị dialog với dữ liệu hiện tại
→ Sửa thông tin
→ Bấm "Cập nhật"
→ Cập nhật object trong List
→ Refresh RecyclerView
```

### 4. DELETE (Xóa)
```
Nhấn giữ item
→ Hiển thị AlertDialog xác nhận
→ Bấm "Xóa"
→ Xóa khỏi List
→ Cập nhật RecyclerView
```

## 🎨 Thành Phần UI

### Màu Sắc
- **Xanh (#4CAF50)**: Phòng còn trống
- **Đỏ (#F44336)**: Phòng đã thuê
- **Xám (#757575)**: Background badge

### Sự Kiện
- **Click**: Sửa thông tin phòng
- **Long Click**: Xóa phòng
- **Button "Thêm"**: Mở dialog thêm phòng mới

## 📝 Dữ Liệu
- Lưu tạm thời bằng List<Room> trong memory
- Không dùng SQLite hay Room Database
- Dữ liệu sẽ mất khi đóng ứng dụng

## 🔍 Validate Dữ Liệu
- Mã phòng: Không để trống
- Tên phòng: Không để trống
- Giá thuê: Không để trống, phải là số hợp lệ
- Tên người thuê: Optional (có thể trống)
- Số điện thoại: Optional (có thể trống)

## 📦 Dependencies
- androidx.recyclerview:recyclerview
- androidx.material:material
- androidx.appcompat:appcompat
