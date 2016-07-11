package ConnectToServer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.example.aturag.shoppingbrowser.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import Product.ProductInfo;

/**
 * Created by Aturag on 30-Jun-16.
 */
public class ServletAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private Context context;
    private int queryNumber;

    public ServletAsyncTask(int queryNumber) {
        this.queryNumber = queryNumber;
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        context = params[0].first;
        String producturl = params[0].second;

        try {
            // Set up the request
            URL url = new URL("http://psychic-empire-135722.appspot.com/hello");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Build name data request params
            Map<String, String> nameValuePairs = new HashMap<>();
            nameValuePairs.put("url", producturl);
            String postParams = buildPostDataString(nameValuePairs);

            // Execute HTTP Post
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(postParams);
            writer.flush();
            writer.close();
            outputStream.close();
            connection.connect();

            // Read response
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return response.toString();
            }
            return "Error: " + responseCode + " " + connection.getResponseMessage();

        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private String buildPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("!!!!!!!! server result " + result);
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        result = result.replace("##", "\r\n");
        System.out.println("Server Replace " + result);
        writeToFile(result);
        //updateProductList(result);
        //MainActivity.serverStop();
    }

    private void updateProductList(String result) {
        ArrayList<ProductInfo> productInfoList = new ArrayList<>();
        String arr[] = result.split("\r\n");
        for(int i = 0;i < arr.length;) {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setName(arr[i]);
            productInfo.setUrl(arr[i + 1]);
            productInfo.setPrice(arr[i + 2]);
            productInfo.setEcommerceIcon(Integer.parseInt(arr[i + 3]));
            productInfoList.add(productInfo);
            MainActivity.datachanged(productInfoList, queryNumber);
            MainActivity._handler.sendEmptyMessage(queryNumber);
            i = i + 4;
        }

    }

    private void writeToFile(String data) {
        try {
            File myFile = new File("/sdcard/mysdfile.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            System.out.println("File Write SuccessFul");
            fOut.close();
        } catch (Exception e) {
            System.out.println("Error Coming in Writing File" + e.getMessage());
        }
    }

}