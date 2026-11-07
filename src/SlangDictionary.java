// 24127147
import java.io.*;
import java.util.*;

public class SlangDictionary implements Serializable {
    private static final long serialVersionUID = 1L;

    private TreeMap<String, List<String>> words;
    private HashMap<String, HashSet<String>> index;

    // Hàm khởi tạo: tạo 2 cấu trúc dữ liệu rỗng (map slang -> nghĩa và index tìm theo definition)
    public SlangDictionary() {
        words = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
        index = new HashMap<String, HashSet<String>>();
    }

    // Hàm tách chuỗi nghĩa (ngăn cách bởi dấu |) thành danh sách các nghĩa, bỏ nghĩa rỗng và nghĩa trùng
    public static List<String> splitMeanings(String text) {
        List<String> result = new ArrayList<String>();
        String[] parts = text.split("\\|");
        for (int i = 0; i < parts.length; i++) {
            String meaning = parts[i].trim();
            if (!meaning.isEmpty() && !containsIgnoreCase(result, meaning)) {
                result.add(meaning);
            }
        }
        return result;
    }

    // Hàm nối danh sách nghĩa thành 1 chuỗi, ngăn cách bởi "| "
    public static String joinMeanings(List<String> meanings) {
        return String.join("| ", meanings);
    }

