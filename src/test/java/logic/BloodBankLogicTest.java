package logic;

import common.EMFactory;
import common.TomcatStartUp;
import entity.BloodBank;
import entity.Person;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

/**
 *
 * @author koji
 */
public class BloodBankLogicTest {

    private BloodBankLogic logic;
    private BloodBank expectedEntity;
    private PersonLogic pLogic;
    SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat("/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test");
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    public void setUp() {

        logic = LogicFactory.getFor("BloodBank");
        pLogic = LogicFactory.getFor("Person");
        /* **********************************
         * ***********IMPORTANT**************
         * **********************************/
        //we only do this for the test.
        //always create Entity using logic.
        //we manually make the bloodbank to not rely on any logic functionality , just for testing

        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();
        //start a Transaction

        // for some reason this returns null :( which creates a lot of person classes (13 for 13 tests each)
        Person pp = em.find(Person.class, 1);
        if (pp == null) {
            pp = new Person();
            pp.setFirstName("JUNIT Test");
            pp.setLastName("Tester");
            pp.setPhone("1122334455");
            pp.setAddress("123 Test St.");
            pp.setBirth(logic.convertStringToDate("1990-02-21 18:30:00"));

            em.getTransaction().begin();
            em.persist(pp);
            em.getTransaction().commit();
        }

        // create new bloodbank entity
        BloodBank entity = new BloodBank();

        Date date = new Date();
        String formattedDate = (FORMATTER.format(date));
        entity.setEstablished(logic.convertStringToDate(formattedDate));

        entity.setName("Junit 5 Test");
        entity.setPrivatelyOwned(true);
        entity.setEmplyeeCount(12345);
        // set person dependency (if applicable)
        entity.setOwner(pp);

        em.getTransaction().begin();
        //add a bloodbank to hibernate, bloodbank is now managed.
        //we use merge instead of add so we can get the updated generated ID.
        expectedEntity = em.merge(entity);
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();
    }

    @AfterEach
    public void tearDown() {
        if (expectedEntity != null) {
            logic.delete(expectedEntity);
        }
        // deletes leftover Person entities in person table
        for(Person person: pLogic.getPersonWithFirstName("JUNIT Test")) {
            pLogic.delete(person);
        }
    }

