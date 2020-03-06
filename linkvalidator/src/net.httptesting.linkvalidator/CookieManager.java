package net.httptesting.linkvalidator

import java.net.http.*;


public class CookieManager{
    CookieManager cm = new CookieManager(null,
                        CookiePolicy.ACCEPT_ALL);

    var client = HttpClient.newBuilder()
                            .cookieHandler
                            .build();

    client.send(HttpRequest.newBuilder(URI.create("https://www.google.com")
                            .build(),
                HttpResponse.BodyHandlers.discarding()));
    
    cm.getCookieStore().getURIs();

    cm.getCookieStore().getCookies();
}


    