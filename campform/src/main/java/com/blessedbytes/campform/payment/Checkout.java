package com.blessedbytes.campform.payment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

public class Checkout {

    public String create(String name, String email, String pacote, String username) throws IOException, InterruptedException{
        String credentials = username + ":";
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\"customer\":{\"name\":\""+name+"\",\"email\":\""+email+"\"},\"items\":[{\"amount\":2000,\"description\":\""+pacote+"\",\"quantity\":1,\"code\":\"121\"}],\"payments\":[{\"checkout\":{\"accepted_payment_methods\":[\"credit_card\",\"debit_card\",\"pix\"],\"success_url\":\"https://www.pagar.me\",\"skip_checkout_success_page\":true,\"customer_editable\":false,\"credit_card\":{\"capture\":true,\"statement_descriptor\":\"TODO\",\"installments\":[{\"number\":1,\"total\":2000},{\"number\":2,\"total\":2500}]},\"pix\":{\"expires_in\":300},\"debit_card\":{\"authentication\":{\"statement_descriptor\":\"TODO\",\"type\":\"threed_secure\",\"threed_secure\":{\"mpi\":\"acquirer\",\"success_url\":\"https://www.pagar.me\"}}}},\"payment_method\":\"checkout\",\"amount\":2000}]}", mediaType);
        Request request = new Request.Builder()
        .url("https://api.pagar.me/core/v5/orders")
        .post(body)
        .addHeader("accept", "application/json")
        .addHeader("content-type", "application/json")
        .addHeader("authorization", "Basic "+base64Credentials)
        .build();

        Response response = client.newCall(request).execute();

        String responseBody = response.body().string();
        
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        JsonArray checkoutsArray = jsonObject.getAsJsonArray("checkouts");

        JsonObject firstCheckout = checkoutsArray.get(0).getAsJsonObject();
        String paymentUrl = firstCheckout.get("payment_url").getAsString();

        return paymentUrl;
    }
}