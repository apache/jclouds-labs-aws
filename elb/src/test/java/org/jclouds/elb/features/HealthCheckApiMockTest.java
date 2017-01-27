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
package org.jclouds.elb.features;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.jclouds.elb.domain.HealthCheck;
import org.jclouds.elb.internal.BaseELBApiMockTest;
import org.testng.annotations.Test;

@Test(groups = "unit", testName = "HealthCheckMockTest", singleThreaded = true)
public class HealthCheckApiMockTest  extends BaseELBApiMockTest {

    public static final String loadBalancerName = "my-loadbalancer";
    public static final int healthyThreshold = 2;
    public static final int unhealthyThreshold = 2;
    public static final String target = "HTTP:80/ping";
    public static final int interval = 30;
    public static final int timeout = 3;

    public void testConfigureHealthCheck() throws InterruptedException {
        server.enqueue(xmlResponse("/configure_health_check.xml"));

        HealthCheck healthCheck = HealthCheck.builder()
                .healthyThreshold(healthyThreshold)
                .unhealthyThreshold(unhealthyThreshold)
                .target(target)
                .interval(interval)
                .timeout(timeout)
                .build();

        HealthCheck response = api.getHealthCheckApi().configureHealthCheck(loadBalancerName, healthCheck);

        assertEquals(response, healthCheck);
        assertEquals(server.getRequestCount(), 1);

        assertSent(server, "POST", "/");
    }

    public void testConfigureHealthCheckReturns404() throws InterruptedException {
        server.enqueue(response404());

        HealthCheck healthCheck = HealthCheck.builder()
                .healthyThreshold(healthyThreshold)
                .unhealthyThreshold(unhealthyThreshold)
                .target(target)
                .interval(interval)
                .timeout(timeout)
                .build();

        HealthCheck response = api.getHealthCheckApi().configureHealthCheck(loadBalancerName, healthCheck);

        assertNull(response);
        assertEquals(server.getRequestCount(), 1);

        assertSent(server, "POST", "/");

    }

}
