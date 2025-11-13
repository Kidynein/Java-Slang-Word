package com.dtphat.slangdictionary.controller;

import com.dtphat.slangdictionary.model.SlangDictionary;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class AddController {

    @FXML
    private TextField slangField;

    @FXML
    private TextField definitionField;

    private SlangDictionary dictionary;

    @FXML
    public void initialize() {
        // Lấy "bộ não" Singleton
        this.dictionary = SlangDictionary.getInstance();
    }

    @FXML
    private void handleAddButton() {
        String slang = slangField.getText().trim();
        String definition = definitionField.getText().trim();

        // 1. Kiểm tra đầu vào rỗng
        if (slang.isEmpty() || definition.isEmpty()) {
            showErrorAlert("Lỗi", "Slang và Definition không được để trống!");
            return;
        }

        try {
            // 2. Kiểm tra xem Slang đã tồn tại chưa
            if (dictionary.slangExists(slang)) {
                // NẾU TỒN TẠI: Hiển thị hộp thoại xác nhận (Overwrite/Duplicate)

                // Tạo các nút tùy chỉnh
                ButtonType overwriteButton = new ButtonType("Overwrite (Ghi đè)");
                ButtonType duplicateButton = new ButtonType("Duplicate (Tạo bản sao)");
                ButtonType cancelButton = ButtonType.CANCEL;

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Slang Word đã tồn tại");
                alert.setHeaderText("Slang '" + slang + "' đã có trong từ điển.");
                alert.setContentText("Bạn muốn Ghi đè hay Tạo bản sao (thêm ý nghĩa mới)?");

                alert.getButtonTypes().setAll(overwriteButton, duplicateButton, cancelButton);

                // Hiển thị và chờ người dùng chọn
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent()) {
                    if (result.get() == overwriteButton) {
                        // Người dùng chọn Ghi đè
                        dictionary.addSlang(slang, definition, false); // isDuplicate = false
                        showSuccessAlert("Đã ghi đè thành công slang: " + slang);
                    } else if (result.get() == duplicateButton) {
                        // Người dùng chọn Tạo bản sao
                        dictionary.addSlang(slang, definition, true); // isDuplicate = true
                        showSuccessAlert("Đã thêm ý nghĩa mới cho slang: " + slang);
                    }
                    // Nếu người dùng chọn Cancel, không làm gì cả
                }

            } else {
                // NẾU KHÔNG TỒN TẠI: Thêm mới
                dictionary.addSlang(slang, definition, false); // Thêm mới thì isDuplicate = false
                showSuccessAlert("Đã thêm thành công slang: " + slang);
            }

            // Xóa các ô text để chuẩn bị cho lần nhập tiếp theo
            clearFields();

        } catch (IOException e) {
            showErrorAlert("Lỗi I/O", "Không thể lưu thay đổi vào file .dat.");
            e.printStackTrace();
        }
    }

    // --- Các hàm tiện ích (Helper Methods) ---

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

    private void clearFields() {
        slangField.clear();
        definitionField.clear();
    }
}