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
package org.jclouds.elb.binders;

import static com.google.common.base.Preconditions.checkNotNull;

import org.jclouds.elb.domain.HealthCheck;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.Binder;

import com.google.common.collect.ImmutableMultimap;

public class BindHealthCheckToFormParams implements Binder {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <R extends HttpRequest> R bindToRequest(R request, Object input) {
        HealthCheck healthCheck = checkNotNull(input, "healthCheck must be set!") instanceof HealthCheck ? 
                HealthCheck.class.cast(input) : (HealthCheck) input;

        ImmutableMultimap.Builder<String, String> formParameters = ImmutableMultimap.builder();
        formParameters.put("HealthCheck.HealthyThreshold", healthCheck.getHealthyThreshold() + "");
        formParameters.put("HealthCheck.UnhealthyThreshold", healthCheck.getUnhealthyThreshold() + "");
        formParameters.put("HealthCheck.Target", healthCheck.getTarget());
        formParameters.put("HealthCheck.Interval", healthCheck.getInterval() + "");
        formParameters.put("HealthCheck.Timeout", healthCheck.getTimeout() + "");
        return (R) request.toBuilder().replaceFormParams(formParameters.build()).build();

    }
}
