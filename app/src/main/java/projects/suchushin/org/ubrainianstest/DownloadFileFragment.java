package projects.suchushin.org.ubrainianstest;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

public class DownloadFileFragment extends Fragment{
    private static final String DOWNLOADED_FILE_NAME = "downloaded_file";
    private static final String DIRECTORY_OF_EXTRACTED_FILES = "extracted_files";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View fragment = inflater.inflate(R.layout.download_file_fragment, container, false);

        final EditText downloadLink = fragment.findViewById(R.id.download_link);
        ImageButton downloadButton = fragment.findViewById(R.id.download_button);

        PRDownloader.initialize(fragment.getContext());

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = String.valueOf(downloadLink.getText());
                PRDownloader.download(url, MainActivity.getDownloadDirectoryPass(), DOWNLOADED_FILE_NAME)
                        .build()
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                File file = new File(MainActivity.getDownloadDirectoryPass(), DOWNLOADED_FILE_NAME);
                                File directory = new File(MainActivity.getDownloadDirectoryPass(), DIRECTORY_OF_EXTRACTED_FILES);
                                try {
                                    ZipFile zipFile = new ZipFile(file.getAbsolutePath());
                                    zipFile.extractAll(directory.getAbsolutePath());
                                    Toast.makeText(fragment.getContext(), "Zip files extracted", Toast.LENGTH_LONG).show();
                                } catch (ZipException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(fragment.getContext(), "Download successful", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Error error) {
                                Toast.makeText(fragment.getContext(), "Download failed...", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        return fragment;
    }


}
