package com.gpay.merchantapp.network;

import android.os.StrictMode;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by gpay on 19/06/20.
 */

public class LoggingInterceptor implements Interceptor {
    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        String requestLog = String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers());
        if (request.method().compareToIgnoreCase("post") == 0) {
            requestLog = "\n" + requestLog + "\n" + bodyToString(request);
        }
        System.out.println("Request : " + requestLog);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //if (!BuildConfig.DEBUG) {
        try {

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            String bodyString = response.body().string();
//                System.out.println("Response : " + bodyString);


            if (response.code() == 502) {
                Request failedRequest = chain.request();
                HttpUrl replicaUrl = failedRequest.url()
                        .newBuilder()
                        .host("psmw.gpay.digital") //Prod
                        .setPathSegment(0, "c_api")//Prod
                        //.setPathSegment(0, "  dev_c_api")//Dev
                        .build();

                        /*.host("dev-c.gpay.digital")
                        .removePathSegment(0)
                        .build();*/

                //Merchant App
                       /* .host("psmw.gpay.digital")
                        .setPathSegment(0, "dev_ewallet_api")//Dev
                        .build();*/


                Request requestBackup = failedRequest.newBuilder()
                        .url(replicaUrl)
                        .build();
//                    System.out.println("Replica URL  : " + replicaUrl);


                Response responseBackup = chain.proceed(requestBackup);
                long t3 = System.nanoTime();
                String bodyStringReplica = responseBackup.body().string();
//                    System.out.println("Response : " + bodyStringReplica);
                return responseBackup.newBuilder()
                        .body(ResponseBody.create(responseBackup.body().contentType(), bodyStringReplica))
                        .build();
            }

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), bodyString))
                    .build();
        } catch (Exception ex) {
            Request failedRequest = chain.request();
            HttpUrl replicaUrl = failedRequest.url()
                    .newBuilder()
                    .host("psmw.gpay.digital")
                    .setPathSegment(0, "c_api")//Prod
                    //.setPathSegment(0, "  dev_c_api")//Dev
                    .build();

                    /*.host("dev-c.gpay.digital")
                    .removePathSegment(0)
                    .build();*/

            //Merchant App
                    /*.host("psmw.gpay.digital")
                    .setPathSegment(0, "dev_ewallet_api")//Dev
                    .build();*/

            Request requestBackup = failedRequest.newBuilder()
                    .url(replicaUrl)
                    .build();

            Response responseBackup = chain.proceed(requestBackup);
            long t2 = System.nanoTime();
            String bodyString = responseBackup.body().string();
//                System.out.println("Response : " + bodyString);
            return responseBackup.newBuilder()
                    .body(ResponseBody.create(responseBackup.body().contentType(), bodyString))
                    .build();
        }
    /*} else {
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            String bodyString = response.body().string();
//            System.out.println("Response : " + bodyString);
            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), bodyString))
                    .build();
        }*/


    }
}