package hotel;

import javax.swing.*;
import java.awt.*;

public class Main extends JPanel {

    public Main(String userId, JFrame jFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 여백 설정

        // 이미지 파일을 ImageIcon 객체로 불러오기
        ImageIcon viewIcon = new ImageIcon(getClass().getResource("/images/view.png"));
        ImageIcon editIcon = new ImageIcon(getClass().getResource("/images/edit.png"));
        ImageIcon logOutIcon = new ImageIcon(getClass().getResource("/images/logOut.png"));

        // JLabel로 이미지 버튼 만들기
        JLabel viewButton = new JLabel(viewIcon);
        JLabel editButton = new JLabel(editIcon);
        JLabel logOutButton = new JLabel(logOutIcon);

        // 버튼처럼 동작하도록 클릭 이벤트 추가
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logOutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 버튼 크기 설정
        Dimension buttonSize = new Dimension(200, 100);  // 크기 설정
        viewButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        logOutButton.setPreferredSize(buttonSize);

        // 버튼 추가
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.CENTER;
        add(viewButton, gbc);

        gbc.gridx = 1;
        add(editButton, gbc);

        gbc.gridx = 2;
        add(logOutButton, gbc);

        jFrame.setResizable(false); // 창 크기 조정 불가
        jFrame.setExtendedState(JFrame.NORMAL); // 전체화면 불가

        // "예약 조회" 버튼 동작
        viewButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                HotelReservationGUI hotelGUI = new HotelReservationGUI();
                hotelGUI.updateButtonsForReservationView(); // "현재 객실 보기"와 "예약하기" 버튼만 표시
            }
        });
        // "정보 수정" 버튼 동작
        editButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Mypage mypage = new Mypage(userId, jFrame);
                jFrame.setContentPane(mypage);
                jFrame.revalidate();
                jFrame.repaint();
            }
        });
        // "로그아웃" 버튼 동작
        logOutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Login login = new Login(jFrame);
                jFrame.setContentPane(login);
                jFrame.revalidate();
                jFrame.repaint();
            }
        });
    }
}