/*
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */
package org.geoserver.jackson.databind.config.dto.mapper;

import org.geoserver.catalog.CatalogInfo;
import org.geoserver.catalog.Info;
import org.geoserver.cog.CogSettings;
import org.geoserver.cog.CogSettingsStore;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.CoverageAccessInfo;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.JAIInfo;
import org.geoserver.config.LoggingInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.SettingsInfo;
import org.geoserver.gwc.wmts.WMTSInfo;
import org.geoserver.gwc.wmts.WMTSInfoImpl;
import org.geoserver.jackson.databind.catalog.dto.CatalogInfoDto;
import org.geoserver.jackson.databind.catalog.dto.InfoDto;
import org.geoserver.jackson.databind.catalog.mapper.CatalogInfoMapper;
import org.geoserver.jackson.databind.config.dto.CogSettingsDto;
import org.geoserver.jackson.databind.config.dto.CogSettingsStoreDto;
import org.geoserver.jackson.databind.config.dto.ConfigInfoDto;
import org.geoserver.jackson.databind.config.dto.Contact;
import org.geoserver.jackson.databind.config.dto.CoverageAccess;
import org.geoserver.jackson.databind.config.dto.GeoServer;
import org.geoserver.jackson.databind.config.dto.JaiDto;
import org.geoserver.jackson.databind.config.dto.Logging;
import org.geoserver.jackson.databind.config.dto.Service;
import org.geoserver.jackson.databind.config.dto.Settings;
import org.geoserver.wcs.WCSInfo;
import org.geoserver.wcs.WCSInfoImpl;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.WFSInfoImpl;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSInfoImpl;
import org.geoserver.wps.WPSInfo;
import org.geoserver.wps.WPSInfoImpl;
import org.geotools.util.Version;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/** Mapper to/from GeoServer config objects and their respective DTO representations */
@Mapper(config = ConfigInfoMapperConfig.class)
public interface GeoServerConfigMapper {

    CatalogInfoMapper catalogInfoMapper = Mappers.getMapper(CatalogInfoMapper.class);

    default <T extends Info> T toInfo(InfoDto dto) {
        if (dto == null) return null;
        if (dto instanceof ConfigInfoDto) return toInfo((ConfigInfoDto) dto);
        if (dto instanceof CatalogInfoDto) return catalogInfoMapper.map((CatalogInfoDto) dto);
        throw new IllegalArgumentException(
                "Unknown config DTO type: " + dto.getClass().getCanonicalName());
    }

    @SuppressWarnings("unchecked")
    default <T extends InfoDto> T toDto(Info info) {
        if (info == null) return null;
        if (info instanceof GeoServerInfo) return (T) toDto((GeoServerInfo) info);
        if (info instanceof SettingsInfo) return (T) toDto((SettingsInfo) info);
        if (info instanceof LoggingInfo) return (T) toDto((LoggingInfo) info);
        if (info instanceof ServiceInfo) return (T) toDto((ServiceInfo) info);
        if (info instanceof CatalogInfo) return (T) catalogInfoMapper.map((CatalogInfo) info);

        throw new IllegalArgumentException(
                "Unknown config info type: " + info.getClass().getCanonicalName());
    }

    @SuppressWarnings("unchecked")
    default <T extends Info> T toInfo(ConfigInfoDto dto) {
        if (dto == null) return null;
        if (dto instanceof GeoServer) return (T) toInfo((GeoServer) dto);
        if (dto instanceof Settings) return (T) toInfo((Settings) dto);
        if (dto instanceof Logging) return (T) toInfo((Logging) dto);
        if (dto instanceof Service) return (T) toInfo((Service) dto);

        throw new IllegalArgumentException(
                "Unknown config DTO type: " + dto.getClass().getCanonicalName());
    }

    @Mapping(target = "id", ignore = true) // set by factory method
    @Mapping(target = "clientProperties", ignore = true)
    GeoServerInfo toInfo(GeoServer dto);

    GeoServer toDto(GeoServerInfo info);

    @Mapping(target = "id", ignore = true) // set by factory method
    @Mapping(target = "clientProperties", ignore = true)
    SettingsInfo toInfo(Settings dto);

    Settings toDto(SettingsInfo info);

