package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * This class is has the example of how to add dependency when working with
 * junit. it is commented because some components need to be made first. You
 * will have to import everything you need.
 *
 * @author Shariar (Shawn) Emami
 */
@TestMethodOrder(OrderAnnotation.class)
class BloodDonationTest {
//

    private BloodDonationLogic logic;
    private BloodDonation expectedEntity;
//

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat("/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test");
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {

        logic = LogicFactory.getFor("BloodDonation");
//        /* **********************************
//         * ***********IMPORTANT**************
//         * **********************************/
//        //we only do this for the test.
//        //always create Entity using logic.
//        //we manually make the account to not rely on any logic functionality , just for testing
//
//        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();
        //start a Transaction
//        //check if the depdendecy exists on DB already
        //em.find takes two arguments, the class type of return result and the primery key.
        BloodDonation bd = em.find(BloodDonation.class, 1);
        BloodBank bb = em.find(BloodBank.class, 1);
//        //if result is null create the entity and persist it
        if (bb == null) {
//            //cearet object
            bb = new BloodBank();
            bb.setName("JUNIT");
            bb.setPrivatelyOwned(true);
            bb.setEstablished(logic.convertStringToDate("1111-11-11 11:11:11"));
            bb.setEmplyeeCount(111);
//            //persist the dependency first
            em.getTransaction().begin();
            em.persist(bb);
//        }
            em.getTransaction().commit();
        }
//
//        //create the desired entity
        BloodDonation entity = new BloodDonation();
        entity.setMilliliters(100);
        entity.setBloodGroup(BloodGroup.AB);
        entity.setRhd(RhesusFactor.Negative);
        //entity.setCreated(logic.convertStringToDate("1111-11-11 11:11:11"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Date currentDate = new Date(System.currentTimeMillis());
        String formattedDate = (formatter.format(currentDate));
        entity.setCreated(logic.convertStringToDate(formattedDate));
        //add dependency to the desired entity
        entity.setBloodBank(bb);
//
        em.getTransaction().begin();
//        //add desired entity to hibernate, entity is now managed.
//        //we use merge instead of add so we can get the managed entity.
        expectedEntity = em.merge(entity);
//        //commit the changes
        em.getTransaction().commit();
//        //close EntityManager
        em.close();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if (expectedEntity != null) {
            logic.delete(expectedEntity);
        }
    }

    @Test
    final void testGetAll() {

        List<BloodDonation> list = logic.getAll();
        int originalSize = list.size();
        assertNotNull(expectedEntity);
        logic.delete(expectedEntity);
        list = logic.getAll();
        assertEquals(originalSize - 1, list.size());
    }

    @Test
    final void testGetWithId() {

        BloodDonation returnedBloodDonation = logic.getWithId(expectedEntity.getId());
        assertAccountEquals(expectedEntity, returnedBloodDonation);
    }

    @Test
    final void testGetWithBloodBank() { //requires id of bloodbank accept blookd bankd or accept id 

        BloodBank bb = (expectedEntity.getBloodBank());
        List<BloodDonation> returnedResults = logic.getBloodDonationsWithBloodBank(bb.getId());

        for (BloodDonation bloodD : returnedResults) {
            assertEquals(expectedEntity.getBloodBank().getId(), bloodD.getBloodBank().getId());

        }
    }

    @Test
    final void testGetWithBloodGroup() {
        List<BloodDonation> returnedResults = logic.getBloodDonationWithBloodGroup(expectedEntity.getBloodGroup());
        for (BloodDonation bg : returnedResults) {
            assertEquals(expectedEntity.getBloodGroup(), bg.getBloodGroup());
        }
    }

    @Test
    final void testGetWithCreated() {
        List<BloodDonation> returnedResults = logic.getBloodDonationWithCreated(expectedEntity.getCreated());
        for (BloodDonation bg : returnedResults) {
            assertEquals(expectedEntity.getCreated(), bg.getCreated());
        }
    }

