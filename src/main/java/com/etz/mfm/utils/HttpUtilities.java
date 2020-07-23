package com.etz.mfm.utils;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpUtilities {
  private static final Logger logger = Logger.getLogger(HttpUtilities.class);
  
  public static void main(String[] args) {
    logger.info("JJDJD");
  }
  
  public static JsonObject doHttpGet(String requestUrl, int timeoutSec) {
    JsonObject responseObj = new JsonObject();
    responseObj.addProperty("requestURL", requestUrl);
    try {
      BasicHttpParams basicHttpParams = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      DefaultHttpClient defaultHttpClient = new DefaultHttpClient((HttpParams)basicHttpParams);
      HttpGet httpGet = new HttpGet(requestUrl);
      httpGet.setHeader("User-Agent", "okhttp");
      HttpResponse httpResponse = defaultHttpClient.execute((HttpUriRequest)httpGet);
      int responseCode = httpResponse.getStatusLine().getStatusCode();
      HttpEntity hentity = httpResponse.getEntity();
      responseObj.addProperty("responseCode", Integer.valueOf(responseCode));
      responseObj.addProperty("responseMessage", 
          EntityUtils.toString(hentity));
    } catch (MalformedURLException e) {
      e.printStackTrace();
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } catch (IOException e) {
      e.printStackTrace();
      responseObj.addProperty("responseCode", Integer.valueOf(1));
    } 
    return responseObj;
  }
  
  public static JsonObject doHttpPost(String requestUrl, String postData, int timeoutSec, String contentType) {
    JsonObject responseObj = new JsonObject();
    responseObj.addProperty("requestURL", requestUrl);
    try {
      BasicHttpParams basicHttpParams = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      DefaultHttpClient defaultHttpClient = new DefaultHttpClient((HttpParams)basicHttpParams);
      HttpPost httpPost = new HttpPost(requestUrl);
      httpPost.setHeader("Content-type", contentType);
      StringEntity entity = new StringEntity(postData);
      httpPost.setEntity((HttpEntity)entity);
      HttpResponse httpResponse = defaultHttpClient.execute((HttpUriRequest)httpPost);
      int responseCode = httpResponse.getStatusLine().getStatusCode();
      HttpEntity hentity = httpResponse.getEntity();
      responseObj.addProperty("responseCode", Integer.valueOf(responseCode));
      responseObj.addProperty("responseMessage", 
          EntityUtils.toString(hentity));
    } catch (MalformedURLException e) {
      e.printStackTrace();
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } catch (IOException e) {
      e.printStackTrace();
      responseObj.addProperty("responseCode", Integer.valueOf(1));
    } 
    return responseObj;
  }
  
  public static JsonObject doHttpPostSSL(String requestUrl, String postRequest, String methodName, int timeoutSec) {
    JsonObject responseObj = new JsonObject();
    responseObj.addProperty("requestURL", requestUrl);
    try {
      BasicHttpParams basicHttpParams = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      SSLContext ctx = SSLContext.getInstance("TLS");
      X509TrustManager tm = new X509TrustManager() {
          public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
          
          public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
          
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }
        };
      ctx.init(null, new TrustManager[] { tm }, null);
      SSLSocketFactory ssf = new SSLSocketFactory(ctx);
      ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
      SchemeRegistry schemeRegistry = new SchemeRegistry();
      schemeRegistry.register(new Scheme("https", (SocketFactory)ssf, 443));
      DefaultHttpClient httpClient = new DefaultHttpClient((ClientConnectionManager)new ThreadSafeClientConnManager((HttpParams)basicHttpParams, schemeRegistry), (HttpParams)basicHttpParams);
      HttpPost httpPost = new HttpPost(requestUrl);
      StringEntity entity = new StringEntity(postRequest);
      httpPost.setEntity((HttpEntity)entity);
      HttpResponse httpResponse = httpClient.execute((HttpUriRequest)httpPost);
      int responseCode = httpResponse.getStatusLine().getStatusCode();
      HttpEntity hentity = httpResponse.getEntity();
      responseObj.addProperty("responseCode", Integer.valueOf(responseCode));
      responseObj.addProperty("responseMessage", 
          EntityUtils.toString(hentity));
    } catch (MalformedURLException e) {
      logger.error("HTTP Error", e);
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } catch (IOException e) {
      logger.error("HTTP Timeout Error", e);
      responseObj.addProperty("responseCode", Integer.valueOf(1));
    } catch (NoSuchAlgorithmException e) {
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } catch (KeyManagementException e) {
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } 
    return responseObj;
  }
  
  public static JsonObject doHttpGetSSL(String requestUrl, int timeoutSec) {
    JsonObject responseObj = new JsonObject();
    responseObj.addProperty("requestURL", requestUrl);
    try {
      BasicHttpParams basicHttpParams = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      SSLContext ctx = SSLContext.getInstance("TLS");
      X509TrustManager tm = new X509TrustManager() {
          public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
          
          public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
          
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }
        };
      ctx.init(null, new TrustManager[] { tm }, null);
      SSLSocketFactory ssf = new SSLSocketFactory(ctx);
      ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
      SchemeRegistry schemeRegistry = new SchemeRegistry();
      schemeRegistry.register(new Scheme("https", (SocketFactory)ssf, 443));
      DefaultHttpClient httpClient = new DefaultHttpClient((ClientConnectionManager)new ThreadSafeClientConnManager((HttpParams)basicHttpParams, schemeRegistry), (HttpParams)basicHttpParams);
      HttpGet httpGet = new HttpGet(requestUrl);
      httpGet.setHeader("User-Agent", "okhttp");
      HttpResponse httpResponse = httpClient.execute((HttpUriRequest)httpGet);
      int responseCode = httpResponse.getStatusLine().getStatusCode();
      HttpEntity hentity = httpResponse.getEntity();
      responseObj.addProperty("responseCode", Integer.valueOf(responseCode));
      responseObj.addProperty("responseMessage", 
          EntityUtils.toString(hentity));
    } catch (MalformedURLException e) {
      logger.error("HTTP Error", e);
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } catch (IOException e) {
      logger.error("HTTP Timeout Error", e);
      responseObj.addProperty("responseCode", Integer.valueOf(1));
    } catch (NoSuchAlgorithmException e) {
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } catch (KeyManagementException e) {
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } 
    return responseObj;
  }
  
  public static JsonObject doHttpGet(String requestUrl, int timeoutSec, Map<String, String> headers, boolean enableSSL) {
    JsonObject responseObj = new JsonObject();
    responseObj.addProperty("requestURL", requestUrl);
    long lStart = System.currentTimeMillis();
    try {
      BasicHttpParams basicHttpParams = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      DefaultHttpClient httpClient = null;
      if (enableSSL) {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
            
            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
            
            public X509Certificate[] getAcceptedIssuers() {
              return null;
            }
          };
        ctx.init(null, new TrustManager[] { tm }, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx);
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", (SocketFactory)ssf, 443));
        httpClient = new DefaultHttpClient((ClientConnectionManager)new ThreadSafeClientConnManager((HttpParams)basicHttpParams, schemeRegistry), (HttpParams)basicHttpParams);
      } else {
        httpClient = new DefaultHttpClient();
      } 
      HttpGet httpGet = new HttpGet(requestUrl);
      for (Map.Entry<String, String> entry : headers.entrySet())
        httpGet.setHeader(entry.getKey(), entry.getValue()); 
      HttpResponse httpResponse = httpClient.execute((HttpUriRequest)httpGet);
      int responseCode = httpResponse.getStatusLine().getStatusCode();
      HttpEntity hentity = httpResponse.getEntity();
      responseObj.addProperty("duration", Long.valueOf(System.currentTimeMillis() - lStart));
      responseObj.addProperty("responseCode", Integer.valueOf(responseCode));
      responseObj.addProperty("responseMessage", 
          EntityUtils.toString(hentity));
    } catch (MalformedURLException e) {
      logger.error("HTTP Error", e);
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } catch (IOException e) {
      logger.error("HTTP Timeout Error", e);
      responseObj.addProperty("responseCode", Integer.valueOf(1));
    } catch (NoSuchAlgorithmException e) {
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } catch (KeyManagementException e) {
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } 
    return responseObj;
  }
  
  public static JsonObject doHttpPost(String requestUrl, String postRequest, int timeoutSec, Map<String, String> headers, boolean enableSSL) {
    JsonObject responseObj = new JsonObject();
    responseObj.addProperty("requestURL", requestUrl);
    long lStart = System.currentTimeMillis();
    try {
      BasicHttpParams basicHttpParams = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      HttpConnectionParams.setSoTimeout((HttpParams)basicHttpParams, timeoutSec * 1000);
      DefaultHttpClient httpClient = null;
      if (enableSSL) {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
            
            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
            
            public X509Certificate[] getAcceptedIssuers() {
              return null;
            }
          };
        ctx.init(null, new TrustManager[] { tm }, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx);
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", (SocketFactory)ssf, 443));
        httpClient = new DefaultHttpClient((ClientConnectionManager)new ThreadSafeClientConnManager((HttpParams)basicHttpParams, schemeRegistry), (HttpParams)basicHttpParams);
      } else {
        httpClient = new DefaultHttpClient();
      } 
      HttpPost httpPost = new HttpPost(requestUrl);
      for (Map.Entry<String, String> entry : headers.entrySet())
        httpPost.setHeader(entry.getKey(), entry.getValue()); 
      StringEntity entity = new StringEntity(postRequest);
      httpPost.setEntity((HttpEntity)entity);
      HttpResponse httpResponse = httpClient.execute((HttpUriRequest)httpPost);
      int responseCode = httpResponse.getStatusLine().getStatusCode();
      HttpEntity hentity = httpResponse.getEntity();
      responseObj.addProperty("duration", Long.valueOf(System.currentTimeMillis() - lStart));
      responseObj.addProperty("responseCode", Integer.valueOf(responseCode));
      responseObj.addProperty("responseMessage", EntityUtils.toString(hentity));
    } catch (MalformedURLException e) {
      logger.error("HTTP Error", e);
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } catch (IOException e) {
      logger.error("HTTP Timeout Error", e);
      responseObj.addProperty("responseCode", Integer.valueOf(1));
    } catch (NoSuchAlgorithmException e) {
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } catch (KeyManagementException e) {
      responseObj.addProperty("responseCode", Integer.valueOf(2));
    } 
    return responseObj;
  }
}
