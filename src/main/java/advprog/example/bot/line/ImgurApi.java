package advprog.example.bot.line;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

public class ImgurApi {
    public static final String UPLOAD_API_URL = "https://api.imgur.com/3/image";
    public static final String ALBUM_API_URL = "https://api.imgur.com/3/album";
    public static final int MAX_UPLOAD_ATTEMPTS = 3;

    //CHANGE TO @CLIENT_ID@ and replace with buildscript.
    private static final String CLIENT_ID = "a24d48b12a28063";
    /**
     * Takes a file and uploads it to Imgur.
     * Does not check to see if the file is an image, this should be done
     * before the file is passed to this method.
     *
     * @param file
     *          The image to be uploaded to Imgur.
     * @return
     *          The JSON response from Imgur.
     */

    public static String upload(File file) {
        HttpURLConnection conn = getHttpConnection(UPLOAD_API_URL);
        writeToConnection(conn, "image=" + toBase64(file));
        return getResponse(conn);
    }

    /**
     * Creates and sets up an HttpURLConnection for use with the Imgur API.
     *
     * @param url
     *          The URL to connect to. (check Imgur API for correct URL).
     * @return
     *          The newly created HttpURLConnection.
     */
    private static HttpURLConnection getHttpConnection(String url) {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            conn.setReadTimeout(100000);
            conn.connect();
            return conn;
        } catch (UnknownHostException e) {
            throw new WebException(StatusCode.UNKNOWN_HOST, e);
        } catch (IOException e) {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
    }
    /**
     * Sends the provided message to the connection as uploaded data.
     *
     * @param conn
     *          The connection to send the data to.
     * @param message
     *          The data to upload.
     */

    private static void writeToConnection(HttpURLConnection conn, String message) {
        OutputStreamWriter writer;
        try {
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
    }

    private static String toBase64(File file) {
        try {
            byte[] b = new byte[(int) file.length()];
            FileInputStream fs = new FileInputStream(file);
            fs.read(b);
            fs.close();
            return URLEncoder.encode(DatatypeConverter.printBase64Binary(b), "UTF-8");
        } catch (IOException e) {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
    }

    private static String getResponse(HttpURLConnection conn) {
        StringBuilder str = new StringBuilder();
        BufferedReader reader;
        try {
            if (conn.getResponseCode() != StatusCode.SUCCESS.getHttpCode()) {
                throw new WebException(conn.getResponseCode());
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new WebException(StatusCode.UNKNOWN_ERROR, e);
        }
        if (str.toString().equals("")) {
            throw new WebException(StatusCode.UNKNOWN_ERROR);
        }
        return str.toString();
    }



}
