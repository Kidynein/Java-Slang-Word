package com.dtphat.slangdictionary.controller;

import com.dtphat.slangdictionary.model.SlangDictionary;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;

public class RandomController {

    @FXML
    private Label slangLabel; // Label để hiển thị Slang

    @FXML
    private Label definitionLabel; // Label để hiển thị Definition

    private SlangDictionary dictionary;

    @FXML
    public void initialize() {
        this.dictionary = SlangDictionary.getInstance();
        loadRandomSlang(); // Tải từ ngẫu nhiên lần đầu tiên
    }

    /**
     * Hàm này được gọi khi nhấn nút "Tìm từ khác"
     */
    @FXML
    private void handleRandomButton() {
        loadRandomSlang(); // Tải một từ ngẫu nhiên mới
    }

    /**
     * Hàm gọi Model, lấy từ ngẫu nhiên và cập nhật giao diện
     */
    private void loadRandomSlang() {
        // Gọi Chức năng 8 từ Model
        Map.Entry<String, List<String>> randomSlang = dictionary.getRandomSlang();

        if (randomSlang != null) {
            // Lấy Slang
            slangLabel.setText(randomSlang.getKey());

            // Lấy và nối các ý nghĩa
            String allDefs = String.join(", ", randomSlang.getValue());
            definitionLabel.setText(allDefs);

        } else {
            // Xử lý trường hợp từ điển bị rỗng
            slangLabel.setText("Lỗi");
            definitionLabel.setText("Không thể tải slang ngẫu nhiên. Hãy thử reset dữ liệu.");
        }
    }
}