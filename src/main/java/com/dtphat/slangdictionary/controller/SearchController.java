package com.dtphat.slangdictionary.controller;

import com.dtphat.slangdictionary.model.SlangDictionary;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.util.List;
import java.util.Map;

public class SearchController {

    // --- Các thành phần FXML ---
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> resultListView;
    @FXML
    private RadioButton radioSearchBySlang;
    @FXML
    private RadioButton radioSearchByDef;
    @FXML
    private ToggleGroup searchToggleGroup;

    private SlangDictionary dictionary;

    /**
     * Hàm initialize() sẽ tự động được gọi khi FXML này được tải.
     */
    @FXML
    public void initialize() {
        this.dictionary = SlangDictionary.getInstance();
    }

    @FXML
    private void handleSearchButton() {
        String keyword = searchField.getText().trim();

        // Xóa kết quả cũ
        resultListView.getItems().clear();

        if (keyword.isEmpty()) {
            resultListView.getItems().add("Vui lòng nhập từ khóa để tìm kiếm.");
            return;
        }

        // Kiểm tra xem người dùng muốn tìm theo Slang hay Definition
        if (radioSearchBySlang.isSelected()) {
            searchBySlang(keyword);
        } else {
            searchByDefinition(keyword);
        }
    }

    /**
     * Xử lý logic tìm theo Slang
     */
    private void searchBySlang(String keyword) {
        //  Gọi từ Model
        List<String> definitions = dictionary.findBySlang(keyword);

        //  Hiển thị kết quả mới
        if (definitions != null && !definitions.isEmpty()) {
            resultListView.getItems().add("Kết quả cho Slang '" + keyword + "':");
            // Thêm từng definition vào ListView
            for (String def : definitions) {
                resultListView.getItems().add("  • " + def);
            }
        } else {
            resultListView.getItems().add("Không tìm thấy Slang Word nào cho '" + keyword + "'");
        }
    }

    /**
     * Xử lý logic tìm theo Definition
     */
    private void searchByDefinition(String keyword) {
        // Gọi từ Model
        Map<String, List<String>> results = dictionary.findByDefinition(keyword);

        // Hiển thị kết quả mới
        if (results != null && !results.isEmpty()) {
            resultListView.getItems().add(results.size() + " Slang(s) chứa từ khóa '" + keyword + "':");
            // Duyệt qua Map và định dạng lại để hiển thị
            for (Map.Entry<String, List<String>> entry : results.entrySet()) {
                String allDefs = String.join(", ", entry.getValue());
                resultListView.getItems().add(entry.getKey() + "  =>  " + allDefs);
            }
        } else {
            resultListView.getItems().add("Không tìm thấy Definition nào chứa '" + keyword + "'");
        }
    }
}