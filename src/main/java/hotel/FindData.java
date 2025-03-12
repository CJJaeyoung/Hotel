package hotel;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class FindData extends JButton {

    Connection con = null;
    PreparedStatement ppst = null;

    FindData(JTableData cjt){
        String imagePath = "C:\\Java_Hotel_final_ver_1.11\\Hotel\\src\\main\\resources\\images\\";
        ImageIcon icon = new ImageIcon(imagePath+"iconSearch.png");
        setIcon(icon);
        setText("");
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setPreferredSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addActionListener(e -> {
            String inputName = JOptionPane.showInputDialog(null, "이름을 입력하세요", "find name", JOptionPane.QUESTION_MESSAGE);
            if (inputName != null) {
                try {
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
                    ppst = con.prepareStatement("SELECT * FROM hotelCustomertbl WHERE name like ?;");
                    ppst.setString(1, "%" + inputName + "%"); //부분 일치 검색
                    ResultSet rs = ppst.executeQuery();
                    int number = 1;

                    cjt.getDtm().setRowCount(0);

                    boolean check = true;

                    while (rs.next()) {
                        check = false;
                        Vector<Object> row = new Vector<>(); //반복될 때마다 초기화
                        row.add(number++);
                        row.add(rs.getString("name"));
                        row.add(rs.getString("id"));
                        row.add("*******");
                        row.add(rs.getString("email"));
                        row.add(rs.getString("tel"));
                        row.add(rs.getString("birthDay"));
                        cjt.getDtm().addRow(row);
                    }
                    if (check) {
                        JOptionPane.showMessageDialog(null, "해당하는 이름이 없습니다", "not found", JOptionPane.INFORMATION_MESSAGE);
                    }
                    for (int i = 0; i < 12; i++) {//빈공간 채우기용 더미셀
                        cjt.getDtm().addRow(new Object[]{"", "", "", "", "", "", ""});
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
