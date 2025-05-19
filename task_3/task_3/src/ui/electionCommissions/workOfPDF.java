package ui.electionCommissions;

import service.electionCommissions.electionResultsExportService;
import service.electionCommissions.getElectionsListId;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class workOfPDF {

    public void workOfPDFPanel() {
        JFrame frame = new JFrame("Панель ЦИК");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton exportButtonMultiple = new JButton("Экспорт всех голосований в PDF");
        JButton exportButtonSelect = new JButton("Экспорт выбранных голосований в PDF");

        panel.add(exportButtonMultiple);
        panel.add(exportButtonSelect);

        frame.add(panel);
        frame.setVisible(true);


        exportButtonMultiple.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Сохранить как PDF");

                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!filePath.endsWith(".pdf")) {
                        filePath += ".pdf";
                    }

                    electionResultsExportService exportService = new electionResultsExportService();
                    exportService.exportAllResultsToPdf(filePath);

                    JOptionPane.showMessageDialog(null, "Все результаты успешно экспортированы в:\n" + filePath);
                }
            }
        });

        exportButtonSelect.addActionListener(e -> {
            List<String> electionIds = new getElectionsListId().getElectionIds();


            String[] electionsArray = electionIds.toArray(new String[0]);
            JList<String> list = new JList<>(electionsArray);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane scrollPane = new JScrollPane(list);
            scrollPane.setPreferredSize(new Dimension(250, 150));

            int result = JOptionPane.showConfirmDialog(null, scrollPane, "Выберите голосования", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                List<String> selected = list.getSelectedValuesList();
                if (!selected.isEmpty()) {
                    int exportOption = JOptionPane.showConfirmDialog(
                            null,
                            "Экспортировать все в один PDF файл?",
                            "Формат экспорта",
                            JOptionPane.YES_NO_OPTION
                    );

                    boolean saveInOneFile = (exportOption == JOptionPane.YES_OPTION);

                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Сохранить как PDF");

                    int userSelection = fileChooser.showSaveDialog(null);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                        if (!filePath.endsWith(".pdf")) {
                            filePath += ".pdf";
                        }

                        electionResultsExportService exportService = new electionResultsExportService();
                        exportService.exportSelectedResultsToPdf(filePath, selected, saveInOneFile);

                        JOptionPane.showMessageDialog(null, "Экспорт завершен!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Выберите хотя бы одно голосование.");
                }
            }
        });

    }
}
