package patrick.pramedia.wire.modul;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import patrick.pramedia.wire.adapter.InterfacePBDevice;

/**
 * Created by LAB-PC on 14/09/2018.
 */

public class Globals {

    //private String server = "http://10.0.2.2/WiRe/"; //localhost
    private String server = "http://192.168.0.25/WiRe/"; //localhost

    public String getServer(){
        return this.server;
    }

    public static InterfacePBDevice id;

    public void selectValueComboBox(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public String generateRandomString(){
        String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<16; i++){
            Random r = new Random();
            int number = r.nextInt(CHAR_LIST.length());
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    public String generateRandomInt(){
        String CHAR_LIST = "1234567890";
        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<16; i++){
            Random r = new Random();
            int number = r.nextInt(CHAR_LIST.length());
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public String KursIndonesia(double harga){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(harga);
    }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getPhoto(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public Bitmap img_frompath(ImageView komponen, String pathName){
        Bitmap bmp = BitmapFactory.decodeFile(pathName);
        DisplayMetrics dm = new DisplayMetrics();

        komponen.setMinimumHeight(dm.heightPixels);
        komponen.setMinimumWidth(dm.widthPixels);
        komponen.setImageBitmap(bmp);

        return bmp;
    }

    public String tanggal_sekarang_format_genah(Date cur_date){
        SimpleDateFormat Format = new SimpleDateFormat("dd MMM yyyy");
        String formatTgl = Format.format(cur_date);
        return formatTgl;
    }

    public String tanggal_sekarang(){
        Date cur_date = new Date();
        SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
        String formatTgl = Format.format(cur_date);
        return formatTgl;
    }

    public String waktu_sekarang(){
        Date cur_date = new Date();
        SimpleDateFormat Format = new SimpleDateFormat("HH:mm:ss");
        String formatTgl = Format.format(cur_date);
        return formatTgl;
    }

    public String FormatTanggalWaktuBagus(Date tanggal){
        SimpleDateFormat Format = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
        String formatTgl = Format.format(tanggal);
        return formatTgl;
    }

    public String tgl_waktu(Date tanggal){
        SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatTgl = Format.format(tanggal);
        return formatTgl;
    }

    public long waktu_long(){
        Date cur_date = new Date();
        long milliseconds = cur_date.getTime();
        return milliseconds;
    }

    public Date long_waktu(long waktu){
        Date d = new Date(waktu);
        return d;
    }

    public String SelisihWaktu(Date waktuSatu, Date waktuDua){
        long selisihMS = Math.abs(waktuSatu.getTime() - waktuDua.getTime());
        long selisihDetik = selisihMS / 1000 % 60;
        long selisihMenit = selisihMS / (60 * 1000) % 60;
        long selisihJam = selisihMS / (60 * 60 * 1000) % 24;
        long selisihHari = selisihMS / (24 * 60 * 60 * 1000);
        String selisih = selisihHari + " Hari " + selisihJam + " Jam " + selisihMenit + " Menit " + selisihDetik + " Detik";
        return selisih;
    }

    public String DateToString(Date tanggal){
        SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");
        String formatTgl = Format.format(tanggal);
        return formatTgl;
    }

    public Date StringToDate(String input) throws Exception{
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date)formater.parse(input);
        return date;
    }

    public long selisih_hari(String tgl_sekarang, String tgl_acuan){
        long hari = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date firstDate = sdf.parse(tgl_acuan);
            Date secondDate = sdf.parse(tgl_sekarang);

            long diff = secondDate.getTime() - firstDate.getTime();
            //diff in days
            hari = diff / (24 * 60 * 60 * 1000);
        }catch (Exception e){
            Log.d("ERROR", e.getMessage());
        }
        return hari;
    }

    public String menambah_tanggal(String tanggal, int jml_tambahan)throws  Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar cal = Calendar.getInstance();
        cal.setTime( dateFormat.parse(tanggal));
        cal.add( Calendar.DATE, jml_tambahan );
        String convertedDate = dateFormat.format(cal.getTime());
        return convertedDate;
    }

    public boolean hasActiveInternetConnection(Context context, String url) {
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL(url).openConnection());
            urlc.setRequestProperty("User-Agent", url);
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1500);
            urlc.connect();
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            Toast.makeText(context.getApplicationContext(), "Error checking internet connection", Toast.LENGTH_LONG).show();
        }
        return false;
    }


}
