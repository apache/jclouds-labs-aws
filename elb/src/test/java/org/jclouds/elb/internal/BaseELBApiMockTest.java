/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.elb.internal;

import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.jclouds.elb.ELBApi;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import com.google.inject.Module;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

public class BaseELBApiMockTest {

    private static final String MOCK_BEARER_TOKEN = "c5401990f0c24135e8d6b5d260603fc71696d4738da9aa04a720229a01a2521d";
    private static final String DEFAULT_ENDPOINT = "http:";
    //new AWSELBProviderMetadata().getEndpoint();

    private final Set<Module> modules = ImmutableSet.<Module> of(new ExecutorServiceModule(newDirectExecutorService()));

    protected MockWebServer server;
    protected ELBApi api;

    @BeforeMethod
    public void start() throws IOException {
        server = new MockWebServer();
        server.play();
        api = ContextBuilder.newBuilder("elb")
                .credentials("", MOCK_BEARER_TOKEN)
                .endpoint(url(""))
                .modules(modules)
                .overrides(overrides())
                .buildApi(ELBApi.class);
    }

    @AfterMethod(alwaysRun = true)
    public void stop() throws IOException {
        server.shutdown();
        api.close();
    }

    protected Properties overrides() {
        return new Properties();
    }

    protected String url(String path) {
        return server.getUrl(path).toString();
    }

    protected MockResponse xmlResponse(String resource) {
        return new MockResponse().addHeader("Content-Type", "application/xml").setBody(stringFromResource(resource));
    }


    protected String stringFromResource(String resourceName) {
        try {
            return Resources.toString(getClass().getResource(resourceName), Charsets.UTF_8)
                    .replace(DEFAULT_ENDPOINT, url(""));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
    protected MockResponse response404() {
        return new MockResponse().setStatus("HTTP/1.1 404 Not Found");
    }

    protected RecordedRequest assertSent(MockWebServer server, String method, String path) throws InterruptedException {
        RecordedRequest request = server.takeRequest();
        assertEquals(request.getMethod(), method);
        assertEquals(request.getPath(), path);
        assertEquals(request.getHeader("Accept"), "application/xml");
        return request;
    }

}
