package moe.ibox.parkingmanagement.entity;

import java.util.Date;

public class Plate {
    private String rfid;
    private String plate;
    private boolean approached;
    private Date approachTime;

    public Plate() {
    }

    public Plate(String rfid, String plate, boolean is_approached, Date approach_time) {
        this.rfid = rfid;
        this.plate = plate;
        this.approached = is_approached;
        this.approachTime = approach_time;
    }

    public String getRfid() {
        return rfid;
    }

    public String getPlate() {
        return plate;
    }

    public boolean isApproached() {
        return approached;
    }

    public Date getApproachTime() {
        return approachTime;
    }

    public Plate setRfid(String rfid) {
        this.rfid = rfid;
        return this;
    }

    public Plate setPlate(String plate) {
        this.plate = plate;
        return this;
    }

    public Plate setApproached(boolean approached) {
        this.approached = approached;
        return this;
    }

    public Plate setApproachTime(Date approachTime) {
        this.approachTime = approachTime;
        return this;
    }
}
