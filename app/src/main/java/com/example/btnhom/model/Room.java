package com.example.btnhom.model;

public class Room {
    private String roomCode;
    private String roomName;
    private double rentalPrice;
    private String status; // "Còn trống" or "Đã thuê"
    private String tenantName;
    private String phoneNumber;

    public Room(String roomCode, String roomName, double rentalPrice, String status,
                String tenantName, String phoneNumber) {
        this.roomCode = roomCode;
        this.roomName = roomName;
        this.rentalPrice = rentalPrice;
        this.status = status;
        this.tenantName = tenantName;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomCode='" + roomCode + '\'' +
                ", roomName='" + roomName + '\'' +
                ", rentalPrice=" + rentalPrice +
                ", status='" + status + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
