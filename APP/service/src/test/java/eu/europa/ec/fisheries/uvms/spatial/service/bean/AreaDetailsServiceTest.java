package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author padhyad
 *         Unit test for Area details service
 */
@RunWith(MockitoJUnitRunner.class)
public class AreaDetailsServiceTest {

    @Mock
    private SpatialRepository repository;

    @InjectMocks
    private AreaDetailsServiceBean areaDetailsServiceBean;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SneakyThrows
    @Ignore("WILL FIX LATER")
    public void getEezDetailsByCoordinates() {
        List<AreaLocationTypesEntity> areaEntities = new ArrayList<>();
        areaEntities.add(getMockAreaTypeEntity(AreaType.EEZ.value(), true));
        EezEntity eezEntity = getMockedEezEntity();
        mockCrudServiceBean(areaEntities, eezEntity);
        mockEntityByCoordinate(Arrays.asList(eezEntity));
        AreaTypeEntry request = new AreaTypeEntry();
        request.setAreaType(AreaType.EEZ);
        request.setLatitude(41.0);
        request.setLongitude(-9.5);
        request.setCrs(4326);
        List<AreaDetails> areaDetails = areaDetailsServiceBean.getAreaDetailsByLocation(request);
        assertNotNull(areaDetails.get(0).getAreaProperties());
        assertEquals(areaDetails.get(0).getAreaProperties().isEmpty(), false);
    }

    @Test
    @SneakyThrows
    public void shouldReturnEmptyResponseWhenNoAreaFound() {
        // given
        AreaTypeEntry areaTypeEntry = createAreaTypeEntry();

        //when
        List<AreaDetails> areaDetailsByLocation = areaDetailsServiceBean.getAreaDetailsByLocation(areaTypeEntry);

        // then
		assertNotNull(areaDetailsByLocation);
        assertEquals(0, areaDetailsByLocation.size());
    }

    private AreaTypeEntry createAreaTypeEntry() {
        List<AreaLocationTypesEntity> areaEntities = new ArrayList<AreaLocationTypesEntity>();
        areaEntities.add(getMockAreaTypeEntity(AreaType.EEZ.value(), true));
        EezEntity eezEntity = getMockedEezEntity();
        mockCrudServiceBean(areaEntities, eezEntity);
        mockEntityByCoordinate(new ArrayList());
        AreaTypeEntry request = new AreaTypeEntry();
        request.setAreaType(AreaType.EEZ);
        request.setLatitude(41.0);
        request.setLongitude(-9.5);
        request.setCrs(4326);
        return request;
    }

    /**
     * Test EEZ entity for valid response
     */
    @Test
    @SneakyThrows
    public void getEezAreaDetailsTest() {
        List<AreaLocationTypesEntity> areaEntities = new ArrayList<AreaLocationTypesEntity>();
        areaEntities.add(getMockAreaTypeEntity(AreaType.EEZ.value(), true));
        EezEntity eezEntity = getMockedEezEntity();
        mockCrudServiceBean(areaEntities, eezEntity);
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType(AreaType.EEZ);
        areaTypeEntry.setId("1");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
        AreaDetailsSpatialRequest request = areaDetailsSpatialRequest;
        AreaDetails areaDetails = areaDetailsServiceBean.getAreaDetails(request);
        assertNotNull(areaDetails);
        List<AreaProperty> list = areaDetails.getAreaProperties();
        assertEquals(list.isEmpty(), false);
    }

    /**
     * Test RFMO entity for valid response
     */
    @Test
    @SneakyThrows
    public void getRfmoAreaDetailsTest() {
        List<AreaLocationTypesEntity> areaEntities = new ArrayList<AreaLocationTypesEntity>();
        areaEntities.add(getMockAreaTypeEntity("RFMO", true));
        RfmoEntity rfmoEntity = getMockedRfmoEntity();
        mockCrudServiceBean(areaEntities, rfmoEntity);
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType(AreaType.RFMO);
        areaTypeEntry.setId("1");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
        AreaDetailsSpatialRequest request = areaDetailsSpatialRequest;
        AreaDetails areaDetails = areaDetailsServiceBean.getAreaDetails(request);
        assertNotNull(areaDetails);
        List<AreaProperty> list = areaDetails.getAreaProperties();
        assertEquals(list.isEmpty(), false);
    }

