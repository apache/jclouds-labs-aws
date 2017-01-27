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
package org.jclouds.elb;

import java.io.Closeable;
import java.util.Set;

import org.jclouds.aws.filters.FormSigner;
import org.jclouds.elb.features.AvailabilityZoneApi;
import org.jclouds.elb.features.HealthCheckApi;
import org.jclouds.elb.features.InstanceApi;
import org.jclouds.elb.features.LoadBalancerApi;
import org.jclouds.elb.features.PolicyApi;
import org.jclouds.elb.features.SubnetApi;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.location.Region;
import org.jclouds.location.functions.RegionToEndpointOrProviderIfNull;
import org.jclouds.rest.annotations.Delegate;
import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.VirtualHost;

import com.google.common.annotations.Beta;
import com.google.inject.Provides;

/**
 * Provides access to EC2 Elastic Load Balancer via their REST API.
 * <p/>
 */
@Beta
@RequestFilters(FormSigner.class)
@VirtualHost
public interface ELBApi extends Closeable {
   /**
    * 
    * @return the Region codes configured
    */
   @Provides
   @Region
   Set<String> getConfiguredRegions();

   /**
    * Provides access to LoadBalancer features.
    */
   @Delegate
   LoadBalancerApi getLoadBalancerApi();

   @Delegate
   LoadBalancerApi getLoadBalancerApiForRegion(
            @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);

   /**
    * Provides access to Policy features.
    */
   @Delegate
   PolicyApi getPolicyApi();

   @Delegate
   PolicyApi getPolicyApiForRegion(
            @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);

   /**
    * Provides access to Instance features.
    */
   @Delegate
   InstanceApi getInstanceApi();

   @Delegate
   InstanceApi getInstanceApiForRegion(
            @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);
   
   /**
    * Provides access to Zone features.
    */
   @Delegate
   AvailabilityZoneApi getAvailabilityZoneApi();

   @Delegate
   AvailabilityZoneApi getAvailabilityZoneApiForRegion(
            @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);

   /**
    * Provides access to HealthCheck features.
    */
   @Delegate
   HealthCheckApi getHealthCheckApi();

   @Delegate
   HealthCheckApi getHealthCheckApiForRegion(
           @EndpointParam(parser = RegionToEndpointOrProviderIfNull.class) @Nullable String region);

   /**
    * Provides access to Subnet features.
    */
   @Delegate
   SubnetApi getSubnetApi();

}
