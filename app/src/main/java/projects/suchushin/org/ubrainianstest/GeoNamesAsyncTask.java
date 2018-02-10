package projects.suchushin.org.ubrainianstest;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

import java.util.ArrayList;
import java.util.List;

public class GeoNamesAsyncTask extends AsyncTask<String, Void, ArrayAdapter>{
    @SuppressLint("StaticFieldLeak")
    private AutoCompleteTextView editText;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private final static String USER_NAME = "geonames_programmer";

    GeoNamesAsyncTask(AutoCompleteTextView editText, Context context){
        this.editText = editText;
        this.context = context;
    }

    @Override
    protected void onPostExecute(ArrayAdapter arrayAdapter) {
        editText.setAdapter(arrayAdapter);
        try {
            editText.showDropDown();
        } catch (WindowManager.BadTokenException e){
            e.printStackTrace();
        }
    }

    @Override
    protected ArrayAdapter doInBackground(String... strings) {
        List<String> locationNameList = getNameListOfToponyms(strings[0]);
        return new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, locationNameList);
    }

    private List<String> getNameListOfToponyms(String name){
        WebService.setUserName(USER_NAME);
        ToponymSearchCriteria criteria = new ToponymSearchCriteria();
        criteria.setNameStartsWith(name);
        List<String> nameList = new ArrayList<>();
        try {
            ToponymSearchResult result = WebService.search(criteria);
            for (Toponym t : result.getToponyms()) {
                nameList.add(t.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nameList;
    }
}
