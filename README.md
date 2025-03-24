# 🏨 호텔 예약 관리 시스템
<p align="center">
<img width="750" alt="Image" src="https://github.com/user-attachments/assets/f62deb82-60ce-409d-9b6c-aeb8e22c2d59" /><br>
</p>

<br>

- 사용자 친화적인 인터페이스를 제공하는 Java 기반 호텔 예약 및 관리 시스템
- 객실 예약, 확인, 체크인/체크아웃 등 전반적인 호텔 운영 기능 제공
- 관리자 및 일반 사용자 계정 분리로 보안성 강화
- 직관적인 GUI를 통한 편리한 사용자 경험 제공

<br>


## 🧑🧑👩 CONTRIBUTORS

<p align="center">

|팀원명   |포지션   | 담당   |깃허브 링크|
|---| ---   |---|---|
|최재영|   팀원 | 객실 예약 기능<br>방문 이력 조회 기능<br>그래픽 사용자 인터페이스(GUI)<br>   | https://github.com/CJJaeyoung/|
|김민주|   팀원|    회원가입 및 로그인 기능<br>관리자 기능 <br> |   https://github.com/dellymnzzu|

</p>

## 📋 주요 기능
### 객실 예약 시스템
- **객실 현황 조회**: 날짜별 예약 가능한 객실 확인
- **객실 예약**: 고객 정보 입력 및 객실 예약 처리
- **체크인/체크아웃 관리**: 고객 체크인 및 체크아웃 처리
- **예약 내역 조회**: 고객별, 날짜별 예약 현황 확인

### 회원 관리
- **회원가입**: 이메일, 비밀번호, 개인정보 등록
- **로그인/로그아웃**: 안전한 인증 시스템
- **비밀번호 찾기**: 이메일 기반 비밀번호 재설정
- **회원 정보 수정**: 개인정보 변경 및 업데이트

### 관리자 기능
- **객실 관리**: 객실 정보 추가, 수정, 삭제
- **예약 현황 관리**: 전체 예약 상태 모니터링
- **고객 데이터 관리**: 고객 정보 조회 및 수정
- **메모 기능**: 고객별 특이사항 기록

## 🛠️ 개발 환경 및 기술
### 언어 및 프레임워크
- **Java**: JDK 8 이상
- **Java Swing**: 사용자 인터페이스 구현
- **JCalendar**: 날짜 선택 컴포넌트

<br>

## 📦 구조

<details><summary>➡️ 플로우차트
</summary>

