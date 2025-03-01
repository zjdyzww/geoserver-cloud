/*
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */
package org.geoserver.cloud.event.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.NonNull;

import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;

/** Event sent when {@link GeoServer#setGlobal(GeoServerInfo)} is called */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonTypeName("GeoServerInfoSet")
public class GeoServerInfoSet extends ConfigInfoAdded<GeoServerInfoSet, GeoServerInfo>
        implements ConfigInfoEvent {

    protected GeoServerInfoSet() {
        // default constructor, needed for deserialization
    }

    protected GeoServerInfoSet(@NonNull Long updateSequence, GeoServerInfo object) {
        super(updateSequence, object);
    }

    public static GeoServerInfoSet createLocal(@NonNull Long updateSequence, GeoServerInfo value) {
        return new GeoServerInfoSet(updateSequence, value);
    }
}
