package logic;

import common.EMFactory;
import common.TomcatStartUp;
import entity.Person;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author Jamie
 */
@Disabled
@TestMethodOrder(OrderAnnotation.class)
public class PersonTest {
    private PersonLogic logic;
    private Person expectedEntity;
    
    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat( "/SimpleBloodBank", "common.ServletListener", "simplebloodbank-PU-test" );
    }
    
    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }
    
    @BeforeEach
    final void setUp() throws Exception {

        logic = LogicFactory.getFor( "Person" );
        /* **********************************
         * ***********IMPORTANT**************
         * **********************************/
        //we only do this for the test.
        //always create Entity using logic.
        //we manually make the person to not rely on any logic functionality , just for testing

        //get an instance of EntityManager
        EntityManager em = EMFactory.getEMF().createEntityManager();

        //start a Transaction
        em.getTransaction().begin();
        
        Person entity = new Person();
        entity.setFirstName( "Jamie" );
        entity.setLastName( "Verner" );
        entity.setPhone( "111-1111" );
        entity.setAddress( "1385 Woodroffe Avenue" );
        entity.setBirth(logic.convertStringToDate("2005-11-25 18:30:00"));
        //entity.setBloodBank()

        //add a person to hibernate, person is now managed.
        //we use merge instead of add so we can get the updated generated ID.
        expectedEntity = em.merge( entity );
        //commit the changes
        em.getTransaction().commit();
        //close EntityManager
        em.close();
    }
    
    @AfterEach
    final void tearDown() throws Exception {
        if( expectedEntity != null ){
            logic.delete( expectedEntity );
        }
    }
    
    @Test
    final void testGetAll() {
        //get all people from the DB
        List<Person> list = logic.getAll();
        //store the size of list, this way we know how many people exist in DB
        int originalSize = list.size();

        //make sure a person was created successfully
        assertNotNull( expectedEntity );
        //delete the new person
        logic.delete( expectedEntity );

        //get all people again
        list = logic.getAll();
        //the new size of people must be one less
        assertEquals( originalSize - 1, list.size() );
    }
    
    /**
     * helper method for testing all people fields
     *
     * @param expected
     * @param actual
     */
    private void assertPersonEquals( Person expected, Person actual ) {
        //assert all field to guarantee they are the same
        assertEquals( expected.getId(), actual.getId() );
        assertEquals( expected.getFirstName(), actual.getFirstName() );
        assertEquals( expected.getLastName(), actual.getLastName() );
        assertEquals( expected.getPhone(), actual.getPhone() );
        assertEquals( expected.getAddress(), actual.getAddress() );
        assertEquals( expected.getBirth(), actual.getBirth() );
    }
    
    @Test
    final void testGetWithId() {
        //using the id of test person get another person from logic
        Person person = logic.getWithId( expectedEntity.getId() );

        //the two peoples (expectedEntity and person) must be the same
        assertPersonEquals( expectedEntity, person );
    }
    
    @Test
    final void testGetWithFirstName() {
        List<Person> people = logic.getPersonWithFirstName(expectedEntity.getFirstName());
        people.forEach(person -> {
            assertEquals(expectedEntity.getFirstName(), person.getFirstName());
        });
    }
    
    @Test
    final void testGetWithLastName() {
        List<Person> people = logic.getPersonWithLastName(expectedEntity.getLastName());
        people.forEach(person -> {
            assertEquals(expectedEntity.getLastName(), person.getLastName());
        });
    }
    
    @Test
    final void testGetWithPhoneNumber() {
        List<Person> people = logic.getPersonWithPhone(expectedEntity.getPhone());
        people.forEach(person -> {
            assertEquals(expectedEntity.getPhone(), person.getPhone());
        });
    }
    
    @Test
    final void testGetWithAddress() {
        List<Person> people = logic.getPersonsWithAddress(expectedEntity.getAddress());
        people.forEach(person -> {
            assertEquals(expectedEntity.getAddress(), person.getAddress());
        });
    }
    
    @Test
    final void testGetWithBirthDate() {
        List<Person> people = logic.getPersonsWithBirth(expectedEntity.getBirth());
        people.forEach(person -> {
            assertEquals(expectedEntity.getBirth(), person.getBirth());
        });
    }
    
    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( PersonLogic.FIRST_NAME, new String[]{ "Jamie" } );
        sampleMap.put( PersonLogic.LAST_NAME, new String[]{ "Verner" } );
        sampleMap.put( PersonLogic.PHONE, new String[]{ "111-1111" } );
        sampleMap.put( PersonLogic.ADDRESS, new String[]{ "1385 Woodroffe Avenue" } );
        sampleMap.put( PersonLogic.BIRTH, new String[]{ "2005-11-25 18:30:00" } );
        
        Person returnedEntity = logic.createEntity(sampleMap);
        logic.add(returnedEntity);
        
        List<Person> people = logic.getPersonWithFirstName(returnedEntity.getFirstName());
        people.forEach(person -> {
            assertEquals( sampleMap.get( PersonLogic.FIRST_NAME )[ 0 ], person.getFirstName() );
            assertEquals( sampleMap.get( PersonLogic.LAST_NAME )[ 0 ], person.getLastName() );
            assertEquals( sampleMap.get( PersonLogic.PHONE )[ 0 ], person.getPhone() );
            assertEquals( sampleMap.get( PersonLogic.ADDRESS )[ 0 ], person.getAddress() );
            assertEquals( sampleMap.get( PersonLogic.BIRTH )[ 0 ], logic.convertDateToString(person.getBirth()));
            
            logic.delete( person );
        });
    }
    
    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( PersonLogic.ID, new String[]{ Integer.toString( expectedEntity.getId() ) } );
        sampleMap.put( PersonLogic.FIRST_NAME, new String[]{ expectedEntity.getFirstName() } );
        sampleMap.put( PersonLogic.LAST_NAME, new String[]{ expectedEntity.getLastName() } );
        sampleMap.put( PersonLogic.PHONE, new String[]{ expectedEntity.getPhone() } );
        sampleMap.put( PersonLogic.ADDRESS, new String[]{ expectedEntity.getAddress() } );
        sampleMap.put( PersonLogic.BIRTH, new String[]{ logic.convertDateToString(expectedEntity.getBirth()) } );

        Person person = logic.createEntity( sampleMap );

        assertPersonEquals( expectedEntity, person );
    }
    
    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = ( Map<String, String[]> map ) -> {
            map.clear();
            map.put( PersonLogic.ID, new String[]{ Integer.toString( expectedEntity.getId() ) } );
            map.put( PersonLogic.FIRST_NAME, new String[]{ expectedEntity.getFirstName() } );
            map.put( PersonLogic.LAST_NAME, new String[]{ expectedEntity.getLastName() } );
            map.put( PersonLogic.PHONE, new String[]{ expectedEntity.getPhone() } );
            map.put( PersonLogic.ADDRESS, new String[]{ expectedEntity.getAddress() } );
            map.put( PersonLogic.BIRTH, new String[]{ logic.convertDateToString(expectedEntity.getBirth()) } );
        };

        //idealy every test should be in its own method
        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.ID, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.ID, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.FIRST_NAME, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.FIRST_NAME, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.LAST_NAME, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.LAST_NAME, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.PHONE, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.PHONE, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );

        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.ADDRESS, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.ADDRESS, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
        
        fillMap.accept( sampleMap );
        sampleMap.replace( PersonLogic.BIRTH, null );
        assertThrows( NullPointerException.class, () -> logic.createEntity( sampleMap ) );
        sampleMap.replace( PersonLogic.BIRTH, new String[]{} );
        assertThrows( IndexOutOfBoundsException.class, () -> logic.createEntity( sampleMap ) );
    }
    
    @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = ( int length ) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints( 'a', 'z' + 1 ).limit( length )
                    .collect( StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append )
                    .toString();
        };

        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put( PersonLogic.ID, new String[]{ Integer.toString( 1 ) } );
        sampleMap.put( PersonLogic.FIRST_NAME, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.LAST_NAME, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.PHONE, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.ADDRESS, new String[]{ generateString.apply( 1 ) } );
        sampleMap.put( PersonLogic.BIRTH, new String[]{ "2005-11-25 18:30:00" } );

        //idealy every test should be in its own method
        Person person = logic.createEntity( sampleMap );
        assertEquals( Integer.parseInt( sampleMap.get( PersonLogic.ID )[ 0 ] ), person.getId() );
        assertEquals( sampleMap.get( PersonLogic.FIRST_NAME )[ 0 ], person.getFirstName() );
        assertEquals( sampleMap.get( PersonLogic.LAST_NAME )[ 0 ], person.getLastName() );
        assertEquals( sampleMap.get( PersonLogic.PHONE )[ 0 ], person.getPhone() );
        assertEquals( sampleMap.get( PersonLogic.ADDRESS )[ 0 ], person.getAddress() );
        assertEquals( sampleMap.get( PersonLogic.BIRTH )[ 0 ], logic.convertDateToString(person.getBirth()));

        sampleMap = new HashMap<>();
        sampleMap.put( PersonLogic.ID, new String[]{ Integer.toString( 1 ) } );
        sampleMap.put( PersonLogic.FIRST_NAME, new String[]{ generateString.apply( 50 ) } );
        sampleMap.put( PersonLogic.LAST_NAME, new String[]{ generateString.apply( 50 ) } );
        sampleMap.put( PersonLogic.PHONE, new String[]{ generateString.apply( 15 ) } );
        sampleMap.put( PersonLogic.ADDRESS, new String[]{ generateString.apply( 100 ) } );
        sampleMap.put( PersonLogic.BIRTH, new String[]{ "2005-11-25 18:30:00" } );

        //idealy every test should be in its own method
        person = logic.createEntity( sampleMap );
        assertEquals( Integer.parseInt( sampleMap.get( PersonLogic.ID )[ 0 ] ), person.getId() );
        assertEquals( sampleMap.get( PersonLogic.FIRST_NAME )[ 0 ], person.getFirstName() );
        assertEquals( sampleMap.get( PersonLogic.LAST_NAME )[ 0 ], person.getLastName() );
        assertEquals( sampleMap.get( PersonLogic.PHONE )[ 0 ], person.getPhone() );
        assertEquals( sampleMap.get( PersonLogic.ADDRESS )[ 0 ], person.getAddress() );
        assertEquals( sampleMap.get( PersonLogic.BIRTH )[ 0 ], logic.convertDateToString(person.getBirth()));
    }
    
    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals( Arrays.asList( "ID", "FirstName", "LastName", "Phone", "Address", "BirthDate" ), list );
    }
    
    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals( Arrays.asList( PersonLogic.ID, PersonLogic.FIRST_NAME, PersonLogic.LAST_NAME, 
                PersonLogic.PHONE, PersonLogic.ADDRESS, PersonLogic.BIRTH ), list );
    }
    
    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList( expectedEntity );
        assertEquals( expectedEntity.getId(), list.get( 0 ) );
        assertEquals( expectedEntity.getFirstName(), list.get( 1 ) );
        assertEquals( expectedEntity.getLastName(), list.get( 2 ) );
        assertEquals( expectedEntity.getPhone(), list.get( 3 ) );
        assertEquals( expectedEntity.getAddress(), list.get( 4 ) );
        assertEquals( expectedEntity.getBirth(), list.get( 5 ) );
    }
    
    
}
