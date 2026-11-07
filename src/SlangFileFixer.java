// 24127147
import java.io.*;
import java.util.*;

public class SlangFileFixer {

    // Hàm sửa file slang.txt gốc rồi lưu thành file mới đúng cú pháp chuẩn (slang`nghĩa 1| nghĩa 2)
    // Các lỗi được xử lý: dòng thiếu dấu ` (là nghĩa tiếp theo của slang ở dòng trên), slang trùng tên
    // (gộp nghĩa lại), khoảng trắng thừa, dấu | thiếu khoảng trắng, dòng trống, tiêu đề "Slag" sai chính tả
    public static void fixFile(File originalFile, File cleanFile) throws IOException {
        TreeMap<String, List<String>> data = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
        String lastSlang = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(originalFile), "UTF-8"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.startsWith("Slag`") || line.startsWith("Slang`")) {
                        continue;
                    }
                }
                if (line.isEmpty()) {
                    continue;
                }

                int pos = line.indexOf('`');
                if (pos < 0) {
                    addToSlang(data, lastSlang, line);
                    continue;
                }

                String slang = line.substring(0, pos).trim();
                String meaningText = line.substring(pos + 1);
                if (slang.isEmpty()) {
                    addToSlang(data, lastSlang, meaningText);
                    continue;
                }
                if (!data.containsKey(slang)) {
                    data.put(slang, new ArrayList<String>());
                }
                lastSlang = slang;
                addToSlang(data, lastSlang, meaningText);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cleanFile), "UTF-8"))) {
            writer.write("Slang`Meaning\n");
            for (Map.Entry<String, List<String>> entry : data.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    continue;
                }
                writer.write(entry.getKey() + "`" + SlangDictionary.joinMeanings(entry.getValue()) + "\n");
            }
        }
    }

    // Hàm thêm các nghĩa trong 1 đoạn text vào slang đã có trong map (bỏ nghĩa trùng, bỏ dấu ` thừa)
    private static void addToSlang(TreeMap<String, List<String>> data, String slang, String meaningText) {
        if (slang == null) {
            return;
        }
        List<String> newMeanings = SlangDictionary.splitMeanings(meaningText.replace('`', '\''));
        List<String> current = data.get(slang);
        for (int i = 0; i < newMeanings.size(); i++) {
            if (!SlangDictionary.containsIgnoreCase(current, newMeanings.get(i))) {
                current.add(newMeanings.get(i));
            }
        }
    }
}
