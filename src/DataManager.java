// 24127147
import java.io.*;

public class DataManager {
    private File dataFolder;
    private File originalFile;
    private File cleanFile;
    private File defaultBinFile;
    private File userBinFile;
    private File userTextFile;

    // Hàm khởi tạo: xác định đường dẫn tất cả các file dữ liệu trong thư mục data
    public DataManager(File dataFolder) {
        this.dataFolder = dataFolder;
        originalFile = new File(dataFolder, "slang.txt");
        cleanFile = new File(dataFolder, "slang_clean.txt");
        defaultBinFile = new File(dataFolder, "slang_default.dat");
        userBinFile = new File(dataFolder, "slang_user.dat");
        userTextFile = new File(dataFolder, "slang_user.txt");
    }

    // Hàm trả về thư mục data (để nơi khác lưu thêm file như history)
    public File getDataFolder() {
        return dataFolder;
    }

    // Hàm kiểm tra 1 thư mục có phải thư mục data của chương trình không
    private static boolean isDataFolder(File folder) {
        if (folder == null || !folder.isDirectory()) {
            return false;
        }
        return new File(folder, "slang.txt").exists() || new File(folder, "slang_clean.txt").exists();
    }

    // Hàm tìm thư mục data: thử thư mục chạy chương trình, rồi thư mục chứa file jar, rồi thư mục cha của nó
    public static File findDataFolder() {
        File folder = new File("data");
        if (isDataFolder(folder)) {
            return folder;
        }
        try {
            File location = new File(DataManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File dir = location;
            if (location.isFile()) {
                dir = location.getParentFile();
            }
            folder = new File(dir, "data");
            if (isDataFolder(folder)) {
                return folder;
            }
            if (dir.getParentFile() != null) {
                folder = new File(dir.getParentFile(), "data");
                if (isDataFolder(folder)) {
                    return folder;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hàm chuẩn bị dữ liệu mặc định: nếu file chuẩn hóa chưa có thì sửa file gốc để tạo ra nó (chỉ làm 1 lần),
    // nếu file nhị phân (đã index) chưa có thì tạo từ file chuẩn hóa
    public void prepareDefaultData() throws IOException {
        boolean needRebuild = false;
        if (!cleanFile.exists()) {
            SlangFileFixer.fixFile(originalFile, cleanFile);
            needRebuild = true;
        }
        if (needRebuild || !defaultBinFile.exists()) {
            SlangDictionary dictionary = SlangDictionary.loadFromText(cleanFile);
            dictionary.saveToBinary(defaultBinFile);
        }
    }

    // Hàm đọc từ điển mặc định (từ file nhị phân, nếu hỏng thì đọc lại từ file chuẩn hóa)
    private SlangDictionary loadDefault() throws IOException {
        try {
            return SlangDictionary.loadFromBinary(defaultBinFile);
        } catch (Exception e) {
            SlangDictionary dictionary = SlangDictionary.loadFromText(cleanFile);
            dictionary.saveToBinary(defaultBinFile);
            return dictionary;
        }
    }

    // Hàm đọc từ điển khi mở chương trình: ưu tiên bản của người dùng (đã add/edit/delete), không có thì dùng bản mặc định
    public SlangDictionary loadDictionary() throws IOException {
        if (userBinFile.exists()) {
            try {
                return SlangDictionary.loadFromBinary(userBinFile);
            } catch (Exception e) {
                e.printStackTrace();
                if (userTextFile.exists()) {
                    return SlangDictionary.loadFromText(userTextFile);
                }
            }
        }
        return loadDefault();
    }

    // Hàm lưu từ điển của người dùng (file nhị phân + file text), trả về false nếu lưu lỗi
    public boolean saveUserDictionary(SlangDictionary dictionary) {
        try {
            dictionary.saveToBinary(userBinFile);
            dictionary.saveToText(userTextFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm reset: xóa các file của người dùng và đọc lại từ điển mặc định ban đầu
    public SlangDictionary resetDictionary() throws IOException {
        if (userBinFile.exists()) {
            userBinFile.delete();
        }
        if (userTextFile.exists()) {
            userTextFile.delete();
        }
        return loadDefault();
    }
}
