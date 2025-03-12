package hotel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.EventListener;
import java.util.regex.Pattern;



class Info{
    private Customer customer;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Info(Customer customer){
        this.customer = customer;
    }



    public Customer getCustomer(){
        return customer;
    }


}





class InfoDB {
    Connection con;

    public InfoDB() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
        System.out.println("DB 연결 성공!");
    }

    public int dateSelect(Info info, Mypage mypage, String userId) {
        int num = -1;
        JTextField nameField = mypage.getNameField();  // getter를 통해 nameField 가져오기
        JTextField emailField = mypage.getEmailField();
        JTextField telField = mypage.getTelField();
        JTextField pwField = mypage.getPwField();
        JComboBox<Integer> year = mypage.getYearComboBox();
        JComboBox<Integer> month = mypage.getMonthComboBox();
        JComboBox<Integer> day = mypage.getDayComboBox();

        JTextField answerField = mypage.getAnswerField();

        String query = "SELECT name, pw, id, email, tel, birthDay FROM hotelCustomertbl WHERE id = ?";
        String queryQ = "SELECT qNumber,answer FROM findPasswordQuestion WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
             PreparedStatement pstmt = conn.prepareStatement(query);
             PreparedStatement pstmtQ = conn.prepareStatement(queryQ)) {

            pstmt.setString(1, userId);  // userId를 쿼리에 세팅
            pstmtQ.setString(1, userId);  // userId를 쿼리에 세팅

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // DB에서 데이터 가져오기
                    String name = rs.getString("name");
                    String id = rs.getString("id");
                    String email = rs.getString("email");
                    String tel = rs.getString("tel");
                    String birthDay = rs.getString("birthDay");



                    // 텍스트 필드에 값을 설정
                    nameField.setText(name);
                    emailField.setText(email);
                    telField.setText(tel);


                    // 생일 설정 (년도, 월, 일)
                    String[] birthDateParts = birthDay.split("-");
                    year.setSelectedItem(Integer.parseInt(birthDateParts[0]));
                    month.setSelectedItem(Integer.parseInt(birthDateParts[1]));
                    day.setSelectedItem(Integer.parseInt(birthDateParts[2]));

                    System.out.println("고객 정보 조회 성공!");
                } else {
                    System.out.println("해당 ID의 데이터가 없습니다.");
                }
            }
            try(ResultSet rsQ = pstmtQ.executeQuery();){
                if(rsQ.next()){
                    System.out.println("rsQ 조회 성공");
                    String qNumber = rsQ.getString("qNumber");
                    System.out.println("디비에서의 qNumber : "+ qNumber);
                    String answer = rsQ.getString("answer");
                    System.out.println("answer+ " + answer);
                    if(qNumber != null){

                        num = Integer.parseInt(qNumber);
                    }else{
                        System.out.println("qNumber의 값이 null입니다.");
                    }
                    answerField.setText(answer);
                    System.out.println("answer : "+ answerField.getText());
                }else{
                    System.out.println("답변 데이터가 없습니다.");

                }
            }
        } catch (SQLException e) {
            System.out.println("DB 조회 오류: " + e.getMessage());
        }
        return num;
    }


    public void dataUpdate(Customer customer) {
        String query = "UPDATE hotelcustomertbl " +
                "SET name = ?, id = ?, pw = ?, email = ?, tel = ?, birthDay = ? " +
                "WHERE id = ?";
        String queryQ = "UPDATE findPasswordQuestion " +
                "SET qNumber=? answer=? " +
                "WHERE id = ?";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "1234");
             PreparedStatement pstmt = con.prepareStatement(query);
             PreparedStatement pstmtQ = con.prepareStatement(queryQ)) {

            // 입력값 검증 및 기본값 설정
            String name = customer.getName().trim();
            String id = customer.getId().trim();
            String pw = customer.getPw().trim();
            String email = customer.getEmail().trim();
            String tel = customer.getTel().trim();
            if (tel.isEmpty()) {
                tel = "00000000000"; // 기본 전화번호 설정
            }
            String birthDay = customer.getBirthDay().trim();
            String qNumber = String.valueOf(customer.getqNumber());
            String answer = customer.getAnswer().trim();

            // SQL 파라미터 설정
            pstmt.setString(1, name);
            pstmt.setString(2, id);
            pstmt.setString(3, pw);
            pstmt.setString(4, email);
            pstmt.setString(5, tel);
            pstmt.setString(6, birthDay);
            pstmt.setString(7, id);



            pstmtQ.setString(1,qNumber);
            pstmtQ.setString(2,answer);
            pstmtQ.setString(3,id);

            // 디버깅 출력
            System.out.println("Executing Query: " + query);
            System.out.println("Parameters:");
            System.out.println("Name: " + name);
            System.out.println("ID: " + id);
            System.out.println("Password: " + pw);
            System.out.println("Email: " + email);
            System.out.println("Tel: " + tel);
            System.out.println("BirthDay: " + birthDay);

            // 쿼리 실행
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("회원 정보가 성공적으로 업데이트되었습니다.");
            } else {
                System.out.println("업데이트할 데이터가 없습니다. 조건에 맞는 데이터가 없습니다.");
            }
        } catch (SQLException e) {
            System.out.println("DB 업데이트 오류: " + e.getMessage());
        }
    }

}

