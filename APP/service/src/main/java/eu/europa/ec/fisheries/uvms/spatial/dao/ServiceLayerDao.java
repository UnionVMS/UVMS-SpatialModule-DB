package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.service.AbstractDAO;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
public class ServiceLayerDao extends AbstractDAO<ServiceLayerEntity> {

    private static final String SUB_TYPE = "subTypes";

    private EntityManager em;

    public ServiceLayerDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @SuppressWarnings("unchecked")
    public ServiceLayerEntity getBy(String locationType) throws ServiceException {

        ServiceLayerEntity serviceLayerEntity = null;

        try {
            List<ServiceLayerEntity> layers = findEntityByNamedQuery(ServiceLayerEntity.class, ServiceLayerEntity.BY_LOCATION_TYPE,
                    QueryParameter.with("locationType", locationType).parameters(), 1);

            if (layers != null && layers.size() == 1) {
                serviceLayerEntity = layers.get(0);
            }

        }
        catch (Exception e){
            String error = "Error when trying to fetch resource layer from db";
            log.error(error);
            throw new ServiceException(error);
        }

        return serviceLayerEntity;

    }

    public ServiceLayerEntity getByAreaLocationType(String areaLocationType) throws ServiceException {
        List<ServiceLayerEntity> serviceLayers = findEntityByNamedQuery(ServiceLayerEntity.class, ServiceLayerEntity.BY_AREA_LOCATION_TYPE,
                QueryParameter.with("typeName", areaLocationType).parameters(), 1);
        if (serviceLayers != null && !serviceLayers.isEmpty()) {
            return serviceLayers.get(0);
        }
        return null;
    }

    public List<ServiceLayerDto> findServiceLayerBySubType(List<String> subAreaTypes, boolean isWithBing) {
        if (isWithBing) {
            return createNamedQueryWithParameterList(QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE, SUB_TYPE, subAreaTypes, ServiceLayerDto.class).list();
        } else {
            return createNamedQueryWithParameterList(QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING, SUB_TYPE, subAreaTypes, ServiceLayerDto.class).list();
        }
    }

    private <T> Query createNamedQueryWithParameterList(String nativeQuery, String parameterName, List<?> parameters, Class<T> dtoClass) {
        Query query = em.unwrap(Session.class).getNamedQuery(nativeQuery);
        query.setParameterList(parameterName, parameters);
        query.setResultTransformer(Transformers.aliasToBean(dtoClass));
        return query;
    }


    public List<ServiceLayerEntity> findServiceLayerEntityByIds(List<Long> ids) {
        Map<String, List<Long>> parameters = ImmutableMap.<String, List<Long>>builder().put("ids", ids).build();

        Query query = em.unwrap(Session.class).getNamedQuery(ServiceLayerEntity.FIND_SERVICE_LAYERS_BY_ID);
        for (Map.Entry<String, List<Long>> entry : parameters.entrySet()) {
            query.setParameterList(entry.getKey(), entry.getValue());
        }
        return query.list();
    }
}
