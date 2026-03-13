package com.example.btnhom;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btnhom.adapter.RoomAdapter;
import com.example.btnhom.model.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView rvRooms;
    private FloatingActionButton btnAddRoom;
    private List<Room> roomList;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Xử lý insets để tránh status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        initializeRoomList();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initializeViews() {
        rvRooms = findViewById(R.id.rv_rooms);
        btnAddRoom = findViewById(R.id.btn_add_room);
    }

    private void initializeRoomList() {
        roomList = new ArrayList<>();

        // Thêm dữ liệu mẫu
        roomList.add(new Room("101", "Phòng 101", 5000000, "Còn trống", "", ""));
        roomList.add(new Room("102", "Phòng 102", 5000000, "Đã thuê", "Nguyễn Văn A", "0987654321"));
        roomList.add(new Room("103", "Phòng 103", 5000000, "Còn trống", "", ""));
        roomList.add(new Room("201", "Phòng 201", 6000000, "Đã thuê", "Trần Thị B", "0912345678"));
    }

    private void setupRecyclerView() {
        roomAdapter = new RoomAdapter(this, roomList);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        rvRooms.setAdapter(roomAdapter);

        // Click để sửa
        roomAdapter.setOnItemClickListener((room, position) -> {
            showEditDialog(room, position);
        });

        // Long click để xóa
        roomAdapter.setOnItemLongClickListener((room, position) -> {
            showDeleteConfirmDialog(position);
        });
    }

    private void setupClickListeners() {
        btnAddRoom.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm Phòng Mới");

        // Tạo view từ layout dialog_room.xml
        android.view.View view = getLayoutInflater().inflate(R.layout.dialog_room, null);
        builder.setView(view);

        EditText etRoomCode = view.findViewById(R.id.et_room_code);
        EditText etRoomName = view.findViewById(R.id.et_room_name);
        EditText etRentalPrice = view.findViewById(R.id.et_rental_price);
        Spinner spinnerStatus = view.findViewById(R.id.spinner_status);
        EditText etTenantName = view.findViewById(R.id.et_tenant_name);
        EditText etPhoneNumber = view.findViewById(R.id.et_phone_number);
        android.view.View layoutTenantInfo = view.findViewById(R.id.layout_tenant_info);

        setupStatusSpinner(spinnerStatus);

        // Xử lý ẩn/hiển thị layout thông tin người thuê
        spinnerStatus.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
                if (position == 1) { // Đã thuê
                    layoutTenantInfo.setVisibility(android.view.View.VISIBLE);
                } else { // Còn trống
                    layoutTenantInfo.setVisibility(android.view.View.GONE);
                    etTenantName.setText("");
                    etPhoneNumber.setText("");
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        // Đặt nút placeholder để tạo dialog (sẽ override sau)
        builder.setPositiveButton("Thêm", null);
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Override nút "Thêm" để dialog KHÔNG tự đóng khi validation fail
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(v -> {
            if (validateInput(etRoomCode, etRoomName, etRentalPrice, spinnerStatus, etTenantName, etPhoneNumber, -1)) {
                try {
                    String roomCode = etRoomCode.getText().toString().trim();
                    String roomName = etRoomName.getText().toString().trim();
                    double rentalPrice = Double.parseDouble(etRentalPrice.getText().toString().trim());
                    String status = spinnerStatus.getSelectedItem().toString();
                    String tenantName = etTenantName.getText().toString().trim();
                    String phoneNumber = etPhoneNumber.getText().toString().trim();

                    Room newRoom = new Room(roomCode, roomName, rentalPrice, status, tenantName, phoneNumber);
                    roomList.add(newRoom);
                    roomAdapter.notifyItemInserted(roomList.size() - 1);
                    rvRooms.scrollToPosition(roomList.size() - 1);

                    Log.d(TAG, "Thêm phòng thành công: " + newRoom.toString());
                    Toast.makeText(this, "Thêm phòng thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e(TAG, "Lỗi khi thêm phòng: " + e.getMessage(), e);
                    Toast.makeText(this, "Lỗi khi thêm phòng: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "Lỗi thêm phòng: Validation thất bại - dữ liệu nhập không hợp lệ");
            }
        });
    }

    private void showEditDialog(Room room, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa Thông Tin Phòng");

        android.view.View view = getLayoutInflater().inflate(R.layout.dialog_room, null);
        builder.setView(view);

        EditText etRoomCode = view.findViewById(R.id.et_room_code);
        EditText etRoomName = view.findViewById(R.id.et_room_name);
        EditText etRentalPrice = view.findViewById(R.id.et_rental_price);
        Spinner spinnerStatus = view.findViewById(R.id.spinner_status);
        EditText etTenantName = view.findViewById(R.id.et_tenant_name);
        EditText etPhoneNumber = view.findViewById(R.id.et_phone_number);
        android.view.View layoutTenantInfo = view.findViewById(R.id.layout_tenant_info);

        // Điền dữ liệu hiện tại
        etRoomCode.setText(room.getRoomCode());
        etRoomName.setText(room.getRoomName());
        etRentalPrice.setText(String.valueOf((int)room.getRentalPrice()));
        etTenantName.setText(room.getTenantName());
        etPhoneNumber.setText(room.getPhoneNumber());

        setupStatusSpinner(spinnerStatus);
        int statusPosition = room.getStatus().equals("Còn trống") ? 0 : 1;
        spinnerStatus.setSelection(statusPosition);

        // Hiển thị/ẩn layout thông tin người thuê
        if (statusPosition == 1) {
            layoutTenantInfo.setVisibility(android.view.View.VISIBLE);
        } else {
            layoutTenantInfo.setVisibility(android.view.View.GONE);
        }

        // Xử lý ẩn/hiển thị layout thông tin người thuê khi thay đổi status
        spinnerStatus.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View v, int pos, long id) {
                if (pos == 1) { // Đã thuê
                    layoutTenantInfo.setVisibility(android.view.View.VISIBLE);
                } else { // Còn trống
                    layoutTenantInfo.setVisibility(android.view.View.GONE);
                    etTenantName.setText("");
                    etPhoneNumber.setText("");
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        // Đặt nút placeholder để tạo dialog (sẽ override sau)
        builder.setPositiveButton("Cập nhật", null);
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Override nút "Cập nhật" để dialog KHÔNG tự đóng khi validation fail
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(v -> {
            if (validateInput(etRoomCode, etRoomName, etRentalPrice, spinnerStatus, etTenantName, etPhoneNumber, position)) {
                try {
                    room.setRoomCode(etRoomCode.getText().toString().trim());
                    room.setRoomName(etRoomName.getText().toString().trim());
                    room.setRentalPrice(Double.parseDouble(etRentalPrice.getText().toString().trim()));
                    room.setStatus(spinnerStatus.getSelectedItem().toString());
                    room.setTenantName(etTenantName.getText().toString().trim());
                    room.setPhoneNumber(etPhoneNumber.getText().toString().trim());

                    roomAdapter.notifyItemChanged(position);

                    Log.d(TAG, "Sửa phòng thành công tại vị trí " + position + ": " + room.toString());
                    Toast.makeText(this, "Cập nhật phòng thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e(TAG, "Lỗi khi sửa phòng tại vị trí " + position + ": " + e.getMessage(), e);
                    Toast.makeText(this, "Lỗi khi cập nhật phòng: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "Lỗi sửa phòng tại vị trí " + position + ": Validation thất bại - dữ liệu nhập không hợp lệ");
            }
        });
    }

    private void showDeleteConfirmDialog(int position) {
        Room room = roomList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác Nhận Xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa phòng " + room.getRoomName() + "?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            roomList.remove(position);
            roomAdapter.notifyItemRemoved(position);
            Toast.makeText(MainActivity.this, "Xóa phòng thành công!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void setupStatusSpinner(Spinner spinner) {
        String[] statuses = {"Còn trống", "Đã thuê"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Validate tất cả fields trong dialog thêm/sửa phòng.
     * @param excludePosition vị trí phòng đang sửa (để bỏ qua khi kiểm tra mã trùng), truyền -1 khi thêm mới.
     * @return true nếu tất cả fields hợp lệ.
     */
    private boolean validateInput(EditText etRoomCode, EditText etRoomName, EditText etRentalPrice,
                                  Spinner spinnerStatus, EditText etTenantName, EditText etPhoneNumber,
                                  int excludePosition) {
        boolean isValid = true;

        // --- Validate Mã phòng ---
        String roomCodeStr = etRoomCode.getText().toString().trim();
        if (roomCodeStr.isEmpty()) {
            setFieldError(etRoomCode, "Vui lòng nhập mã phòng");
            isValid = false;
        } else if (isRoomCodeDuplicate(roomCodeStr, excludePosition)) {
            setFieldError(etRoomCode, "Mã phòng \"" + roomCodeStr + "\" đã tồn tại");
            isValid = false;
        } else {
            clearFieldError(etRoomCode);
        }

        // --- Validate Tên phòng ---
        String roomNameStr = etRoomName.getText().toString().trim();
        if (roomNameStr.isEmpty()) {
            setFieldError(etRoomName, "Vui lòng nhập tên phòng");
            isValid = false;
        } else {
            clearFieldError(etRoomName);
        }

        // --- Validate Giá thuê ---
        String priceStr = etRentalPrice.getText().toString().trim();
        if (priceStr.isEmpty()) {
            setFieldError(etRentalPrice, "Vui lòng nhập giá thuê");
            isValid = false;
        } else {
            try {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    setFieldError(etRentalPrice, "Giá thuê phải lớn hơn 0");
                    isValid = false;
                } else {
                    clearFieldError(etRentalPrice);
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Lỗi parse giá thuê: \"" + priceStr + "\" - " + e.getMessage());
                setFieldError(etRentalPrice, "Giá thuê không hợp lệ");
                isValid = false;
            }
        }

        // --- Validate thông tin người thuê (chỉ khi status = "Đã thuê") ---
        String status = spinnerStatus.getSelectedItem().toString();
        if (status.equals("Đã thuê")) {
            // Validate Tên người thuê
            String tenantNameStr = etTenantName.getText().toString().trim();
            if (tenantNameStr.isEmpty()) {
                setFieldError(etTenantName, "Vui lòng nhập tên người thuê");
                isValid = false;
            } else {
                clearFieldError(etTenantName);
            }

            // Validate Số điện thoại
            String phoneStr = etPhoneNumber.getText().toString().trim();
            if (phoneStr.isEmpty()) {
                setFieldError(etPhoneNumber, "Vui lòng nhập số điện thoại");
                isValid = false;
            } else if (!phoneStr.matches("\\d{10}")) {
                setFieldError(etPhoneNumber, "Số điện thoại phải có 10 chữ số");
                isValid = false;
            } else {
                clearFieldError(etPhoneNumber);
            }
        } else {
            // Reset trạng thái lỗi nếu chuyển về "Còn trống"
            clearFieldError(etTenantName);
            clearFieldError(etPhoneNumber);
        }

        // Focus vào field lỗi đầu tiên
        if (!isValid) {
            EditText[] fields = {etRoomCode, etRoomName, etRentalPrice, etTenantName, etPhoneNumber};
            for (EditText field : fields) {
                if (field.getError() != null) {
                    field.requestFocus();
                    break;
                }
            }
        }

        return isValid;
    }

    /**
     * Kiểm tra mã phòng có bị trùng hay không.
     * @param roomCode mã phòng cần kiểm tra
     * @param excludePosition vị trí bỏ qua (-1 nếu thêm mới)
     */
    private boolean isRoomCodeDuplicate(String roomCode, int excludePosition) {
        for (int i = 0; i < roomList.size(); i++) {
            if (i == excludePosition) continue;
            if (roomList.get(i).getRoomCode().equalsIgnoreCase(roomCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Đặt trạng thái lỗi cho EditText: hiển thị error message và viền đỏ.
     */
    private void setFieldError(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
        editText.setBackgroundResource(R.drawable.bg_edittext_error);
    }

    /**
     * Xóa trạng thái lỗi cho EditText: xóa error message và đặt lại viền bình thường.
     */
    private void clearFieldError(EditText editText) {
        editText.setError(null);
        editText.setBackgroundResource(R.drawable.bg_edittext);
    }
}