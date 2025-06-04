package gataulagigw;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class Main {
    private static Trie trie = new Trie(); // Trie untuk menyimpan dan mencari model kendaraan.

    public static void main(String[] args) {
        loadData("src/data.csv"); // Load data CSV ke dalam struktur Trie.

        // Inisialisasi frame utama aplikasi GUI
        JFrame frame = new JFrame("Pencarian Model Kendaraan (Trie)");
        frame.setSize(1000, 600); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel utama menggunakan BorderLayout
        JPanel panel = new JPanel(new BorderLayout());

        // Input pencarian
        JTextField inputField = new JTextField(40);
        JButton searchButton = new JButton("Cari");

        // Panel untuk bagian atas (input)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputRow.add(inputField);
        inputRow.add(searchButton);
        inputPanel.add(inputRow);

        panel.add(inputPanel, BorderLayout.NORTH); // Letakkan input di atas

        // Tabel hasil pencarian
        String[] columnNames = {"nama_model", "brand", "tahun", "CC", "Transmisi", "Jumlah penumpang"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Event saat tombol cari diklik
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String keyword = inputField.getText().toLowerCase(); // Ambil kata kunci
                List<String> results = trie.searchByPrefix(keyword); // Cari model di Trie

                tableModel.setRowCount(0); // Hapus isi tabel sebelumnya

                // Tambahkan hasil ke tabel
                for (String row : results) {
                    // Pisah kolom CSV dengan aman dari tanda koma dalam kutipan
                    String[] parts = row.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (parts.length == 6) {
                        // Hapus tanda kutip dari tiap elemen
                        for (int i = 0; i < parts.length; i++) {
                            parts[i] = parts[i].replaceAll("^\"|\"$", "");
                        }
                        tableModel.addRow(parts);
                    }
                }

                resizeColumnWidth(table); // Atur ulang lebar kolom
                JOptionPane.showMessageDialog(null, "Jumlah data ditemukan: " + results.size());
            }
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null); // Tampilkan di tengah layar
        frame.setVisible(true);
    }

    // Fungsi untuk membaca file CSV dan masukkan ke Trie
    private static void loadData(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // Lewati header CSV

            while ((line = br.readLine()) != null) {
                String cleanLine = line.startsWith(",") ? line.substring(1) : line;
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    String model = parts[1].trim().toLowerCase(); // Ambil model kendaraan
                    trie.insert(model, cleanLine); // Simpan ke Trie
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Gagal membaca file: " + e.getMessage());
        }
    }

    // Fungsi untuk menyesuaikan lebar kolom tabel berdasarkan isi
    private static void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 75;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 10, width);
            }
            if (width > 300) width = 300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
}