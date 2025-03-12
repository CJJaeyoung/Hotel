package hotel;

import javax.swing.*;
import java.sql.*;

public class ResetPw extends JMenuItem {
    Connection con = null;
    PreparedStatement ppst = null;

    ResetPw(JTableData jtd){
        setText("비밀번호 초기화");
        addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(null,"비밀번호를 초기화 하시겠습니까?","pw reset",JOptionPane.OK_CANCEL_OPTION);
            if(ok==0) {
                int selectedRow = jtd.getSelectedRow();
                try {
                    con = DriverManager.getConnection("jdbc:mysql://localhost/hotel", "root", "1234");
                    String id = jtd.getDtm().getValueAt(selectedRow, 2).toString();
                    ppst = con.prepareStatement("UPDATE hotelCustomertbl SET pw = '123456789a' WHERE id = ?;"); //비밀번호 초기화
                    ppst.setString(1, id);
                    ppst.executeUpdate();
                    ppst = con.prepareStatement("SELECT * FROM hotelCustomertbl WHERE id = ?;"); //해당 id로 number가져오기
                    ppst.setString(1, id);
                    ResultSet rs = ppst.executeQuery();
                    if (rs.next()) { //number로 hashmap의 비밀번호 동기화
                        jtd.replaceValue(rs.getString("number"), "123456789a");
                        JOptionPane.showMessageDialog(null, "비밀번호 초기화 완료(123456789a)", "reset pw", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "에러 : 비밀번호 Map 동기화 실패");
                    }

                } catch (SQLException e1) {
                    e1.printStackTrace();
                    errorCheck();
                }
            }
        });
    }
    private void errorCheck(){
        JOptionPane.showMessageDialog(null, "에러 발생 콘솔 확인", "exception error", JOptionPane.ERROR_MESSAGE);
    }
}
