package com.dtphat.slangdictionary.controller;

import com.dtphat.slangdictionary.model.SlangDictionary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class HistoryController {

    @FXML
    private ListView<String> historyListView;

    private SlangDictionary dictionary;

    @FXML
    public void initialize() {
        this.dictionary = SlangDictionary.getInstance();

        List<String> history = dictionary.getHistory();

        historyListView.getItems().clear();

        if (history != null && !history.isEmpty()) {
            // Chuyển List<String> thành ObservableList<String> ( cho ListView)
            ObservableList<String> items = FXCollections.observableArrayList(history);

            // Bơm dữ liệu vào ListView
            historyListView.setItems(items);
        } else {
            historyListView.getItems().add("Lịch sử tìm kiếm của bạn đang trống.");
        }
    }
}