![Image](https://github.com/user-attachments/assets/5a80d62a-dcb3-4838-a7e3-c25673bf9454)
## 



</details>

<details><summary>📊 데이터베이스
</summary>

![Image](https://github.com/user-attachments/assets/7abfcbb7-c40c-4253-9e01-f940bbac1cbf)

## 



</details>

<details><summary>📂 디렉토리 구조
</summary>

```
📂Hotel
 └─📂src
    └─📂main
       ├─📂java
       │  └─📂hotel
       │      ├─📜AddData.java            # 데이터 추가 관련 클래스
       │      ├─📜AdminPage.java          # 관리자 페이지 UI 및 기능
       │      ├─📜DeleteData.java         # 데이터 삭제 관련 클래스
       │      ├─📜FindData.java           # 데이터 검색 관련 클래스
       │      ├─📜HistoryData.java        # 기록 데이터 관리 클래스
       │      ├─📜HotelReservationGUI.java # 메인 예약 시스템 GUI
       │      ├─📜JTableData.java         # 테이블 데이터 처리 클래스
       │      ├─📜Login.java              # 로그인 UI 및 기능
       │      ├─📜Main.java               # 메인 화면 UI 및 기능
       │      ├─📜MainFrame.java          # 메인 프레임 관리
       │      ├─📜MemoData.java           # 메모 데이터 관리 클래스
       │      ├─📜ModifyData.java         # 데이터 수정 관련 클래스
       │      ├─📜Mypage.java             # 마이페이지 UI 및 기능
       │      ├─📜Popup.java              # 팝업 창 관리 클래스
       │      ├─📜ResetPw.java            # 비밀번호 재설정 클래스
       │      ├─📜Room.java               # 객실 정보 클래스
       │      └─📜Sign_up.java            # 회원가입 UI 및 기능
       └─📂resources
          └─📂images                      # UI 이미지 리소스

```

</details>



## 📌 트러블 슈팅

<details><summary>👩‍💻 김민주
</summary>

<br>
<br>

# 1️⃣ 회원 수정 기능에서 콤보 박스 값이 정상적으로 반영되지 않는 문제

## 📝 문제 설명
회원 정보 수정 기능에서 **생성자를 과도하게 사용**한 결과, 콤보 박스의 값이 정상적으로 반영되지 않는 오류 발생

---

## 🔍 문제 발생 경과
- **데이터베이스 저장 확인**: 콤보 박스의 값은 이미 데이터베이스에 정상적으로 저장되어 있음.
- **회원 정보 수정 요청**: 사용자가 회원 정보를 수정할 때, 기존 콤보 박스 값을 변경하려고 시도함.
- **값 변경 오류 발생**: 콤보 박스의 값이 변경되지 않고, 초기 값으로 유지되는 현상 발생.
- **문제 지속**: 다른 입력 필드는 정상적으로 수정되지만, 콤보 박스 값만 변경되지 않는 문제가 계속 발생함.

---

## ⚠️ 원인
- 불필요한 생성자 호출로 인해 **새로운 객체가 생성**되면서 기존 객체의 상태가 유지되지 않았습니다.
- 수정 시 기존 객체를 변경해야 하지만 생성자 호출로 인해 값이 초기화되어 변경 사항이 반영되지 않았습니다.

---

## 🛠️ 해결 방법

**생성자 활용 방식을 점검하고 불필요한 생성자를 제거했습니다.**

---

## ✅ 결과
이 문제를 해결한 후, 사용자가 입력한 정보가 정상적으로 콤보박스에 불러졌습니다.

---





## 


</details>

<details><summary>👩‍💻 최재영
</summary>

<br>

<details><summary><h2>1️⃣ 중복 예약 방지 로직 구현의 어려움</h2>
</summary>
<hr>
 
## 📝 문제 설명

여러 사용자가 동일한 방을 같은 날짜에 예약할 경우, 중복 예약이 발생할 수 있는 문제가 있었습니다.

## 🔍 문제 발생 경과

- 사용자 A가 방을 예약하고 DB에 저장된 직후
- 사용자 B가 같은 방, 같은 날짜로 예약을 시도
- 예약 중복을 막기 위한 조건이 없어 동시에 처리될 경우 중복 예약 발생 가능성 존재

## ⚠️ 원인
- insertReservation() 호출 전, 방의 상태를 검사하는 로직이 없었습니다.
- 예약 중복 체크 로직이 없거나 불완전했고, 동시성 상황에서는 중복 저장이 가능했습니다.

## 🛠️ 해결 방법
- HotelDB.insertReservation() 호출 전, 해당 날짜에 방의 상태가 "빈방"인지 확인하는 로직을 추가했습니다.

```
String currentState = HotelDB.getRoomState(parsedRoomNo, LocalDate.now());
if (!"빈방".equals(currentState)) {
   JOptionPane.showMessageDialog(null,
         "해당 방 번호는 이미 예약되었거나 사용 중입니다.",
         "예약 불가",
         JOptionPane.WARNING_MESSAGE);
   continue;
}
```
- DB에도 **제약조건(PK 또는 Unique)**을 추가하여 논리적인 중복뿐 아니라 물리적인 중복도 방지했습니다.
## ✅ 결과
- 중복 예약이 프론트/백엔드 모두에서 차단되어, 예약 안정성이 향상 되었습니다.

<br>

</details>

<details><summary><h2> 2️⃣ 예약 수정 시 데이터 일관성 유지 문제</h2>
</summary>
<hr>
 
## 📝 문제 설명
예약 정보를 수정할 때 기존 예약 삭제 없이 새로운 예약이 추가되거나, 상태 갱신이 누락되는 문제가 발생했습니다.

## 🔍 문제 발생 경과
- 사용자가 예약 정보를 수정하면 기존 예약과 동일한 정보가 중복 삽입됨
- 일부 경우에는 이전 예약이 삭제되지 않아 상태 정보가 꼬이는 문제 발생

## ⚠️ 원인
- updateReservation() 메서드 내에서 기존 예약 정보를 정확하게 찾아 삭제하지 않고, 단순 삽입으로만 처리됐습니다.
- 예약 정보 식별 기준이 명확하지 않아 기존 데이터를 제대로 갱신하지 못했습니다.

## 🛠️ 해결 방법
- 기존 예약 정보를 찾아 정확하게 삭제한 후, 새 예약 정보를 삽입했습니다.
- 예약 테이블의 PK를 (방 번호 + 시작일)로 구성하여 데이터 무결성을 강화했습니다.
- 트랜잭션 단위 처리로 중복 또는 누락을 방지했습니다.

```
LocalDate oldReserveDate = LocalDate.parse((String) reservation[4]);
int oldRoomNo = Integer.parseInt((String) reservation[6]);
```
## ✅ 결과
- 예약 수정 시 이전 정보가 정확히 삭제되고 새 정보로 반영되어 데이터 일관성이 유지 되었습니다.

<br>

</details>

<details><summary> <h2> 3️⃣ 체크인 / 체크아웃 처리 로직 오류</h2>
</summary>
<hr>
 
## 문제 설명
예약 상태가 "예약중"인 경우에만 체크인이 가능해야 하지만, 모든 방에 대해 체크인이 허용되는 오류가 발생했습니다.

## 🔍 문제 발생 경과
- 사용자 체크인 시 상태 확인 없이 무조건 체크인 처리
- 이미 체크인된 방이나 예약되지 않은 방도 체크인 가능

## ⚠️ 원인
- 체크인 시 방의 상태를 검증하는 조건 분기가 부족했습니다.
- "예약중" 상태 확인 없이 처리되어 상태가 엉키는 경우 발생했습니다.

## 🛠️ 해결 방법
- 체크인/체크아웃 로직을 명확하게 분기 처리하여, "예약중" 상태일 때만 체크인 가능하도록 변경하였습니다.
```
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
```

- 현재 날짜 기준으로 예약 테이블과 동기화되도록 로직 정비하였습니다.

## ✅ 결과
- 체크인/체크아웃 조건이 명확하게 분리되어, 올바른 상태의 방만 체크인 가능하도록 개선 되었습니다.

<br>

</details>

<br>

## 💬 느낀 점
이번 프로젝트는 팀으로 진행한 첫 협업 경험이었고, 핵심 기능을 구현하며 서로의 코드 흐름과 설계를 맞추는 과정에서 협업의 중요성을 체감했습니다.

- 기능 간 연동 구조 고민
- UI와 로직의 분리
- 사용자 중심의 예외 처리 및 피드백 설계를 직접 경험하며 실무적인 감각을 쌓을 수 있었습니다.

무엇보다 역할이 다르더라도 데이터 구조, 상태 설계, 로직 일관성은 프로젝트의 전체 품질에 큰 영향을 준다는 사실을 배웠습니다.
이번 프로젝트는 단순 구현을 넘어서 협업, 설계, 사용성에 대한 인사이트를 얻게 된 값진 경험이었습니다.

</details>

<br>

## 👨‍ 정보
- 본 프로젝트는 자바 프로그래밍 학습 및 실습을 위해 개발되었습니다.
