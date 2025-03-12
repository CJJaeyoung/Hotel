package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class HistoryData extends JMenuItem {
    Connection con = null;
    PreparedStatement ppst = null;

    HistoryData(JTableData jtd){
        setText("이력 조회");
        addActionListener(e -> {
            int seteledRow = jtd.getSelectedRow();
            String[] header = {"예약자", "연락처", "이메일", "체크인 날짜", "체크아웃 날짜", "방번호"};
            DefaultTableModel historyDTM = new DefaultTableModel(header, 0);

            try {
                String tel = jtd.getDtm().getValueAt(seteledRow, 5).toString();
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
                ppst = con.prepareStatement("SELECT * FROM v_checkoutdb WHERE tel = ?;");
                ppst.setString(1, tel);
                ResultSet rs = ppst.executeQuery();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getString("rName"));
                    row.add(rs.getString("tel"));
                    row.add(rs.getString("email"));
                    row.add(rs.getString("reserveDate"));
                    row.add(rs.getString("endDate"));
                    row.add(rs.getString("roomNo"));
                    historyDTM.addRow(row);
                }
                JTable historyTBL = new JTable(historyDTM);
                historyTBL.setPreferredScrollableViewportSize(new Dimension(800, 400));
                TableColumn hiscol;
                for (int i = 0; i < historyTBL.getColumnCount(); i++) {
                    hiscol = historyTBL.getColumnModel().getColumn(i);
                    if (i == 0) {
                        hiscol.setPreferredWidth(100);
                    } else if (i == 1) {
                        hiscol.setPreferredWidth(100);
                    } else if (i == 2) {
                        hiscol.setPreferredWidth(250);
                    } else if (i == 3) {
                        hiscol.setPreferredWidth(150);
                    } else if (i == 4) {
                        hiscol.setPreferredWidth(150);
                    } else if (i == 5) {
                        hiscol.setPreferredWidth(150);
                    } else {
                        hiscol.setPreferredWidth(50);
                    }
                }

                JScrollPane jsp = new JScrollPane(historyTBL);
                jsp.setPreferredSize(new Dimension(800, 400));
                JOptionPane.showMessageDialog(null, jsp, "숙박 이력", JOptionPane.PLAIN_MESSAGE);
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
