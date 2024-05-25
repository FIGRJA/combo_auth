package org.figrja.combo_auth.ely.by;

import com.google.gson.Gson;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


public class httpHelper {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson gson = new Gson();

    private static Proxy proxy = Proxy.NO_PROXY;

    public static void setProxy(Proxy proxyN) {
        Validate.notNull(proxyN);
        proxy = proxyN;
    }

    protected static HttpURLConnection createUrlConnection(URL url) throws IOException {
        Validate.notNull(url);
        LOGGER.debug("Opening connection to " + url);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setUseCaches(false);
        return connection;
    }

    public static String performPostRequest(URL url, String post, String contentType) throws IOException {
        Validate.notNull(url);
        Validate.notNull(post);
        Validate.notNull(contentType);
        HttpURLConnection connection = createUrlConnection(url);
        byte[] postAsBytes = post.getBytes(Charsets.UTF_8);
        connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
        connection.setRequestProperty("Content-Length", "" + postAsBytes.length);
        connection.setDoOutput(true);
        LOGGER.debug("Writing POST data to " + url + ": " + post);
        OutputStream outputStream = null;

        try {
            outputStream = connection.getOutputStream();
            IOUtils.write(postAsBytes, outputStream);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }

        LOGGER.debug("Reading data from " + url);
        InputStream inputStream = null;

        String var10;
        try {
            String result;
            try {
                inputStream = connection.getInputStream();
                result = IOUtils.toString(inputStream, Charsets.UTF_8);
                LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
                LOGGER.debug("Response: " + result);
                return result;
            } catch (IOException var19) {
                IOUtils.closeQuietly(inputStream);
                inputStream = connection.getErrorStream();
                if (inputStream == null) {
                    LOGGER.debug("Request failed", var19);
                    throw var19;
                }

                LOGGER.debug("Reading error page from " + url);
                result = IOUtils.toString(inputStream, Charsets.UTF_8);
                LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
                LOGGER.debug("Response: " + result);
                var10 = result;
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return var10;
    }

    public static String performGetRequest(URL url) throws IOException {
        Validate.notNull(url);
        HttpURLConnection connection = createUrlConnection(url);
        LOGGER.debug("Reading data from " + url);
        InputStream inputStream = null;

        String var6;
        try {
            String result;
            try {
                inputStream = connection.getInputStream();
                result = IOUtils.toString(inputStream, Charsets.UTF_8);
                LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
                LOGGER.debug("Response: " + result);
                return result;
            } catch (IOException var10) {
                IOUtils.closeQuietly(inputStream);
                inputStream = connection.getErrorStream();
                if (inputStream == null) {
                    LOGGER.debug("Request failed", var10);
                    throw var10;
                }
            }

            LOGGER.debug("Reading error page from " + url);
            result = IOUtils.toString(inputStream, Charsets.UTF_8);
            LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
            LOGGER.debug("Response: " + result);
            var6 = result;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return var6;
    }

    public static URL constantURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException var2) {
            throw new Error("Couldn't create constant for " + url, var2);
        }
    }

    public static String buildQuery(Map<String, Object> query) {
    if (query == null) {
        return "";
    } else {
        StringBuilder builder = new StringBuilder();
        Iterator i$ = query.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)i$.next();
            if (builder.length() > 0) {
                builder.append('&');
            }

            try {
                builder.append(URLEncoder.encode((String)entry.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException var6) {
                LOGGER.error("Unexpected exception building query", var6);
            }

            if (entry.getValue() != null) {
                builder.append('=');

                try {
                    builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                } catch (UnsupportedEncodingException var5) {
                    LOGGER.error("Unexpected exception building query", var5);
                }
            }
        }

        return builder.toString();
    }
}

    public static URL concatenateURL(URL url, String query) {
        try {
            return url.getQuery() != null && url.getQuery().length() > 0 ? new URL(url.getProtocol(), url.getHost(), url.getFile() + "&" + query) : new URL(url.getProtocol(), url.getHost(), url.getFile() + "?" + query);
        } catch (MalformedURLException var3) {
            throw new IllegalArgumentException("Could not concatenate given URL with GET arguments!", var3);
        }
    }

    public static resultElyGson makeRequest(URL url) throws AuthenticationException {
        try {
            String jsonResult = performGetRequest(url) ;
            resultElyGson result = gson.fromJson(jsonResult, resultElyGson.class);
            if (result == null) {
                return null;
            } else if (result.getError() != null) {
                return null;
            }
            result.setId(str2uuid(result.id));
            return result;
        } catch (IOException var6) {
            throw new AuthenticationUnavailableException("Cannot contact authentication server", var6);
        }
    }

    private static UUID str2uuid(String uuid){
        String comUUID = uuid.length() == 32 ?
                uuid.substring(0,8)
                        +"-"
                        + uuid.substring(8,12)
                        +"-"
                        + uuid.substring(12,16)
                        +"-"
                        + uuid.substring(16,20)
                        +"-"
                        + uuid.substring(20)
                : uuid;
        return UUID.fromString(comUUID);
    }
}
