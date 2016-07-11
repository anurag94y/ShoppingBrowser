/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.Aturag.myapplication.backend;

import com.example.Aturag.myapplication.backend.BackendLogic.BackendExtractDetailFromUrl;
import com.example.Aturag.myapplication.backend.BackendLogic.BackendProductInfo;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.*;

public class MyServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        //resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String url = req.getParameter("url");
        resp.setContentType("text/plain");
        if(url == null) {
            //resp.getWriter().println("Please enter a name");
        }
       // resp.getWriter().println("Hello " + url);
        ArrayList<BackendProductInfo> temp =  getProductDetailFromServer(url, resp);
        print(temp,resp);
    }

    public ArrayList<BackendProductInfo> getProductDetailFromServer(String Url,HttpServletResponse resp) {
        try {
            BackendExtractDetailFromUrl extractDetailFromUrl = new BackendExtractDetailFromUrl();
            if (extractDetailFromUrl.isProductUrl(Url,resp)) {
                resp.getWriter().println("!!!! Yes It is product Url My Bro How U identify that !!!!");
                return extractDetailFromUrl.isValidProduct(Url, 1, resp);

            } else {
                //resp.getWriter().println("No it is not product Page");
            }
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        //productAdapter.setProductInfoList(productInfoList);
        return null;
    }

    private void print(ArrayList<BackendProductInfo> productInfoArrayList,HttpServletResponse resp) throws IOException {
        if(productInfoArrayList == null || productInfoArrayList.size() == 0) {
            //resp.getWriter().println("Nhi hai kuch ");
        }
        else {
            String builder = "";
            for (BackendProductInfo productInfo : productInfoArrayList) {
                builder = builder + ("name:" + productInfo.getName() + "\n");
                builder = builder + ("price:" + productInfo.getPrice() + "\n");
                builder = builder + ("url:" + productInfo.getUrl() + "\n");
            }
            //resp.getWriter().println(builder);
        }

    }




}