    @Mapping(target = "tileCache", ignore = true)
    @Mapping(target = "JAI", ignore = true)
    JAIInfo jaiInfo(JaiDto dto);

    JaiDto jaiInfo(JAIInfo info);

    @Mapping(target = "id", ignore = true) // set by factory method
    LoggingInfo toInfo(Logging dto);

    Logging toDto(LoggingInfo info);

    @Mapping(target = "threadPoolExecutor", ignore = true)
    CoverageAccessInfo coverageAccessInfo(CoverageAccess dto);

    CoverageAccess coverageAccessInfo(CoverageAccessInfo info);

    @Mapping(target = "id", ignore = true) // set by factory method
    ContactInfo contactInfo(Contact dto);

    Contact contactInfo(ContactInfo info);

    default ServiceInfo toInfo(Service dto) {
        if (dto == null) return null;
        if (dto instanceof Service.WmsService) return toInfo((Service.WmsService) dto);
        if (dto instanceof Service.WfsService) return toInfo((Service.WfsService) dto);
        if (dto instanceof Service.WcsService) return toInfo((Service.WcsService) dto);
        if (dto instanceof Service.WpsService) return toInfo((Service.WpsService) dto);
        if (dto instanceof Service.WmtsService) return toInfo((Service.WmtsService) dto);

        throw new IllegalArgumentException(
                "Unknown ServiceInfo type: " + dto.getClass().getCanonicalName());
    }

    default Service toDto(ServiceInfo info) {
        if (info == null) return null;
        if (info instanceof WMSInfo) return toDto((WMSInfo) info);
        if (info instanceof WFSInfo) return toDto((WFSInfo) info);
        if (info instanceof WCSInfo) return toDto((WCSInfo) info);
        if (info instanceof WPSInfo) return toDto((WPSInfo) info);
        if (info instanceof WMTSInfo) return toDto((WMTSInfo) info);

        throw new IllegalArgumentException(
                "Unknown ServiceInfo type: " + info.getClass().getCanonicalName());
    }

    /**
     * {@link ServiceInfo#getVersions()} does not parameterize the list, hence Mapstruct assigns the
     * {@code List<String>} as is
     */
    default List<org.geotools.util.Version> stringListToVersionList(List<String> list) {
        return list == null ? null : list.stream().map(Version::new).collect(Collectors.toList());
    }

    @Mapping(target = "clientProperties", ignore = true)
    @Mapping(target = "geoServer", ignore = true)
    @Mapping(target = "versions", expression = "java(stringListToVersionList(dto.getVersions()))")
    WMSInfoImpl toInfo(Service.WmsService dto);

    Service.WmsService toDto(WMSInfo info);

    @Mapping(target = "clientProperties", ignore = true)
    @Mapping(target = "geoServer", ignore = true)
    @Mapping(target = "versions", expression = "java(stringListToVersionList(dto.getVersions()))")
    WFSInfoImpl toInfo(Service.WfsService dto);

    Service.WfsService toDto(WFSInfo info);

    @Mapping(target = "clientProperties", ignore = true)
    @Mapping(target = "geoServer", ignore = true)
    @Mapping(target = "versions", expression = "java(stringListToVersionList(dto.getVersions()))")
    WCSInfoImpl toInfo(Service.WcsService dto);

    Service.WcsService toDto(WCSInfo info);

    @Mapping(target = "clientProperties", ignore = true)
    @Mapping(target = "geoServer", ignore = true)
    @Mapping(target = "versions", expression = "java(stringListToVersionList(dto.getVersions()))")
    WPSInfoImpl toInfo(Service.WpsService dto);

    Service.WpsService toDto(WPSInfo info);

    @Mapping(target = "clientProperties", ignore = true)
    @Mapping(target = "geoServer", ignore = true)
    @Mapping(target = "versions", expression = "java(stringListToVersionList(dto.getVersions()))")
    WMTSInfoImpl toInfo(Service.WmtsService dto);

    Service.WmtsService toDto(WMTSInfo info);

    CogSettings cogSettings(CogSettingsDto dto);

    CogSettingsDto cogSettings(CogSettings info);

    CogSettingsStore cogSettingsStore(CogSettingsStoreDto dto);

    CogSettingsStoreDto cogSettingsStore(CogSettingsStore info);
}
