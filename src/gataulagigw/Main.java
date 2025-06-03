package gataulagigw;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class Main {
    private static Trie trie = new Trie();
    public static void main(String[] args) {
        loadData("src/data.csv");

        JFrame frame = new JFrame("Pencarian Model Kendaraan (Trie)");
        frame.setSize(1000, 600); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        JTextField inputField = new JTextField(40);
        JButton searchButton = new JButton("Cari");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputRow.add(inputField);
        inputRow.add(searchButton);

        inputPanel.add(inputRow);

        panel.add(inputPanel, BorderLayout.NORTH);

        String[] columnNames = {"nama_model", "brand", "tahun", "CC", "Transmisi", "Jumlah penumpang"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String keyword = inputField.getText().toLowerCase();
                List<String> results = trie.searchByPrefix(keyword);

                tableModel.setRowCount(0); // clear table

                for (String row : results) {
                    String[] parts = row.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (parts.length == 6) {
                        for (int i = 0; i < parts.length; i++) {
                            parts[i] = parts[i].replaceAll("^\"|\"$", "");
                        }
                        tableModel.addRow(parts);
                    }
                }

                resizeColumnWidth(table);
                JOptionPane.showMessageDialog(null, "Jumlah data ditemukan: " + results.size());
            }
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    private static void loadData(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String cleanLine = line.startsWith(",") ? line.substring(1) : line;
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    String model = parts[1].trim().toLowerCase();
                    trie.insert(model, cleanLine);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Gagal membaca file: " + e.getMessage());
        }
    }

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