package hotel;

import java.sql.*;
import java.time.LocalDate;


class Guest {
   private String id;
   private String tel;
   private String email;
   private LocalDate birthDate;

   public Guest(String id, String tel, String email, LocalDate birthDate) {
      this.id = id;
      this.tel = tel;
      this.email = email;
      this.birthDate = birthDate; // 생년월일 초기화
   }

   public String getId() {
      return id;
   }

   public String getTel() {
      return tel;
   }

   public String getEmail() {
      return email;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }

   @Override
   public String toString() {
      return "Guest {" +
              " 예약자명 : " + id +
              ", 연락처 : " + tel +
              ", 이메일 : " + email +
              ", 생년월일 : " + birthDate +
              '}';
   }
}

class Reserve {
   private LocalDate reserveDate;
   private int reserveRoomNo;
   private String guestName;
   private String guestTel;
   private String guestEmail;
   private LocalDate birthDate;

   public Reserve(LocalDate reserveDate, int reserveRoomNo, String guestName,
                  String guestTel, String guestEmail, LocalDate birthDate) {
      this.reserveDate = reserveDate;
      this.reserveRoomNo = reserveRoomNo;
      this.guestName = guestName;
      this.guestTel = guestTel;
      this.guestEmail = guestEmail;
      this.birthDate = birthDate;
   }

   public LocalDate getReserveDate() {
      return reserveDate;
   }

   public int getReserveRoomNo() {
      return reserveRoomNo;
   }

   public String getGuestName() {
      return guestName;
   }

   public String getGuestTel() {
      return guestTel;
   }

   public String getGuestEmail() {
      return guestEmail;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }
}

class HotelDB {
   static LocalDate today = LocalDate.now();
   private static final String booking1 = "○ ";
   private static final String booking2 = "X ";
   private static final String booking3 = "● ";
   private static Room[][] room = new Room[5][6];

   // MySQL 연결 정보
   private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "1234";

