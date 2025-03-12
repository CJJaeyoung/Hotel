package hotel;

import com.mysql.cj.log.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.*;
import java.time.YearMonth;
import java.util.regex.Pattern;


class Customer {
    private String name;
    private String id;
    private String pw;
    private String email;
    private String tel;
    private String birthDay;
    private String qNumber;
    private String answer;



    public Customer(String name, String id, String pw, String email, String tel, String birthDay,String qNumber,String answer ) {

        this.name = name;
        this.id = id;
        this.pw = pw;
        this.email = email;
        this.tel = tel;
        this.birthDay = birthDay;
        this.qNumber = qNumber;
        this.answer = answer;
    }
    public String getAnswer() {
        return answer;
    }

    public String getqNumber() {
        return qNumber;
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public String getPw() {
        return pw;
    }

    public String getEmail() {
        return email;
    }

    public String getTel() {
        return tel;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setName(String name) {
    }
}
class SignUpDB {
    private Connection con;
    public SignUpDB() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
        System.out.println("database 연결 성공!");
    }
    public void dataInsert(Customer customer) throws SQLException {
        String query = "INSERT INTO hotelCustomertbl (name, id, pw, email, tel, birthDay) VALUES (?, ?, ?, ?, ?, ?)";
        String queryQ = "INSERT INTO findPasswordQuestion (id, qNumber, answer) VALUES (?, ?, ?)";
        String queryM = "INSERT INTO memotbl (id) VALUES (?)";
        try {
            PreparedStatement stm = con.prepareStatement(query);
            PreparedStatement Qstm = con.prepareStatement(queryQ);
            PreparedStatement Mstm = con.prepareStatement(queryM);

            stm.setString(1, customer.getName());
            stm.setString(2, customer.getId());
            stm.setString(3, customer.getPw());
            stm.setString(4, customer.getEmail());
            stm.setString(5, customer.getTel());
            stm.setString(6, customer.getBirthDay());

            Qstm.setString(1,customer.getId());
            Qstm.setString(2, customer.getqNumber());
            Qstm.setString(3, customer.getAnswer());

            Mstm.setString(1,customer.getId());


            stm.executeUpdate();
            Qstm.executeUpdate();
            Mstm.executeUpdate();


            System.out.println("데이터 삽입 성공!");
        }catch(SQLException e) {
            System.err.println("데이터 삽입 오류: " + e.getMessage());
            throw e; // 오류를 상위 메서드로 전달
        }
    }

    public boolean idCheck(String id) throws SQLException {
        String query = "SELECT COUNT(*) FROM hotelCustomertbl WHERE id = ?";
        PreparedStatement stm = con.prepareStatement(query);  // 초기화
        stm.setString(1,id);
        ResultSet rs = stm.executeQuery();
        if(rs.next()){
            return rs.getInt(1)>0;
        }
        return false;
    }
}




public class Sign_up extends JPanel{
    Sign_up(JFrame frame) {

        // GridBagLayout과 GridBagConstraints 설정
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 컴포넌트 간 여백
        gbc.fill = GridBagConstraints.HORIZONTAL; // 가로로 확장

        // 컴포넌트 생성
        JLabel nameLabel = new JLabel("이름:");
        JTextField nameField = new JTextField(15);

        JLabel idLabel = new JLabel("아이디:");
        JTextField idField = new JTextField(15);
        JButton dupButton = new JButton("중복검사");

        JLabel pwLabel = new JLabel("비밀번호:");
        JTextField pwField = new JPasswordField(15);
        String placehoder = "대소문자,특수문자,15글자 이하 작성";
        pwField.setText(placehoder);
        pwField.setForeground(Color.GRAY);
        ((JPasswordField) pwField).setEchoChar((char) 0);

        JLabel passReLabel = new JLabel("비밀번호 재확인:");
        JTextField passReField = new JPasswordField(15);

        JLabel emailLabel = new JLabel("이메일:");
        JTextField emailField = new JTextField(15);

        JLabel telLabel = new JLabel("전화번호:");
        JTextField telField = new JTextField(15);

        JLabel birthDayLabel = new JLabel("생년월일:");
        JComboBox<Integer> year = new JComboBox<>();
        JComboBox<Integer> month = new JComboBox<>();
        JComboBox<Integer> day = new JComboBox<>();


        JLabel questionLabel = new JLabel("본인 확인 질문 :");
        JComboBox<String> questionComboBox = new JComboBox<>(new String[]{
                "가장 존경하는 위인은?",
                "어릴 때 다니던 초등학교 이름은?",
                "좋아하는 동물은?",
                "기억에 남는 추억의 장소는?",
                "가장 좋아하는 음식은?"
        });
        JLabel answerLabel = new JLabel("답변:");
        JTextField answerField = new JTextField(15);






        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        for (int i = 1940; i <= currentYear; i++) {
            year.addItem(i);
        }

        pwField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(((JPasswordField) pwField).getPassword()).equals(placehoder)) {
                    pwField.setText("");
                    pwField.setForeground(Color.BLACK); // 입력 시 검정색으로 바뀜
                    ((JPasswordField) pwField).setEchoChar('•');
                }

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(((JPasswordField) pwField).getPassword()).isEmpty()) {
                    ((JPasswordField) pwField).setEchoChar((char) 0);
                    pwField.setText(placehoder);
                    pwField.setForeground(Color.GRAY); // 플레이스홀더 색상
                }
            }
        });

        passReField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(((JPasswordField) passReField).getPassword()).equals(placehoder)) {
                    passReField.setText("");  // 텍스트 필드를 비우고
                    passReField.setForeground(Color.BLACK);  // 입력 시 검정색으로 변경
                    ((JPasswordField) passReField).setEchoChar('•');  // 특수문자 ⦁로 표시
                }

            }

            @Override
            public void focusLost(FocusEvent e) {


            }
        });

