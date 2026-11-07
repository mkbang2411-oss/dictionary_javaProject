// 24127147
import java.io.File;

// Giai đoạn 1: chương trình chạy trên terminal để xử lý file slang.txt gốc của đề
// (sửa lỗi cú pháp -> slang_clean.txt, rồi tạo cấu trúc dữ liệu đã index -> slang_default.dat)
public class ProcessOriginalData {

    // Hàm main: tìm thư mục data, chạy bước chuẩn hóa file gốc và bước tạo file nhị phân, in kết quả ra màn hình
    public static void main(String[] args) {
        File dataFolder = DataManager.findDataFolder();
        if (dataFolder == null) {
            System.err.println("Cannot find the \"data\" folder (it must contain slang.txt).");
            return;
        }
        System.out.println("Data folder: " + dataFolder.getAbsolutePath());

        File original = new File(dataFolder, "slang.txt");
        File clean = new File(dataFolder, "slang_clean.txt");
        File binary = new File(dataFolder, "slang_default.dat");
        System.out.println("slang.txt exists         : " + original.exists());
        System.out.println("slang_clean.txt existed  : " + clean.exists() + (clean.exists() ? " (skip fixing, delete it to fix again)" : ""));
        System.out.println("slang_default.dat existed: " + binary.exists());

        DataManager dataManager = new DataManager(dataFolder);
        try {
            long start = System.nanoTime();
            dataManager.prepareDefaultData();
            double ms = (System.nanoTime() - start) / 1000000.0;
            System.out.println("\nProcessing done in " + String.format("%.1f", ms) + " ms");
            System.out.println("slang_clean.txt   : " + clean.length() + " bytes");
            System.out.println("slang_default.dat : " + binary.length() + " bytes");

            SlangDictionary dictionary = dataManager.loadDictionary();
            System.out.println("Slang words loaded: " + dictionary.size());
        } catch (Exception e) {
            System.err.println("Processing failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
