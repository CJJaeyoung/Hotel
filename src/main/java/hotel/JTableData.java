package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;

public class JTableData extends JTable {

    HashMap<String, String> pwMap = new HashMap<>(); // number(key), pw(value) 담아두는 Map
    Connection con = null;
    PreparedStatement ppst = null;
    DefaultTableModel dtm;

    public void putValue(String key, String value){
        pwMap.put(key,value);
    }

    public void removeValue(String key){
        pwMap.remove(key);
    }

    public void replaceValue(String key, String value){
        pwMap.replace(key,value);
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }

    JTableData(){

        Font font = new Font("맑은 고딕", Font.PLAIN, 14);
        //JTable 만들기
        String[] columnNames = {"번호", "이름", "ID", "비밀번호", "이메일", "전화번호", "생년월일"};
        dtm = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) { // 셀 편집 불가
                return false;
            }
        }; // 헤더 설정
        this.setModel(dtm);

        setFont(font);
        setRowHeight(25);
        getTableHeader().setBackground(Color.LIGHT_GRAY);
        setGridColor(Color.GRAY);
        setSelectionBackground(Color.DARK_GRAY);
        setSelectionForeground(Color.WHITE);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // 다중선택 가능


        //각 열 너비 설정
        TableColumn column;
        for (int i = 0; i < getColumnCount(); i++) {
            column = getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(40);
            } else if (i == 1) {
                column.setPreferredWidth(70);
            } else if (i == 2) {
                column.setPreferredWidth(150);
            } else if (i == 3) {
                column.setPreferredWidth(100);
            } else if (i == 4) {
                column.setPreferredWidth(250);
            } else if (i == 5) {
                column.setPreferredWidth(150);
            } else {
                column.setPreferredWidth(150);
            }
        }
    }

    public void loadAll() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
            ppst = con.prepareStatement("SELECT * FROM hotelCustomertbl;");
            ResultSet rs = ppst.executeQuery();
            int number = 1;
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector<Object> row = new Vector<>(); //반복될 때마다 초기화
                row.add(number++);
                row.add(rs.getString("name"));
                row.add(rs.getString("id"));
                row.add("*******"); //비밀번호 마스킹
                row.add(rs.getString("email"));
                row.add(rs.getString("tel"));
                row.add(rs.getString("birthDay"));
                pwMap.put(rs.getString("number"),rs.getString("pw")); //비밀번호 맵에 number(키),pw(값) 등록
                dtm.addRow(row);
            }
            for (int i = 0; i < 12; i++) {//빈공간 채우기용 더미셀
                dtm.addRow(new Object[]{"", "", "", "", "", "", ""});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            errorCheck();
        }
    }
    private void errorCheck(){
        JOptionPane.showMessageDialog(null, "에러 발생 콘솔 확인", "exception error", JOptionPane.ERROR_MESSAGE);
    }
}
