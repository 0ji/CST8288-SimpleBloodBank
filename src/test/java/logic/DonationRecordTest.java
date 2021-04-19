package logic;

import common.EMFactory;
import common.TomcatStartUp;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.DonationRecord;
import entity.Person;
import entity.RhesusFactor;
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
 * @author ugsli
 */
class DonationRecordTest {
    
    private DonationRecordLogic logic;
    private DonationRecord expectedEntity;
        
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

        logic = LogicFactory.getFor("DonationRecord");
//        /* **********************************
//         * ***********IMPORTANT**************
//         * **********************************/
//        //we only do this for the test.
//        //always create Entity using logic.
//        //we manually make the account to not rely on any logic functionality , just for testing
//
//        //get an instance of EntityManager
        EntityManager bankEM = EMFactory.getEMF().createEntityManager();
        EntityManager donationEM = EMFactory.getEMF().createEntityManager();
        EntityManager personEM = EMFactory.getEMF().createEntityManager();
        //start a Transaction
//        //check if the depdendecy exists on DB already
        //em.find takes two arguments, the class type of return result and the primery key.
        Person person = personEM.find(Person.class, 1);
        BloodDonation bloodDonation = donationEM.find(BloodDonation.class, 1);
        //if result is null create the entity and persist it
        if (bloodDonation == null) {

            //Bloodbank is a subdependency of bloodDonation, so must create a placeholder/test entity.
            BloodBank bb = bankEM.find(BloodBank.class, 1);
//        //if result is null create the entity and persist it
            if (bb == null) {
//            //cearet object
             bb = new BloodBank();
             bb.setName("DRTest");
             bb.setPrivatelyOwned(true);
             bb.setEstablished(logic.convertStringToDate("1111-11-11 11:11:11"));
             bb.setEmplyeeCount(111);
//            //persist the dependency first
                bankEM.getTransaction().begin();
                bankEM.persist(bb);
                bankEM.getTransaction().commit();
              }
            
            bloodDonation = new BloodDonation();
            bloodDonation.setBloodBank(bb);
            bloodDonation.setBloodGroup(BloodGroup.AB);
            bloodDonation.setCreated(logic.convertStringToDate("1111-11-11 11:11:11"));
            bloodDonation.setMilliliters(111);
            bloodDonation.setRhd(RhesusFactor.Negative);
//            //persist the dependency first
            donationEM.getTransaction().begin();
            donationEM.persist(bloodDonation);
//        }
            donationEM.getTransaction().commit();
        }
        
        //if result is null create the entity and persist it
        if (person == null) {
            person = new Person();
            person.setFirstName("Testing");
            person.setLastName("Tester");
            person.setPhone("123-456-7890");
            person.setAddress("123 Test St.");
            person.setBirth(logic.convertStringToDate("1990-02-21 18:30:00") );
            
            personEM.getTransaction().begin();
            personEM.persist(person);
            personEM.getTransaction().commit();
        }
//
//        //create the desired entity
        EntityManager recordEM = EMFactory.getEMF().createEntityManager();
                
