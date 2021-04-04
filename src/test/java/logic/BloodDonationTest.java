package logic;

import common.EMFactory;
import common.TomcatStartUp;
import common.ValidationException;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.util.Arrays;
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
            entity.setCreated(logic.convertStringToDate("1111-11-11 11:11:11"));
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
//

    @Order(1)
    @Test
    final void testGetAll() {
//        //get all the accounts from the DB
        List<BloodDonation> list = logic.getAll();
//        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();
//
//        //make sure account was created successfully
        assertNotNull(expectedEntity);
//        //delete the new account
        logic.delete(expectedEntity);
//
//        //get all accounts again
        list = logic.getAll();
//        //the new size of accounts must be one less
        assertEquals(originalSize - 1, list.size());
    }

    @Order(2)
    @Test
    final void testGetWithId() {
        //using the id of test account get another account from logic
        BloodDonation returnedBloodDonation = logic.getWithId(expectedEntity.getId());

        //the two accounts (testAcounts and returnedAccounts) must be the same
        assertAccountEquals(expectedEntity, returnedBloodDonation);
    }

    
    @Test
    final void testGetWithBloodBank() { //requires id of bloodbank accept blookd bankd or accept id 

        
          BloodBank bb= (expectedEntity.getBloodBank()); 
          List<BloodDonation> returnedResults = logic.getBloodDonationsWithBloodBank(bb.getId());
         
          for (BloodDonation bloodD : returnedResults) {
            //all accounts must have the same password
            assertEquals(expectedEntity.getBloodBank().getId(),bloodD.getBloodBank().getId());
          
    }
    }

    @Order(3)
    @Test
    final void testGetWithMillileters() { //PASSES

        //int bb= (int)(expectedEntity.getBloodBank());
        List<BloodDonation> returnedResults = logic.getBloodDonationWithMilliliters(expectedEntity.getMilliliters());
        for (BloodDonation account : returnedResults) {
            //all accounts must have the same password
            assertEquals(expectedEntity.getMilliliters(), account.getMilliliters());

        }

    }

    @Order(4)
    @Test
    final void testGetWithRhd() {
        List<BloodDonation> returnedResults = logic.getBloodDonationsWithRhd(expectedEntity.getRhd());
        for (BloodDonation account : returnedResults) {
            //all accounts must have the same password
            assertEquals(expectedEntity.getRhd(), account.getRhd());
        }
    }

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
