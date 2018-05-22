package advprog.example.bot.countryhot;

import org.jetbrains.annotations.NotNull;

public class Hospital implements Comparable<Hospital>{
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String phone;
    private String imageLink;
    private String description;
    private int distance = -1;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
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

    @Override
    public int compareTo(@NotNull Hospital hospital) {
        return this.distance - hospital.distance;
    }
}
