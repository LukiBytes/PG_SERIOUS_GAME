package ModelScore;

import ModelScore.EntryScore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ScoreView extends JDialog {

    private JTable scoreTable;
    private DefaultTableModel tableModel;

    public ScoreView(JFrame parent, List<EntryScore> scores) {
        super(parent, "Ranking wyników", true);

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(new BorderLayout());

        initTable(scores);
        initBottomPanel();

        setVisible(true);
    }

    private void initTable(List<EntryScore> scores) {
        String[] columns = {"Gracz", "Wynik", "Poziom"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // brak edycji
            }
        };

        for (EntryScore entry : scores) {
            tableModel.addRow(new Object[]{
                    entry.getPlayerName(),
                    entry.getScore(),
                    entry.getDifficulty()

            });
        }

        scoreTable = new JTable(tableModel);
        scoreTable.setRowHeight(24);
        scoreTable.setFont(new Font("Arial", Font.PLAIN, 14));
        scoreTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initBottomPanel() {
        JButton closeButton = new JButton("Zamknij");
        closeButton.addActionListener(e -> dispose());

        JPanel panel = new JPanel();
        panel.add(closeButton);

        add(panel, BorderLayout.SOUTH);
    }
}
