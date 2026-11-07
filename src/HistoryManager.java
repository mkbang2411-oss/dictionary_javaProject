// 24127147
import java.io.*;
import java.util.*;

public class HistoryManager {
    private File file;
    private ArrayList<String> list = new ArrayList<String>();

    // Hàm khởi tạo: nhớ đường dẫn file history và đọc lịch sử cũ (nếu có)
    public HistoryManager(File file) {
        this.file = file;
        load();
    }

    // Hàm đọc lịch sử tìm kiếm từ file lên danh sách
    private void load() {
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    list.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hàm thêm 1 slang vừa tìm vào danh sách và ghi nối vào cuối file
    public void add(String word) {
        list.add(word);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"))) {
            writer.write(word + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hàm trả về toàn bộ lịch sử tìm kiếm (cũ nhất ở đầu)
    public List<String> getAll() {
        return list;
    }

    // Hàm xóa toàn bộ lịch sử tìm kiếm
    public void clear() {
        list.clear();
        if (file.exists()) {
            file.delete();
        }
    }
}
