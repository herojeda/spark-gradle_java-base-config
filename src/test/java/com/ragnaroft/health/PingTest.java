package com.ragnaroft.health;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.ragnaroft.functional.FunctionalTestCase;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class PingTest extends FunctionalTestCase {

    @Test
    public void test_ping() throws UnirestException, InterruptedException {
        Thread.sleep(2000);
        HttpResponse<String> response = Unirest.get("http://localhost:8080/ping").asString();

        System.out.println(response.getBody());

        // Validate status, should be 200
        assertThat(response.getStatus(), is(HttpStatus.SC_OK));

        // Validate message, should be "pong"
        assertThat(response.getBody(), is("pong"));
    }
}
