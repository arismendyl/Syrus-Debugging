package com.example.syrus;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jakewharton.threetenabp.AndroidThreeTen;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Time;


public class MainActivity extends AppCompatActivity {

    private Button btnGPS;
    private TextView tv1;
    private EditText car;
    String Message1=">REV002044275600+1301956-0778515000000012;ID=0000<";
    String ID;
    long timer1= System.currentTimeMillis();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_main);
        //java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        tv1= (TextView)findViewById(R.id.coordenadas);
        btnGPS= (Button)findViewById(R.id.buttonGPS);
        car= (EditText) findViewById(R.id.carid);
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID="ID=";
                ID=ID+car.getText().toString();
                // Acquire a reference to the system Location Manager
                LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

                // Define a listener that responds to location updates
                                LocationListener locationListener = new LocationListener() {

                                    public void onLocationChanged(Location location) {
                                        LocalDate today = LocalDate.now ( ZoneOffset.UTC );  // Using UTC to match the definition of the Java epoch.
                                        //LocalDate epoch = LocalDate.ofEpochDay ( 0 );
                                        LocalDate epoch = LocalDate.parse("1980-01-06");
                                        long weeks = ChronoUnit.WEEKS.between ( epoch , today );
                                        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                                        //tv1.setText(""+location.getLatitude()+" "+location.getLongitude()+" "+" "+calendar.get(Calendar.DAY_OF_WEEK)+" "+System.currentTimeMillis()/1000);
                                        String A= "00";
                                        long B1=weeks;
                                        String B= String.valueOf(B1);
                                        Calendar gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        int C1= gmtCalendar.get(Calendar.DAY_OF_WEEK)-1;
                                        //int C1=calendar.get(Calendar.DAY_OF_WEEK)-1;
                                        String C=String.valueOf(C1);
                                        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        Calendar midnight = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

                                        midnight.set(Calendar.HOUR_OF_DAY, 0);
                                        midnight.set(Calendar.MINUTE, 0);
                                        midnight.set(Calendar.SECOND, 0);
                                        midnight.set(Calendar.MILLISECOND, 0);

                                        long diffeerence  = now.getTimeInMillis() - midnight.getTimeInMillis();
                                        diffeerence=diffeerence/1000;
                                        int time = (int) (diffeerence);
                                        String D=String.format("%05d", time);
                                        double lat=location.getLatitude();
                                        double lon=location.getLongitude();
                                        int E1=(int)lat;
                                        String E=String.format("%02d", E1);
                                        double F1=(lat-E1)*100000;
                                        int F2=Math.abs((int)F1);
                                        String F=String.format("%05d", F2);
                                        int G1=(int)lon;
                                        String G=String.format("%04d", G1);
                                        double H1=(lon-G1)*100000;
                                        int H2=Math.abs((int)H1);
                                        String H=String.format("%05d", H2);
                                        String I=String.format("%03d", 000);
                                        String J=String.format("%03d", 000);
                                        String K="1";
                                        String L="2";
                                        String Message1=">REV"+A+B+C+D+E+F+G+H+I+J+K+L+";"+ID+"<";
                                        if (E1>=0) {
                                            Message1=">REV"+A+B+C+D+"+"+E+F+G+H+I+J+K+L+";"+ID+"<";
                                        }
                                        if (G1>=0) {
                                            Message1=">REV"+A+B+C+D+E+F+"+"+G+H+I+J+K+L+";"+ID+"<";
                                        }
                                        if (G1>=0 && E1>=0) {
                                            Message1=">REV"+A+B+C+D+"+"+E+F+"+"+G+H+I+J+K+L+";"+ID+"<";
                                        }
                                        tv1.setText(Message1);
                                        long timer2= System.currentTimeMillis();
                                        if ((timer2-timer1)>=5000) {
                                            try {
                                                timer1= System.currentTimeMillis();
                                                int port = 1500;
                                                InetAddress adresse = InetAddress.getByName("52.206.207.241");
                                                DatagramSocket socket;


                                                String leMessage = Message1;
                                                int longueur = leMessage.length();
                                                byte[] message = new byte[longueur];
                                                message = leMessage.getBytes();


                                                socket = new DatagramSocket();
                                                DatagramPacket packet = new DatagramPacket(message, longueur, adresse, port);
                                                socket.send(packet);
                                                System.out.println("test d'execution");
                                                socket.close();
                                            } catch (UnknownHostException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            } catch (SocketException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                                    public void onProviderEnabled(String provider) {}

                                    public void onProviderDisabled(String provider) {}



                                };

                // Register the listener with the Location Manager to receive location updates
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);



            }
        });
                // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck== PackageManager.PERMISSION_DENIED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
        }

    }
}

