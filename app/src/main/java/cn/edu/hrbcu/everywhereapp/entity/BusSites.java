package cn.edu.hrbcu.everywhereapp.entity;

//{"note":"2","pos":2,"longitutde":126.4546,"latitude":45.676454,"siteName":"爱建站","busname":"工大线","id":2},
public class BusSites {
    private int id;
    private String busname;
    private Double longitutde;
    private Double latitude;
    private Integer pos;
    private String siteName;
    private String note;

    public BusSites(int id, String busname, Double longitutde, Double latitude, Integer pos, String siteName, String note) {
        this.id = id;
        this.busname = busname;
        this.longitutde = longitutde;
        this.latitude = latitude;
        this.pos = pos;
        this.siteName = siteName;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusname() {
        return busname;
    }

    public void setBusname(String busname) {
        this.busname = busname;
    }

    public Double getLongitutde() {
        return longitutde;
    }

    public void setLongitutde(Double longitutde) {
        this.longitutde = longitutde;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "BusSites{" +
                "id=" + id +
                ", busname='" + busname + '\'' +
                ", longitutde=" + longitutde +
                ", latitude=" + latitude +
                ", pos=" + pos +
                ", siteName='" + siteName + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
