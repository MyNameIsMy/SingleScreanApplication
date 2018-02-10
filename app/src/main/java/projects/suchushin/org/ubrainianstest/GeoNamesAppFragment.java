package projects.suchushin.org.ubrainianstest;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class GeoNamesAppFragment extends Fragment {
    DatabaseHelper helper;
    LinearLayout listItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.geonames_app_fragment, container, false);

        helper = new DatabaseHelper(view.getContext());
        final AutoCompleteTextView actv = view.findViewById(R.id.actv);
        listItem = view.findViewById(R.id.list_item);
        Button add = view.findViewById(R.id.add);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        actv.setThreshold(0);

        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String[] strings = new String[1];
                String name = String.valueOf(editable);
                if (!Objects.equals(name, "")){
                    strings[0] = name;
                    GeoNamesAsyncTask asyncTask = new GeoNamesAsyncTask(actv, view.getContext());
                    asyncTask.execute(strings);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = String.valueOf(actv.getText());
                if (!Objects.equals(text, "")){
                    TextView textView = getClickableTextView(text);
                    listItem.addView(textView);
                    helper.putInformation(text);
                }
            }
        });

        setEntriesFromDataBase();


        return view;
    }

    private void setEntriesFromDataBase(){
        Cursor cursor = helper.getInformation();
        cursor.moveToFirst();
        try {
            do {
                listItem.addView(getClickableTextView(cursor.getString(0)));
            } while (cursor.moveToNext());
        } catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    private TextView getClickableTextView(final String text){
        final TextView textView = new TextView(listItem.getContext());
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.border);
        textView.setText(text);
        textView.setBackground(drawable);
        textView.setClickable(true);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete")
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                helper.deleteInformation(text);
                                textView.setVisibility(View.GONE);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        return textView;
    }
}
