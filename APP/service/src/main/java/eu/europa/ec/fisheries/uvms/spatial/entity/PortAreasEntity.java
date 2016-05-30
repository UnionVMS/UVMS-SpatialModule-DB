package eu.europa.ec.fisheries.uvms.spatial.entity;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Map;

@Entity
@NamedQueries({
        @NamedQuery(name = PortAreasEntity.PORT_AREA_BY_COORDINATE,
                query = "FROM PortAreasEntity WHERE intersects(geom, :shape) = true AND enabled = 'Y'"),
        @NamedQuery(name = PortAreasEntity.DISABLE_PORT_AREAS, query = "UPDATE PortAreasEntity SET enabled = 'N'"),
        @NamedQuery(name = PortAreasEntity.SEARCH_PORTAREAS, query = "FROM PortAreasEntity where upper(name) like :name OR upper(code) like :code AND enabled='Y' GROUP BY gid")
})
@Table(name = "port_area")
public class PortAreasEntity extends BaseSpatialEntity {

    public static final String PORT_AREA_BY_COORDINATE = "portEntity.PortAreaByCoordinate";
    public static final String DISABLE_PORT_AREAS = "portAreasEntity.disablePortAreas";
    public static final String SEARCH_PORTAREAS = "portAreaEntity.searchPortAreaByNameOrCode";

    public PortAreasEntity() {
        // why JPA why
    }

    public PortAreasEntity(Map<String, Object> values) throws ServiceException {
        super(values);
    }
}
