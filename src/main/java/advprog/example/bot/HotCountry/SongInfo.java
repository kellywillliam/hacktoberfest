package advprog.example.bot.HotCountry;

public class SongInfo {
    private String songArtist;
    private String songTitle;
    private int rank;

    public SongInfo(String songArtist, String songTitle, int rank) {
        this.songArtist = songArtist;
        this.songTitle = songTitle;
        this.rank = rank;
    }

    public String getSongArtist() {
        return this.songArtist;
    }

    public String getSongTitle() {
        return this.songTitle;
    }

    public int getRank() {
        return this.rank;
    }
}
