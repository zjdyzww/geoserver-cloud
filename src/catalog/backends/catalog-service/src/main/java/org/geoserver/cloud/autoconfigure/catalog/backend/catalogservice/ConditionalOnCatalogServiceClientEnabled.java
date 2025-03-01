/*
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */
package org.geoserver.cloud.autoconfigure.catalog.backend.catalogservice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
@ConditionalOnClass(org.geoserver.cloud.catalog.client.impl.CatalogClientConfiguration.class)
@ConditionalOnProperty(
        prefix = "geoserver.backend.catalog-service",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false)
public @interface ConditionalOnCatalogServiceClientEnabled {}