// 월 초기화: 1~12
        for (int i = 1; i <= 12; i++) {
            month.addItem(i);
        }

// 일 초기화: 기본값은 1~31
        for (int i = 1; i <= 31; i++) {
            day.addItem(i);
        }
        year.addActionListener(e -> updateDays(year, month, day));
        month.addActionListener(e -> updateDays(year, month, day));
        day.addActionListener(e -> updateDays(year, month, day));
        JButton submitButton = new JButton("회원가입");
        gbc.insets = new Insets(5,5,5,5);


        panel.setBorder(BorderFactory.createEmptyBorder(65, 10, 10, 10)); // 상단 50px 여백 추가


        // 컴포넌트 배치
        gbc.gridx = 0; gbc.gridy = 0; // 첫 번째 열, 첫 번째 행
        panel.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(idLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(idField, gbc);

        gbc.gridx = 2; gbc.gridy = 1; // 세 번째 열
        panel.add(dupButton, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(pwLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(pwField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(passReLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(passReField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(telLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(telField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(birthDayLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 6; panel.add(birthDayLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 6; panel.add(year, gbc);
        gbc.gridx = 2; gbc.gridy = 6; panel.add(month, gbc);
        gbc.gridx = 3; gbc.gridy = 6; panel.add(day, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(questionLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 7;
        panel.add(questionComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(answerLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 8;
        panel.add(answerField, gbc);

        gbc.gridx = 1; gbc.gridy = 9; // 버튼을 중앙 정렬
        panel.add(submitButton, gbc);
        // 패널 추가 및 창 표시
        add(panel);
        setVisible(true);
        dupButton.addActionListener(e->{
            try {
                String id = idField.getText().trim();
                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "아이디를 입력하세요.");
                    idField.requestFocus();
                    return;
                }
                if(id.length()<6||id.length()>16){
                    JOptionPane.showMessageDialog(frame, "아이디는 6자 이상 16자 이하로 입력하세요.");
                    idField.requestFocus();
                    return;
                }
                if (!id.matches("[a-zA-Z0-9]+")) {
                    JOptionPane.showMessageDialog(frame, "아이디는 영문자와 숫자만 포함할 수 있습니다.");
                    idField.setText("");
                    idField.requestFocus();
                    return;
                }
                SignUpDB db = new SignUpDB();
                if(db.idCheck(id)){
                    JOptionPane.showMessageDialog(frame, "이미 사용 중인 아이디입니다.");
                    telField.setText("");
                    telField.requestFocus();
                }else{
                    JOptionPane.showMessageDialog(frame, "사용 가능한 아이디입니다.");

                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "DB 오류: " + ex.getMessage());
            }
        });
        submitButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String id = idField.getText();
                String pw = pwField.getText();
                String email = emailField.getText();
                String answer = answerField.getText();
                String qNumber = String.valueOf(questionComboBox.getSelectedIndex()+1); //질문 번호 (0이 아닌 1부터 )
                String tel = telField.getText();
                String birthDay = String.format("%04d-%02d-%02d",
                        year.getSelectedItem(), month.getSelectedItem(), day.getSelectedItem());
                if(name.isEmpty()||tel.isEmpty()||name.isEmpty()||id.isEmpty()||pw.isEmpty()||birthDay.isEmpty()
                ||answer.isEmpty()){
                    JOptionPane.showMessageDialog(frame, "모든 필드를 입력하세요");
                    return;
                }
                if(!isValidName(name)){
                    JOptionPane.showMessageDialog(frame, "이름은 10자 이하 한글로 입력해주세요");
                    return;
                }
                if(!isValidId(id)){
                    JOptionPane.showMessageDialog(frame, "아이디는 6글자이상 16글자 이하로 써주세요");
                    return;
                }
                if(!isValidPw(pw)){
                    JOptionPane.showMessageDialog(frame, "비밀번호는 대소문자,숫자 8자 이상 16자 이하, 특수문자를 포함해야합니다.");
                    pwField.setText("");
                    pwField.requestFocus();
                    return;
                }if(!pw.equals(passReField.getText())){
                    JOptionPane.showMessageDialog(frame, "비밀번호가 일치하지 않습니다.");
                    pwField.setText("");
                    pwField.requestFocus();
                    return;
                }
                if (!isValidTel(tel)) {
                    JOptionPane.showMessageDialog(frame, "전화번호는 10자리 또는 11자리여야 합니다. (-없이)");
                    telField.setText("");
                    telField.requestFocus();
                    return;
                }
                if(!isValidEmail(email)){
                    JOptionPane.showMessageDialog(frame, "올바른 이메일 형식을 입력하세요.");
                    emailField.setText("");
                    emailField.requestFocus();
                    return;
                }
                Customer customer = new Customer(name, id, pw, email, tel, birthDay, qNumber, answer);
                SignUpDB db = new SignUpDB();
                db.dataInsert(customer);
                JOptionPane.showMessageDialog(frame, "회원가입 완료!");
                Login login = new Login(frame);
                frame.setContentPane(login);
                frame.revalidate();
                frame.repaint();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "DB 오류: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "전화번호에는 '-'를 포함할 수 없습니다.");
            }
        });
    }



    static void updateDays(JComboBox<Integer> year, JComboBox<Integer> month, JComboBox<Integer> day) {
        int Year = (int) year.getSelectedItem();
        int Month = (int) month.getSelectedItem();
        int maxDays = YearMonth.of(Year, Month).lengthOfMonth();

        day.removeAllItems();
        for (int i = 1; i <= maxDays; i++) {
            day.addItem(i);
        }
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

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    private static boolean isValidPw(String pw){
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,16}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pw !=null && pattern.matcher(pw).matches();
    }

    private boolean isValidTel(String birth) {
        // 전화번호 유효성 검사 정규표현식
        String birthRegex = "^\\d{10,11}$";
        return birth.matches(birthRegex);
    }
}

