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
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.jclouds.elb.internal.BaseELBApiMockTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

@Test(groups = "unit", testName = "SubnetMockTest", singleThreaded = true)
public class SubnetApiMockTest extends BaseELBApiMockTest {

    public static final String loadBalancerName = "my-loadbalancer";
    public static final String subnetId = "subnet-3561b05e";

    public void testAttachSubnets() throws InterruptedException {
        server.enqueue(xmlResponse("/attach_load_balancer_to_subnets.xml"));

        Set<String> response = api.getSubnetApi().attachLoadBalancerToSubnets(loadBalancerName, ImmutableSet.of(subnetId));

        assertTrue(response.contains(subnetId));
        assertEquals(server.getRequestCount(), 1);

        assertSent(server, "POST", "/");
    }
    public void testAttachSubnets404() throws InterruptedException {
        server.enqueue(response404());

        Set<String> response = api.getSubnetApi().attachLoadBalancerToSubnets(loadBalancerName, ImmutableSet.of(subnetId));

        assertNull(response);
        assertEquals(server.getRequestCount(), 1);
        assertSent(server, "POST", "/");
    }
    public void testDetachSubnets() throws InterruptedException {
        server.enqueue(xmlResponse("/detach_load_balancer_from_subnets.xml"));

        Set<String> response = api.getSubnetApi().detachLoadBalancerFromSubnets(loadBalancerName, ImmutableSet.of(subnetId));

        assertTrue(response.contains(subnetId));
        assertEquals(server.getRequestCount(), 1);

        assertSent(server, "POST", "/");
    }
    public void testDetachSubnets404() throws InterruptedException {
        server.enqueue(response404());

        Set<String> response = api.getSubnetApi().detachLoadBalancerFromSubnets(loadBalancerName, ImmutableSet.of(subnetId));

        assertNull(response);
        assertEquals(server.getRequestCount(), 1);
        assertSent(server, "POST", "/");
    }

}