    @Test
    final void testGetWithMillileters() {
        List<BloodDonation> returnedResults = logic.getBloodDonationWithMilliliters(expectedEntity.getMilliliters());
        for (BloodDonation account : returnedResults) {
            assertEquals(expectedEntity.getMilliliters(), account.getMilliliters());
        }
    }

    @Test
    final void testGetWithRhd() {
        List<BloodDonation> returnedResults = logic.getBloodDonationsWithRhd(expectedEntity.getRhd());
        for (BloodDonation account : returnedResults) {
            assertEquals(expectedEntity.getRhd(), account.getRhd());
        }
    }

    @Test
    final void testGetColumnNames() {
        List<String> bdList = logic.getColumnNames();
        assertEquals(Arrays.asList("id", "bank_id", "milliliters", "blood_group", "rhesus_factor", "created"), bdList);
    }

    @Test
    final void testGetColumnCodes() {
        List<String> bdList = logic.getColumnCodes();
        assertEquals(Arrays.asList(BloodDonationLogic.ID, BloodDonationLogic.BANK_ID, BloodDonationLogic.MILLILITERS, BloodDonationLogic.BLOOD_GROUP, BloodDonationLogic.RHESUS_FACTOR, BloodDonationLogic.CREATED), bdList);
    }

    @Test
    final void testExtractDataAsList() {
        List<?> bdList = logic.extractDataAsList(expectedEntity);
        assertEquals(expectedEntity.getId(), bdList.get(0));
        assertEquals(expectedEntity.getBloodBank(), bdList.get(1));
        assertEquals(expectedEntity.getMilliliters(), bdList.get(2));
        assertEquals(expectedEntity.getBloodGroup(), bdList.get(3));
        assertEquals(expectedEntity.getRhd(), bdList.get(4));
        assertEquals(expectedEntity.getCreated(), bdList.get(5));
    }

