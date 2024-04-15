package cn.edu.hrbcu.everywhereapp.entity;

// {"note":"","changetime":"2024-04-11 21:26:39","latitude":45.123498,"name":"南区线","longtitude":123.45334,"id":3}
public class BusLocation {
    private int id;
    private String name;
    private Double longtitude;
    private Double latitude;
    private String changetime;
    private String note;

    public BusLocation(int id, String name, Double longtitude, Double latitude, String changetime, String note) {
        this.id = id;
        this.name = name;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.changetime = changetime;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getChangetime() {
        return changetime;
    }

    public void setChangetime(String changetime) {
        this.changetime = changetime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "BusLocation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                ", changetime='" + changetime + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
