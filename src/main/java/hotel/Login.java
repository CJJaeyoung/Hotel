package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Login extends JPanel {

    private Connection con = null;
    private PreparedStatement ppst = null;

    Login(JFrame frame){
        setLayout(new GridBagLayout());
        Font font = new Font("맑은 고딕",Font.PLAIN,15);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        JLabel lId = new JLabel("아이디 : ");
        lId.setFont(font);
        JLabel lPw = new JLabel("비밀번호 : ");
        lPw.setFont(font);
        JTextField id = new JTextField(20);
        JTextField pw = new JPasswordField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(lId,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(id,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(lPw,gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(pw,gbc);

        JButton loginB = new JButton("로그인");
        loginB.setFont(font);
        loginB.addActionListener(e -> {
            String inputId = id.getText();
            String inputPw = pw.getText();
            try{
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel","root","1234");
                ppst = con.prepareStatement("SELECT * FROM hotelCustomertbl WHERE id = ?;");
                ppst.setString(1,inputId);
                ResultSet rs = ppst.executeQuery();
                boolean check = true;

                while(rs.next()){
                    check = false;
                    if(rs.getString("number").equals("1") && inputPw.equals(rs.getString("pw"))){
                        JOptionPane.showMessageDialog(null, "관리자 계정으로 로그인합니다.", "login success", JOptionPane.INFORMATION_MESSAGE);
                        AdminPage adminPage = new AdminPage(frame);
                        frame.setContentPane(adminPage);
                        frame.revalidate();
                        frame.repaint();
                    }
                    else if(inputPw.equals(rs.getString("pw"))){
                        JOptionPane.showMessageDialog(null, "로그인 성공!", "login success", JOptionPane.INFORMATION_MESSAGE);
                        Main main = new Main(inputId,frame);
                        frame.setContentPane(main);
                        frame.revalidate();
                        frame.repaint();
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "비밀번호가 잘못되었습니다.", "login failed", JOptionPane.INFORMATION_MESSAGE);
                        pw.setText("");
                    }
                }

                if(check){
                    JOptionPane.showMessageDialog(null, "해당하는 아이디를 찾을 수 없습니다.", "id not found", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                errorCheck();
            }
        });

        pw.addActionListener(e -> loginB.doClick());

        JButton newAccountB = new JButton("회원가입");
        newAccountB.setFont(font);
        newAccountB.addActionListener(e->{
            Sign_up signUp = new Sign_up(frame);
            frame.setContentPane(signUp);
            frame.revalidate();
            frame.repaint();
        });
        JPanel jp = new JPanel();
        jp.setLayout(new FlowLayout());
        jp.add(loginB);
        jp.add(newAccountB);
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(jp,gbc);


        //아이디 찾기
        JPanel findId = new JPanel(new GridBagLayout());
        JLabel nameJL = new JLabel("이름 : ");
        JTextField nameJtf = new JTextField(10);
        JLabel birthJL = new JLabel("생년월일 : ");
        JTextField birthJtf = new JTextField(10);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 0;
        gbc.gridx = 0;
        findId.add(nameJL,gbc);
        gbc.gridx = 1;
        findId.add(nameJtf,gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1;
        gbc.gridx = 0;
        findId.add(birthJL,gbc);
        gbc.gridx = 1;
        findId.add(birthJtf,gbc);

        JLabel forgetId = new JLabel("<html><a href='' style='text-decoration:none'>아이디</a></html>");
        forgetId.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgetId.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                while (true) {
                    int result = JOptionPane.showConfirmDialog(null, findId, "find id", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == 0) {
                        if(!isValidName(nameJtf.getText()) && nameJtf.getText().length()<=10){
                            JOptionPane.showMessageDialog(null, "올바른 이름을 입력하세요 (한글 2-10자)");
                        }
                        else{
                            if(!isValidBirth(birthJtf.getText())){
                                JOptionPane.showMessageDialog(null, "올바른 생년월일을 입력하세요 (8자)");
                            }
                            else{
                                try {
                                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
                                    ppst = con.prepareStatement("SELECT * FROM hotelCustomertbl WHERE name = ?;");
                                    ppst.setString(1, nameJtf.getText());
                                    ResultSet rs = ppst.executeQuery();
                                    if(rs.next()) { //해당 이름으로 가입된 아이디가 있으면서 생년월일이 일치하면
                                        while(true) {
                                            char getCharId;
                                            String getId = "";
                                            for (int i = 0; i < rs.getString("id").length(); i++) {
                                                if (i < 3) {
                                                    getCharId = rs.getString("id").charAt(i);
                                                    getId += getCharId;
                                                } else if (i >= 3 && i <= 6) {
                                                    getId += "*";
                                                } else {
                                                    getCharId = rs.getString("id").charAt(i);
                                                    getId += getCharId;
                                                }

                                            }
                                            JOptionPane.showMessageDialog(null, "가입된 아이디는 " + getId + " 입니다.");
                                            nameJtf.setText("");
                                            birthJtf.setText("");
                                            break;
                                        }
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(null, "해당하는 이름으로 가입된 아이디가 없습니다.");
                                    }
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                    errorCheck();
                                }
                            }
                        }
                    }
                    else{
                        nameJtf.setText("");
                        birthJtf.setText("");
                        break;
                    }
                }
            }
        });

        //비밀번호 찾기
        JPanel findPw = new JPanel(new GridBagLayout());
        JLabel inputId = new JLabel("아이디 : ");
        JTextField idJtf = new JTextField(18);
        JLabel emailJL1 = new JLabel("이메일 : ");
        JTextField emailJtf1 = new JTextField(18);
        JLabel pwQL = new JLabel("본인 확인 질문 : ");
        String[] pwQ = {"가장 존경하는 위인은?","어릴 때 다니던 초등학교 이름은?","좋아하는 동물은?","기억에 남는 추억의 장소는?","가장 좋아하는 음식은?"};
        JComboBox pwQCombo = new JComboBox(pwQ);
        JLabel pwA = new JLabel("질문에 대한 답변 : ");
        JTextField pwQJtf = new JTextField(18);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        findPw.add(inputId, gbc);

        gbc.gridy = 1;
        findPw.add(emailJL1, gbc);

        gbc.gridy = 2;
        findPw.add(pwQL,gbc);

        gbc.gridy = 3;
        findPw.add(pwA, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 0;
        findPw.add(idJtf, gbc);

        gbc.gridy = 1;
        findPw.add(emailJtf1, gbc);

        gbc.gridy = 2;
        findPw.add(pwQCombo,gbc);

        gbc.gridy = 3;
        findPw.add(pwQJtf,gbc);

        //비밀번호 변경창
        JPanel newPwWindow = new JPanel(new GridBagLayout());
        JLabel newPw = new JLabel("비밀번호 : ");
        JTextField newPwJtf = new JPasswordField(15);
        JLabel newPwCheck = new JLabel("비밀번호 확인 : ");
        JTextField newPwCheckJtf = new JPasswordField(15);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        newPwWindow.add(newPw, gbc);
        gbc.gridy = 2;
        newPwWindow.add(newPwCheck, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        newPwWindow.add(newPwJtf, gbc);
        gbc.gridy = 2;
        newPwWindow.add(newPwCheckJtf, gbc);

        JLabel forgetPw = new JLabel("<html><a href='' style='text-decoration:none'>비밀번호 찾기</a></html>");
        forgetPw.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgetPw.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                while (true) {
                    int result = JOptionPane.showConfirmDialog(null, findPw, "find pw", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == 0) {
                        try {
                            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
                            ppst = con.prepareStatement("SELECT * FROM hotelCustomertbl WHERE id = ?;");
                            ppst.setString(1, idJtf.getText());
                            ResultSet rs = ppst.executeQuery();
                            if (rs.next()) { //id 체크
                                ppst = con.prepareStatement("SELECT * FROM hotelCustomertbl WHERE email = ?;");
                                ppst.setString(1,emailJtf1.getText());
                                rs = ppst.executeQuery();
                                if(rs.next()){ //이메일 체크
                                    ppst = con.prepareStatement("SELECT * FROM findPasswordQuestion WHERE id = ?;");
                                    ppst.setString(1,idJtf.getText());
                                    rs = ppst.executeQuery();
                                    if(rs.next() && rs.getString("qNumber").equals(pwQCombo.getSelectedIndex()+"") && rs.getString("answer").equals(pwQJtf.getText())){ //질의응답 체크
                                        JOptionPane.showMessageDialog(null,"비밀번호를 변경해주세요.");
                                        while(true){
                                            int result2 = JOptionPane.showConfirmDialog(null, newPwWindow,"change pw",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE); // 비밀번호 변경창
                                            if(result2 == 0){//ok누르면
                                                if (isValidPassword(newPwJtf.getText()) && newPwJtf.getText().length() <= 16) { // 비밀번호 정규식 체크
                                                    if (newPwJtf.getText().equals(newPwCheckJtf.getText())) { //비밀번호, 비번확인이 같으면
                                                        ppst = con.prepareStatement("UPDATE hotelCustomertbl SET pw = ? WHERE id = ?;");
                                                        ppst.setString(1,newPwJtf.getText());
                                                        ppst.setString(2,idJtf.getText());
                                                        ppst.executeUpdate();
                                                        JOptionPane.showMessageDialog(null, "비밀번호가 변경되었습니다.");
                                                        idJtf.setText("");
                                                        emailJtf1.setText("");
                                                        pwQCombo.setSelectedIndex(0);
                                                        pwQJtf.setText("");
                                                        break;
                                                    } else { //다르면
                                                        JOptionPane.showMessageDialog(null, "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                                                    }
                                                }
                                                else{
                                                    JOptionPane.showMessageDialog(null, "올바른 비밀번호를 입력해주세요 (8~16자, 영어 대소문자, 숫자, 특수문자 포함)", "Invalid Password", JOptionPane.INFORMATION_MESSAGE);
                                                }
                                            }
                                            else{//x나 취소 누르면
                                                newPwJtf.setText("");
                                                newPwCheckJtf.setText("");
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(null, "본인 확인 질의응답이 올바르지 않습니다.");
                                    }
                                }
                                else{
                                    JOptionPane.showMessageDialog(null, "이메일이 올바르지 않습니다.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "해당하는 아이디가 없습니다.");
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                            errorCheck();
                        }
                    }
                    else{ //ok 이외 누르면 창 반복문 종료
                        idJtf.setText("");
                        emailJtf1.setText("");
                        pwQCombo.setSelectedIndex(0);
                        pwQJtf.setText("");
                        break;
                    }
                }
            }
        });

        JPanel findP = new JPanel();
        JLabel slash = new JLabel("/");
        findP.add(forgetId);
        findP.add(slash);
        findP.add(forgetPw);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(findP, gbc);

    }

    private void errorCheck(){
        JOptionPane.showMessageDialog(null, "에러 발생 콘솔 확인", "exception error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isValidPassword(String pw) {
        // 비밀번호 영어 대, 소문자, 숫자, 특수문자 포함 8자 이상 16자 이하
        String pwRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,16}$";
        return pw.matches(pwRegex);
    }
    public boolean isValidBirth(String birth) {
        // 생년월일 유효성 검사 정규표현식
        String birthRegex = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$";
        return birth.matches(birthRegex);
    }
    private boolean isValidName(String name){
        //이름 유효성 검사 정규표현식
        String nameRegex = "^[가-힣]{2,10}$";
        return name.matches(nameRegex);
    }
}