   public static Connection getConnection() throws SQLException {
      return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
   }

   
   public static void insertReservation(Reserve newReserve, int nights) {
      LocalDate startDate = newReserve.getReserveDate();
      LocalDate endDate = startDate.plusDays(nights - 1); // 종료 날짜 계산

      String overlapCheckQuery = "SELECT COUNT(*) FROM reservationtbl " +
              "WHERE roomNo = ? AND (" +
              "(reserveDate <= ? AND endDate >= ?) OR " + // 새 예약 종료 날짜가 기존 예약 시작 날짜와 겹침
              "(reserveDate <= ? AND endDate >= ?) OR " + // 새 예약 시작 날짜가 기존 예약 종료 날짜와 겹침
              "(reserveDate >= ? AND endDate <= ?))";    // 기존 예약이 새 예약 범위 안에 포함됨

      String insertQuery = "INSERT INTO reservationtbl (rName, tel, email, birthDate, reserveDate, endDate, roomNo, state) " +
              "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

      try (Connection conn = getConnection();
           PreparedStatement overlapCheckStmt = conn.prepareStatement(overlapCheckQuery);
           PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

         // 날짜 중복 확인
         overlapCheckStmt.setInt(1, newReserve.getReserveRoomNo());
         overlapCheckStmt.setDate(2, java.sql.Date.valueOf(endDate));
         overlapCheckStmt.setDate(3, java.sql.Date.valueOf(startDate));
         overlapCheckStmt.setDate(4, java.sql.Date.valueOf(startDate));
         overlapCheckStmt.setDate(5, java.sql.Date.valueOf(endDate));
         overlapCheckStmt.setDate(6, java.sql.Date.valueOf(startDate));
         overlapCheckStmt.setDate(7, java.sql.Date.valueOf(endDate));

         try (ResultSet rs = overlapCheckStmt.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
               System.out.println("이미 해당 기간에 예약된 방입니다. 다른 날짜나 방을 선택하세요.");
               return; // 중복 예약 방지
            }
         }

         // 예약 저장
         insertStmt.setString(1, newReserve.getGuestName());
         insertStmt.setString(2, newReserve.getGuestTel());
         insertStmt.setString(3, newReserve.getGuestEmail());
         insertStmt.setDate(4, java.sql.Date.valueOf(newReserve.getBirthDate()));
         insertStmt.setDate(5, java.sql.Date.valueOf(startDate));
         insertStmt.setDate(6, java.sql.Date.valueOf(endDate)); // 종료 날짜 추가
         insertStmt.setInt(7, newReserve.getReserveRoomNo());
         insertStmt.setString(8, "예약중");

         insertStmt.executeUpdate();

         // 로그 출력
         System.out.println("예약 저장되었습니다. 시작일 : " + startDate +
                 ", 종료일 : " + endDate +
                 ", 방번호 : " + newReserve.getReserveRoomNo());
      } catch (SQLException e) {
         System.err.println("insertReservation 오류: " + e.getMessage());
      }
   }


   public static String fetchReservations(String filterType, String filterValue) {
      StringBuilder result = new StringBuilder();
      String query;

      if ("date".equalsIgnoreCase(filterType)) {
         query = "SELECT * FROM reservationtbl WHERE reserveDate = ?";
      } else if ("room".equalsIgnoreCase(filterType)) {
         query = "SELECT * FROM reservationtbl WHERE roomNo = ?";
      } else if ("all".equalsIgnoreCase(filterType)) {
         query = "SELECT * FROM reservationtbl";
      } else {
         return "잘못된 조회 방식입니다.\n";
      }

      try (Connection conn = getConnection();
           PreparedStatement pstmt = conn.prepareStatement(query)) {
         if (!"all".equalsIgnoreCase(filterType)) {
            pstmt.setString(1, filterValue);
         }

         try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
               result.append("예약자명: ").append(rs.getString("rName"))
                       .append(", 전화번호: ").append(rs.getString("tel"))
                       .append(", 이메일: ").append(rs.getString("email"))
                       .append(", 생년월일: ").append(rs.getDate("birthDate"))
                       .append(", 예약일: ").append(rs.getDate("reserveDate"))
                       .append(", 종료일: ").append(rs.getDate("endDate"))
                       .append(", 방 번호: ").append(rs.getInt("roomNo"))
                       .append(", 상태: ").append(rs.getString("state"))
                       .append("\n");
            }
         }
      } catch (SQLException e) {
         return "데이터 조회 중 오류: " + e.getMessage() + "\n";
      }

      return result.toString();
   }


   public static void handleCheckIn(int roomNo) {
      String checkQuery = "SELECT state, reserveDate FROM reservationtbl WHERE roomNo = ? AND reserveDate <= ?";
      String updateQuery = "UPDATE reservationtbl SET state = '체크인' WHERE roomNo = ? AND (reserveDate = ? OR state = '체크아웃')";
      LocalDate today = LocalDate.now(); // 오늘 날짜

      try (Connection conn = getConnection();
           PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
         checkStmt.setInt(1, roomNo);
         checkStmt.setDate(2, java.sql.Date.valueOf(today));
         try (ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next()) {
               String currentState = rs.getString("state");
               System.out.println("현재 상태: " + currentState);

               if ("체크인".equals(currentState)) {
                  System.out.println("이미 체크인 상태입니다.");
               } else if ("체크아웃".equals(currentState)) {
                  // 체크아웃 상태에서 바로 체크인 처리
                  try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                     updateStmt.setInt(1, roomNo);
                     updateStmt.setDate(2, java.sql.Date.valueOf(today));
                     updateStmt.executeUpdate();
                     System.out.println("체크인 완료. 방 번호: " + roomNo);

                     // 배열 상태 업데이트
                     int floorIndex = (roomNo / 100) - 2;
                     int roomIndex = (roomNo % 100) - 1;
                     if (floorIndex >= 0 && floorIndex < room.length &&
                             roomIndex >= 0 && roomIndex < room[floorIndex].length) {
                        room[floorIndex][roomIndex].setState("체크인");
                     }
                  }
               } else if ("예약중".equals(currentState)) {
                  // 예약중 상태에서 체크인 처리
                  try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                     updateStmt.setInt(1, roomNo);
                     updateStmt.setDate(2, java.sql.Date.valueOf(today));
                     int updatedRows = updateStmt.executeUpdate();
                     if (updatedRows > 0) {
                        System.out.println("체크인 완료. 방 번호: " + roomNo);

                        // 배열 상태 업데이트
                        int floorIndex = (roomNo / 100) - 2;
                        int roomIndex = (roomNo % 100) - 1;
                        if (floorIndex >= 0 && floorIndex < room.length &&
                                roomIndex >= 0 && roomIndex < room[floorIndex].length) {
                           room[floorIndex][roomIndex].setState("체크인");
                        }
                     } else {
                        System.out.println("체크인 실패. 조건에 맞는 예약이 없습니다.");
                     }
                  }
               } else {
                  System.out.println("예약이 없습니다. 방을 예약 후 체크인하세요.");
               }
            } else {
               System.out.println("해당 방 번호와 날짜로 예약된 기록이 없습니다.");
            }
         }
      } catch (SQLException e) {
         System.err.println("체크인 처리 중 오류: " + e.getMessage());
      }
   }

   
   public static void handleCheckOut(int roomNo) {
      String checkQuery = "SELECT * FROM reservationtbl WHERE roomNo = ? AND state = '체크인';";
      String updateQuery = "UPDATE reservationtbl SET state = '체크아웃' WHERE roomNo = ? AND state = '체크인'";
      String insertCheckoutQuery = "INSERT INTO checkoutDB (rName, tel, email, birthDate, reserveDate, roomNo, state, endDate) " +
              "VALUES (?, ?, ?, ?, ?, ?, '체크아웃', ?);"; // 여기에서 상태를 '체크아웃'으로 고정
      String deleteReservationQuery = "DELETE FROM reservationtbl WHERE roomNo = ? AND state = '체크아웃';";

      LocalDate today = LocalDate.now(); // 오늘 날짜

      try (Connection conn = getConnection();
           PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

         checkStmt.setInt(1, roomNo);
         try (ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next()) {
               // 고객 정보 읽기
               String rName = rs.getString("rName");
               String tel = rs.getString("tel");
               String email = rs.getString("email");
               LocalDate birthDate = rs.getDate("birthDate").toLocalDate();
               LocalDate reserveDate = rs.getDate("reserveDate").toLocalDate();
               LocalDate endDate = rs.getDate("endDate").toLocalDate(); // 종료 날짜 추가

               // 체크아웃 상태로 변경
               try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                  updateStmt.setInt(1, roomNo);
                  updateStmt.executeUpdate();
               }

               // 기록을 checkoutDB로 옮기기
               try (PreparedStatement insertCheckoutStmt = conn.prepareStatement(insertCheckoutQuery)) {
                  insertCheckoutStmt.setString(1, rName);
                  insertCheckoutStmt.setString(2, tel);
                  insertCheckoutStmt.setString(3, email);
                  insertCheckoutStmt.setDate(4, java.sql.Date.valueOf(birthDate));
                  insertCheckoutStmt.setDate(5, java.sql.Date.valueOf(reserveDate));
                  insertCheckoutStmt.setInt(6, roomNo);
                  insertCheckoutStmt.setDate(7, java.sql.Date.valueOf(endDate)); // 종료 날짜 추가
                  insertCheckoutStmt.executeUpdate();
               }

               // reservationtbl에서 기록 삭제
               try (PreparedStatement deleteStmt = conn.prepareStatement(deleteReservationQuery)) {
                  deleteStmt.setInt(1, roomNo);
                  deleteStmt.executeUpdate();
               }

               System.out.println("체크아웃 완료. 방 번호: " + roomNo);

               // 배열 상태를 "빈방"으로 변경
               int floorIndex = (roomNo / 100) - 2;
               int roomIndex = (roomNo % 100) - 1;
               if (floorIndex >= 0 && floorIndex < room.length &&
                       roomIndex >= 0 && roomIndex < room[floorIndex].length) {
                  room[floorIndex][roomIndex].setState("빈방");
               }
            } else {
               System.out.println("해당 방 번호로 체크인된 상태가 아닙니다.");
            }
         }
      } catch (SQLException e) {
         System.err.println("체크아웃 처리 중 오류: " + e.getMessage());
      }
   }


   public static void roomSet() {
      for (int i = 0; i < room.length; i++) {
         for (int j = 0; j < room[i].length; j++) {
            int roomNo = (i + 2) * 100 + (j + 1);
            room[i][j] = new Room(roomNo); // Room 객체 생성
         }
      }
   }

   
   public static String getRoomState(int roomNo, LocalDate selectedDate) {
      String query = "SELECT state FROM reservationtbl " +
              "WHERE roomNo = ? AND reserveDate <= ? AND endDate >= ? LIMIT 1";

      try (Connection conn = getConnection();
           PreparedStatement pstmt = conn.prepareStatement(query)) {
         pstmt.setInt(1, roomNo);
         pstmt.setDate(2, java.sql.Date.valueOf(selectedDate));
         pstmt.setDate(3, java.sql.Date.valueOf(selectedDate));

         try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
               return rs.getString("state"); // 예약 상태 반환
            }
         }
      } catch (SQLException e) {
         System.err.println("getRoomState 오류: " + e.getMessage());
      }
      return "빈방"; // 해당 날짜에 예약이 없으면 빈방 반환
   }
   
   
   public static boolean updateReservation(String name, String phone, String email, LocalDate birth,
                                           LocalDate newStartDate, int nights, int oldRoomNo, int newRoomNo, LocalDate oldReserveDate) {
      String overlapCheckQuery = "SELECT COUNT(*) FROM reservationtbl " +
            "WHERE roomNo = ? AND (" +
            "(reserveDate <= ? AND endDate >= ?) OR " +
            "(reserveDate <= ? AND endDate >= ?) OR " +
            "(reserveDate >= ? AND endDate <= ?)) " +
            "AND (reserveDate != ? OR roomNo != ?)";
      String updateQuery = "UPDATE reservationtbl SET rName = ?, tel = ?, email = ?, birthDate = ?, " +
            "reserveDate = ?, endDate = ?, roomNo = ? WHERE roomNo = ? AND reserveDate = ?";
      
      try (Connection conn = getConnection();
           PreparedStatement overlapCheckStmt = conn.prepareStatement(overlapCheckQuery);
           PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
         
         LocalDate newEndDate = newStartDate.plusDays(nights - 1);
         
         // 중복 체크 (자신의 예약은 제외)
         overlapCheckStmt.setInt(1, newRoomNo);
         overlapCheckStmt.setDate(2, java.sql.Date.valueOf(newEndDate));
         overlapCheckStmt.setDate(3, java.sql.Date.valueOf(newStartDate));
         overlapCheckStmt.setDate(4, java.sql.Date.valueOf(newStartDate));
         overlapCheckStmt.setDate(5, java.sql.Date.valueOf(newEndDate));
         overlapCheckStmt.setDate(6, java.sql.Date.valueOf(newStartDate));
         overlapCheckStmt.setDate(7, java.sql.Date.valueOf(newEndDate));
         overlapCheckStmt.setDate(8, java.sql.Date.valueOf(oldReserveDate)); // 자신 예약일 제외
         overlapCheckStmt.setInt(9, oldRoomNo); // 자신 방번호 제외
         
         try (ResultSet rs = overlapCheckStmt.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
               return false; // 중복 예약이 있는 경우
            }
         }
         
         // 예약 업데이트
         updateStmt.setString(1, name);
         updateStmt.setString(2, phone);
         updateStmt.setString(3, email);
         updateStmt.setDate(4, java.sql.Date.valueOf(birth));
         updateStmt.setDate(5, java.sql.Date.valueOf(newStartDate));
         updateStmt.setDate(6, java.sql.Date.valueOf(newEndDate));
         updateStmt.setInt(7, newRoomNo); // 새로운 방 번호
         updateStmt.setInt(8, oldRoomNo); // 기존 방 번호
         updateStmt.setDate(9, java.sql.Date.valueOf(oldReserveDate));
         
         int updatedRows = updateStmt.executeUpdate();
         return updatedRows > 0;
      } catch (SQLException e) {
         System.err.println("updateReservation 오류: " + e.getMessage());
         return false;
      }
   }
   
   
   
   public static void deleteReservation(int roomNo, LocalDate reserveDate) {
      String deleteQuery = "DELETE FROM reservationtbl WHERE roomNo = ? AND reserveDate = ?";
      try (Connection conn = getConnection();
           PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

         pstmt.setInt(1, roomNo);
         pstmt.setDate(2, java.sql.Date.valueOf(reserveDate));

         int rowsDeleted = pstmt.executeUpdate();
         if (rowsDeleted > 0) {
            System.out.println("예약이 삭제되었습니다. 방 번호: " + roomNo + ", 예약 날짜: " + reserveDate);
         } else {
            System.out.println("삭제할 예약을 찾을 수 없습니다.");
         }
      } catch (SQLException e) {
         System.err.println("deleteReservation 오류: " + e.getMessage());
      }
   }
}