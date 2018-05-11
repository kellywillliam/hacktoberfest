package advprog.oricon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class OriconBlurayMostSelling {
    public static void main(String[] args) throws IOException {
        ScreenScraping ss = new ScreenScraping();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(in.readLine());

        st.nextToken();
        st.nextToken();
        String category = st.nextToken();
        String date = st.nextToken();

        if (category.equals("weekly")) {
            ss.weekly(date);
        }
        else if (category.equals("daily")) {
            ss.daily(date);
        }
    }
}