        DonationRecord entity = new DonationRecord();
        entity.setAdministrator("Admin");
        entity.setHospital("Test Hospital");
        entity.setTested(true);
        //entity.setCreated(logic.convertStringToDate("1111-11-11 11:11:11"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Date currentDate = new Date(System.currentTimeMillis());
        String formattedDate = (formatter.format(currentDate));
        entity.setCreated(logic.convertStringToDate(formattedDate));
        //add dependency to the desired entity
        entity.setPerson(person);
        entity.setBloodDonation(bloodDonation);
//
        recordEM.getTransaction().begin();
//        //add desired entity to hibernate, entity is now managed.
//        //we use merge instead of add so we can get the managed entity.
        expectedEntity = recordEM.merge(entity);
//        //commit the changes
        recordEM.getTransaction().commit();
//        //close EntityManager
        recordEM.close();
    }
      
    @AfterEach
    final void tearDown() throws Exception {
        if (expectedEntity != null) {
            logic.delete(expectedEntity);
        }
    }
    
    /**
     * Test of getId method, of class DonationRecord.
     */
    @Test
    final void testGetAll() {

        List<DonationRecord> list = logic.getAll();
        int originalSize = list.size();
        assertNotNull(expectedEntity);
        logic.delete(expectedEntity);
        list = logic.getAll();
        assertEquals(originalSize - 1, list.size());
    }
    
    private void assertDREquals(DonationRecord expected, DonationRecord actual) {
        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAdministrator(), actual.getAdministrator());
        assertEquals(expected.getHospital(), actual.getHospital());
        assertEquals(expected.getTested(), actual.getTested());
        assertEquals(expected.getPerson(), actual.getPerson());
        assertEquals(expected.getBloodDonation(), actual.getBloodDonation());
        assertEquals(expected.getCreated(), actual.getCreated());

    }

    @Test
    final void testGetWithId() {

        DonationRecord returnedDonationRecord = logic.getWithId(expectedEntity.getId());
        assertDREquals(expectedEntity, returnedDonationRecord);
    }

    /**
     * Test of getTested method, of class DonationRecord.
     */
    @Test
    final void testGetDonationRecordWithTested() {
        List<DonationRecord> returnedRecord = logic.getDonationRecordWithTested(expectedEntity.getTested());
        returnedRecord.forEach(dr -> {
            assertEquals(expectedEntity.getTested(), dr.getTested() );
        });
    }


    /**
     * Test of setTested method, of class DonationRecord.
     */
    @Test
    final void getDonationRecordWithAdministrator() {
        List<DonationRecord> returnedRecord = logic.getDonationRecordWithAdministrator(expectedEntity.getAdministrator());
        //the two accounts (testAcounts and returnedAccounts) must be the same
        assertEquals( expectedEntity, returnedRecord );
    }

    /**
     * Test of getAdministrator method, of class DonationRecord.
     */
    @Test
    final void testGetDonationRecordWithHospital () {
        List<DonationRecord> returnedRecord = logic.getDonationRecordWithHospital(expectedEntity.getHospital());
        //the two accounts (testAcounts and returnedAccounts) must be the same
        assertEquals( expectedEntity, returnedRecord );
    }

    /**
     * Test of setAdministrator method, of class DonationRecord.
     */
    @Test
    final void getDonationRecordWithCreated() {
        List<DonationRecord> returnedRecords = logic.getDonationRecordWithCreated(expectedEntity.getCreated());
        returnedRecords.forEach(dr -> {
            assertEquals(expectedEntity.getCreated(), dr.getCreated());
        });
    }

    /**
     * Test of getHospital method, of class DonationRecord.
     */
    @Test
    final void getDonationRecordWithPerson() {
        Person person = (expectedEntity.getPerson());
        List<DonationRecord> returnedRecord = logic.getDonationRecordWithPerson(person.getId());

        returnedRecord.forEach(dr -> {
            assertEquals(expectedEntity.getPerson().getId(), dr.getPerson().getId());
        });
    }

    /**
     * Test of setHospital method, of class DonationRecord.
     */
    @Test
    final void getDonationRecordWithDonation() {
        BloodDonation bd = (expectedEntity.getBloodDonation());
        List<DonationRecord> returnedRecord = logic.getDonationRecordWithDonation(bd.getId());

        returnedRecord.forEach(dr -> {
            assertEquals(expectedEntity.getBloodDonation().getId(), dr.getBloodDonation().getId());
        });
    }

    
    @Test
    final void testCreateEntity() {
        Map<String, String[]> testMap = new HashMap<>();
        testMap.put(DonationRecordLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
        testMap.put(DonationRecordLogic.PERSON_ID, new String[]{Integer.toString(expectedEntity.getPerson().getId())});
        testMap.put(DonationRecordLogic.DONATION_ID, new String[]{Integer.toString(expectedEntity.getBloodDonation().getId())});
        testMap.put(DonationRecordLogic.ADMINISTRATOR, new String[]{(expectedEntity.getAdministrator())});
        testMap.put(DonationRecordLogic.HOSPITAL, new String[]{(expectedEntity.getHospital())});
        testMap.put(DonationRecordLogic.CREATED, new String[]{expectedEntity.getCreated().toString()});
        testMap.put(DonationRecordLogic.TESTED, new String[]{Boolean.toString(expectedEntity.getTested())});

        DonationRecord returnedRecord = logic.createEntity(testMap);

        assertDREquals(expectedEntity, returnedRecord);

    }
    
    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put(DonationRecordLogic.ID, new String[]{Integer.toString(expectedEntity.getId())});
            map.put(DonationRecordLogic.PERSON_ID, new String[]{Integer.toString(expectedEntity.getPerson().getId())});
            map.put(DonationRecordLogic.DONATION_ID, new String[]{Integer.toString(expectedEntity.getBloodDonation().getId())});
            map.put(DonationRecordLogic.ADMINISTRATOR, new String[]{(expectedEntity.getAdministrator())});
            map.put(DonationRecordLogic.HOSPITAL, new String[]{(expectedEntity.getHospital())});
            map.put(DonationRecordLogic.CREATED, new String[]{expectedEntity.getCreated().toString()});
            map.put(DonationRecordLogic.TESTED, new String[]{Boolean.toString(expectedEntity.getTested())});
        };
        
        //idealy every test should be in its own method
        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.ID, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( DonationRecordLogic.ID, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.PERSON_ID, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( DonationRecordLogic.PERSON_ID, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.DONATION_ID, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( DonationRecordLogic.DONATION_ID, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.ADMINISTRATOR, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( DonationRecordLogic.ADMINISTRATOR, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.HOSPITAL, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( DonationRecordLogic.HOSPITAL, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.CREATED, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( DonationRecordLogic.CREATED, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( DonationRecordLogic.TESTED, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( DonationRecordLogic.TESTED, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
    }
    
    
    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals( Arrays.asList( "ID", "Owner", "Name", "Privately Owned", "Established", "Employee Count" ), list );
    }
    
    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals( Arrays.asList(DonationRecordLogic.ID, DonationRecordLogic.PERSON_ID, DonationRecordLogic.DONATION_ID, DonationRecordLogic.TESTED, 
                DonationRecordLogic.ADMINISTRATOR, DonationRecordLogic.HOSPITAL, DonationRecordLogic.CREATED), list );
    }
    
    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList( expectedEntity );
        assertEquals( expectedEntity.getId(), list.get( 0 ) );
        assertEquals( expectedEntity.getPerson(), list.get( 2 ) );
        assertEquals( expectedEntity.getBloodDonation(), list.get( 3 ) );
        assertEquals( expectedEntity.getTested(), list.get( 4 ) );
        assertEquals( expectedEntity.getAdministrator(), list.get( 5 ) );
        assertEquals( expectedEntity.getHospital(), list.get( 6 ) );
        assertEquals( expectedEntity.getCreated(), list.get( 7 ) );
    }
}