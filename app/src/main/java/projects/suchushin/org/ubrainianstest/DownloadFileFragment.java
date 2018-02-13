package projects.suchushin.org.ubrainianstest;


import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

public class DownloadFileFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View fragment = inflater.inflate(R.layout.download_file_fragment, container, false);

        ImageButton downloadButton = fragment.findViewById(R.id.download_button);
        Resources res = getResources();

        final EditText downloadLink = fragment.findViewById(R.id.download_link);
        final Drawable fileDrawable = res.getDrawable(R.drawable.ic_file);
        final Drawable folderDrawable = res.getDrawable(R.drawable.ic_folder);

        PRDownloader.initialize(fragment.getContext());

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = String.valueOf(downloadLink.getText());
                final File downloadedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), "downloaded_file.zip");
                if (downloadedFile.exists()){
                    downloadedFile.delete();
                }
                if (isExternalStorageWritable()) {
                    PRDownloader.download(url, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), "downloaded_file.zip")
                            .build()
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                    Toast.makeText(fragment.getContext(), "Success", Toast.LENGTH_LONG).show();
                                    LinearLayout linearLayout = fragment.findViewById(R.id.main_lin_layout);
                                    File directoryTo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), "unzip_downloaded_file");
                                    if (directoryTo.exists()) {
                                        directoryTo.delete();
                                    }
                                    directoryTo.mkdir();
                                    try {
                                        ZipFile zipFrom = new ZipFile(downloadedFile);
                                        zipFrom.extractAll(directoryTo.getAbsolutePath());
                                        for (File f : directoryTo.listFiles()) {
                                            ImageView imageView = new ImageView(linearLayout.getContext());
                                            if (f.isDirectory()){
                                                imageView.setImageDrawable(folderDrawable);
                                            } else {
                                                imageView.setImageDrawable(fileDrawable);
                                            }


                                            TextView textView = new TextView(linearLayout.getContext());
                                            textView.setText(f.getName());

                                            LinearLayout ll = new LinearLayout(linearLayout.getContext());
                                            ll.setOrientation(LinearLayout.VERTICAL);

                                            ll.addView(imageView);
                                            ll.addView(textView);

                                            linearLayout.addView(ll);
                                        }
                                    } catch (ZipException e) {
                                        Toast.makeText(fragment.getContext(), "Cannot extract downloadedFile", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Error error) {
                                    Toast.makeText(fragment.getContext(), "Failure", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
        return fragment;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
