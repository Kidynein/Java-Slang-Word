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
     * Định dạng file giả định: Slang`Definition1|Definition2
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
}