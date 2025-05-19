package ui.admin;

import dao.electionCommissionDeletionService;
import service.admin.loadElectionCommissionListService;

import javax.swing.*;
import java.awt.*;

public class electionCommissionManagementUI {
    public void openElectionCommissionManagementWindow() {
        JFrame frame = new JFrame("Управление ЦИК");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> commissionModel = new DefaultListModel<>();
        JList<String> commissionList = new JList<>(commissionModel);
        panel.add(new JScrollPane(commissionList), BorderLayout.CENTER);

        JButton deleteButton = new JButton("Удалить ЦИК");
        panel.add(deleteButton, BorderLayout.SOUTH);


        new loadElectionCommissionListService().loadElectionCommissions(commissionModel);


        deleteButton.addActionListener(e -> {
            String selectedCommission = commissionList.getSelectedValue();
            if (selectedCommission != null) {
                int confirm = JOptionPane.showConfirmDialog(frame, "Вы уверены, что хотите удалить " + selectedCommission + "?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    electionCommissionDeletionService.deleteElectionCommission(selectedCommission);
                    new loadElectionCommissionListService().loadElectionCommissions(commissionModel);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Выберите ЦИК для удаления", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
