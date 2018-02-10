package projects.suchushin.org.ubrainianstest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private static String DOWNLOAD_DIRECTORY_PASS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DOWNLOAD_DIRECTORY_PASS = getFilesDir().getAbsolutePath();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.fragment_container, new GeoNamesAppFragment(), "geonames_fragment").commit();
        }
    }

    static String getDownloadDirectoryPass(){
        return DOWNLOAD_DIRECTORY_PASS;
    }
}
