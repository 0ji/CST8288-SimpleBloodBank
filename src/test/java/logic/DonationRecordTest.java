package logic;

import entity.BloodDonation;
import entity.DonationRecord;
import entity.Person;
import java.util.Date;
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
@Disabled
public class DonationRecordTest {
    
    public DonationRecordTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    /**
     * Test of getId method, of class DonationRecord.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        DonationRecord instance = new DonationRecord();
        Integer expResult = null;
        Integer result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setId method, of class DonationRecord.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        Integer recordId = null;
        DonationRecord instance = new DonationRecord();
        instance.setId(recordId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTested method, of class DonationRecord.
     */
    @Test
    public void testGetTested() {
        System.out.println("getTested");
        DonationRecord instance = new DonationRecord();
        boolean expResult = false;
        boolean result = instance.getTested();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTested method, of class DonationRecord.
     */
    @Test
    public void testSetTested() {
        System.out.println("setTested");
        boolean tested = false;
        DonationRecord instance = new DonationRecord();
        instance.setTested(tested);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdministrator method, of class DonationRecord.
     */
    @Test
    public void testGetAdministrator() {
        System.out.println("getAdministrator");
        DonationRecord instance = new DonationRecord();
        String expResult = "";
        String result = instance.getAdministrator();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAdministrator method, of class DonationRecord.
     */
    @Test
    public void testSetAdministrator() {
        System.out.println("setAdministrator");
        String administrator = "";
        DonationRecord instance = new DonationRecord();
        instance.setAdministrator(administrator);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHospital method, of class DonationRecord.
     */
    @Test
    public void testGetHospital() {
        System.out.println("getHospital");
        DonationRecord instance = new DonationRecord();
        String expResult = "";
        String result = instance.getHospital();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHospital method, of class DonationRecord.
     */
    @Test
    public void testSetHospital() {
        System.out.println("setHospital");
        String hospital = "";
        DonationRecord instance = new DonationRecord();
        instance.setHospital(hospital);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCreated method, of class DonationRecord.
     */
    @Test
    public void testGetCreated() {
        System.out.println("getCreated");
        DonationRecord instance = new DonationRecord();
        Date expResult = null;
        Date result = instance.getCreated();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCreated method, of class DonationRecord.
     */
    @Test
    public void testSetCreated() {
        System.out.println("setCreated");
        Date created = null;
        DonationRecord instance = new DonationRecord();
        instance.setCreated(created);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBloodDonation method, of class DonationRecord.
     */
    @Test
    public void testGetBloodDonation() {
        System.out.println("getBloodDonation");
        DonationRecord instance = new DonationRecord();
        BloodDonation expResult = null;
        BloodDonation result = instance.getBloodDonation();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBloodDonation method, of class DonationRecord.
     */
    @Test
    public void testSetBloodDonation() {
        System.out.println("setBloodDonation");
        BloodDonation bloodDonation = null;
        DonationRecord instance = new DonationRecord();
        instance.setBloodDonation(bloodDonation);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPerson method, of class DonationRecord.
     */
    @Test
    public void testGetPerson() {
        System.out.println("getPerson");
        DonationRecord instance = new DonationRecord();
        Person expResult = null;
        Person result = instance.getPerson();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPerson method, of class DonationRecord.
     */
    @Test
    public void testSetPerson() {
        System.out.println("setPerson");
        Person person = null;
        DonationRecord instance = new DonationRecord();
        instance.setPerson(person);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class DonationRecord.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        DonationRecord instance = new DonationRecord();
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class DonationRecord.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object object = null;
        DonationRecord instance = new DonationRecord();
        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class DonationRecord.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        DonationRecord instance = new DonationRecord();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
