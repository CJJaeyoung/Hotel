package hotel;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ModifyData extends JMenuItem {
    Connection con = null;
    PreparedStatement ppst = null;

    ModifyData(JTableData cjt){

        setText("수정");
        Font font = new Font("맑은 고딕", Font.PLAIN, 14);

        addActionListener(e -> {
            int row = cjt.getSelectedRow();
            String id1 = cjt.getDtm().getValueAt(row, 2).toString(); // 선택된 행의 id가져오기
            //수정 메뉴
            JLabel nameL2 = new JLabel("이름 : ");
            nameL2.setFont(font);
            JTextField nameT2 = new JTextField(12);
            JLabel idL2 = new JLabel("아이디 : ");
            idL2.setFont(font);
            JLabel idT2 = new JLabel(id1);
            JLabel emailL2 = new JLabel("이메일 : ");
            emailL2.setFont(font);
            JTextField emailT2 = new JTextField(12);
            JLabel telL2 = new JLabel("전화번호 : ");
            telL2.setFont(font);
            JTextField telT2 = new JTextField(12);
            JLabel birthL2 = new JLabel("생년월일 : ");
            birthL2.setFont(font);
            JTextField birthT2 = new JTextField(12);
            JLabel pwQL2 = new JLabel("본인확인 질문 : ");
            pwQL2.setFont(font);
            String[] pwQ2 = {"가장 존경하는 위인은?", "어릴 때 다니던 초등학교 이름은?", "좋아하는 동물은?", "기억에 남는 추억의 장소는?", "가장 좋아하는 음식은?"};
            JComboBox pwQCombo2 = new JComboBox(pwQ2);
            JLabel pwQAL2 = new JLabel("질문에 대한 답 : ");
            pwQAL2.setFont(font);
            JTextField pwQAT2 = new JTextField(12);

            JPanel editP = new JPanel();
            editP.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.anchor = GridBagConstraints.EAST;
            gbc.gridx = 0;
            gbc.gridy = 0;
            editP.add(nameL2, gbc);
            gbc.gridy = 1;
            editP.add(idL2, gbc);
            gbc.gridy = 2;
            editP.add(emailL2, gbc);
            gbc.gridy = 3;
            editP.add(telL2, gbc);
            gbc.gridy = 4;
            editP.add(birthL2, gbc);
            gbc.gridy = 5;
            editP.add(pwQL2, gbc);
            gbc.gridy = 6;
            editP.add(pwQAL2, gbc);

            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridx = 1;
            gbc.gridy = 0;
            editP.add(nameT2, gbc);
            gbc.gridy = 1;
            editP.add(idT2, gbc);
            gbc.gridy = 2;
            editP.add(emailT2, gbc);
            gbc.gridy = 3;
            editP.add(telT2, gbc);
            gbc.gridy = 4;
            editP.add(birthT2, gbc);
            gbc.gridy = 5;
            editP.add(pwQCombo2, gbc);
            gbc.gridy = 6;
            editP.add(pwQAT2, gbc);
            try {
                //기존 데이터를 jtf의 초기값으로 세팅해서
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
                ppst = con.prepareStatement("select * from hotelCustomertbl where id = ?;");
                ppst.setString(1, id1);
                ResultSet rs = ppst.executeQuery();

                if (rs.next()) {
                    nameT2.setText(rs.getString("name"));
                    emailT2.setText(rs.getString("email"));
                    telT2.setText(rs.getString("tel"));
                    birthT2.setText(rs.getString("birthDay"));
                }
                ppst = con.prepareStatement("select * from findPasswordQuestion where id = ?;");
                ppst.setString(1, id1);
                rs = ppst.executeQuery();
                if (rs.next()) {
                    pwQCombo2.setSelectedIndex(Integer.parseInt(rs.getString("qNumber")));
                    pwQAT2.setText(rs.getString("answer"));
                }
                while (true) {
                    //입력 값 받는 창 띄우기
                    int ok = JOptionPane.showConfirmDialog(null, editP, "edited data", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                    if (ok == 0) {//확인 버튼 눌리면
                        String name = nameT2.getText();
                        String email = emailT2.getText();
                        String tel = telT2.getText();
                        String birth = birthT2.getText();
                        String answer = pwQAT2.getText();
                        ppst = con.prepareStatement("SELECT * FROM hotelCustomertbl where id = ?;");
                        ppst.setString(1, id1);
                        rs = ppst.executeQuery();
                        if (rs.next()) {
                            if (!isValidName(name) || name.length() > 10) {
                                JOptionPane.showMessageDialog(null, "올바른 이름을 입력해주세요 (2-10자)", "invalid name", JOptionPane.INFORMATION_MESSAGE);
                            } else if (!isValidEmail(email) || email.length() > 20) {
                                JOptionPane.showMessageDialog(null, "올바른 이메일을 입력해주세요 (20자 이하)", "invalid email", JOptionPane.INFORMATION_MESSAGE);
                            } else if (!isValidTel(tel)) {
                                JOptionPane.showMessageDialog(null, "올바른 전화번호를 입력해주세요 (-없이)", "invalid tel", JOptionPane.INFORMATION_MESSAGE);
                            } else if (!isValidBirth2(birth)) {
                                JOptionPane.showMessageDialog(null, "올바른 생년월일 입력해주세요 (8자)", "invalid birth", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                ppst = con.prepareStatement("UPDATE hotelCustomertbl SET name = ?, email = ?, tel = ?, birthDay = ? WHERE id = ?;");
                                ppst.setString(1, name);
                                ppst.setString(2, email);
                                ppst.setString(3, tel);
                                ppst.setString(4, birth);
                                ppst.setString(5, id1);
                                ppst.executeUpdate();
                                cjt.getDtm().setRowCount(0);
                                cjt.loadAll();
                                JOptionPane.showMessageDialog(null, "수정 완료", "update", JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                        }
                    } else { //아니면
                        break;
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
                errorCheck();
            }
        });
    }
    private boolean isValidName(String name){
        //이름 유효성 검사 정규표현식
        String nameRegex = "^[가-힣]{2,10}$";
        return name.matches(nameRegex);
    }

    private boolean isValidEmail(String email) {
        // 이메일 유효성 검사 정규표현식
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidTel(String birth) {
        // 전화번호 유효성 검사 정규표현식
        String birthRegex = "^(\\+82|82|0)?(10|11|16|17|18|19|2|31|32|33|41|42|43|51|52|53|54|55|61|62|63|64)\\d{7,8}$";
        return birth.matches(birthRegex);
    }

    private boolean isValidBirth2(String birth) {
        // 생년월일 유효성 검사 정규표현식
        String birthRegex = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        return birth.matches(birthRegex);
    }

    private void errorCheck(){
        JOptionPane.showMessageDialog(null, "에러 발생 콘솔 확인", "exception error", JOptionPane.ERROR_MESSAGE);
    }
}
