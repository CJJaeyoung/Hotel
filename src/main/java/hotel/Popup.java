package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Pattern;

class Drop{
    private Customer customer;
    public Drop(Customer customer){
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
class DB {
    Connection con;

    public DB() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
        System.out.println("DB 연결 성공!");
    }
    public boolean deleteCustomer(String id, String password) throws SQLException {
        String dbPw = getPassword(id);
        if (dbPw != null && dbPw.equals(password)) {
            String query = "DELETE FROM hotelCustomertbl WHERE id = ?";
            String queryQ = "DELETE FROM findPasswordQuestion WHERE id = ?";
            String queryM = "DELETE FROM memotbl WHERE id = ?";
            try (var pstmt = con.prepareStatement(query);
                 var pstmtQ = con.prepareStatement(queryQ);
                 var pstmtM = con.prepareStatement(queryM)) {
                pstmtQ.setString(1, id);
                pstmt.setString(1, id);
                pstmtM.setString(1, id);
                pstmtQ.executeUpdate();
                pstmtM.executeUpdate();
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        }
        return false;
    }

    public String getPassword(String id) throws SQLException {
        String query = "SELECT pw FROM hotelCustomertbl WHERE id = ?";
        try (var pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, id);
            System.out.println("실행할 쿼리: " + pstmt.toString());
            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("pw");
                }
            }
        }
        return null;
    }
}
    interface ButtonPressCallback {
        void onButtonPressed();
    }

    public class Popup {
        private String id;

        public Popup(String id, ButtonPressCallback callback) {
            this.id = id;
            JFrame frame = new JFrame("회원탈퇴");
            frame.setSize(800, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setLocationRelativeTo(null);

            // 패널 생성 및 레이아웃 설정
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // 컴포넌트 간 여백
            gbc.fill = GridBagConstraints.HORIZONTAL;
            // 비밀번호 입력
            JLabel pwLabel = new JLabel("비밀번호:");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            panel.add(pwLabel, gbc);

            JPasswordField pwField = new JPasswordField(15);
            gbc.gridx = 1;
            gbc.gridy = 2;
            panel.add(pwField, gbc);

            // 비밀번호 확인 입력
            JLabel rePwLabel = new JLabel("비밀번호 확인:");
            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(rePwLabel, gbc);

            JPasswordField rePwField = new JPasswordField(15);
            gbc.gridx = 1;
            gbc.gridy = 3;
            panel.add(rePwField, gbc);

            // 버튼 패널
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(new Color(240, 248, 255)); // 배경색 통일
            JButton dropButton = new JButton("탈퇴");
            dropButton.setBackground(new Color(255, 99, 71)); // 탈퇴 버튼 색상
            dropButton.setForeground(Color.WHITE);
            dropButton.setFocusPainted(false);

            JButton preButton = new JButton("취소");
            preButton.setBackground(new Color(144, 238, 144)); // 취소 버튼 색상
            preButton.setFocusPainted(false);

            buttonPanel.add(dropButton);
            buttonPanel.add(preButton);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            panel.add(buttonPanel, gbc);

            // 버튼 동작 추가

            frame.add(panel);
            frame.setVisible(true);

            preButton.addActionListener(e -> {
                // 프레임 닫기
                frame.dispose();
            });
            // 패널을 프레임에 추가
            dropButton.addActionListener(e -> {
                String pw = new String(pwField.getPassword());
                String rePw = new String(rePwField.getPassword());
                if (!pw.equals(rePw)) {
                    System.out.println("DB 비밀번호: '" + pw + "', 입력 비밀번호: '" + rePw + "'");
                    JOptionPane.showMessageDialog(frame, "비밀번호가 일치하지 않습니다!", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    DB db = new DB();
                    if (db.deleteCustomer(id, pw)) {
                        JOptionPane.showMessageDialog(frame, "탈퇴 성공!");
                        frame.dispose();
                        callback.onButtonPressed();
                    } else {
                        JOptionPane.showMessageDialog(frame, "비밀번호가 올바르지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "DB 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

    }