    /**
     * Test for Invalid entity in the DB
     */
    @Test(expected = SpatialServiceException.class)
    @SneakyThrows
    public void invalidEntityTest() {
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType(null);
        areaTypeEntry.setId("1");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
        AreaDetailsSpatialRequest request = areaDetailsSpatialRequest;
        areaDetailsServiceBean.getAreaDetails(request);
    }

    /**
     * Test for invalid for in input
     */
    @Test(expected = SpatialServiceException.class)
    @SneakyThrows
    public void invalidRowTest() {
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType(AreaType.EEZ);
        areaTypeEntry.setId("INVALID_ROW");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
        areaDetailsServiceBean.getAreaDetails(areaDetailsSpatialRequest);
    }

    /**
     * Test for non existing row in DB
     */
    @Test(expected = SpatialServiceException.class)
    @SneakyThrows
    public void nonExistingRowTest() {
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType(AreaType.EEZ);
        areaTypeEntry.setId("100000");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
        areaDetailsServiceBean.getAreaDetails(areaDetailsSpatialRequest);
    }

    private void mockEntityByCoordinate(List list) {
        Mockito.when(repository.findAreaOrLocationByCoordinates(Mockito.any(Point.class), Mockito.any(String.class))).thenReturn(list);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public void mockCrudServiceBean(List<AreaLocationTypesEntity> returnList, Object entity) {
        Mockito.when(repository.findEntityByNamedQuery(Mockito.any(Class.class), Mockito.any(String.class), Mockito.any(Map.class), Mockito.any(Integer.class))).thenReturn(returnList);
        Mockito.when(repository.findEntityById(Mockito.any(Class.class), Mockito.any(Object.class))).thenReturn(entity);
    }

    private AreaLocationTypesEntity getMockAreaTypeEntity(String typeName, Boolean isSystemWide) {
        AreaLocationTypesEntity areaEntity = Mockito.mock(AreaLocationTypesEntity.class);
        Mockito.when(areaEntity.getTypeName()).thenReturn(typeName.toUpperCase());
        Mockito.when(areaEntity.getIsSystemWide()).thenReturn(isSystemWide);
        return areaEntity;
    }

    private EezEntity getMockedEezEntity() {
        EezEntity eezEntity = new EezEntity();
        eezEntity.setAreaM2(50.0);
        eezEntity.setCountry("Belgium");
        eezEntity.setDateChang(String.valueOf(new Date().getTime()));
        eezEntity.setName(AreaType.EEZ.value());
        eezEntity.setEezId((long) 123);
        eezEntity.setGeom(new GeometryBuilder().point());
        eezEntity.setGid(1);
        eezEntity.setCode("iso3digit");
        eezEntity.setLatitude(345.60);
        eezEntity.setLongitude(234.54);
        eezEntity.setMrgid(new BigDecimal("100"));
        eezEntity.setMrgidEez(123);
        eezEntity.setRemarks("This is Test");
        eezEntity.setSovereign("Test");
        eezEntity.setSovId(555);
        return eezEntity;
    }

    private RfmoEntity getMockedRfmoEntity() {
        RfmoEntity rfmoEntity = new RfmoEntity();
        rfmoEntity.setGeom(new GeometryBuilder().point());
        rfmoEntity.setGid(1);
        rfmoEntity.setName("RFMO");
        rfmoEntity.setCode("Test RFMO");
        rfmoEntity.setTuna("no");
        return rfmoEntity;
    }
}
