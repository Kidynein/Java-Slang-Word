package com.dtphat.slangdictionary;

import com.dtphat.slangdictionary.model.SlangDictionary;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane rootPane;
    @FXML
    public void initialize() {
        loadView("welcome-view.fxml");
    }

    // --- Các hàm xử lý sự kiện cho Menu ---

    @FXML
    private void handleShowSearch() {
        System.out.println("Tải màn hình tìm kiếm...");
        loadView("search-view.fxml");
    }

    @FXML
    private void handleShowHistory() {
        System.out.println("Tải màn hình lịch sử...");
        loadView("history-view.fxml");
    }

    @FXML
    private void handleShowAdd() {
        System.out.println("Tải màn hình Thêm Slang...");
        loadView("add-view.fxml");
    }

    @FXML
    private void handleShowEdit() {
        System.out.println("Tải màn hình Sửa Slang...");
        loadView("edit-view.fxml");
    }

    @FXML
    private void handleShowDelete() {
        System.out.println("Tải màn hình Xóa Slang...");
        loadView("delete-view.fxml");
    }
    
    @FXML
    private void handleReset() {
        try {
            SlangDictionary.getInstance().resetDictionary();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Đã reset dữ liệu về file gốc thành công!");
            alert.showAndWait();

            rootPane.setCenter(null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowRandom() {
        System.out.println("Tải màn hình Random Slang...");
        loadView("random-view.fxml");
    }

    @FXML
    private void handleShowSlangQuiz() {
        System.out.println("Tải màn hình Đố Slang...");
        loadView("slang-quiz-view.fxml");
    }

    @FXML
    private void handleShowDefQuiz() {
        System.out.println("Tải màn hình Đố Definition...");
        loadView("def-quiz-view.fxml");
    }

    // --- HÀM TIỆN ÍCH ---

    private void loadView(String fxmlFile) {
        try {
            // Đường dẫn tuyệt đối (từ gốc resources)
            String pathToFxml = "/com/dtphat/slangdictionary/" + fxmlFile;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToFxml));

            Node view = loader.load();
            rootPane.setCenter(view); // Đặt FXML con vào trung tâm

        } catch (IOException e) {
            System.err.println("Không thể tải file FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }
}