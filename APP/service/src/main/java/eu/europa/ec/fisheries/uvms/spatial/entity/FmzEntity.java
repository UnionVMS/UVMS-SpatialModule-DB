package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.annotation.ColumnAliasName;
import java.util.Date;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Entity
@Table(name = "fmz")
@NamedQueries({
        @NamedQuery(name = FmzEntity.DISABLE, query = "UPDATE FmzEntity SET enabled = 'N'"),
        @NamedQuery(name = FmzEntity.BY_INTERSECT,
                query = "FROM FmzEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = FmzEntity.SEARCH_FMZ, query = "FROM FmzEntity where upper(name) like :name OR upper(code) like :code AND enabled='Y' GROUP BY gid")
})
public class FmzEntity extends BaseSpatialEntity {

    public static final String DISABLE = "fmzEntity.disable";
    public static final String BY_INTERSECT = "fmzEntity.byIntersect";
    public static final String SEARCH_FMZ = "fmzEntity.SearcgFmzByNameOrCode";

    private static final String FMZ_ID = "fmz_id";
    private static final String EDITED = "edited";

    @Column(name = "fmz_id")
    @ColumnAliasName(aliasName = FMZ_ID)
    private Long fmzId;

    @Column(name = "edited")
    @ColumnAliasName(aliasName = EDITED)
    private String edited;

    public FmzEntity() {
        // why JPA why
    }

    public FmzEntity(Map<String, Object> values) throws ServiceException {
        super(values);
        String fmzId = readStringProperty(values, FMZ_ID);
        if (fmzId != null){
            this.fmzId = Long.valueOf(fmzId);
        }
        String edited = readStringProperty(values, EDITED);
        if (edited != null){
            this.edited = edited;
        }
    }


    public Long getFmzId() {
        return fmzId;
    }

    public void setFmzId(Long fmzId) {
        this.fmzId = fmzId;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }
}
