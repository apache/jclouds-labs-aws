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

import static org.assertj.core.util.Preconditions.checkNotNull;

import org.jclouds.elb.domain.HealthCheck;
import org.jclouds.elb.domain.Listener;
import org.jclouds.elb.domain.Protocol;
import org.jclouds.elb.internal.BaseELBApiLiveTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

@Test(groups = "live", testName = "HealthCheckApiLiveTest")
public class HealthCheckApiLiveTest extends BaseELBApiLiveTest {

   protected HealthCheckApi api() {
      return api.getHealthCheckApi();
   }

   @BeforeMethod
   private void setUp() {
      super.setup();
      Listener listener = Listener.builder()
              .protocol(Protocol.HTTP)
              .port(80)
              .instanceProtocol(Protocol.HTTP)
              .instancePort(80)
              .build();
      api.getLoadBalancerApi().createListeningInAvailabilityZones("test", listener, ImmutableList.of("us-east-1b"));
   }

   @AfterClass(alwaysRun = true)
   @Override
   protected void tearDown() {
      super.tearDown();
      if (api.getLoadBalancerApi().get("test") != null) {
         api.getLoadBalancerApi().delete("test");
      }
   }
   
   @Test
   protected void testConfigureHealthCheck() {

      HealthCheck healthCheck = api().configureHealthCheck("test", HealthCheck.builder()
              .healthyThreshold(2)
              .unhealthyThreshold(2)
              .interval(30)
              .target("HTTP:80/ping")
              .timeout(3)
              .build());
      checkHealthCheck(healthCheck);

   }

   private void checkHealthCheck(HealthCheck healthCheck) {
      checkNotNull(healthCheck.getHealthyThreshold(), "Description cannot be null for InstanceState");
      checkNotNull(healthCheck.getInterval(), "InstanceId cannot be null for InstanceState");
      checkNotNull(healthCheck.getTarget(), "Target must not be null");
      checkNotNull(healthCheck.getTimeout(), "State cannot be null for InstanceState");
      checkNotNull(healthCheck.getUnhealthyThreshold(), "Description cannot be null for InstanceState");
   }
}
