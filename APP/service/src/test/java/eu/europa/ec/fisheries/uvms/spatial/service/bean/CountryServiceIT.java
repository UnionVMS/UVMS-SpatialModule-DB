package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class CountryServiceIT extends BaseSpatialArquillianTest {

    @EJB
    private AreaService countryService;

    @Test
    public void TestGetAllCountries() {
        Map<String, String> allCountries =  countryService.getAllCountriesDesc();
        assertNotNull(allCountries);
        assertFalse(allCountries.isEmpty());
    }
}
