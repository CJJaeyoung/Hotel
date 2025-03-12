package hotel;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddData extends JButton{
    Connection con = null;
    PreparedStatement ppst = null;

    AddData(JTableData cjt){
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        String imagePath = "C:\\Java_Hotel_final_ver_1.11\\Hotel\\src\\main\\resources\\images\\";
        ImageIcon icon = new ImageIcon(imagePath+"iconAdd.png");
        setIcon(icon);
        setText("");
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setPreferredSize(new Dimension(icon.getIconWidth(),icon.getIconHeight()));
        Font font = new Font("맑은 고딕", Font.PLAIN, 14);

        JLabel nameL = new JLabel("이름 : ");
        nameL.setFont(font);
        JTextField nameT = new JTextField(12);
        JLabel idL = new JLabel("아이디 : ");
        idL.setFont(font);
        JTextField idT = new JTextField(12);
        JLabel pwL = new JLabel("비밀번호 : ");
        pwL.setFont(font);
        JTextField pwT = new JPasswordField(12);
        JLabel emailL = new JLabel("이메일 : ");
        emailL.setFont(font);
        JTextField emailT = new JTextField(12);
        JLabel telL = new JLabel("전화번호 : ");
        telL.setFont(font);
        JTextField telT = new JTextField(12);
        JLabel birthL = new JLabel("생년월일 : ");
        birthL.setFont(font);
        JTextField birthT = new JTextField(12);
        JLabel pwQL = new JLabel("본인확인 질문 : ");
        pwQL.setFont(font);
        String[] pwQ = {"가장 존경하는 위인은?", "어릴 때 다니던 초등학교 이름은?", "좋아하는 동물은?", "기억에 남는 추억의 장소는?", "가장 좋아하는 음식은?"};
        JComboBox pwQCombo = new JComboBox(pwQ);
        JLabel pwQAL = new JLabel("질문에 대한 답 : ");
        pwQAL.setFont(font);
        JTextField pwQAT = new JTextField(12);

        JPanel inputP = new JPanel();
        inputP.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputP.add(nameL, gbc);
        gbc.gridy = 1;
        inputP.add(idL, gbc);
        gbc.gridy = 2;
        inputP.add(pwL, gbc);
        gbc.gridy = 3;
        inputP.add(emailL, gbc);
        gbc.gridy = 4;
        inputP.add(telL, gbc);
        gbc.gridy = 5;
        inputP.add(birthL, gbc);
        gbc.gridy = 6;
        inputP.add(pwQL, gbc);
        gbc.gridy = 7;
        inputP.add(pwQAL, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        inputP.add(nameT, gbc);
        gbc.gridy = 1;
        inputP.add(idT, gbc);
        gbc.gridy = 2;
        inputP.add(pwT, gbc);
        gbc.gridy = 3;
        inputP.add(emailT, gbc);
        gbc.gridy = 4;
        inputP.add(telT, gbc);
        gbc.gridy = 5;
        inputP.add(birthT, gbc);
        gbc.gridy = 6;
        inputP.add(pwQCombo, gbc);
        gbc.gridy = 7;
        inputP.add(pwQAT, gbc);
        addActionListener(e -> {
            while (true) {
                int result = JOptionPane.showConfirmDialog(null, inputP, "Form", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) { // OK 버튼 클릭 시
                    try {
                        // 입력값 유효성 검사
                        if (!isValidName(nameT.getText()) || nameT.getText().length() > 10) {
                            JOptionPane.showMessageDialog(null, "올바른 이름을 입력해주세요 (10자 이하 한글)", "Invalid Name", JOptionPane.INFORMATION_MESSAGE);
                        } else if (!isValidId(idT.getText()) || idT.getText().length() > 16) {
                            JOptionPane.showMessageDialog(null, "올바른 아이디를 입력해주세요 (4-16자, 영, 숫자만 가능", "Invalid ID", JOptionPane.INFORMATION_MESSAGE);
                        } else if (!isValidPassword(pwT.getText()) || pwT.getText().length() > 16) {
                            JOptionPane.showMessageDialog(null, "올바른 비밀번호를 입력해주세요 (8~16자, 영어 대소문자, 숫자, 특수문자 포함)", "Invalid Password", JOptionPane.INFORMATION_MESSAGE);
                        } else if (!isValidEmail(emailT.getText()) || emailT.getText().length() > 20) {
                            JOptionPane.showMessageDialog(null, "올바른 이메일을 입력해주세요 (20자 이하)", "Invalid Email", JOptionPane.INFORMATION_MESSAGE);
                        } else if (!isValidTel(telT.getText())) {
                            JOptionPane.showMessageDialog(null, "올바른 전화번호를 입력해주세요 (-없이)", "Invalid Tel", JOptionPane.INFORMATION_MESSAGE);
                        } else if (!isValidBirth(birthT.getText())) {
                            JOptionPane.showMessageDialog(null, "올바른 생년월일을 입력해주세요 (8자리)", "Invalid Birth", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else if(pwQAT.getText().isEmpty()){
                            JOptionPane.showMessageDialog(null,"본인확인용 질문에 대한 답을 입력해주세요","need answer", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            // 모든 유효성 검사 통과

                            // 중복 체크
                            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
                            ppst = con.prepareStatement("SELECT * FROM hotelCustomertbl WHERE id = ?;");
                            ppst.setString(1, idT.getText());
                            ResultSet rs = ppst.executeQuery();

                            if (rs.next()) { //반환값이 있으면 중복
                                JOptionPane.showMessageDialog(null, "이미 존재하는 아이디입니다.", "Duplicated ID", JOptionPane.INFORMATION_MESSAGE);
                            } else { // 아니면 중복 없음
                                ppst = con.prepareStatement("INSERT INTO hotelCustomertbl (name, id, pw, email, tel, birthDay) VALUES (?, ?, ?, ?, ?, ?);");
                                ppst.setString(1, nameT.getText());
                                ppst.setString(2, idT.getText());
                                ppst.setString(3, pwT.getText());
                                ppst.setString(4, emailT.getText());
                                ppst.setString(5, telT.getText());
                                ppst.setString(6, birthT.getText());
                                ppst.executeUpdate();
                                ppst = con.prepareStatement("INSERT INTO memotbl (id) VALUES (?);");
                                ppst.setString(1, idT.getText());
                                ppst.executeUpdate();
                                ppst = con.prepareStatement("INSERT INTO findPasswordQuestion (id,qNumber,answer) VALUES (?,?,?);");
                                ppst.setString(1, idT.getText());
                                ppst.setString(2, pwQCombo.getSelectedIndex() + "");
                                ppst.setString(3, pwQAT.getText());
                                ppst.executeUpdate();
                                ppst = con.prepareStatement("SELECT * FROM hotelCustomertbl where id = ?;");
                                ppst.setString(1, idT.getText());
                                rs = ppst.executeQuery();
                                if (rs.next()) {
                                    cjt.putValue(rs.getString("number"), rs.getString("pw")); //비번 맵에 추가
                                } else {
                                    JOptionPane.showMessageDialog(null, "에러 : 비밀번호 Map 저장 실패");
                                }
                                JOptionPane.showMessageDialog(null, "추가 완료", "Insert", JOptionPane.INFORMATION_MESSAGE);

                                // 입력값 초기화 및 JTabel 데이터 동기화
                                nameT.setText("");
                                idT.setText("");
                                pwT.setText("");
                                emailT.setText("");
                                telT.setText("");
                                birthT.setText("");
                                pwQCombo.setSelectedIndex(0);
                                pwQAT.setText("");
                                cjt.getDtm().setRowCount(0);
                                cjt.loadAll();

                                break;
                            }
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        errorCheck();
                    }
                } else {// Cancel 버튼 클릭 시 루프 종료
                    nameT.setText("");
                    idT.setText("");
                    pwT.setText("");
                    emailT.setText("");
                    telT.setText("");
                    birthT.setText("");
                    pwQCombo.setSelectedIndex(0);
                    pwQAT.setText("");
                    break;
                }
            }
        });
    }
    private boolean isValidName(String name){
        //이름 유효성 검사 정규표현식
        String nameRegex = "^[가-힣]{2,10}$";
        return name.matches(nameRegex);
    }

    private boolean isValidId(String id){
        //아이디는 영,숫자만 가능
        String idRegex = "^[A-Za-z0-9]{6,16}$";
        return id.matches(idRegex);
    }

    private boolean isValidPassword(String pw) {
        // 비밀번호 영어 대, 소문자, 숫자, 특수문자 포함 8자 이상 16자 이하
        String pwRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,16}$";
        return pw.matches(pwRegex);
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

    private boolean isValidBirth(String birth) {
        // 생년월일 유효성 검사 정규표현식
        String birthRegex = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$";
        return birth.matches(birthRegex);
    }

    private void errorCheck(){
        JOptionPane.showMessageDialog(null, "에러 발생 콘솔 확인", "exception error", JOptionPane.ERROR_MESSAGE);
    }
}