public class Mypage extends JPanel {

    private JTextField nameField;
    private JTextField idField;
    private JTextField pwField;
    private JTextField emailField;
    private JTextField telField;
    private JTextField birthDayField;
    private JComboBox<Integer> year;
    private JComboBox<Integer> month;
    private JComboBox<Integer> day;
    private String userId;

    private JTextField answer;





    public JTextField getAnswer() {
        return answer;
    }

    public void setAnswer(JTextField answer) {
        this.answer = answer;
    }

    public JTextField getBirthDayField() {
        return birthDayField;
    }

    public void setBirthDayField(JTextField birthDayField) {
        this.birthDayField = birthDayField;
    }

    public JTextField getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(JTextField birthDay) {
        this.birthDay = birthDay;
    }

    private JTextField birthDay;

    public JTextField getNameField() {
        return nameField;
    }

    public void setNameField(JTextField nameField) {
        this.nameField = nameField;
    }

    public JTextField getIdField() {
        return idField;
    }

    public void setIdField(JTextField idField) {
        this.idField = idField;
    }

    public JTextField getPwField() {
        return pwField;
    }

    public void setPwField(JTextField pwField) {
        this.pwField = pwField;
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public void setEmailField(JTextField emailField) {
        this.emailField = emailField;
    }

    public JTextField getTelField() {
        return telField;
    }

    public void setTelField(JTextField telField) {
        this.telField = telField;
    }

    public JComboBox<Integer> getYearComboBox() {
        return year;
    }

    public JComboBox<Integer> getMonthComboBox() {
        return month;
    }

    public JComboBox<Integer> getDayComboBox() {
        return day;
    }

    public JTextField getAnswerField() {
        return answer;
    }
    public void setAnswerField(JTextField answer) {
        this.answer = answer;
    }




    public Mypage(String userId, JFrame jFrame) {
        this.userId = userId;

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL; // 가로로 확장


        panel.setBorder(BorderFactory.createEmptyBorder(65, 10, 10, 10)); // 상단 50px 여백 추가

        JLabel nameLabel = new JLabel("이름:");
        nameField = new JTextField(15);  // 필드를 초기화

        JLabel idLabel = new JLabel("아이디:");
        JLabel idField = new JLabel(userId);

        JLabel pwLabel = new JLabel("비밀번호:");
        pwField = new JPasswordField(15);
        String placehoder = "대소문자,특수문자,15글자 이하 작성";
        pwField.setText(placehoder);
        pwField.setForeground(Color.GRAY);
        ((JPasswordField) pwField).setEchoChar((char) 0);

        JLabel passReLabel = new JLabel("비밀번호 재확인:");
        JTextField passReField = new JPasswordField(15);

        JLabel emailLabel = new JLabel("이메일:");
        emailField = new JTextField(15);

        JLabel telLabel = new JLabel("전화번호:");
        telField = new JTextField(15);

        JLabel birthDayLabel = new JLabel("생년월일:");
        year = new JComboBox<>();
        month = new JComboBox<>();
        day = new JComboBox<>();

        JLabel questionLabel = new JLabel("본인 확인 질문 :");
        JComboBox<String> questionComboBox = new JComboBox<>(new String[]{
                "가장 존경하는 위인은?",
                "어릴 때 다니던 초등학교 이름은?",
                "좋아하는 동물은?",
                "기억에 남는 추억의 장소는?",
                "가장 좋아하는 음식은?"
        });



        questionComboBox.setBounds(50, 50, 150, 30);
        add(questionComboBox);



        JLabel answerLabel = new JLabel("답변:");
        answer = new JTextField(15);



        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
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
        Dimension buttonSize = new Dimension(100, 40);
        JButton preButton = new JButton("이전");
        JButton submitButton = new JButton("수정");
        JButton dropButton = new JButton("탈퇴");
        submitButton.setPreferredSize(buttonSize);
        dropButton.setPreferredSize(buttonSize);
        preButton.setPreferredSize(buttonSize);
        // 컴포넌트 배치
        gbc.gridx = 0;
        gbc.gridy = 0; // 첫 번째 열, 첫 번째 행
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(pwLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(pwField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(passReLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(passReField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(telLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(telField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(birthDayLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(birthDayLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(year, gbc);
        gbc.gridx = 2;
        gbc.gridy = 6;
        panel.add(month, gbc);
        gbc.gridx = 3;
        gbc.gridy = 6;
        panel.add(day, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(questionLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 7;
        panel.add(questionComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(answerLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 8;
        panel.add(answer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9; // 버튼을 중앙 정렬
        panel.add(preButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 9; // 버튼을 중앙 정렬
        panel.add(submitButton, gbc);
        gbc.gridx = 2;
        gbc.gridy = 9; // 버튼을 중앙 정렬
        panel.add(dropButton, gbc);

        try {
            String name = nameField.getText();
            String id = idField.getText();
            String pw = pwField.getText();
            String email = emailField.getText();
            String tel = telField.getText();
            String birthDay = String.format("%04d-%02d-%02d",
                    year.getSelectedItem(), month.getSelectedItem(), day.getSelectedItem());

            String qNumber = String.valueOf(questionComboBox.getSelectedItem());
            System.out.println("qNumber의 값: " +qNumber);

            String ansWer = answer.getText();


            InfoDB db = new InfoDB();
            Info info = new Info(new Customer(name, id, pw, email, tel, birthDay,qNumber,ansWer));
            int num =db.dateSelect(info, this, userId);
            System.out.println(num);
            questionComboBox.setSelectedIndex(num);
            System.out.println(questionComboBox.getSelectedItem());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 패널 추가 및 창 표시
        add(panel);
        setVisible(true);
        dropButton.addActionListener(e -> {
            new Popup(userId, ()-> {
                Login login = new Login(jFrame);
                jFrame.setContentPane(login);
                jFrame.revalidate();
                jFrame.repaint();
            });
        });

        preButton.addActionListener(e -> {
            Main main = new Main(userId,jFrame);
            jFrame.setContentPane(main);
            jFrame.revalidate();
            jFrame.repaint();
        });
        submitButton.addActionListener(e -> {

            try {
                String name = nameField.getText().trim();
                String id = idField.getText().trim();
                String pw = pwField.getText().trim();
                String email = emailField.getText().trim();
                String tel = telField.getText().trim();
                String birthDay = String.format("%04d-%02d-%02d",
                        year.getSelectedItem(), month.getSelectedItem(), day.getSelectedItem());
                String qNumber = String.valueOf(questionComboBox.getSelectedIndex());
                System.out.println("qNumber : "+qNumber);
                String ansWer = answer.getText().trim();


                if(name.isEmpty()||tel.isEmpty()||name.isEmpty()||id.isEmpty()||pw.isEmpty()||birthDay.isEmpty()
                ){
                    JOptionPane.showMessageDialog(jFrame, "모든 필드를 입력하세요");
                    return;
                }
                if(!isValidName(name)){
                    JOptionPane.showMessageDialog(jFrame, "이름은 10자 이하 한글로 입력해주세요");
                    return;
                }
                if(!isValidId(id)){
                    JOptionPane.showMessageDialog(jFrame, "아이디는 6글자이상 16글자 이하로 써주세요");
                    return;
                }
                if(!isValidPw(pw)){
                    JOptionPane.showMessageDialog(jFrame, "비밀번호는 대소문자,숫자 8자 이상 16자 이하, 특수문자를 포함해야합니다.");
                    pwField.setText("");
                    pwField.requestFocus();
                    return;
                }if(!pw.equals(passReField.getText())){
                    JOptionPane.showMessageDialog(jFrame, "비밀번호가 일치하지 않습니다.");
                    pwField.setText("");
                    pwField.requestFocus();
                    return;
                }
                if (!isValidTel(tel)) {
                    JOptionPane.showMessageDialog(jFrame, "전화번호는 10자리 또는 11자리여야 합니다. (-없이)");
                    telField.setText("");
                    telField.requestFocus();
                    return;
                }
                if(!isValidEmail(email)){
                    JOptionPane.showMessageDialog(jFrame, "올바른 이메일 형식을 입력하세요.");
                    emailField.setText("");
                    emailField.requestFocus();
                    return;
                }

                // 데이터 업데이트
                InfoDB db = new InfoDB();
                db.dataUpdate(new Customer(name, id, pw, email, tel, birthDay,qNumber,ansWer));
                JOptionPane.showMessageDialog(jFrame, "수정 완료!", "Update Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(jFrame, "DB 오류: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

// updateDays를 받아서 해보기

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
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,15}$";
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



