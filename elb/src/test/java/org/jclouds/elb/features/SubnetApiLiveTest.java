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
import static org.jclouds.reflect.Reflection2.typeToken;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;
import org.jclouds.ec2.compute.EC2ComputeServiceContext;
import org.jclouds.ec2.domain.Subnet;
import org.jclouds.elb.domain.Listener;
import org.jclouds.elb.domain.LoadBalancer;
import org.jclouds.elb.domain.Protocol;
import org.jclouds.elb.internal.BaseELBApiLiveTest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

@Test(groups = "live", testName = "SubnetApiLiveTest")
public class SubnetApiLiveTest extends BaseELBApiLiveTest {
   private String loadBalancerName;
   private EC2Api ec2Api;


   @BeforeMethod
   private void setUp() {
      super.setup();

      final EC2ComputeServiceContext ec2ComputeServiceContext = ContextBuilder.newBuilder("aws-ec2")
              .credentials(identity, credential)
              .buildView(typeToken(EC2ComputeServiceContext.class));
      createTestLoadBalancer();

     ec2Api = ec2ComputeServiceContext.unwrapApi(EC2Api.class);

   }

   private void createTestLoadBalancer (){
      loadBalancerName = generateRandomName();
      api.getLoadBalancerApi().createListeningInAvailabilityZones(loadBalancerName, Listener.builder()
              .protocol(Protocol.HTTP)
              .port(80)
              .instanceProtocol(Protocol.HTTP)
              .instancePort(80)
              .build(), ImmutableList.of("us-east-1b"));
   }
   @AfterMethod(alwaysRun = true)
   @Override
   protected void tearDown() {
      super.tearDown();
      if (api.getLoadBalancerApi().get(loadBalancerName) != null) {
         api.getLoadBalancerApi().delete(loadBalancerName);
      }
   }
   protected EC2Api ec2Api() {
      return ec2Api;
   }
   protected SubnetApi subnetApi() {
      return api.getSubnetApi();
   }

   @Test
   protected void testAttachToSubnet() {
      final LoadBalancer loadBalancer = api.getLoadBalancerApi().get(loadBalancerName);
      final FluentIterable<Subnet> list = ec2Api().getSubnetApi().get().list();

      String subnetIdToAttach = null;

      for (Iterator<Subnet> subnet = list.iterator(); subnet.hasNext();) {
         Subnet currentSubnet = subnet.next();
         if (!loadBalancer.getSubnets().contains(currentSubnet.getSubnetId())) {
            subnetIdToAttach = currentSubnet.getSubnetId();
            break;
         }
      }

      checkNotNull(subnetIdToAttach, "Could not find a subnet to attach to the load balancer");
      Assert.assertEquals(1, loadBalancer.getSubnets().size());

      final ImmutableSet<String> subnets = ImmutableSet.<String>builder()
              .addAll(loadBalancer.getSubnets())
              .add(subnetIdToAttach)
              .build();


      final Set<String> result = subnetApi().attachLoadBalancerToSubnets(loadBalancerName, subnets);

      Assert.assertEquals(result.size(), subnets.size());
      Assert.assertEquals(api.getLoadBalancerApi().get(loadBalancerName).getSubnets().size(), 2);


   }
   @Test
   protected void testDetachToSubnet() {
      final LoadBalancer loadBalancer = api.getLoadBalancerApi().get(loadBalancerName);
      final FluentIterable<Subnet> list = ec2Api().getSubnetApi().get().list();

      String subnetIdToAttach = null;

      for (Iterator<Subnet> subnet = list.iterator(); subnet.hasNext();) {
         Subnet currentSubnet = subnet.next();
         if (!loadBalancer.getSubnets().contains(currentSubnet.getSubnetId())) {
            subnetIdToAttach = currentSubnet.getSubnetId();
            break;
         }
      }

      checkNotNull(subnetIdToAttach, "Could not find a subnet to attach to the load balancer");
      Assert.assertEquals(1, loadBalancer.getSubnets().size());

      final ImmutableSet<String> subnets = ImmutableSet.<String>builder()
              .addAll(loadBalancer.getSubnets())
              .add(subnetIdToAttach)
              .build();


      final Set<String> resultOfAttach = subnetApi().attachLoadBalancerToSubnets(loadBalancerName, subnets);

      Assert.assertEquals(resultOfAttach.size(), subnets.size());
      Assert.assertEquals(api.getLoadBalancerApi().get(loadBalancerName).getSubnets().size(), 2);

      final Set<String> resultOfDetach = subnetApi().detachLoadBalancerFromSubnets(loadBalancerName, ImmutableSet.of(subnetIdToAttach));

      Assert.assertEquals(resultOfDetach.size(), 1);
      Assert.assertEquals(api.getLoadBalancerApi().get(loadBalancerName).getSubnets().size(), 1);

   }

   private String generateRandomName() {
      return String.format("%s-%s",  "test", new Random().nextInt(10));
   }
}
