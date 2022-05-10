package com.helmes.interview;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SpringBootTest(classes = InterviewApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class InterviewApplicationTests {

    private static FutureCallback<SimpleHttpResponse> callback;

    @BeforeAll
    static void init() {
        callback = new SilentFutureCallback();
    }

    @Test
    void testCounter() throws ExecutionException, InterruptedException {
        final IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .build();

        PoolingAsyncClientConnectionManager connectionManager =
                new PoolingAsyncClientConnectionManager();
        connectionManager.setMaxTotal(8);
        connectionManager.setDefaultMaxPerRoute(8);

        final CloseableHttpAsyncClient client = HttpAsyncClients.custom()
                .setIOReactorConfig(ioReactorConfig)
                .setConnectionManager(connectionManager)
                .build();

        client.start();

        final SimpleHttpRequest counterRequest = SimpleRequestBuilder.get()
                .setHttpHost(new HttpHost("localhost", 8080))
                .setPath("/counter")
                .build();

        final SimpleHttpRequest usersRequest = SimpleRequestBuilder.get()
                .setHttpHost(new HttpHost("localhost", 8080))
                .setPath("/users/all")
                .build();

        List<Future<SimpleHttpResponse>> futures = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            futures.add(client.execute(usersRequest, callback));
        }


        while (!allDone(futures)) {
            //waiting
        }

        final Future<SimpleHttpResponse> future = client.execute(counterRequest, callback);

        String counterResponse = future.get().getBodyText();
        Assertions.assertEquals("5000", counterResponse);
    }

    private <T> boolean allDone(List<Future<T>> futures) {
        return futures.stream().allMatch(Future::isDone);
    }

    private static class SilentFutureCallback implements FutureCallback<SimpleHttpResponse> {
        @Override
        public void completed(SimpleHttpResponse simpleHttpResponse) {
        }

        @Override
        public void failed(Exception e) {

        }

        @Override
        public void cancelled() {

        }
    };
}