    @Test
    final void testCreateEntity() {
        Map<String, String[]> testMap = new HashMap<>();
        testMap.put(BloodDonationLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
        testMap.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
        testMap.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().name()});
        testMap.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
        testMap.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().name()});
        testMap.put(BloodDonationLogic.CREATED, new String[]{expectedEntity.getCreated().toString()});

        BloodDonation returnedBD = logic.createEntity(testMap);

        assertAccountEquals(expectedEntity, returnedBD);

    }

    @Test
    final void testCreateEntityNullAndEmptyIDValues() {
        Map<String, String[]> testMap = new HashMap<>();

        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            testMap.put(BloodDonationLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
            map.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().name()});
            map.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
            map.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().name()});
            map.put(BloodDonationLogic.CREATED, new String[]{expectedEntity.getCreated().toString()});
        };

        fillMap.accept(testMap);
        testMap.replace(BloodDonationLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(testMap));

    }

    @Test
    final void testCreateEntityNullAndEmptyBankIDValues() {
        Map<String, String[]> testMap = new HashMap<>();

        Consumer<Map<String, String[]>> fillMap = getTempMap();

        fillMap.accept(testMap);
        testMap.replace(BloodDonationLogic.BANK_ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(testMap));

    }

    /**
     * @Test final void testCreateEntityInvalidMillileterValues1() {
     * Map<String, String[]> testMap = new HashMap<>();
     *
     * Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) ->
     * { map.clear(); testMap.put(BloodDonationLogic.ID, new
     * String[]{Integer.toString(expectedEntity.getId())});
     * map.put(BloodDonationLogic.MILLILITERS, new
     * String[]{Integer.toString(expectedEntity.getMilliliters())});
     * map.put(BloodDonationLogic.BLOOD_GROUP, new
     * String[]{expectedEntity.getBloodGroup().name()});
     * map.put(BloodDonationLogic.BANK_ID, new
     * String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
     * map.put(BloodDonationLogic.RHESUS_FACTOR, new
     * String[]{expectedEntity.getRhd().name()});
     * map.put(BloodDonationLogic.CREATED, new
     * String[]{expectedEntity.getCreated().toString()}); };
     *
     * fillMap.accept(testMap); testMap.replace(BloodDonationLogic.MILLILITERS,
     * new String [] {Integer.toString(-1)});
     * assertThrows(IllegalArgumentException.class, ()->
     * logic.createEntity(testMap));
     *
     * }
     */
    @Test
    final void testCreateEntityInvalidMillileterValues() {
        Map<String, String[]> testMap = new HashMap<>();

        Consumer<Map<String, String[]>> fillMap = getTempMap();

        fillMap.accept(testMap);
        testMap.replace(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(-1)});
        assertThrows(IllegalArgumentException.class, () -> logic.createEntity(testMap));

    }
    
     @Test
    final void testCreateEntityEdgeMillileterValues() {
        Map<String, String[]> testMap = new HashMap<>();

        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            testMap.put(BloodDonationLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
            map.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().name()});
            map.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
            map.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().name()});
            map.put(BloodDonationLogic.CREATED, new String[]{expectedEntity.getCreated().toString()});
        };

        fillMap.accept(testMap);
        testMap.replace(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(1000000000)});
        expectedEntity.setMilliliters(1000000000);
        BloodDonation returnedBD = logic.createEntity(testMap);
        
        assertAccountEquals(expectedEntity, returnedBD);

    }
    

    @Test
    final void testCreateEntityInvalidBloodGroupValues() {
        Map<String, String[]> testMap = new HashMap<>();

        Consumer<Map<String, String[]>> fillMap = getTempMap();

        fillMap.accept(testMap);
        testMap.replace(BloodDonationLogic.BLOOD_GROUP, null);
        assertThrows(ValidationException.class, () -> logic.createEntity(testMap));

    }

    /**
     * Method creates a String Map populated with values of a BloodDonation
     * entity
     *
     * @return fillMap: temporary String Map to test values added to create an
     * Entity
     */
    public Consumer<Map<String, String[]>> getTempMap() {
        Map<String, String[]> testMap = new HashMap<>();

        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            testMap.put(BloodDonationLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(BloodDonationLogic.MILLILITERS, new String[]{Integer.toString(expectedEntity.getMilliliters())});
            map.put(BloodDonationLogic.BLOOD_GROUP, new String[]{expectedEntity.getBloodGroup().name()});
            map.put(BloodDonationLogic.BANK_ID, new String[]{Integer.toString(expectedEntity.getBloodBank().getId())});
            map.put(BloodDonationLogic.RHESUS_FACTOR, new String[]{expectedEntity.getRhd().name()});
            map.put(BloodDonationLogic.CREATED, new String[]{expectedEntity.getCreated().toString()});
        };

        return fillMap;
    }

    /**
     * @Test final void testCreateEntityAndAdd(){ Map<String, String[]> testMap
     * = new HashMap<>(); testMap.put(BloodDonationLogic.ID, new
     * String[]{"22"}); testMap.put(BloodDonationLogic.MILLILITERS, new
     * String[]{"1234"}); testMap.put(BloodDonationLogic.BLOOD_GROUP, new
     * String[]{"B"}); testMap.put(BloodDonationLogic.BANK_ID, new
     * String[]{"2"}); testMap.put(BloodDonationLogic.RHESUS_FACTOR, new
     * String[]{"Positive"}); testMap.put(BloodDonationLogic.CREATED, new
     * String[]{"2021-04-07 15:58:49.0"});
     *
     * BloodDonation returnedBD = logic.createEntity(testMap);
     *
     * logic.add(returnedBD);
     *
     * returnedBD= logic.getWithId(returnedBD.getId());
     *
     * assertEquals(testMap.get(BloodDonationLogic.BANK_ID)[0],
     * returnedBD.getBloodBank());
     *
     * logic.delete(returnedBD);
     *
     * }
     */
    private void assertAccountEquals(BloodDonation expected, BloodDonation actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBloodBank().getId(), actual.getBloodBank().getId());
        assertEquals(expected.getMilliliters(), actual.getMilliliters());
        assertEquals(expected.getBloodGroup(), actual.getBloodGroup());
        assertEquals(expected.getRhd(), actual.getRhd());
        assertEquals(expected.getCreated(), actual.getCreated());

    }
}
