package advprog.example.bot.countryhot;

import org.jetbrains.annotations.NotNull;

public class Hospital implements Comparable<Hospital> {
    private String address;
    private String email;
    private int id;
    private String description;
    private String kecamatan;
    private String kelurahan;
    private String kodePos;
    private String kota;
    private double latitude;
    private double longitude;
    private String name;
    private String noFax;
    private String phone;
    private String web;
    private String imageLink;

    private int distance = -1;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getKota() {
        return kota;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getDescription() {
        return description;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public String getKodePos() {
        return kodePos;
    }

    public String getNoFax() {
        return noFax;
    }

    public String getWeb() {
        return web;
    }

    @Override
    public int compareTo(@NotNull Hospital hospital) {
        return this.distance - hospital.distance;
    }
}