    /**
     * helper method for testing all bloodbank fields
     *
     * @param expected
     * @param actual
     */
    private void assertAccountEquals(BloodBank expected, BloodBank actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmplyeeCount(), actual.getEmplyeeCount());
        assertEquals(expected.getPrivatelyOwned(), actual.getPrivatelyOwned());
        assertEquals(expected.getEstablished(), actual.getEstablished());
    }

    @Test
    final void testGetAll() {
        //get all the bloodbanks from the DB
        List<BloodBank> list = logic.getAll();
        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();

        //make sure bloodbank was created successfully
        assertNotNull(expectedEntity);
        //delete the new bloodbank
        logic.delete(expectedEntity);

        //get all bloodbanks again
        list = logic.getAll();
        //the new size of bloodbanks must be one less
        assertEquals(originalSize - 1, list.size());
    }

    @Test
    final void testGetWithId() {
        // using the id of test bloodbank get another bloodbank from logic
        BloodBank returnedBloodBank = logic.getWithId(expectedEntity.getId());

        //the two bloodbanks (testBloodBank and returnedBloodBank) must be the same
        assertAccountEquals(expectedEntity, returnedBloodBank);
    }

    @Test
    final void getBloodBankWithName() {
        BloodBank returnedBloodBank = logic.getBloodBankWithName(expectedEntity.getName());

        //the two bloodbanks (testBloodBank and returnedBloodBank) must be the same
        assertAccountEquals(expectedEntity, returnedBloodBank);
    }

    @Test
    final void getBloodBankWithPrivatelyOwned() {
        int foundFull = 0;
        List<BloodBank> returnedBloodBank = logic.getBloodBankWithPrivatelyOwned(expectedEntity.getPrivatelyOwned());
        for (BloodBank bloodbank : returnedBloodBank) {
            //all bloodbanks must have the same privatelyOwned boolean 
            assertEquals(expectedEntity.getPrivatelyOwned(), bloodbank.getPrivatelyOwned());
            if (bloodbank.getId().equals(expectedEntity.getId())) {
                //the two bloodbanks (testBloodBank and returnedBloodBank) must be the same
                assertAccountEquals(expectedEntity, bloodbank);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void getBloodBankWithEstablished() {
        int foundFull = 0;
        List<BloodBank> returnedBloodBank = logic.getBloodBankWithEstablished(expectedEntity.getEstablished());
        for (BloodBank bloodbank : returnedBloodBank) {
            //all bloodbanks must have the same established date
            assertEquals(expectedEntity.getEstablished(), bloodbank.getEstablished());
            if (bloodbank.getId().equals(expectedEntity.getId())) {
                //the two bloodbanks (testBloodBank and returnedBloodBank) must be the same
                assertAccountEquals(expectedEntity, bloodbank);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void getBloodBankWithOwner() {
        // using the id of test bloodbank get another bloodbnak from logic
        BloodBank returnedBloodBank = logic.getBloodBanksWithOwner(expectedEntity.getOwner().getId());

        //the two bloodbanks (testBloodBank and returnedBloodBank) must be the same
        assertAccountEquals(expectedEntity, returnedBloodBank);
    }

    @Test
    final void getBloodBankWithEmplyeeCount() {
        int foundFull = 0;
        List<BloodBank> returnedBloodBank = logic.getBloodBanksWithEmplyeeCount(expectedEntity.getEmplyeeCount());
        for (BloodBank bloodbank : returnedBloodBank) {
            //all bloodbanks must have the same employee count
            assertEquals(expectedEntity.getEmplyeeCount(), bloodbank.getEmplyeeCount());
            if (bloodbank.getId().equals(expectedEntity.getId())) {
                //the two bloodbanks (testBloodBank and returnedBloodBank) must be the same
                assertAccountEquals(expectedEntity, bloodbank);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(BloodBankLogic.NAME, new String[]{"testCreateBloodBank"});
        sampleMap.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{Boolean.toString(true)});
        sampleMap.put(BloodBankLogic.ESTABLISHED, new String[]{"1234-12-24 12:34:56"});
        sampleMap.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{"12345"});

        BloodBank returnedBloodBank = logic.createEntity(sampleMap);
        logic.add(returnedBloodBank);

        returnedBloodBank = logic.getBloodBankWithName(returnedBloodBank.getName());

        assertEquals(sampleMap.get(BloodBankLogic.NAME)[0], returnedBloodBank.getName());
        assertEquals(sampleMap.get(BloodBankLogic.PRIVATELY_OWNED)[0], Boolean.toString(returnedBloodBank.getPrivatelyOwned()));
        assertEquals(sampleMap.get(BloodBankLogic.ESTABLISHED)[0], logic.convertDateToString(returnedBloodBank.getEstablished()));
        assertEquals(sampleMap.get(BloodBankLogic.EMPLOYEE_COUNT)[0], Integer.toString(returnedBloodBank.getEmplyeeCount()));

        logic.delete(returnedBloodBank);
    }

    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(BloodBankLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
        sampleMap.put(BloodBankLogic.NAME, new String[]{expectedEntity.getName()});
        sampleMap.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{Boolean.toString(expectedEntity.getPrivatelyOwned())});
        sampleMap.put(BloodBankLogic.ESTABLISHED, new String[]{logic.convertDateToString(expectedEntity.getEstablished())});
        sampleMap.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{Integer.toString(expectedEntity.getEmplyeeCount())});

        BloodBank returnedBloodBank = logic.createEntity(sampleMap);

        assertAccountEquals(expectedEntity, returnedBloodBank);
    }

    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(BloodBankLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(BloodBankLogic.NAME, new String[]{expectedEntity.getName()});
            map.put(BloodBankLogic.PRIVATELY_OWNED, new String[]{Boolean.toString(expectedEntity.getPrivatelyOwned())});
            map.put(BloodBankLogic.ESTABLISHED, new String[]{logic.convertDateToString(expectedEntity.getEstablished())});
            map.put(BloodBankLogic.EMPLOYEE_COUNT, new String[]{Integer.toString(expectedEntity.getEmplyeeCount())});
        };

        //idealy every test should be in its own method
        // tests Id for nulls and blanks
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        // tests name for nulls and blanks
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.NAME, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.NAME, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        // tests Privately owned for nulls and blanks
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.PRIVATELY_OWNED, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.PRIVATELY_OWNED, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        // tests established for wrong format. should default to current time.
        // test may fail, because dates may be created and cause a difference of seconds.
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.ESTABLISHED, new String[]{"1/2/31/1/4 11;1:1;:"});
        assertEquals(FORMATTER.format( new Date() ), FORMATTER.format( logic.createEntity(sampleMap).getEstablished() ) );

        //tests employee count for nulls and blanks
        fillMap.accept(sampleMap);
        sampleMap.replace(BloodBankLogic.EMPLOYEE_COUNT, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(BloodBankLogic.EMPLOYEE_COUNT, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
    }

    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("ID", "Owner", "Name", "Privately Owned", "Established", "Employee Count"), list);
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(BloodBankLogic.ID, BloodBankLogic.OWNER_ID, BloodBankLogic.NAME,
                BloodBankLogic.PRIVATELY_OWNED, BloodBankLogic.ESTABLISHED, BloodBankLogic.EMPLOYEE_COUNT), list);
    }

    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList(expectedEntity);
        assertEquals(expectedEntity.getId(), list.get(0));
        //TODO: implement person test here
        //assertEquals( expectedEntity.getOwner(), list.get( 1 ) );
        assertEquals(expectedEntity.getName(), list.get(2));
        assertEquals(expectedEntity.getPrivatelyOwned(), list.get(3));
        assertEquals(expectedEntity.getEstablished(), list.get(4));
        assertEquals(expectedEntity.getEmplyeeCount(), list.get(5));
    }
}
