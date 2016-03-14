package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestLocationDto;

import java.util.List;

public interface ClosestLocationService {

    List<Location> getClosestLocationByLocationType(ClosestLocationSpatialRQ request) throws ServiceException;

    List<ClosestLocationDto> getClosestLocations(Double lat, Double lon, Integer crs, String unit, List<String> locationTypes) throws ServiceException;
}
