package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.toedter.calendar.JCalendar;

import java.time.ZoneId;


public class HotelReservationGUI extends JFrame {
   private JPanel mainPanel;
   private JPanel mainContentPanel; // 동적 콘텐츠를 표시할 패널
   private JPanel buttonPanel;
   private JFrame popupFrame;
   
   
   public HotelReservationGUI() {
      setTitle("호텔 예약 시스템");
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setSize(1020, 600);
      
      setResizable(false); // 창 크기 조정 불가
      setExtendedState(JFrame.NORMAL);
      
      HotelDB.roomSet();
      
      setLocationRelativeTo(null); // 화면 중앙에 표시
      
      mainPanel = new JPanel(new BorderLayout());
      
      // 고정된 왼쪽 버튼 패널
      buttonPanel = new JPanel(new GridLayout(6, 1, 5, 5));
      buttonPanel.setPreferredSize(new Dimension(150, 600)); // 고정된 크기
      
      // 이미지 버튼 만들기
      JButton viewRoomsButton = createImageButton("viewRooms.png", null);
      JButton reserveButton = createImageButton("reserve.png", null);
      JButton checkInButton = createImageButton("checkIn.png", null);
      JButton checkOutButton = createImageButton("checkOut.png", null);
      JButton viewReservationsButton = createImageButton("viewReservations.png", null);
      JButton exitButton = createImageButton("exit.png", null);
      
      buttonPanel.add(viewRoomsButton);
      buttonPanel.add(reserveButton);
      buttonPanel.add(checkInButton);
      buttonPanel.add(checkOutButton);
      buttonPanel.add(viewReservationsButton);
      buttonPanel.add(exitButton);
      
      // 버튼 이벤트 추가
      viewRoomsButton.addActionListener(e -> showRoomsWithCalendar());
      reserveButton.addActionListener(e -> reserveRoom());
      checkInButton.addActionListener(e -> checkIn());
      checkOutButton.addActionListener(e -> checkOut());
      viewReservationsButton.addActionListener(e -> viewReservations());
      exitButton.addActionListener(e -> {
         // 팝업창 닫기
         Window window = SwingUtilities.getWindowAncestor(buttonPanel);
         if (window != null) {
            window.dispose(); // 현재 팝업 윈도우 닫기
         }
      });
      
      viewRoomsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      reserveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      checkInButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      checkOutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      viewReservationsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      
      // 초기 오른쪽 콘텐츠 패널
      mainContentPanel = new JPanel(new BorderLayout()); // 동적 레이아웃 사용
      
      JLabel welcomeLabel = new JLabel("환영합니다!");
      welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 40)); // 폰트 설정
      welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
      
      // 라벨 추가
      mainContentPanel.add(welcomeLabel, BorderLayout.CENTER); // CENTER 위치에 추가
      
      // 메인 패널 구성
      mainPanel.add(buttonPanel, BorderLayout.WEST); // 왼쪽 버튼 패널 고정
      mainPanel.add(mainContentPanel, BorderLayout.CENTER); // 초기 콘텐츠
      
      add(mainPanel);
      setVisible(true);
   }
   
   // 이미지 버튼 생성 메소드
   private JButton createImageButton(String imageName, String tooltip) {
      // 이미지 아이콘 불러오기
      ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/" + imageName)); // 이미지 경로
      Image originalImage = originalIcon.getImage();
      
      
      Image scaledImage = originalImage.getScaledInstance(150, 90, Image.SCALE_SMOOTH);
      ImageIcon scaledIcon = new ImageIcon(scaledImage);
      
      // 버튼 생성
      JButton button = new JButton(scaledIcon);
      button.setToolTipText(tooltip);  // 툴팁 텍스트 설정
      
      // 버튼 스타일 설정
      button.setBorderPainted(false); // 버튼의 기본 경계선 제거
      button.setFocusPainted(false);  // 포커스 시 버튼 테두리 제거
      button.setContentAreaFilled(false); // 배경색 제거
      
      // 버튼 크기 설정
      button.setPreferredSize(new Dimension(150, 90)); // 크기 고정
      
      return button;
   }
   
   
   private void updateMainContentPanel(JPanel newContent) {
      mainContentPanel.removeAll(); // 기존 콘텐츠 제거
      mainContentPanel.add(newContent, BorderLayout.CENTER); // 새로운 콘텐츠 추가
      mainContentPanel.revalidate();
      mainContentPanel.repaint();
   }
   
   private void showRooms(LocalDate date) {
      JPanel roomsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
      roomsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      
      JLabel dateLabel = new JLabel("조회 날짜: " + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      dateLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
      dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
      dateLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      
      JLabel legendLabel = new JLabel("○ : 빈방 / ● : 예약중 / X : 체크인");
      legendLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
      legendLabel.setHorizontalAlignment(SwingConstants.CENTER);
      legendLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      
      JPanel headerPanel = new JPanel(new GridLayout(2, 1));
      headerPanel.add(dateLabel);
      headerPanel.add(legendLabel);
      
      // 층별 방 정보 생성
      for (int i = 2; i <= 6; i++) {
         JPanel floorPanel = new JPanel(new GridLayout(1, 6, 10, 10));
         floorPanel.setBorder(BorderFactory.createTitledBorder(i + "층"));
         
         for (int j = 1; j <= 6; j++) {
            int roomNo = i * 100 + j;
            
            // 방 상태 가져오기
            String roomState = getRoomStateFromDB(roomNo, date); // 선택 날짜 전달
            String stateSymbol = switch (roomState) {
               case "빈방" -> "○";
               case "예약중" -> "●";
               case "체크인" -> "X";
               default -> "?";
            };
            
            JPanel roomPanel = new JPanel(new BorderLayout());
            roomPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            
            JLabel roomImageLabel = new JLabel();
            roomImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            roomImageLabel.setIcon(getRoomImage(roomState)); // 상태별 이미지 설정
            
            JLabel roomInfoLabel = new JLabel(roomNo + "호 - " + stateSymbol);
            roomInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            roomPanel.add(roomImageLabel, BorderLayout.CENTER);
            roomPanel.add(roomInfoLabel, BorderLayout.SOUTH);
            
            // 방 클릭 시 예약 팝업 호출
            roomImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
               @Override
               public void mouseClicked(java.awt.event.MouseEvent e) {
                  // 상태 확인
                  if ("빈방".equals(roomState)) {
                     // 예약 팝업 열기
                     showReservationPopup(roomNo, date);
                  } else {
                     JOptionPane.showMessageDialog(null, "예약되었거나 체크인된 방은 선택할 수 없습니다.",
                           "선택 불가", JOptionPane.WARNING_MESSAGE);
                  }
               }
            });
            
            // 팝업 효과 추가
            addHoverPopupEffect(roomImageLabel, roomState);
            
            floorPanel.add(roomPanel);
         }
         
         roomsPanel.add(floorPanel);
      }
      
      JScrollPane scrollPane = new JScrollPane(roomsPanel);
      scrollPane.getVerticalScrollBar().setUnitIncrement(20); // 스크롤 속도 조정
      JPanel newContent = new JPanel(new BorderLayout());
      newContent.add(headerPanel, BorderLayout.NORTH);
      newContent.add(scrollPane, BorderLayout.CENTER);
      
      updateMainContentPanel(newContent);
   }
   
   
   private void showReservationPopup(int roomNo, LocalDate selectedDate) {
      // 예약 팝업을 열기 전에 현재 열려있는 호버 팝업을 닫음
      if (popupFrame != null && popupFrame.isVisible()) {
         popupFrame.dispose();
         popupFrame = null; // popupFrame 해제
      }
      
      boolean valid = false; // 유효성 검사 성공 여부 플래그
      
      // 기본값 설정: 방 번호와 날짜
      String name = "";
      String phone = "";
      String email = "";
      String birth = "";
      String startDate = selectedDate.toString(); // 선택된 날짜 자동 입력
      String nights = "";
      String roomNoString = Integer.toString(roomNo); // 선택된 방 번호 자동 입력
      
      while (!valid) { // 유효한 입력이 될 때까지 반복
         JPanel inputPanel = new JPanel();
         inputPanel.setLayout(new GridLayout(8, 2, 10, 10));
         
         JLabel nameLabel = new JLabel("예약자 이름:");
         JTextField nameField = new JTextField(name);
         
         JLabel phoneLabel = new JLabel("전화번호:");
         JTextField phoneField = new JTextField(phone);
         
         JLabel emailLabel = new JLabel("이메일:");
         JTextField emailField = new JTextField(email);
         
         JLabel birthLabel = new JLabel("생년월일 (YYYY-MM-DD):");
         JTextField birthField = new JTextField(birth);
         
         JLabel startDateLabel = new JLabel("예약 시작 날짜 (YYYY-MM-DD):");
         JTextField startDateField = new JTextField(startDate); // 예약 날짜 자동 입력
         startDateField.setEditable(false); // 수정 불가능하게 설정
         
         JLabel nightsLabel = new JLabel("예약 기간 (박):");
         JTextField nightsField = new JTextField(nights);
         
         JLabel roomNoLabel = new JLabel("방 번호:");
         JTextField roomNoField = new JTextField(roomNoString); // 방 번호 자동 입력
         roomNoField.setEditable(false); // 수정 불가능하게 설정
         
         inputPanel.add(nameLabel);
         inputPanel.add(nameField);
         inputPanel.add(phoneLabel);
         inputPanel.add(phoneField);
         inputPanel.add(emailLabel);
         inputPanel.add(emailField);
         inputPanel.add(birthLabel);
         inputPanel.add(birthField);
         inputPanel.add(startDateLabel);
         inputPanel.add(startDateField);
         inputPanel.add(nightsLabel);
         inputPanel.add(nightsField);
         inputPanel.add(roomNoLabel);
         inputPanel.add(roomNoField);
         
         int result = JOptionPane.showConfirmDialog(null, inputPanel,
               "예약하기", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
         if (result != JOptionPane.OK_OPTION) {
            return; // 취소 버튼 누르면 종료
         }
         
         try {
            // 입력 필드에서 값을 가져옴
            name = nameField.getText().trim();
            phone = phoneField.getText().trim();
            email = emailField.getText().trim();
            birth = birthField.getText().trim();
            nights = nightsField.getText().trim();
            
            // **유효성 검사**
            if (!isValidName(name)) {
               continue; // 유효하지 않으면 다시 입력
            }
            if (!isValidPhoneNumber(phone)) {
               continue; // 유효하지 않으면 다시 입력
            }
            if (!isValidEmail(email)) {
               continue; // 유효하지 않으면 다시 입력
            }
            if (!validateBirthDate(birth)) {
               continue; // 유효하지 않으면 다시 입력
            }
            if (!validateNights(nights)) {
               continue; // 유효하지 않으면 다시 입력
            }
            
            LocalDate parsedBirth = LocalDate.parse(birth);
            LocalDate parsedStartDate = LocalDate.parse(startDate);
            int parsedNights = Integer.parseInt(nights);
            int parsedRoomNo = Integer.parseInt(roomNoString);
            
            if (!validateInputs(name, phone, email, parsedBirth, parsedStartDate,
                  parsedNights, parsedRoomNo)) {
               continue; // 유효성 검사 실패 시 다시 입력
            }
            
            // 상태 확인
            String currentState = HotelDB.getRoomState(parsedRoomNo, parsedStartDate);
            if (!"빈방".equals(currentState)) {
               JOptionPane.showMessageDialog(null,
                     "해당 방 번호는 이미 예약되었거나 사용 중입니다.",
                     "예약 불가",
                     JOptionPane.WARNING_MESSAGE);
               continue; // 방 상태가 예약 불가면 다시 입력
            }
            
            // 예약 가능 -> 예약 진행
            Reserve reserve = new Reserve(parsedStartDate, parsedRoomNo,
                  name, phone, email, parsedBirth);
            HotelDB.insertReservation(reserve, parsedNights);
            
            LocalDate endDate = parsedStartDate.plusDays(parsedNights);
            
            JOptionPane.showMessageDialog(null, String.format(
                  "예약이 완료되었습니다!\n\n예약 시작일: %s\n예약 종료일: %s",
                  parsedStartDate, endDate));
            showRooms(LocalDate.now());
            valid = true; // 유효한 입력이 완료되면 루프 종료
         } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "입력 중 오류가 발생했습니다: " + ex.getMessage(),
                  "오류", JOptionPane.ERROR_MESSAGE);
         }
      }
   }
   
   
   private ImageIcon getResizedImageIcon(ImageIcon originalIcon, int width, int height) {
      Image originalImage = originalIcon.getImage();
      Image resizedImage = originalImage.getScaledInstance(width,
            height, Image.SCALE_SMOOTH);
      return new ImageIcon(resizedImage);
   }
   
   
   private void addHoverPopupEffect(JLabel roomImageLabel, String roomState) {
      roomImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
         @Override
         public void mouseEntered(java.awt.event.MouseEvent e) {
            // 기존 팝업이 열려 있다면 닫기
            if (popupFrame != null && popupFrame.isVisible()) {
               popupFrame.dispose();
               popupFrame = null;
            }
            
            // 팝업 창 생성
            popupFrame = new JFrame();
            popupFrame.setUndecorated(true); // 테두리 제거
            popupFrame.setFocusableWindowState(false); // 포커스 비활성화
            popupFrame.setSize(300, 300); // 팝업 크기 설정
            
            // 확대 이미지 설정
            ImageIcon expandedIcon = getResizedImageIcon(getRoomImage(roomState), 300, 300);
            JLabel imageLabel = new JLabel(expandedIcon);
            popupFrame.add(imageLabel);
            
            // 팝업 위치를 마우스 위치 기준으로 설정
            Point location = roomImageLabel.getLocationOnScreen();
            popupFrame.setLocation(location.x - popupFrame.getWidth() - 5, location.y);
            
            // 팝업을 항상 상단에 표시하되, 다른 팝업이 열리면 우선순위 낮추기
            popupFrame.setVisible(true);
            
            // AlwaysOnTop 설정은 popupFrame이 null이 아닐 때만 호출
            SwingUtilities.invokeLater(() -> {
               if (popupFrame != null) {
                  popupFrame.setAlwaysOnTop(false);
               }
            });
         }
         
         @Override
         public void mouseExited(java.awt.event.MouseEvent e) {
            // 마우스가 이미지 레이블에서 벗어날 때 팝업을 바로 닫음
            if (popupFrame != null && popupFrame.isVisible()) {
               popupFrame.dispose();
               popupFrame = null;
            }
         }
      });
   }
   
   
   private ImageIcon getRoomImage(String roomState) {
      String imagePath;
      switch (roomState) {
         case "빈방" -> imagePath = "/images/empty_room.png";
         case "예약중" -> imagePath = "/images/reserved_room.png";
         case "체크인" -> imagePath = "/images/occupied_room.png";
         default -> imagePath = "/images/unknown_room.png";
      }
      
      try {
         // 리소스를 클래스패스를 기준으로 로드
         java.net.URL imageUrl = getClass().getResource(imagePath);
         if (imageUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imageUrl); // 원본 이미지 로드
            
            // **이미지 크기 조정**
            Image scaledImage = originalIcon.getImage().getScaledInstance(
                  120, // 폭 (width)
                  120, // 높이 (height)
                  Image.SCALE_SMOOTH // 부드러운 스케일링 옵션
            );
            
            return new ImageIcon(scaledImage); // 크기 조정된 이미지를 아이콘으로 반환
         } else {
            System.out.println("이미지 경로를 찾을 수 없음: " + imagePath);
            return new ImageIcon(); // 빈 아이콘 반환
         }
      } catch (Exception e) {
         System.out.println("이미지 로드 실패: " + imagePath);
         e.printStackTrace();
         return new ImageIcon(); // 빈 아이콘 반환
      }
   }
   
   
   private void initializeHotelDB() {
      HotelDB.roomSet(); // HotelDB에서 Room 배열 초기화
      
      int floors = 5; // 배열에서 사용할 층 수 (2층부터 6층까지 총 5개 층)
      int roomsPerFloor = 6; // 각 층에 방 6개가 있다고 가정
      
      for (int i = 0; i < floors; i++) {
         for (int j = 0; j < roomsPerFloor; j++) {
            Room currentRoom = getRoom(i, j);
            if (currentRoom == null) {
               int roomNo = (i + 2) * 100 + (j + 1); // 방 번호 계산 (예: 201, 202, ... 601, 602)
               Room newRoom = new Room(roomNo); // Room 객체 생성 시 방 번호를 전달
               newRoom.setState("빈방");
               setRoom(i, j, newRoom); // 배열의 해당 위치에 Room 객체 설정
            }
         }
      }
   }
   
   private Room getRoom(int floorIndex, int roomIndex) {
      try {
         // 리플렉션을 사용하여 private 필드에 접근
         java.lang.reflect.Field roomField = HotelDB.class.getDeclaredField("room");
         roomField.setAccessible(true); // private 필드 접근 가능 설정
         
         Room[][] rooms = (Room[][]) roomField.get(null); // static 필드이므로 null로 접근
         return rooms[floorIndex][roomIndex];
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }
   
   private void setRoom(int floorIndex, int roomIndex, Room room) {
      try {
         // 리플렉션을 사용하여 private 필드에 접근
         java.lang.reflect.Field roomField = HotelDB.class.getDeclaredField("room");
         roomField.setAccessible(true); // private 필드 접근 가능 설정
         
         Room[][] rooms = (Room[][]) roomField.get(null); // static 필드이므로 null로 접근
         rooms[floorIndex][roomIndex] = room;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   private void showRoomsWithCalendar() {
      JFrame calendarFrame = new JFrame("날짜 선택");
      calendarFrame.setSize(400, 400);
      calendarFrame.setLayout(new BorderLayout());
      calendarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      
      JCalendar calendar = new JCalendar();
      JButton selectButton = new JButton("날짜 선택");
      selectButton.addActionListener(e -> {
         if (calendar.getDate() == null) {
            JOptionPane.showMessageDialog(calendarFrame, "날짜를 선택하세요.",
                  "오류", JOptionPane.ERROR_MESSAGE);
            return;
         }
         LocalDate selectedDate = calendar.getDate().toInstant().atZone(ZoneId.
               systemDefault()).toLocalDate();
         calendarFrame.dispose();
         showRooms(selectedDate); // 선택된 날짜 전달
      });
      
      calendarFrame.add(calendar, BorderLayout.CENTER);
      calendarFrame.add(selectButton, BorderLayout.SOUTH);
      
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (screenSize.width - calendarFrame.getWidth()) / 2;
      int y = (screenSize.height - calendarFrame.getHeight()) / 2;
      calendarFrame.setLocation(x, y);
      calendarFrame.setVisible(true);
   }
   
   
   private String getRoomStateFromDB(int roomNo, LocalDate selectedDate) {
      try {
         return HotelDB.getRoomState(roomNo, selectedDate); // HotelDB의 메서드에 날짜 전달
      } catch (Exception e) {
         System.err.println("DB 호출 오류: " + e.getMessage());
         return "빈방"; // 기본 상태 반환
      }
   }
   
   
   private void reserveRoom() {
      boolean valid = false; // 유효성 검사 성공 여부 플래그
      
      // 입력 필드를 유지하기 위한 변수 선언
      String name = "";
      String phone = "";
      String email = "";
      String birth = "";
      String startDate = "";
      String nights = "";
      String roomNo = "";
      
      while (!valid) { // 유효한 입력이 될 때까지 반복
         JPanel inputPanel = new JPanel();
         inputPanel.setLayout(new GridLayout(8, 2, 10, 10));
         
         JLabel nameLabel = new JLabel("예약자 이름:");
         JTextField nameField = new JTextField(name);
         
         JLabel phoneLabel = new JLabel("전화번호:");
         JTextField phoneField = new JTextField(phone);
         
         JLabel emailLabel = new JLabel("이메일:");
         JTextField emailField = new JTextField(email);
         
         JLabel birthLabel = new JLabel("생년월일 (YYYY-MM-DD):");
         JTextField birthField = new JTextField(birth);
         
         JLabel startDateLabel = new JLabel("예약 시작 날짜 (YYYY-MM-DD):");
         JTextField startDateField = new JTextField(startDate);
         
         JLabel nightsLabel = new JLabel("예약 기간 (박):");
         JTextField nightsField = new JTextField(nights);
         
         JLabel roomNoLabel = new JLabel("방 번호:");
         JTextField roomNoField = new JTextField(roomNo);
         
         inputPanel.add(nameLabel);
         inputPanel.add(nameField);
         inputPanel.add(phoneLabel);
         inputPanel.add(phoneField);
         inputPanel.add(emailLabel);
         inputPanel.add(emailField);
         inputPanel.add(birthLabel);
         inputPanel.add(birthField);
         inputPanel.add(startDateLabel);
         inputPanel.add(startDateField);
         inputPanel.add(nightsLabel);
         inputPanel.add(nightsField);
         inputPanel.add(roomNoLabel);
         inputPanel.add(roomNoField);
         
         int result = JOptionPane.showConfirmDialog(null, inputPanel,
               "예약하기", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
         if (result != JOptionPane.OK_OPTION) {
            return; // 취소 버튼 누르면 종료
         }
         
         try {
            // 입력 필드에서 값을 가져옴
            name = nameField.getText().trim();
            phone = phoneField.getText().trim();
            email = emailField.getText().trim();
            birth = birthField.getText().trim();
            startDate = startDateField.getText().trim();
            nights = nightsField.getText().trim();
            roomNo = roomNoField.getText().trim();
            
            
            if (!isValidName(name)) {
               continue; // 유효하지 않으면 다시 입력
            }
            if (!isValidPhoneNumber(phone)) {
               continue; // 유효하지 않으면 다시 입력
            }
            if (!isValidEmail(email)) {
               continue; // 유효하지 않으면 다시 입력
            }
            
            if (!validateBirthDate(birth)) {
               continue; // 유효하지 않으면 다시 입력
            }
            if (!validateNights(nights)) {
               continue; // 유효하지 않으면 다시 입력
            }
            
            if (!isValidStartDate(startDate)) {
               continue; // 유효하지 않으면 다시 입력
            }
            if (!isValidRoomNumber(roomNo)) {
               continue; // 유효하지 않으면 다시 입력
            }
            
            LocalDate parsedBirth = LocalDate.parse(birth);
            LocalDate parsedStartDate = LocalDate.parse(startDate);
            int parsedNights = Integer.parseInt(nights);
            int parsedRoomNo = Integer.parseInt(roomNo);
            
            if (!validateInputs(name, phone, email, parsedBirth, parsedStartDate,
                  parsedNights, parsedRoomNo)) {
               continue; // 유효성 검사 실패 시 다시 입력
            }
            
            // 상태 확인
            String currentState = HotelDB.getRoomState(parsedRoomNo, LocalDate.now());
            if (!"빈방".equals(currentState)) {
               JOptionPane.showMessageDialog(null,
                     "해당 방 번호는 이미 예약되었거나 사용 중입니다.",
                     "예약 불가",
                     JOptionPane.WARNING_MESSAGE);
               continue; // 방 상태가 예약 불가면 다시 입력
            }
            
            // 예약 가능 -> 예약 진행
            Reserve reserve = new Reserve(parsedStartDate, parsedRoomNo,
                  name, phone, email, parsedBirth);
            HotelDB.insertReservation(reserve, parsedNights);
            
            LocalDate endDate = parsedStartDate.plusDays(parsedNights);
            
            JOptionPane.showMessageDialog(null, String.format(
                  "예약이 완료되었습니다!\n\n예약 시작일: %s\n예약 종료일: %s",
                  parsedStartDate, endDate));
            showRooms(LocalDate.now());
            valid = true; // 유효한 입력이 완료되면 루프 종료
         } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "입력 중 오류가 발생했습니다: " + ex.getMessage(),
                  "오류", JOptionPane.ERROR_MESSAGE);
         }
      }
   }
   
   
   private void checkIn() {
      String roomNoStr = JOptionPane.showInputDialog(null, "체크인할 방 번호를 입력하세요:", "방 번호 입력", JOptionPane.PLAIN_MESSAGE);
      
      if (roomNoStr == null || roomNoStr.trim().isEmpty()) {
         return; // 사용자 취소 시 종료
      }
      
      try {
         int roomNo = Integer.parseInt(roomNoStr);
         
         // 방 번호가 유효한지 먼저 확인
         if (!isRoomNumberValid(roomNo)) {
            JOptionPane.showMessageDialog(null, "유효하지 않은 방 번호입니다. " +
                  "다시 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
         }
         
         // 방 상태를 확인하여 null 상태 처리
         String currentState = HotelDB.getRoomState(roomNo, LocalDate.now());
         if (currentState == null) {
            JOptionPane.showMessageDialog(null, "해당 방 번호는 존재하지 않거나" +
                  " 초기화되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
         }
         
         if ("체크인".equals(currentState)) {
            JOptionPane.showMessageDialog(null, "해당 방은 이미 체크인 상태입니다.",
                  "오류", JOptionPane.WARNING_MESSAGE);
         } else if ("예약중".equals(currentState)) {
            HotelDB.handleCheckIn(roomNo);
            JOptionPane.showMessageDialog(null, roomNo + "호 체크인 완료.");
            showRooms(LocalDate.now());
         } else {
            JOptionPane.showMessageDialog(null, "해당 방 번호는 체크인할 수 없는 상태입니다.",
                  "오류", JOptionPane.WARNING_MESSAGE);
         }
      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(null, "유효한 방 번호를 입력하세요.",
               "오류", JOptionPane.ERROR_MESSAGE);
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "체크인 중 오류가 발생했습니다: "
               + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
      }
   }
   
   
   private void checkOut() {
      String roomNoStr = JOptionPane.showInputDialog(null, "체크아웃할 방 번호를 입력하세요:", "방 번호 입력", JOptionPane.PLAIN_MESSAGE);
      
      if (roomNoStr == null || roomNoStr.trim().isEmpty()) {
         return; // 사용자 취소 시 종료
      }
      
      try {
         int roomNo = Integer.parseInt(roomNoStr);
         
         // 방 번호가 유효한지 먼저 확인
         if (!isRoomNumberValid(roomNo)) {
            JOptionPane.showMessageDialog(null, "유효하지 않은 방 번호입니다. " +
                  "다시 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
         }
         
         // 방 상태를 확인하여 null 상태 처리
         String currentState = HotelDB.getRoomState(roomNo, LocalDate.now());
         if (currentState == null) {
            JOptionPane.showMessageDialog(null, "해당 방 번호는 존재하지 않거나 " +
                  "초기화되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
         }
         
         if ("체크인".equals(currentState)) {
            // 체크인 상태인 경우 체크아웃 처리
            HotelDB.handleCheckOut(roomNo);
            JOptionPane.showMessageDialog(null, roomNo + "호 체크아웃 완료.");
            showRooms(LocalDate.now());
         } else if ("빈방".equals(currentState) || "체크아웃".equals(currentState)) {
            JOptionPane.showMessageDialog(null, "해당 방 번호는 체크아웃할 수 없는 상태입니다.",
                  "오류", JOptionPane.WARNING_MESSAGE);
         } else {
            JOptionPane.showMessageDialog(null, "해당 방 번호는 유효하지 않은 상태입니다.",
                  "오류", JOptionPane.WARNING_MESSAGE);
         }
      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(null, "유효한 방 번호를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "체크아웃 중 오류가 발생했습니다: "
               + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
      }
   }
   
   
   private boolean isRoomNumberValid(int roomNo) {
      // 방 번호를 2층에서 6층까지의 객실 번호로 제한 (예: 201~206, 301~306 등)
      int floor = roomNo / 100; // 100 단위로 층 계산
      int room = roomNo % 100; // 방 번호
      return floor >= 2 && floor <= 6 && room >= 1 && room <= 6;
   }
   
   
   private void viewReservations() {
      String[] options = {"1. 날짜로 검색", "2. 방 번호로 검색", "3. 전체 조회"};
      String choice = (String) JOptionPane.showInputDialog(
            null,
            "조회 방식을 선택하세요:",
            "예약 내역 확인",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]);
      
      if (choice == null || choice.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "조회 방식을 선택하지 않았습니다.",
               "오류", JOptionPane.ERROR_MESSAGE);
         return;
      }
      
      String filterType = "";
      String filterValue = "";
      
      switch (choice.charAt(0)) {
         case '1':
            filterType = "date";
            boolean validDate = false;
            while (!validDate) {
               filterValue = JOptionPane.showInputDialog(null, "조회할 날짜를 입력하세요 (YYYY-MM-DD):", "날짜 입력", JOptionPane.PLAIN_MESSAGE);
               if (filterValue == null || filterValue.trim().isEmpty()) {
                  JOptionPane.showMessageDialog(null, "날짜를 입력하지 않았습니다.",
                        "오류", JOptionPane.ERROR_MESSAGE);
                  return;
               }
               
               try {
                  // 입력된 문자열을 LocalDate로 변환하여 형식 검증
                  LocalDate.parse(filterValue.trim());
                  validDate = true; // 유효한 날짜 형식이면 루프 종료
               } catch (Exception e) {
                  JOptionPane.showMessageDialog(null, "잘못된 날짜 형식입니다. 형식은 YYYY-MM-DD입니다.",
                        "입력 오류", JOptionPane.ERROR_MESSAGE);
               }
            }
            break;
         case '2':
            filterType = "room";
            filterValue = JOptionPane.showInputDialog(null, "조회할 방 번호를 입력하세요:", "방 번호 입력", JOptionPane.PLAIN_MESSAGE);
            if (filterValue == null || filterValue.trim().isEmpty()) {
               JOptionPane.showMessageDialog(null, "방 번호를 입력하지 않았습니다.",
                     "오류", JOptionPane.ERROR_MESSAGE);
               return;
            }
            break;
         case '3':
            filterType = "all";
            break;
         default:
            JOptionPane.showMessageDialog(null, "잘못된 선택입니다.", "오류",
                  JOptionPane.ERROR_MESSAGE);
            return;
      }
      
      try {
         String results = HotelDB.fetchReservations(filterType, filterValue);
         if (results.isEmpty()) {
            JOptionPane.showMessageDialog(null, "조회 결과가 없습니다.",
                  "조회 결과", JOptionPane.INFORMATION_MESSAGE);
         } else {
            Object[][] tableData = parseResultsToTable(results);
            showReservationTableWithEdit(tableData);
         }
      } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "조회 중 오류가 발생했습니다: "
               + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
      }
   }
   
   
   private Object[][] parseResultsToTable(String results) {
      String[] rows = results.split("\n");
      Object[][] tableData = new Object[rows.length][8];
      
      for (int i = 0; i < rows.length; i++) {
         String[] columns = rows[i].split(", ");
         for (int j = 0; j < Math.min(columns.length, 8); j++) {
            String[] keyValue = columns[j].split(": ");
            tableData[i][j] = keyValue.length > 1 ? keyValue[1].trim() : "";
         }
      }
      
      return tableData;
   }
   
   
   private void showReservationTableWithEdit(Object[][] data) {
      String[] columnNames = {"예약자명", "전화번호", "이메일", "생년월일", "예약 시작일",
            "예약 종료일", "방 번호", "상태"};
      
      DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // 모든 셀 비활성화
         }
      };
      
      JTable table = new JTable(tableModel);
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 단일 선택
      JScrollPane scrollPane = new JScrollPane(table);
      
      // 삭제 버튼 생성
      JButton deleteButton = new JButton("선택 예약 삭제");
      deleteButton.addActionListener(e -> {
         int selectedRow = table.getSelectedRow();
         if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "삭제할 예약을 선택하세요.",
                  "오류", JOptionPane.ERROR_MESSAGE);
            return;
         }
         
         int confirmation = JOptionPane.showConfirmDialog(null, "선택한 예약을 삭제하시겠습니까?",
               "예약 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
         if (confirmation == JOptionPane.YES_OPTION) {
            try {
               int roomNo = Integer.parseInt((String) tableModel.getValueAt(selectedRow, 6));
               LocalDate reserveDate = LocalDate.parse((String) tableModel.getValueAt(selectedRow, 4));
               HotelDB.deleteReservation(roomNo, reserveDate);
               
               tableModel.removeRow(selectedRow);
               JOptionPane.showMessageDialog(null, "예약이 삭제되었습니다.");
            } catch (Exception ex) {
               JOptionPane.showMessageDialog(null, "삭제 중 오류가 발생했습니다: "
                     + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            }
         }
      });
      
      // 수정 버튼 생성
      JButton editButton = new JButton("선택 예약 수정");
      editButton.addActionListener(e -> {
         int selectedRow = table.getSelectedRow();
         if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "수정할 예약을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
         }
         Object[] updatedReservation = editReservation(data[selectedRow], tableModel, selectedRow);
         if (updatedReservation != null) {
            data[selectedRow] = updatedReservation;
         }
      });
      
      // 버튼 패널 구성
      JPanel buttonPanel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(5, 5, 5, 5); // 버튼 간격
      
      // 삭제 버튼: 4/5 크기
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1; // 4:1 비율
      buttonPanel.add(deleteButton, gbc);
      
      // 수정 버튼: 1/5 크기
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbc.weightx = 4; // 4:1 비율
      buttonPanel.add(editButton, gbc);
      
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(scrollPane, BorderLayout.CENTER);
      panel.add(buttonPanel, BorderLayout.SOUTH);
      
      JFrame popupFrame = new JFrame("예약 내역");
      popupFrame.setSize(800, 400);
      popupFrame.setLocationRelativeTo(null);
      popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      popupFrame.add(panel);
      popupFrame.setVisible(true);
   }
   
   
   private Object[] editReservation(Object[] reservation,
                                    DefaultTableModel tableModel, int rowIndex) {
      boolean valid = false;
      Object[] updatedReservation = reservation.clone();
      
      while (!valid) {
         JPanel inputPanel = new JPanel();
         inputPanel.setLayout(new GridLayout(7, 2, 10, 10));
         
         JLabel nameLabel = new JLabel("예약자 이름:");
         JTextField nameField = new JTextField((String) updatedReservation[0]);
         
         JLabel phoneLabel = new JLabel("전화번호:");
         JTextField phoneField = new JTextField((String) updatedReservation[1]);
         
         JLabel emailLabel = new JLabel("이메일:");
         JTextField emailField = new JTextField((String) updatedReservation[2]);
         
         JLabel birthLabel = new JLabel("생년월일 (YYYY-MM-DD):");
         JTextField birthField = new JTextField((String) updatedReservation[3]);
         
         JLabel startDateLabel = new JLabel("예약 시작 날짜 (YYYY-MM-DD):");
         JTextField startDateField = new JTextField((String) updatedReservation[4]);
         
         JLabel nightsLabel = new JLabel("예약 기간 (박):");
         int existingNights = calculateNights((String) updatedReservation[4], (String) updatedReservation[5]);
         JTextField nightsField = new JTextField(String.valueOf(existingNights));
         
         JLabel roomNoLabel = new JLabel("방 번호:");
         JTextField roomNoField = new JTextField((String) updatedReservation[6]);
         
         inputPanel.add(nameLabel);
         inputPanel.add(nameField);
         inputPanel.add(phoneLabel);
         inputPanel.add(phoneField);
         inputPanel.add(emailLabel);
         inputPanel.add(emailField);
         inputPanel.add(birthLabel);
         inputPanel.add(birthField);
         inputPanel.add(startDateLabel);
         inputPanel.add(startDateField);
         inputPanel.add(nightsLabel);
         inputPanel.add(nightsField);
         inputPanel.add(roomNoLabel);
         inputPanel.add(roomNoField);
         
         int result = JOptionPane.showConfirmDialog(null, inputPanel,
               "예약 수정", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
         if (result != JOptionPane.OK_OPTION) {
            return null; // 취소 시 창 닫기
         }
         
         try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            LocalDate birth = LocalDate.parse(birthField.getText().trim());
            LocalDate newStartDate = LocalDate.parse(startDateField.getText().trim());
            int nights = Integer.parseInt(nightsField.getText().trim());
            int newRoomNo = Integer.parseInt(roomNoField.getText().trim()); // 새 방 번호
            
            if (!validateInputs(name, phone, email, birth, newStartDate, nights, newRoomNo)) {
               continue;
            }
            
            LocalDate oldReserveDate = LocalDate.parse((String) reservation[4]);
            int oldRoomNo = Integer.parseInt((String) reservation[6]); // 기존 방 번호
            
            // 데이터 수정 요청
            boolean updateSuccessful = HotelDB.updateReservation(name, phone, email, birth,
                  newStartDate, nights, oldRoomNo, newRoomNo, oldReserveDate);
            
            if (updateSuccessful) {
               // 테이블 데이터 갱신
               LocalDate newEndDate = newStartDate.plusDays(nights - 1);
               updatedReservation[0] = name;
               updatedReservation[1] = phone;
               updatedReservation[2] = email;
               updatedReservation[3] = birth.toString();
               updatedReservation[4] = newStartDate.toString();
               updatedReservation[5] = newEndDate.toString();
               updatedReservation[6] = String.valueOf(newRoomNo);
               updatedReservation[7] = "예약중";
               
               tableModel.setValueAt(name, rowIndex, 0);
               tableModel.setValueAt(phone, rowIndex, 1);
               tableModel.setValueAt(email, rowIndex, 2);
               tableModel.setValueAt(birth.toString(), rowIndex, 3);
               tableModel.setValueAt(newStartDate.toString(), rowIndex, 4);
               tableModel.setValueAt(newEndDate.toString(), rowIndex, 5);
               tableModel.setValueAt(newRoomNo, rowIndex, 6);
               tableModel.setValueAt("예약중", rowIndex, 7);
               
               tableModel.fireTableDataChanged();
               
               JOptionPane.showMessageDialog(null, "예약이 성공적으로 수정되었습니다.");
               showRooms(LocalDate.now());
               valid = true;
            } else {
               JOptionPane.showMessageDialog(null, "이미 다른 예약과 겹칩니다. 예약 수정이 불가능합니다.",
                     "예약 수정 오류", JOptionPane.ERROR_MESSAGE);
            }
         } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "입력 중 오류가 발생했습니다: " + ex.getMessage(),
                  "오류", JOptionPane.ERROR_MESSAGE);
         }
      }
      return updatedReservation;
   }
   
   
   private int calculateNights(String startDate, String endDate) {
      try {
         LocalDate start = LocalDate.parse(startDate);
         LocalDate end = LocalDate.parse(endDate);
         return (int) java.time.temporal.ChronoUnit.DAYS.between(start, end) + 1;
      } catch (Exception e) {
         System.err.println("calculateNights 오류: " + e.getMessage());
         return 0; // 기본값 반환
      }
   }
   
   private boolean validateInputs(String name, String phone, String email,
                                  LocalDate birthDate, LocalDate startDate, int nights, int roomNo) {
      // 1. 이름 유효성 검사
      if (name.isEmpty()) {
         JOptionPane.showMessageDialog(null, "예약자 이름은 비워둘 수 없습니다.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      
      // 2. 전화번호 유효성 검사 (숫자만)
      if (!phone.matches("\\d+")) {
         JOptionPane.showMessageDialog(null, "전화번호는 숫자만 입력해야 합니다.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      
      // 3. 이메일 유효성 검사 (이메일 형식 확인)
      if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
         JOptionPane.showMessageDialog(null, "이메일 형식이 올바르지 않습니다. " +
               "예: example@example.com", "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      
      // 4. 생년월일 유효성 검사 (1900년 이상 2100년 이하)
      if (birthDate.isBefore(LocalDate.of(1900, 1, 1)) ||
            birthDate.isAfter(LocalDate.of(2100, 12, 31))) {
         JOptionPane.showMessageDialog(null, "생년월일이 올바르지 않습니다.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      
      // 5. 예약 시작일 유효성 검사
      if (startDate.isBefore(LocalDate.now())) {
         JOptionPane.showMessageDialog(null, "예약 시작일은 오늘 이후의 " +
               "날짜로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      
      // 6. 예약 기간 유효성 검사 (1박 이상)
      if (nights <= 0) {
         JOptionPane.showMessageDialog(null, "예약 기간은 1박 이상이어야 합니다.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      
      // 7. 방 번호 유효성 검사 (2층 이상 6층 이하)
      if (roomNo < 200 || roomNo > 699 || roomNo % 100 == 0 || roomNo % 100 > 6) {
         JOptionPane.showMessageDialog(null, "방 번호는 2층에서 6층까지의 유효한 " +
               "방 번호를 입력해야 합니다. 예: 201 ~ 606", "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      // 모든 유효성 검사 통과
      return true;
   }
   
   private boolean isValidName(String name) {
      if (name == null || name.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "이름을 입력해주세요.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      if (!name.matches("[a-zA-Z가-힣 ]+")) { // 영어, 한글, 공백만 허용
         JOptionPane.showMessageDialog(null, "이름은 영어, 한글만 입력할 수 있습니다.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      return true; // 유효한 이름
   }
   
   private boolean isValidEmail(String email) {
      if (email == null || email.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "이메일을 입력해주세요.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
         JOptionPane.showMessageDialog(null,
               "이메일 형식이 올바르지 않습니다. 예: example@example.com",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      return true; // 유효한 이메일
   }
   
   
   // yyyy-MM-dd 형식 유효성 검사
   private boolean isValidDate(String date) {
      if (date == null || date.trim().isEmpty()) {
         return false; // null 또는 비어 있는 경우 유효하지 않음
      }
      try {
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
         LocalDate.parse(date.trim(), formatter); // 날짜 형식 검증
         return true; // 유효한 날짜 형식
      } catch (Exception e) {
         return false; // 예외 발생 시 유효하지 않음
      }
   }
   
   // 숫자 검증
   private boolean isNumeric(String str) {
      if (str == null || str.trim().isEmpty()) {
         return false; // null 또는 비어 있는 경우 유효하지 않음
      }
      return str.matches("\\d+"); // 숫자만 포함되어 있는지 확인
   }
   
   
   private boolean validateBirthDate(String birthDate) {
      if (birthDate == null || birthDate.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "생년월일을 입력해주세요.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      if (!isValidDate(birthDate)) {
         JOptionPane.showMessageDialog(null, "생년월일이 올바르지 않습니다. 형식: YYYY-MM-DD",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      return true; // 유효한 생년월일
   }
   
   
   private boolean validateNights(String nights) {
      if (nights == null || nights.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "예약 기간을 입력해주세요.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      if (!isNumeric(nights)) {
         JOptionPane.showMessageDialog(null, "예약 기간은 숫자로 입력해야 합니다.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      if (Integer.parseInt(nights) <= 0) {
         JOptionPane.showMessageDialog(null, "예약 기간은 1박 이상이어야 합니다.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      return true; // 유효한 예약 기간
   }
   
   
   private boolean isValidPhoneNumber(String phone) {
      if (phone == null || phone.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "전화번호를 입력해주세요.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      
      // 대시(-) 없이 숫자만 포함된 경우 대시를 추가해 형식을 맞춥니다.
      phone = phone.replaceAll("-", "");
      
      // 정규식: 한국 및 국제전화번호 유효성 검사
      String regex = "^(\\+82|82|0)?(10|11|16|17|18|19|2|31|32|33|41|42|43|51|52|53|54|55|61|62|63|64)\\d{7,8}$";
      
      if (!phone.matches(regex)) {
         JOptionPane.showMessageDialog(null,
               "올바른 전화번호를 입력해주세요.\n\n" +
                     "예: +82-10-1234-5678, 010-1234-5678, 031-123-4567",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      
      return true; // 유효한 전화번호
   }
   
   
   private boolean isValidStartDate(String startDate) {
      if (startDate == null || startDate.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "예약 시작 날짜를 입력해주세요.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      if (!isValidDate(startDate)) {
         JOptionPane.showMessageDialog(null, "예약 시작 날짜가 올바르지 않습니다. 형식: YYYY-MM-DD",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      LocalDate parsedStartDate = LocalDate.parse(startDate);
      if (parsedStartDate.isBefore(LocalDate.now())) {
         JOptionPane.showMessageDialog(null, "예약 시작 날짜는 오늘 이후의 날짜여야 합니다.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      return true; // 유효한 예약 시작일
   }
   
   
   private boolean isValidRoomNumber(String roomNo) {
      if (roomNo == null || roomNo.trim().isEmpty()) {
         JOptionPane.showMessageDialog(null, "방 번호를 입력해주세요.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      if (!isNumeric(roomNo)) {
         JOptionPane.showMessageDialog(null, "방 번호는 숫자로 입력해야 합니다.",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      int parsedRoomNo = Integer.parseInt(roomNo);
      if (!isRoomNumberValid(parsedRoomNo)) {
         JOptionPane.showMessageDialog(null, "방 번호는 2층에서 6층까지의 유효한 방 번호를 입력해야 합니다. 예: 201 ~ 606",
               "입력 오류", JOptionPane.ERROR_MESSAGE);
         return false;
      }
      return true; // 유효한 방 번호
   }
   
   
   public void updateButtonsForReservationView() {
      buttonPanel.removeAll(); // 기존 버튼 제거
      
      // 이미지 버튼 생성
      JButton viewRoomsButton = createCustomImageButton("u_viewRooms.png", 150, 220, null); // 툴팁 제거
      JButton reserveButton = createCustomImageButton("u_reserve.png", 150, 220, null); // 툴팁 제거
      JButton exitButton = createCustomImageButton("u_exit.png", 150, 100, null); // 툴팁 제거
      
      viewRoomsButton.setPreferredSize(new Dimension(150, 230)); // 버튼 크기만 줄이기
      reserveButton.setPreferredSize(new Dimension(150, 230)); // 버튼 크기만 줄이기
      exitButton.setPreferredSize(new Dimension(150, 100)); // 버튼 크기만 줄이기
      
      viewRoomsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      reserveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      
      // 이벤트 핸들러 추가
      viewRoomsButton.addActionListener(e -> showRoomsWithCalendar());
      reserveButton.addActionListener(e -> reserveRoom());
      exitButton.addActionListener(e -> {
         // 팝업창 닫기
         Window window = SwingUtilities.getWindowAncestor(buttonPanel);
         if (window != null) {
            window.dispose(); // 현재 팝업 윈도우 닫기
         }
      });
      
      // 새로운 패널 생성
      JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 5, 5)); // 두 개의 버튼만 포함
      buttonsPanel.add(viewRoomsButton);
      buttonsPanel.add(reserveButton);
      
      // 메인 패널에 버튼 추가
      buttonPanel.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      
      // 버튼 패널 설정
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1.0;
      gbc.weighty = 5.0; // 상대적인 비율
      gbc.fill = GridBagConstraints.BOTH;
      buttonPanel.add(buttonsPanel, gbc);
      
      // 프로그램 종료 버튼 설정
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.weightx = 1.0;
      gbc.weighty = 1.0; // 상대적인 비율
      gbc.fill = GridBagConstraints.BOTH;
      buttonPanel.add(exitButton, gbc);
      
      // 버튼 패널 갱신
      buttonPanel.revalidate(); // 레이아웃 새로 고침
      buttonPanel.repaint(); // 화면 새로 고침
   }
   
   private JButton createCustomImageButton(String imageName, int width, int height, String tooltip) {
      // 이미지 아이콘 불러오기
      ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/" + imageName)); // 이미지 경로
      Image img = originalIcon.getImage();
      Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); // 지정한 크기로 조정
      ImageIcon scaledIcon = new ImageIcon(scaledImg); // 크기 조정된 아이콘
      
      JButton button = new JButton(scaledIcon);
      
      // 버튼 스타일 조정
      button.setBorderPainted(false); // 경계선 제거
      button.setFocusPainted(false);  // 포커스 테두리 제거
      button.setContentAreaFilled(false); // 배경 제거
      
      return button;
   }
}