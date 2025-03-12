package hotel;

import javax.swing.*;
import java.sql.*;

public class DeleteData extends JMenuItem {
    Connection con = null;
    PreparedStatement ppst = null;

    DeleteData(JTableData jtd){
        setText("삭제");
        addActionListener(e -> {
            int[] selectedRow = jtd.getSelectedRows();
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
                for (int i = selectedRow.length - 1; i >= 0; i--) {
                    int rowIndex = selectedRow[i];
                    String id = jtd.getDtm().getValueAt(rowIndex, 2).toString();
                    if (id.equals("admin")) {
                        JOptionPane.showMessageDialog(null, "관리자 계정은 삭제할 수 없습니다.", "no permission", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    } else {
                        int result = JOptionPane.showConfirmDialog(null, "정말로 삭제하시겠습니까?", "delete check", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE); //최종 확인
                        if(result == 0) {//ok를 누르면
                            //해당 id를 가진 number를 가져와서 pwMap에서 제거
                            ppst = con.prepareStatement("SELECT number FROM hotelCustomertbl WHERE id = ?;");
                            ppst.setString(1, id);
                            ResultSet rs = ppst.executeQuery();
                            String number = null;
                            if (rs.next()) {
                                number = rs.getString("number");
                            }
                            if (number != null) {
                                jtd.removeValue(number);
                            }
                            //모든 테이블에서 해당 id 데이터 제거

                            ppst = con.prepareStatement("DELETE FROM memotbl WHERE id = ?;");
                            ppst.setString(1, id);
                            ppst.executeUpdate();
                            ppst = con.prepareStatement("DELETE FROM findPasswordQuestion WHERE id = ?;");
                            ppst.setString(1, id);
                            ppst.executeUpdate();
                            ppst = con.prepareStatement("DELETE FROM hotelCustomertbl WHERE id = ?;");
                            ppst.setString(1, id);
                            ppst.executeUpdate();
                            jtd.getDtm().setRowCount(0); // JTable 동기화
                            jtd.loadAll();
                            JOptionPane.showMessageDialog(null, "삭제 완료", "delete", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                errorCheck();
            }
        });
    }
    private void errorCheck(){
        JOptionPane.showMessageDialog(null, "에러 발생 콘솔 확인", "exception error", JOptionPane.ERROR_MESSAGE);
    }
}
