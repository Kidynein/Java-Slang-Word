import com.dtphat.slangdictionary.model.SlangDictionary;

void main() {
    // 1. Khởi tạo "bộ não"
    // Console sẽ in ra log "Đang tải file..."
    System.out.println("--- BẮT ĐẦU KHỞI TẠO TỪ ĐIỂN ---");
    SlangDictionary dictionary = SlangDictionary.getInstance();
    System.out.println("--- KHỞI TẠO TỪ ĐIỂN THÀNH CÔNG ---\n");

    // ---------------------------------------------------
    // TEST CHỨC NĂNG 1: findBySlang
    // ---------------------------------------------------
    System.out.println("========= TEST 1: TÌM THEO SLANG =========");

    // Test 1.1: Tìm từ có 2+ ý nghĩa
    String slangToFind = "$";
    System.out.println("Đang tìm slang: \"" + slangToFind + "\"");
    List<String> resultsSlang = dictionary.findBySlang(slangToFind);

    if (resultsSlang != null) {
        System.out.println("Kết quả: TÌM THẤY!");
        for (String def : resultsSlang) {
            System.out.println("  • " + def);
        }
    } else {
        System.out.println("Kết quả: KHÔNG TÌM THẤY.");
    }

    // Test 1.2: Tìm từ không tồn tại
    slangToFind = "NonExistentSlang123";
    System.out.println("\nĐang tìm slang: \"" + slangToFind + "\"");
    resultsSlang = dictionary.findBySlang(slangToFind);

    if (resultsSlang != null) {
        System.out.println("Kết quả: TÌM THẤY! (Lỗi)");
    } else {
        System.out.println("Kết quả: KHÔNG TÌM THẤY. (Đúng)");
    }

    // ---------------------------------------------------
    // TEST CHỨC NĂNG 2: findByDefinition
    // ---------------------------------------------------
    System.out.println("\n========= TEST 2: TÌM THEO DEFINITION =========");

    // Test 2.1: Tìm từ khóa có trong nhiều slang
    String keywordToFind = "money";
    System.out.println("Đang tìm definition chứa: \"" + keywordToFind + "\"");
    Map<String, List<String>> resultsDef = dictionary.findByDefinition(keywordToFind);

    if (resultsDef.isEmpty()) {
        System.out.println("Kết quả: KHÔNG TÌM THẤY.");
    } else {
        System.out.println("Kết quả: TÌM THẤY " + resultsDef.size() + " SLANG(S)!");
        // Duyệt qua Map và in kết quả
        for (Map.Entry<String, List<String>> entry : resultsDef.entrySet()) {
            System.out.println("  • Slang: " + entry.getKey());
            System.out.println("    Định nghĩa: " + entry.getValue());
        }
    }

    // Test 2.2: Tìm từ khóa không tồn tại
    keywordToFind = "NonExistentKeyword123";
    System.out.println("\nĐang tìm definition chứa: \"" + keywordToFind + "\"");
    resultsDef = dictionary.findByDefinition(keywordToFind);

    if (resultsDef.isEmpty()) {
        System.out.println("Kết quả: KHÔNG TÌM THẤY. (Đúng)");
    } else {
        System.out.println("Kết quả: TÌM THẤY! (Lỗi)");
    }
}