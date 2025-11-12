package com.dtphat.slangdictionary.controller;

import com.dtphat.slangdictionary.model.SlangDictionary;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;

public class WelcomeController {

    @FXML
    private Label slangLabel; // Label để hiển thị Slang

    @FXML
    private Label definitionLabel; // Label để hiển thị Definition

    private SlangDictionary dictionary;

    @FXML
    public void initialize() {
        this.dictionary = SlangDictionary.getInstance();

        Map.Entry<String, List<String>> randomSlang = dictionary.getRandomSlang();

        if (randomSlang != null) {
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