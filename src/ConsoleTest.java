// 24127147
import java.io.*;
import java.util.*;

// Giai đoạn 2: hàm main test phần xử lý từ điển gốc (chạy trên terminal):
// nạp dữ liệu, thống kê, thử tìm theo slang word, thử lưu/đọc file nhị phân, rồi cho gõ slang để tìm
public class ConsoleTest {

    // Hàm main: chạy lần lượt các bước kiểm tra và in kết quả
    public static void main(String[] args) throws Exception {
        File dataFolder = DataManager.findDataFolder();
        if (dataFolder == null) {
            System.err.println("Cannot find the \"data\" folder (it must contain slang.txt).");
            return;
        }
        DataManager dataManager = new DataManager(dataFolder);

        System.out.println("=== 1. Load the dictionary ===");
        long start = System.nanoTime();
        dataManager.prepareDefaultData();
        SlangDictionary dictionary = dataManager.loadDictionary();
        System.out.println("Loaded in " + String.format("%.1f", (System.nanoTime() - start) / 1000000.0) + " ms");
        printStatistics(dictionary);

        System.out.println("\n=== 2. First 5 slang words ===");
        List<String> all = dictionary.getAllSlangs();
        for (int i = 0; i < 5 && i < all.size(); i++) {
            printSlang(dictionary, all.get(i));
        }

        System.out.println("\n=== 3. Search by slang word (not case sensitive) ===");
        String[] samples = { "#1", "LOL", "lol", "BRB", "this_slang_does_not_exist" };
        for (int i = 0; i < samples.length; i++) {
            testSearch(dictionary, samples[i]);
        }

        System.out.println("\n=== 4. Save to binary file and read back ===");
        File temp = File.createTempFile("slang_test", ".dat");
        dictionary.saveToBinary(temp);
        SlangDictionary loaded = SlangDictionary.loadFromBinary(temp);
        System.out.println("Words after reading back: " + loaded.size() + (loaded.size() == dictionary.size() ? " (OK)" : " (MISMATCH!)"));
        temp.delete();

        System.out.println("\n=== 5. Try it yourself ===");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a slang word to search (empty line to quit): ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String text = scanner.nextLine().trim();
            if (text.isEmpty()) {
                break;
            }
            testSearch(dictionary, text);
        }
        System.out.println("Bye.");
    }

    // Hàm in thống kê: số slang word và tổng số nghĩa
    private static void printStatistics(SlangDictionary dictionary) {
        int meaningCount = 0;
        List<String> all = dictionary.getAllSlangs();
        for (int i = 0; i < all.size(); i++) {
            meaningCount += dictionary.getMeanings(all.get(i)).size();
        }
        System.out.println("Slang words : " + dictionary.size());
        System.out.println("Meanings    : " + meaningCount);
    }

    // Hàm in 1 slang cùng các nghĩa của nó
    private static void printSlang(SlangDictionary dictionary, String slang) {
        List<String> meanings = dictionary.getMeanings(slang);
        System.out.println(slang + "  ->  " + SlangDictionary.joinMeanings(meanings));
    }

    // Hàm tìm 1 slang, in kết quả và thời gian tìm
    private static void testSearch(SlangDictionary dictionary, String slang) {
        long start = System.nanoTime();
        Map.Entry<String, List<String>> entry = dictionary.findEntry(slang);
        double ms = (System.nanoTime() - start) / 1000000.0;
        if (entry == null) {
            System.out.println("\"" + slang + "\" : not found (" + String.format("%.3f", ms) + " ms)");
        } else {
            System.out.println("\"" + slang + "\" : " + entry.getKey() + "  ->  " + SlangDictionary.joinMeanings(entry.getValue())
                    + " (" + String.format("%.3f", ms) + " ms)");
        }
    }
}
