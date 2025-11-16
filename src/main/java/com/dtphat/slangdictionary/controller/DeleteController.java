package com.dtphat.slangdictionary.controller;

import com.dtphat.slangdictionary.model.SlangDictionary;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class DeleteController {

    @FXML
    private TextField slangField;

    private SlangDictionary dictionary;

    @FXML
    public void initialize() {
        this.dictionary = SlangDictionary.getInstance();
    }

    @FXML
    private void handleDeleteButton() {
        String slang = slangField.getText().trim();

        // 1. Kiểm tra đầu vào rỗng
        if (slang.isEmpty()) {
            showErrorAlert("Lỗi", "Bạn chưa nhập Slang Word để xóa!");
            return;
        }

        try {
            // 2. Kiểm tra xem Slang có tồn tại không
            if (dictionary.slangExists(slang)) {

                // 3. HIỂN THỊ HỘP THOẠI XÁC NHẬN (Yêu cầu bắt buộc)
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận xóa");
                alert.setHeaderText("Bạn có chắc chắn muốn xóa slang: \"" + slang + "\"?");
                alert.setContentText("Hành động này không thể hoàn tác.");

                // Hiển thị và chờ người dùng chọn
                Optional<ButtonType> result = alert.showAndWait();

                // 4. Nếu người dùng nhấn "OK"
                if (result.isPresent() && result.get() == ButtonType.OK) {

                    // 5. Gọi Model để xóa
                    dictionary.deleteSlang(slang);
                    showSuccessAlert("Đã xóa thành công slang: " + slang);
                    slangField.clear(); // Xóa ô text
                }
                // Nếu người dùng nhấn "Cancel", không làm gì cả

            } else {
                // 6. Thông báo lỗi nếu Slang không tồn tại
                showErrorAlert("Không tìm thấy", "Slang '" + slang + "' không tồn tại trong từ điển.");
            }

        } catch (IOException e) {
            showErrorAlert("Lỗi I/O", "Không thể lưu thay đổi vào file .dat.");
            e.printStackTrace();
        }
    }

    // --- Các hàm tiện ích ---

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