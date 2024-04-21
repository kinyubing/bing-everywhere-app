package cn.edu.hrbcu.everywhereapp.entity;

public class BusStop {
    private Integer id;
    private Integer pos;
    private Double longitutde;
    private Double latitude;
    private String siteName;
    private String busname;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public Integer getPos() {
        return pos;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLongitutde(Double longitutde) {
        this.longitutde = longitutde;
    }

    public Double getLongitutde() {
        return longitutde;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getBusname() {
        return busname;
    }

    public void setBusname(String busname) {
        this.busname = busname;
    }

    @Override
    public String toString() {
        return "BusStop{" +
                "id=" + id +
                ", pos=" + pos +
                ", longitutde=" + longitutde +
                ", latitude=" + latitude +
                ", siteName='" + siteName + '\'' +
                ", busname='" + busname + '\'' +
                '}';
    }

    public BusStop(Integer id, Integer pos, Double longitutde, Double latitude, String siteName, String busname) {
        this.id = id;
        this.pos = pos;
        this.longitutde = longitutde;
        this.latitude = latitude;
        this.siteName = siteName;
        this.busname = busname;
    }

    public BusStop() {
    }
}
