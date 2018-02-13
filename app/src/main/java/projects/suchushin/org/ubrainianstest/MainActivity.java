package projects.suchushin.org.ubrainianstest;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    Fragment geonamesFragment;
    Fragment downloadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            geonamesFragment = new GeoNamesAppFragment();
            downloadFragment = new DownloadFileFragment();

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, geonamesFragment, "geonames_fragment")
                    .add(R.id.fragment_container, downloadFragment, "download_fragment")
                    .commit();
        } else {
            geonamesFragment = getFragmentManager().findFragmentByTag("geonames_fragment");
            downloadFragment = getFragmentManager().findFragmentByTag("download_fragment");
        }

        final BottomNavigationView navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Menu menu = navigation.getMenu();
                MenuItem item1 = menu.findItem(R.id.geonames_app);
                MenuItem item2 = menu.findItem(R.id.download_app);

                if (geonamesFragment != null && downloadFragment != null){
                    if (item == item1) {
                        getFragmentManager().beginTransaction().hide(downloadFragment).show(geonamesFragment).commit();
                        return true;
                    } else if (item == item2) {
                        getFragmentManager().beginTransaction().hide(geonamesFragment).show(downloadFragment).commit();
                        return true;
                    }
                }

                return false;
            }
        });

        navigation.setSelectedItemId(R.id.geonames_app);
    }


}
