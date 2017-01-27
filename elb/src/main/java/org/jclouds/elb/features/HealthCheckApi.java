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

import static org.jclouds.aws.reference.FormParameters.ACTION;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks;
import org.jclouds.aws.filters.FormSigner;
import org.jclouds.elb.binders.BindHealthCheckToFormParams;
import org.jclouds.elb.domain.HealthCheck;
import org.jclouds.elb.xml.HealthCheckHandler;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.FormParams;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.VirtualHost;
import org.jclouds.rest.annotations.XMLResponseParser;

/**
 * Provides access to Amazon ELB Health Check functionality via the Query API
 * <p/>
 */
@RequestFilters(FormSigner.class)
@Consumes(MediaType.APPLICATION_XML)
@VirtualHost
public interface HealthCheckApi {

   /**
    * @see <a href="http://docs.aws.amazon.com/elasticloadbalancing/2012-06-01/APIReference/API_ConfigureHealthCheck.html">doc</a>
    *      
    * @param loadBalancerName
    * @param healthCheck
    * @return
    */
   @Named("ConfigureHealthCheck")
   @POST
   @Path("/")
   @XMLResponseParser(HealthCheckHandler.class)
   @FormParams(keys = ACTION, values = "ConfigureHealthCheck")
   @Fallback(Fallbacks.NullOnNotFoundOr404.class)
   HealthCheck configureHealthCheck(@FormParam("LoadBalancerName") String loadBalancerName,
                                         @BinderParam(BindHealthCheckToFormParams.class) HealthCheck healthCheck);

}
