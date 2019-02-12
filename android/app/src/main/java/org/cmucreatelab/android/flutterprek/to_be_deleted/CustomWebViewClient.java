package org.cmucreatelab.android.flutterprek.to_be_deleted;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static org.cmucreatelab.android.flutterprek.Constants.LOG_TAG;

/**
 * Created by mike on 8/2/17.
 *
 * A subclass of WebViewClient designed to override loading URLs with the custom schema "schema://".
 *
 */
public class CustomWebViewClient extends WebViewClient {

    private AppCompatActivity mainActivity;


    public CustomWebViewClient(AppCompatActivity mainActivity) {
        super();
        this.mainActivity = mainActivity;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.v(LOG_TAG, "shouldOverrideUrlLoading: "+url);
        Uri uri = Uri.parse(url);

        // TODO implement parameter override logic (see honeybee app)
//        // if request is "schema://" then we override
//        if (uri.getScheme().equals("schema")) {
//            String host = uri.getHost();
//            String path = uri.getEncodedPath().substring(1);
//            Log.i(LOG_TAG, "Caught message from browser: host=" + host+"\nPath=/" + path);
//            // NOTE: this ignores trailing slashes
//            ApplicationInterface.parseSchema(GlobalHandler.getInstance(mainActivity), host, path.split("/"));
//            return true;
//        } else if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
//            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//            mainActivity.startActivity(intent);
//            return true;
//        }
        // otherwise, do not override
        return false;
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.i(LOG_TAG, "Finished loading page URL="+url);
    }

}
