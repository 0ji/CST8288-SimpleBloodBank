package logic;

import common.EMFactory;
import common.TomcatStartUp;
import entity.BloodBank;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author koji
 */
public class BloodBankLogicTest {
    
    private BloodBankLogic logic;
    private BloodBank expectedEntity;
    
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }
    
    @BeforeEach
    public void setUp() {
        
        logic = LogicFactory.getFor("BloodBank");
        /* **********************************
         * ***********IMPORTANT**************
         * **********************************/
        //we only do this for the test.
        //always create Entity using logic.
        //we manually make the bloodbank to not rely on any logic functionality , just for testing

        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();
        //start a Transaction
        em.getTransaction().begin();

        BloodBank entity = new BloodBank();
        entity.setName( "Junit 5 Test" );
        entity.setEmplyeeCount(99999);
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();  
        entity.setEstablished(date);

        entity.setId(1);
        entity.setPrivatelyOwned(true);
        // entity.setOwner(owner);
        
        //add a bloodbank to hibernate, bloodbank is now managed.
        //we use merge instead of add so we can get the updated generated ID.
        expectedEntity = em.merge( entity );
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();
    }
    
    @AfterEach
    public void tearDown() {
        if( expectedEntity != null ){
            logic.delete( expectedEntity );
        }
    }

    @Test
    final void testGetAll() {
        //get all the bloodbanks from the DB
        List<BloodBank> list = logic.getAll();
        //store the size of list, this way we know how many accounts exits in DB
        int originalSize = list.size();

        //make sure bloodbank was created successfully
        assertNotNull( expectedEntity );
        //delete the new bloodbank
        logic.delete( expectedEntity );

        //get all bloodbanks again
        list = logic.getAll();
        //the new size of bloodbanks must be one less
        assertEquals( originalSize - 1, list.size() );
    }
    
    /**
     * helper method for testing all bloodbank fields
     *
     * @param expected
     * @param actual
     */
    private void assertAccountEquals( BloodBank expected, BloodBank actual ) {
        //assert all field to guarantee they are the same
        assertEquals( expected.getId(), actual.getId() );
        assertEquals( expected.getName(), actual.getName() );
        assertEquals( expected.getEmplyeeCount(), actual.getEmplyeeCount() );
        assertEquals( expected.getPrivatelyOwned(), actual.getPrivatelyOwned() );
        assertEquals( expected.getEstablished(), actual.getEstablished() );
    }
    
}
