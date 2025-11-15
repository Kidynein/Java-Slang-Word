package com.dtphat.slangdictionary.controller;

import com.dtphat.slangdictionary.model.SlangDictionary;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SlangQuizController {

    // --- Các thành phần FXML ---
    @FXML private Label questionLabel;
    @FXML private Button optionButton1;
    @FXML private Button optionButton2;
    @FXML private Button optionButton3;
    @FXML private Button optionButton4;
    @FXML private Label feedbackLabel;
    @FXML private Button nextButton;

    // --- Biến nội bộ ---
    private SlangDictionary dictionary;
    private String correctAnswer; // Biến để lưu đáp án đúng của câu hiện tại

    @FXML
    public void initialize() {
        this.dictionary = SlangDictionary.getInstance();
        loadNewQuestion(); // Tải câu hỏi đầu tiên
    }

    /**
     * Tải một câu đố mới từ Model và cập nhật giao diện
     */
    private void loadNewQuestion() {
        // Lấy câu đố từ Model
        SlangDictionary.QuizQuestion quiz = dictionary.createSlangQuiz();

        // Lưu đáp án đúng
        this.correctAnswer = quiz.correctAnswer();

        // Cập nhật giao diện
        questionLabel.setText(quiz.question()); // Hiển thị Slang
        optionButton1.setText(quiz.options().get(0)); // Hiển thị 4 definitions
        optionButton2.setText(quiz.options().get(1));
        optionButton3.setText(quiz.options().get(2));
        optionButton4.setText(quiz.options().get(3));

        // Reset trạng thái
        feedbackLabel.setText(""); // Xóa phản hồi cũ
        setAnswerButtonsDisabled(false); // Kích hoạt lại các nút đáp án
    }

    // --- Các hàm xử lý sự kiện cho 4 nút đáp án ---

    @FXML private void handleOption1() { checkAnswer(optionButton1.getText()); }
    @FXML private void handleOption2() { checkAnswer(optionButton2.getText()); }
    @FXML private void handleOption3() { checkAnswer(optionButton3.getText()); }
    @FXML private void handleOption4() { checkAnswer(optionButton4.getText()); }

    /**
     * Hàm này được gọi khi nhấn nút "Câu hỏi tiếp theo"
     */
    @FXML
    private void handleNextButton() {
        loadNewQuestion();
    }

    /**
     * Hàm kiểm tra đáp án chung
     * @param selectedAnswer Văn bản trên nút mà người dùng đã chọn
     */
    private void checkAnswer(String selectedAnswer) {
        // Vô hiệu hóa các nút sau khi đã chọn
        setAnswerButtonsDisabled(true);

        // Kiểm tra và hiển thị phản hồi
        if (selectedAnswer.equals(this.correctAnswer)) {
            feedbackLabel.setText("Đúng rồi!");
            feedbackLabel.setStyle("-fx-text-fill: green;"); // Tô màu xanh
        } else {
            feedbackLabel.setText("Sai! Đáp án đúng là: \n" + this.correctAnswer);
            feedbackLabel.setStyle("-fx-text-fill: red;"); // Tô màu đỏ
        }
    }

    /**
     * Hàm tiện ích để bật/tắt 4 nút đáp án
     */
    private void setAnswerButtonsDisabled(boolean disabled) {
        optionButton1.setDisable(disabled);
        optionButton2.setDisable(disabled);
        optionButton3.setDisable(disabled);
        optionButton4.setDisable(disabled);
    }
}