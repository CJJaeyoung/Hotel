package hotel;

class Room {
    private int roomNo;
    private String state;

    public Room(int roomNo) {
        this.roomNo = roomNo;
        this.state = "빈방"; // 기본 상태는 "빈방"
    }

    public int getRoomNo() {
        return roomNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Room{" +
                "방번호=" + roomNo +
                ", 상태='" + state + '\'' +
                '}';
    }
}