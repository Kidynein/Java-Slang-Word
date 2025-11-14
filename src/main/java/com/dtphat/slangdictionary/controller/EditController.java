package com.dtphat.slangdictionary.controller;

import com.dtphat.slangdictionary.model.SlangDictionary;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditController {

    // Vùng Tìm kiếm
    @FXML private TextField findField;

    // Vùng Chỉnh sửa
    @FXML private VBox editPane;
    @FXML private TextField slangField;
    @FXML private TextArea definitionArea;

    private SlangDictionary dictionary;
    private String originalSlang; // Biến để lưu slang GỐC (phòng trường hợp người dùng đổi tên)

    /**
     * Hàm này được gọi tự động khi FXML được tải.
     */
    @FXML
    public void initialize() {
        this.dictionary = SlangDictionary.getInstance();

        // Vô hiệu hóa vùng chỉnh sửa cho đến khi tìm thấy
        this.editPane.setDisable(true);
    }

    @FXML
    private void handleFindButton() {
        String slangToFind = findField.getText().trim();
        if (slangToFind.isEmpty()) {
            showErrorAlert("Lỗi", "Vui lòng nhập Slang Word để tìm.");
            return;
        }

        // Gọi Model để tìm
        List<String> definitions = dictionary.findBySlang(slangToFind);

        // Xử lý kết quả
        if (definitions != null) {
            // TÌM THẤY
            this.originalSlang = slangToFind; // Lưu lại slang gốc

            slangField.setText(slangToFind); // Điền slang vào ô text

            // Chuyển List<String> thành 1 String (mỗi ý nghĩa 1 dòng)
            String defString = String.join("\n", definitions);
            definitionArea.setText(defString);

            // Kích hoạt vùng chỉnh sửa
            this.editPane.setDisable(false);

        } else {
            // KHÔNG TÌM THẤY
            showErrorAlert("Không tìm thấy", "Slang '" + slangToFind + "' không tồn tại.");
            this.editPane.setDisable(true); // Vô hiệu hóa vùng chỉnh sửa
            clearEditFields();
        }
    }

    /**
     * Hàm này được gọi khi nhấn nút "Lưu Thay Đổi"
     */
    @FXML
    private void handleSaveButton() {
        // Lấy dữ liệu mới từ các ô
        String newSlang = slangField.getText().trim();
        String newDefinitionsString = definitionArea.getText().trim();

        if (newSlang.isEmpty() || newDefinitionsString.isEmpty()) {
            showErrorAlert("Lỗi", "Slang và Definition không được để trống!");
            return;
        }

        // Chuyển đổi TextArea (nhiều dòng) thành List<String>
        List<String> newDefinitions = Arrays.asList(newDefinitionsString.split("\\n"))
                .stream()
                .filter(def -> !def.trim().isEmpty()) // Bỏ qua các dòng trống
                .collect(Collectors.toList());

        try {
            // Gọi Model để Sửa (dùng slang gốc để tìm và slang mới để cập nhật)
            dictionary.editSlang(this.originalSlang, newSlang, newDefinitions);

            showSuccessAlert("Đã cập nhật thành công slang: " + newSlang);

            // Reset giao diện
            findField.clear();
            clearEditFields();
            this.editPane.setDisable(true);

        } catch (IOException e) {
            showErrorAlert("Lỗi I/O", "Không thể lưu thay đổi vào file .dat.");
            e.printStackTrace();
        }
    }

    // --- Các hàm tiện ích ---

    private void clearEditFields() {
        this.originalSlang = null;
        slangField.clear();
        definitionArea.clear();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}