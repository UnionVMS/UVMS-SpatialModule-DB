package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.spatial.entity.converter.CharBooleanConverter;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "area_location_types", schema = "spatial", uniqueConstraints = @UniqueConstraint(columnNames = "type_name"))
@NamedQueries({
        @NamedQuery(name = AreaLocationTypesEntity.FIND_ALL_AREA_AND_LOCATION_TYPE_NAMES, query = "FROM AreaLocationTypesEntity area"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_ALL_IS_LOCATION, query = "FROM AreaLocationTypesEntity area WHERE isLocation = :isLocation"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE, query = "FROM AreaLocationTypesEntity WHERE isLocation = :isLocation AND isSystemWide = :isSystemWide"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_TYPE_BY_NAME, query = "FROM AreaLocationTypesEntity WHERE typeName= :typeName"),
        @NamedQuery(name = QueryNameConstants.FIND_TYPE_BY_NAMES, query = "FROM AreaLocationTypesEntity WHERE typeName in (:typeNames)"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_SYSTEM_AREA_LAYER, query = "select area.typeName as typeName, area.areaTypeDesc as areaTypeDesc,"
                + " layer.geoName as geoName, layer.isInternal as isInternal, layer.styleLabelGeom as style,"
                + " provider.serviceType as serviceType FROM AreaLocationTypesEntity as area INNER JOIN area.serviceLayer as layer"
                + " INNER JOIN layer.providerFormat as provider WHERE area.isSystemWide = 'Y' AND area.isLocation =  'N'"
                + " AND area.serviceLayer = layer AND layer.providerFormat = provider"),
        @NamedQuery(name = AreaLocationTypesEntity.FIND_SYSTEM_AREA_AND_LOCATION_LAYER, query = "select area.typeName as typeName, area.areaTypeDesc as areaTypeDesc, area.isLocation as isLocation,"
                + " layer.geoName as geoName, layer.isInternal as isInternal, layer.styleLabelGeom as style,"
                + " provider.serviceType as serviceType FROM AreaLocationTypesEntity as area INNER JOIN area.serviceLayer as layer"
                + " INNER JOIN layer.providerFormat as provider WHERE area.isSystemWide = 'Y'"
                + " AND area.serviceLayer = layer AND layer.providerFormat = provider"),
        @NamedQuery(name = QueryNameConstants.FIND_USER_AREA_LAYER, query = "SELECT area.typeName as typeName, layer.geoName as geoName, layer.isInternal as isInternal, layer.serviceUrl as serviceUrl, layer.styleLabelGeom as style,"
                + " provider.serviceType as serviceType FROM AreaLocationTypesEntity as area INNER JOIN area.serviceLayer as layer"
                + " INNER JOIN layer.providerFormat as provider WHERE area.isSystemWide = 'N' AND area.isLocation =  'N'"
                + " AND area.serviceLayer = layer AND area.areaDbTable = 'user_areas' AND layer.providerFormat = provider")
})

@ToString
@EqualsAndHashCode(callSuper = true)
public class AreaLocationTypesEntity extends BaseEntity {

    public static final String FIND_ALL_IS_LOCATION  = "AreaLocationType.findAllIsLocation";
    public static final String FIND_TYPE_BY_NAME = "AreaLocationType.findAreaByName";
    public static final String FIND_ALL_IS_LOCATION_IS_SYSTEM_WIDE = "AreaLocationType.findAllByIsLocationIsSystemWide";
    public static final String FIND_SYSTEM_AREA_LAYER = "AreaLocationType.findSystemAreaLayerMappings";
    public static final String FIND_SYSTEM_AREA_AND_LOCATION_LAYER = "AreaLocationType.findSystemAreaAndLocationLayerMappings";
    public static final String FIND_ALL_AREA_AND_LOCATION_TYPE_NAMES = "AreaLocationType.findAllAreaAndLocationTypeNames";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_layer_id", nullable = false)
    private ServiceLayerEntity serviceLayer;

    @Column(name = "type_name", unique = true, nullable = false, length=255)
    private String typeName;

    @Column(name = "area_type_desc", length=255)
    private String areaTypeDesc;

    @Column(name = "area_db_table", nullable = false, length=255)
    private String areaDbTable;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "is_system_wide", nullable = false, length = 1)
    private Boolean isSystemWide = false;

    @Convert(converter = CharBooleanConverter.class)
    @Column(name = "is_location", nullable = false, length = 1)
    private Boolean isLocation = false;

    public AreaLocationTypesEntity() {
        // No args constructor for use in serialization
    }

    public ServiceLayerEntity getServiceLayer() {
        return this.serviceLayer;
    }

    public void setServiceLayer(ServiceLayerEntity serviceLayer) {
        this.serviceLayer = serviceLayer;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAreaTypeDesc() {
        return this.areaTypeDesc;
    }

    public void setAreaTypeDesc(String areaTypeDesc) {
        this.areaTypeDesc = areaTypeDesc;
    }

    public String getAreaDbTable() {
        return this.areaDbTable;
    }

    public void setAreaDbTable(String areaDbTable) {
        this.areaDbTable = areaDbTable;
    }

    public Boolean getIsSystemWide() {
        return this.isSystemWide;
    }

    public void setIsSystemArea(Boolean isSystemWide) {
        this.isSystemWide = isSystemWide;
    }

    public Boolean getIsLocation() {
        return this.isLocation;
    }

    public void setIsLocation(Boolean isLocation) {
        this.isLocation = isLocation;
    }


}
