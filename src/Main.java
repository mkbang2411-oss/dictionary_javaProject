// 24127147
import java.io.File;
import javax.swing.*;

public class Main {

    // Hàm main: tìm thư mục data, chuẩn bị dữ liệu (chỉ sửa file gốc nếu file chuẩn hóa chưa tồn tại), nạp từ điển và mở giao diện
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        File dataFolder = DataManager.findDataFolder();
        if (dataFolder == null) {
            JOptionPane.showMessageDialog(null, "Cannot find the \"data\" folder (it must be next to the jar file).", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        final DataManager dataManager = new DataManager(dataFolder);
        final SlangDictionary dictionary;
        try {
            dataManager.prepareDefaultData();
            dictionary = dataManager.loadDictionary();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot load the data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        final HistoryManager history = new HistoryManager(new File(dataFolder, "history.txt"));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame(dictionary, dataManager, history);
                frame.setVisible(true);
            }
        });
    }
}
