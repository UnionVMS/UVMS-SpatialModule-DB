package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Map;

@Entity
@NamedQueries({
        @NamedQuery(name = RfmoEntity.RFMO_BY_COORDINATE,
                query = "FROM RfmoEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = QueryNameConstants.RFMO_COLUMNS,
                query = "SELECT rfmo.name AS name, rfmo.code AS code FROM RfmoEntity AS rfmo WHERE rfmo.id =:gid"),
        @NamedQuery(name = RfmoEntity.DISABLE_RFMO_AREAS,
                query = "UPDATE RfmoEntity SET enabled = 'N'"),
        @NamedQuery(name = RfmoEntity.SEARCH_RFMO, query = "FROM RfmoEntity where upper(name) like :name OR upper(code) like :code AND enabled='Y' GROUP BY gid")
})
@Table(name = "rfmo")
public class RfmoEntity extends BaseSpatialEntity {

    public static final String RFMO_BY_COORDINATE = "rfmoEntity.ByCoordinate";
    public static final String DISABLE_RFMO_AREAS = "rfmoEntity.disableRfmoAreas";
    public static final String SEARCH_RFMO = "rfmoEntity.searchRfmoByNameOrCode";

    private static final String TUNA = "tuna";

    @Column(length = 10)
    @ColumnAliasName(aliasName = TUNA)
    private String tuna;

    public RfmoEntity() {
        // why JPA why
    }

    public RfmoEntity(Map<String, Object> values) throws ServiceException {
        super(values);
        setTuna(readStringProperty(values, TUNA));
    }

    public String getTuna() {
        return this.tuna;
    }

    public void setTuna(String tuna) {
        this.tuna = tuna;
    }

}
