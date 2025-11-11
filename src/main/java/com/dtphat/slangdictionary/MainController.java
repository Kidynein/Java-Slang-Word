package com.dtphat.slangdictionary;

import com.dtphat.slangdictionary.model.SlangDictionary;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    // Tham chiếu đến BorderPane gốc trong FXML
    @FXML
    private BorderPane rootPane;
    @FXML
    public void initialize() {
        // (Code tải danh sách bên trái của bạn_
        // loadSlangList(); // (Giữ lại nếu bạn đang dùng)

        // SỬA DÒNG NÀY:
        // Tải màn hình Chào mừng vào trung tâm
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
    }

    // (Thêm các hàm handleShowAdd, handleShowEdit, handleShowDelete...)

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

    // (Thêm các hàm handleShowRandom, handleShowSlangQuiz...)

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