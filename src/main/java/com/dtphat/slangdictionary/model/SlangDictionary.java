package com.dtphat.slangdictionary.model;

import java.io.*;
import java.util.*;

/**
 * Lớp {@code SlangDictionary} quản lý toàn bộ dữ liệu của từ điển Slang.
 * <p>
 * Lớp này sử dụng mô hình Singleton để đảm bảo chỉ có duy nhất một thể hiện
 * tồn tại trong suốt vòng đời của ứng dụng. Dữ liệu được lưu bằng {@link HashMap}
 * giúp tra cứu nhanh O(1). Ngoài ra, lịch sử tìm kiếm được lưu bằng {@link List}.
 * <p>
 * Cơ chế khởi tạo:
 * <ul>
 *   <li>Thử tải dữ liệu từ file nhị phân {@code slang.dat} (đã được tuần tự hóa).</li>
 *   <li>Nếu thất bại (chưa có file hoặc lỗi đọc), hệ thống sẽ đọc dữ liệu gốc từ file {@code slang.txt},
 *   xây dựng {@code HashMap}, và sau đó lưu lại dưới dạng {@code slang.dat} cho những lần chạy sau.</li>
 * </ul>
 *
 * @author Doan Thanh Phat
 */
public class SlangDictionary implements Serializable{
    // --- Singleton Pattern ---

    private static SlangDictionary instance = null;

    // --- Cấu trúc dữ liệu ---

    // Dùng HashMap để đảm bảo tìm kiếm Slang (Key) ra Definition (Value) với tốc độ O(1)
    private HashMap<String, List<String>> slangMap;
    // Dùng List để lưu lịch sử tìm kiếm
    private List<String> searchHistory;

    // --- Quản lý File ---

    // Tên file .dat (đã tối ưu) để lưu/tải HashMap
    private static final String DATA_FILE_PATH = "slang.dat";
    // Tên file .txt (dữ liệu thô)
    private static final String SOURCE_FILE_PATH = "slang.txt";

