package hotel;

import javax.swing.*;
import java.sql.*;

public class MemoData extends JMenuItem {
    Connection con = null;
    PreparedStatement ppst = null;

    MemoData(JTableData jtd){
        setText("메모");
        addActionListener(e -> {
            int selectedRow = jtd.getSelectedRow();
            try {
                String id = jtd.getDtm().getValueAt(selectedRow, 2).toString();
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
                ppst = con.prepareStatement("SELECT memo FROM memotbl WHERE id = ?;");
                ppst.setString(1, id);
                ResultSet rs = ppst.executeQuery();
                String memo;
                if (rs.next()) {
                    memo = rs.getString("memo");
                } else {
                    memo = "";
                }
                JTextArea jta = new JTextArea(10, 30);
                jta.setText(memo);
                jta.setLineWrap(true);
                jta.setWrapStyleWord(true);
                JScrollPane sp = new JScrollPane(jta);
                int result = JOptionPane.showConfirmDialog(null, sp, "memo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == 0) { // ok를 눌렀을 때
                    String update = jta.getText();
                    ppst = con.prepareStatement("UPDATE memotbl SET memo = ? WHERE id = ?;");
                    ppst.setString(1, update);
                    ppst.setString(2, id);
                    ppst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "저장 완료");
                }

            } catch (SQLException e1) {
                e1.printStackTrace();
                errorCheck();
            }
        });
    }
    private void errorCheck(){
        JOptionPane.showMessageDialog(null, "에러 발생 콘솔 확인", "exception error", JOptionPane.ERROR_MESSAGE);
    }
}
