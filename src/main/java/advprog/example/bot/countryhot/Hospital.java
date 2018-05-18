package advprog.example.bot.countryhot;

public class Hospital {
    String alamat;
    int id;
    String jenis;
    String kota;
    long latitude;
    long longitude;
    String nama;
    String image;

    public Hospital(String alamat, int id, String jenis, String kota, long latitude, long longitude, String nama, String image) {
        this.alamat = alamat;
        this.id = id;
        this.jenis = jenis;
        this.kota = kota;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nama = nama;
        this.image = image;
    }

    public String getAlamat() {
        return alamat;
    }

    public int getId() {
        return id;
    }

    public String getJenis() {
        return jenis;
    }

    public String getKota() {
        return kota;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public String getNama() {
        return nama;
    }

    public String getImage() {
        return image;
    }
}
