package hotel;

import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class AdminPage extends JPanel {

    private Connection con = null;
    private PreparedStatement ppst = null;

    AdminPage(JFrame frame) {
        Font font = new Font("맑은 고딕", Font.PLAIN, 14);
        JLabel topLabel = new JLabel("호텔 고객 관리 프로그램");
        topLabel.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        add(topLabel);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JTableData jTableData = new JTableData();

        jTableData.loadAll();
        jTableData.setPreferredScrollableViewportSize(new Dimension(900, 330));
        JScrollPane scrollPane = new JScrollPane(jTableData);
        scrollPane.setPreferredSize(new Dimension(900, 330)); // 테이블 화면 설정
        add(scrollPane);

        AddData addData = new AddData(jTableData); // 추가 버튼
        FindData findData = new FindData(jTableData); // 이름 검색 버튼

        String imagePath = "C:\\Java_Hotel_final_ver_1.11\\Hotel\\src\\main\\resources\\images\\";
        ImageIcon icon1 = new ImageIcon(imagePath+"iconPrint.png");
        ImageIcon icon2 = new ImageIcon(imagePath+"iconReserve.png");
        ImageIcon icon3 = new ImageIcon(imagePath+"iconOut.png");


        JButton printB = new JButton(icon1); // 전체 출력 버튼
        printB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        printB.setText("");
        printB.setBorderPainted(false);
        printB.setContentAreaFilled(false);
        printB.setFocusPainted(false);
        printB.setPreferredSize(new Dimension(icon1.getIconWidth(),icon1.getIconHeight()));
        printB.addActionListener(e -> {
            jTableData.loadAll();
        });

        JButton reserveB = new JButton(icon2); // 예약관리 버튼
        reserveB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        reserveB.setText("");
        reserveB.setBorderPainted(false);
        reserveB.setContentAreaFilled(false);
        reserveB.setFocusPainted(false);
        reserveB.setPreferredSize(new Dimension(icon2.getIconWidth(),icon2.getIconHeight()));
        reserveB.addActionListener(e -> {
            new HotelReservationGUI();
        });

        JButton loginB = new JButton(icon3); //로그인 화면 버튼
        loginB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginB.setText("");
        loginB.setBorderPainted(false);
        loginB.setContentAreaFilled(false);
        loginB.setFocusPainted(false);
        loginB.setPreferredSize(new Dimension(icon3.getIconWidth(),icon3.getIconHeight()));
        loginB.addActionListener(e -> {
            Login login = new Login(frame);
            frame.setContentPane(login);
            frame.revalidate();
            frame.repaint();
        });

        JPanel buttonP = new JPanel();
        buttonP.setFont(font);
        buttonP.add(addData);
        buttonP.add(printB);
        buttonP.add(findData);
        buttonP.add(reserveB);
        buttonP.add(loginB);
        add(buttonP);

        //테이블 우클릭 시 팝업메뉴
        ModifyData modifyData = new ModifyData(jTableData); // 수정
        DeleteData deleteData = new DeleteData(jTableData); //삭제
        MemoData memoData = new MemoData(jTableData); //메모
        ResetPw resetPw = new ResetPw(jTableData); //비밀번호 초기화
        HistoryData historyData = new HistoryData(jTableData); //이력 조회

        modifyData.setFont(font);
        deleteData.setFont(font);
        memoData.setFont(font);
        resetPw.setFont(font);
        historyData.setFont(font);

        JPopupMenu tableMenu = new JPopupMenu();
        tableMenu.add(modifyData);
        tableMenu.add(deleteData);
        tableMenu.add(memoData);
        tableMenu.add(resetPw);
        tableMenu.add(historyData);

        jTableData.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { handleMouseEvent(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseEvent(e);
            }
            private void handleMouseEvent(MouseEvent e){
                if(e.isPopupTrigger()){
                    int row = jTableData.rowAtPoint(e.getPoint()); //클릭한 위치의 행 인덱스 가져오기

                    if(row != -1){
                        jTableData.setRowSelectionInterval(row,row); //클릭한 위치 행 선택
                    }
                    else{
                        return;
                    }

                    int selectedRow = jTableData.getSelectedRow();
                    if(e.isPopupTrigger() && selectedRow != -1) {
                        if (selectedRow >= 0 && selectedRow < jTableData.getRowCount()) {
                            boolean isEmptyRow = true;
                            for (int i = 0; i < jTableData.getColumnCount(); i++) {
                                if (jTableData.getValueAt(selectedRow, i).toString().isEmpty()) { //해당 행의 모든 열을 대상으로 빈칸 체크
                                    isEmptyRow = false;
                                    break;
                                }
                            }
                            if (isEmptyRow) {// 내용이 없는 행을 선택하면 메뉴가 나오지 않음
                                tableMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    }
                }
            }
        });

    }
}