    /**
     * Constructor (private) của Singleton.
     * Sẽ được gọi MỘT LẦN DUY NHẤT khi getInstance() được gọi lần đầu.
     * Logic:
     * 1. Thử tải file slang.dat (đã tối ưu).
     * 2. Nếu thất bại (lần chạy đầu), đọc file slang.txt, build HashMap.
     * 3. Lưu HashMap đó ra slang.dat để dùng cho các lần sau.
     */
    private SlangDictionary() {
        this.slangMap = new HashMap<>();
        this.searchHistory = new ArrayList<>();

        System.out.println("Đang khởi tạo SlangDictionary...");

        // 1. Thử tải file .dat đã tối ưu
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE_PATH))) {
            this.slangMap = (HashMap<String, List<String>>) ois.readObject();
            this.searchHistory = (List<String>) ois.readObject();
            System.out.println("Tải dữ liệu từ " + DATA_FILE_PATH + " thành công!");
        }
        // 2. Nếu thất bại (file .dat không tồn tại hoặc lỗi)
        catch (Exception e) {
            System.out.println("Không tìm thấy file .dat, đang tải từ " + SOURCE_FILE_PATH + "...");
            try {
                // 3. Đọc file .txt gốc
                loadFromTextFile(SOURCE_FILE_PATH);
                // 4. Và lưu lại thành file .dat cho lần sau
                saveData(DATA_FILE_PATH);
                System.out.println("Nạp dữ liệu từ .txt và lưu ra .dat thành công!");
            } catch (IOException ex) {
                System.err.println("Lỗi nghiêm trọng: Không thể tải file .txt gốc.");
                ex.printStackTrace();
            }
        }
    }

    /**
     * Phương thức public để các Controller có thể lấy đối tượng duy nhất
     */
    public static SlangDictionary getInstance() {
        if (instance == null) {
            instance = new SlangDictionary();
        }
        return instance;
    }

    // --- Các hàm I/O (Đọc/Ghi File) ---
    /**
     * (Public) Lưu cấu trúc dữ liệu ra file .dat (dùng Serialization)
     * Được gọi bởi các hàm add, edit, delete.
     */
    public void saveData(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this.slangMap);
            oos.writeObject(this.searchHistory);
        }
    }

    /**
     * (Private) Đọc file .txt gốc (slang.txt) và build HashMap
     */
    private void loadFromTextFile(String fileName) throws IOException {
        // Dùng ClassLoader để đọc file từ thư mục resources
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new FileNotFoundException("Không tìm thấy file " + fileName + " trong resources");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            // Bỏ qua dòng tiêu đề đầu tiên (Slag`Meaning)
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("`");

                if (parts.length == 2) {
                    String slang = parts[0];
                    String definitionsString = parts[1];

                    // Tách các definitions bằng dấu |
                    // Dùng Arrays.asList để chuyển mảng thành List<String>
                    List<String> definitions = new ArrayList<>(Arrays.asList(definitionsString.split("\\|")));

                    this.slangMap.put(slang, definitions);
                }
            }
        }
    }
    // --- API Công khai cho Controller ---

    /**
     * Chức năng 1: Tìm kiếm theo slang word.
     */
    public List<String> findBySlang(String slang) {
        if (slang != null && !slang.isEmpty()) {
            // Thêm vào lịch sử (nếu chưa có)
            if (!this.searchHistory.contains(slang)) {
                this.searchHistory.add(slang);
            }
        }
        return this.slangMap.get(slang); // Trả về null nếu không tìm thấy
    }

    /**
     * Chức năng 2: Tìm kiếm theo definition.
     */
    public Map<String, List<String>> findByDefinition(String keyword) {
        Map<String, List<String>> results = new HashMap<>();
        String lowerKeyword = keyword.toLowerCase();

        // Duyệt qua toàn bộ HashMap
        for (Map.Entry<String, List<String>> entry : this.slangMap.entrySet()) {
            // Duyệt qua danh sách definition của mỗi slang
            for (String definition : entry.getValue()) {
                if (definition.toLowerCase().contains(lowerKeyword)) {
                    results.put(entry.getKey(), entry.getValue());
                    break; // Đã tìm thấy, không cần check các definition khác của slang này
                }
            }
        }
        return results;
    }

    /**
     * Chức năng 3: Hiển thị history.
     */
    public List<String> getHistory() {
        return this.searchHistory;
    }

    /**
     * Chức năng 4: Add 1 slang word mới.
     * Controller sẽ hỏi người dùng, sau đó gọi hàm này.
     * @param isDuplicate true = "Duplicate" (thêm definition mới), false = "Overwrite" (xóa def cũ, thêm def mới)
     */
    public void addSlang(String slang, String definition, boolean isDuplicate) throws IOException {
        List<String> definitions;

        if (isDuplicate && this.slangMap.containsKey(slang)) {
            // 1. Chế độ "Duplicate": Lấy list cũ và thêm vào
            definitions = this.slangMap.get(slang);
        } else {
            // 2. Chế độ "Overwrite" hoặc "Thêm mới": Tạo list mới
            definitions = new ArrayList<>();
        }

        definitions.add(definition);
        this.slangMap.put(slang, definitions);

        // Lưu lại thay đổi ra file .dat
        saveData(DATA_FILE_PATH);
    }
    public boolean slangExists(String slang) {
        return this.slangMap.containsKey(slang);
    }

    /**
     * Chức năng 5: Edit 1 slang word.
     */
    public void editSlang(String oldSlang, String newSlang, List<String> newDefinitions) throws IOException {
        // Nếu tên slang thay đổi, ta phải xóa key cũ
        if (!oldSlang.equals(newSlang)) {
            this.slangMap.remove(oldSlang);
        }
        // Thêm/cập nhật key mới
        this.slangMap.put(newSlang, newDefinitions);
        saveData(DATA_FILE_PATH);
    }

    /**
     * Chức năng 6: Delete 1 slang word.
     */
    public void deleteSlang(String slang) throws IOException {
        this.slangMap.remove(slang);
        saveData(DATA_FILE_PATH);
    }

    /**
     * Chức năng 7: Reset danh sách slang words gốc.
     */
    public void resetDictionary() throws IOException {
        System.out.println("Đang reset dữ liệu...");
        this.slangMap.clear();
        this.searchHistory.clear();
        loadFromTextFile(SOURCE_FILE_PATH); // Tải lại từ file .txt gốc
        saveData(DATA_FILE_PATH);          // Lưu lại file .dat mới
        System.out.println("Reset thành công!");
    }

    /**
     * Chức năng 8: Random 1 slang word.
     * Trả về một cặp Key-Value (Slang và List<Definition>)
     */
    public Map.Entry<String, List<String>> getRandomSlang() {
        if (this.slangMap.isEmpty()) {
            return null;
        }
        // Chuyển KeySet thành List để lấy random theo index
        List<String> keys = new ArrayList<>(this.slangMap.keySet());
        Random random = new Random();

        String randomSlangKey = keys.get(random.nextInt(keys.size()));
        List<String> definitions = this.slangMap.get(randomSlangKey);

        // Trả về một Entry (cặp key-value)
        return new AbstractMap.SimpleEntry<>(randomSlangKey, definitions);
    }

    /**
     * Dùng record (một lớp data đơn giản) để chứa dữ liệu câu đố
     * Nó sẽ chứa câu hỏi, 4 lựa chọn, và đáp án đúng (dưới dạng text)
     */
    public record QuizQuestion(String question, List<String> options, String correctAnswer) {}
    /**
     * Chức năng 9: Đố vui (1 slang, 4 definitions).
     */
    public QuizQuestion createSlangQuiz() {
        List<String> options = new ArrayList<>();

        // Lấy đáp án đúng
        Map.Entry<String, List<String>> correctEntry = getRandomSlang();
        String question = correctEntry.getKey(); // Slang là câu hỏi
        String correctAnswer = correctEntry.getValue().get(0); // Lấy definition đầu tiên làm đáp án
        options.add(correctAnswer);

        // Lấy 3 đáp án sai
        while (options.size() < 4) {
            Map.Entry<String, List<String>> wrongEntry = getRandomSlang();
            String wrongAnswer = wrongEntry.getValue().get(0);

            if (!options.contains(wrongAnswer)) {
                options.add(wrongAnswer);
            }
        }

        // Xáo trộn đáp án
        Collections.shuffle(options);
        return new QuizQuestion(question, options, correctAnswer);
    }

    /**
     * Chức năng 10: Đố vui (1 definition, 4 slangs).
     */
    public QuizQuestion createDefinitionQuiz() {
        List<String> options = new ArrayList<>();

        // Lấy đáp án đúng
        Map.Entry<String, List<String>> correctEntry = getRandomSlang();
        String question = correctEntry.getValue().get(0); // Definition là câu hỏi
        String correctAnswer = correctEntry.getKey(); // Slang là đáp án
        options.add(correctAnswer);

        // Lấy 3 đáp án sai
        while (options.size() < 4) {
            String wrongAnswer = getRandomSlang().getKey(); // Lấy 3 slang ngẫu nhiên khác

            if (!options.contains(wrongAnswer)) {
                options.add(wrongAnswer);
            }
        }

        // Xáo trộn đáp án
        Collections.shuffle(options);
        return new QuizQuestion(question, options, correctAnswer);
    }
}