    // Hàm kiểm tra danh sách có chứa chuỗi cho trước hay không (không phân biệt hoa thường)
    public static boolean containsIgnoreCase(List<String> list, String text) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }

    // Hàm tách một đoạn text thành các từ thường (chữ + số), dùng để xây index
    private static List<String> tokenize(String text) {
        List<String> result = new ArrayList<String>();
        String[] parts = text.toLowerCase().split("[^\\p{L}\\p{N}]+");
        for (int i = 0; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                result.add(parts[i]);
            }
        }
        return result;
    }

    // Hàm trả về số lượng slang word hiện có
    public int size() {
        return words.size();
    }

    // Hàm tìm 1 slang (không phân biệt hoa thường), trả về cặp (slang, danh sách nghĩa) hoặc null nếu không có
    public Map.Entry<String, List<String>> findEntry(String slang) {
        Map.Entry<String, List<String>> entry = words.ceilingEntry(slang);
        if (entry != null && entry.getKey().equalsIgnoreCase(slang)) {
            return entry;
        }
        return null;
    }

    // Hàm kiểm tra slang có tồn tại trong từ điển hay không
    public boolean contains(String slang) {
        return findEntry(slang) != null;
    }

    // Hàm lấy danh sách nghĩa của 1 slang, trả về null nếu không có slang đó
    public List<String> getMeanings(String slang) {
        Map.Entry<String, List<String>> entry = findEntry(slang);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    // Hàm lấy danh sách tất cả slang (đã sắp xếp), dùng cho chức năng random và đố vui
    public List<String> getAllSlangs() {
        return new ArrayList<String>(words.keySet());
    }

    // Hàm đưa tất cả các từ trong 1 nghĩa vào index (từ -> tập các slang có chứa từ đó)
    private void indexMeaning(String slang, String meaning) {
        List<String> tokens = tokenize(meaning);
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            HashSet<String> set = index.get(token);
            if (set == null) {
                set = new HashSet<String>();
                index.put(token, set);
            }
            set.add(slang);
        }
    }

    // Hàm đưa tất cả nghĩa của 1 slang vào index
    private void indexSlang(String slang, List<String> meanings) {
        for (int i = 0; i < meanings.size(); i++) {
            indexMeaning(slang, meanings.get(i));
        }
    }

    // Hàm xóa slang khỏi index (dùng khi xóa, sửa, ghi đè)
    private void unindexSlang(String slang, List<String> meanings) {
        for (int i = 0; i < meanings.size(); i++) {
            List<String> tokens = tokenize(meanings.get(i));
            for (int j = 0; j < tokens.size(); j++) {
                HashSet<String> set = index.get(tokens.get(j));
                if (set != null) {
                    set.remove(slang);
                    if (set.isEmpty()) {
                        index.remove(tokens.get(j));
                    }
                }
            }
        }
    }

    // Hàm thêm 1 slang hoàn toàn mới (người gọi phải kiểm tra slang chưa tồn tại)
    public void addNew(String slang, List<String> meanings) {
        words.put(slang, new ArrayList<String>(meanings));
        indexSlang(slang, meanings);
    }

    // Hàm duplicate: thêm nghĩa mới vào slang đã có, trả về số nghĩa thực sự được thêm
    public int addMeanings(String slang, List<String> newMeanings) {
        Map.Entry<String, List<String>> entry = findEntry(slang);
        if (entry == null) {
            return 0;
        }
        String key = entry.getKey();
        List<String> current = entry.getValue();
        int count = 0;
        for (int i = 0; i < newMeanings.size(); i++) {
            String meaning = newMeanings.get(i);
            if (!containsIgnoreCase(current, meaning)) {
                current.add(meaning);
                indexMeaning(key, meaning);
                count++;
            }
        }
        return count;
    }

    // Hàm sửa slang: xóa slang cũ rồi thêm slang mới (có thể đổi cả tên và nghĩa)
    public void edit(String oldSlang, String newSlang, List<String> newMeanings) {
        delete(oldSlang);
        addNew(newSlang, newMeanings);
    }

    // Hàm xóa 1 slang khỏi từ điển và khỏi index
    public void delete(String slang) {
        Map.Entry<String, List<String>> entry = findEntry(slang);
        if (entry == null) {
            return;
        }
        String key = entry.getKey();
        unindexSlang(key, entry.getValue());
        words.remove(key);
    }

    // Hàm tìm các slang có definition chứa keyword (dùng index để thu hẹp rồi kiểm tra lại), kết quả đã sắp xếp
    public List<String> searchByDefinition(String keyword) {
        String kw = keyword.trim().toLowerCase();
        List<String> result = new ArrayList<String>();
        if (kw.isEmpty()) {
            return result;
        }

        HashSet<String> candidates = new HashSet<String>();
        List<String> tokens = tokenize(kw);
        if (tokens.isEmpty()) {
            candidates.addAll(words.keySet());
        } else {
            String firstToken = tokens.get(0);
            for (Map.Entry<String, HashSet<String>> e : index.entrySet()) {
                if (e.getKey().contains(firstToken)) {
                    candidates.addAll(e.getValue());
                }
            }
        }

        for (String slang : candidates) {
            List<String> meanings = words.get(slang);
            for (int i = 0; i < meanings.size(); i++) {
                if (meanings.get(i).toLowerCase().contains(kw)) {
                    result.add(slang);
                    break;
                }
            }
        }
        Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
        return result;
    }

    // Hàm thay toàn bộ dữ liệu hiện tại bằng dữ liệu của từ điển khác (dùng cho chức năng reset)
    public void replaceData(SlangDictionary other) {
        this.words = other.words;
        this.index = other.index;
    }

    // Hàm lưu từ điển ra file text theo cú pháp chuẩn: slang`nghĩa 1| nghĩa 2
    public void saveToText(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            writer.write("Slang`Meaning\n");
            for (Map.Entry<String, List<String>> entry : words.entrySet()) {
                writer.write(entry.getKey() + "`" + joinMeanings(entry.getValue()) + "\n");
            }
        }
    }

    // Hàm đọc từ điển từ file text chuẩn (đã được chuẩn hóa) và xây dựng index
    public static SlangDictionary loadFromText(File file) throws IOException {
        SlangDictionary dictionary = new SlangDictionary();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.startsWith("Slang`")) {
                        continue;
                    }
                }
                int pos = line.indexOf('`');
                if (pos < 0) {
                    continue;
                }
                String slang = line.substring(0, pos).trim();
                List<String> meanings = splitMeanings(line.substring(pos + 1));
                if (slang.isEmpty() || meanings.isEmpty()) {
                    continue;
                }
                if (dictionary.contains(slang)) {
                    dictionary.addMeanings(slang, meanings);
                } else {
                    dictionary.addNew(slang, meanings);
                }
            }
        }
        return dictionary;
    }

    // Hàm lưu nguyên cấu trúc dữ liệu (đã index) ra file nhị phân để lần sau không phải index lại
    public void saveToBinary(File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            out.writeObject(this);
        }
    }

    // Hàm đọc lại cấu trúc dữ liệu đã lưu từ file nhị phân
    public static SlangDictionary loadFromBinary(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            return (SlangDictionary) in.readObject();
        }
    }
}
