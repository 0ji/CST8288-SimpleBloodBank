package logic;

import common.ValidationException;
import dal.PersonDAL;
import entity.Person;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jamie
 */
public class PersonLogic extends GenericLogic<Person, PersonDAL> {
    
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String BIRTH = "birth";
    public static final String ID = "id";
    
    PersonLogic() {
        super(new PersonDAL());
    }
    
    @Override
    public List<Person> getAll() {
        return get( () -> dal().findAll());
    }
    
    @Override
    public Person getWithId(int id) {
        return get( () -> dal().findById(id));
    }
    
    public List<Person> getPersonWithPhone(String phone) {
        return get( () -> dal().findByPhone(phone));
    }
    
    public List<Person> getPersonWithFirstName(String firstName) {
        return get( () -> dal().findByFirstName(firstName));
    }
    
    public List<Person> getPersonWithLastName(String lastName) {
        return get( () -> dal().findByLastName(lastName));
    }
    
    public List<Person> getPersonsWithAddress(String address) {
        return get( () -> dal().findByAddress(address));
    }
    
    public List<Person> getPersonsWithBirth(Date birth) {
        return get( () -> dal().findByBirth(birth));
    }
    
    @Override
    public Person createEntity(Map<String, String[]> parameterMap) {
        
        Objects.requireNonNull( parameterMap, "parameterMap cannot be null" );
        
        Person entity = new Person();
        
        if(parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID) [0] ) );
            } catch(java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }
        
        ObjIntConsumer<String> validator = (value, length) -> {
            if( value == null || value.trim().isEmpty() || value.length() > length ){
                String error = "";
                if( value == null || value.trim().isEmpty() ){
                    error = "value cannot be null or empty: " + value;
                }
                if( value.length() > length ){
                    error = "string length is " + value.length() + " > " + length;
                }
                throw new ValidationException( error );
            }
        };
        
        if (parameterMap.containsKey(BIRTH)) {
            try {
                Date date = convertStringToDate(parameterMap.get(BIRTH)[0]);
                entity.setBirth(date);
            } catch (Exception e) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                Date currentDate = new Date(System.currentTimeMillis());
                String formattedDate = (formatter.format(currentDate));
                Date convertedFormattedDate = convertStringToDate(formattedDate);
                entity.setBirth(convertedFormattedDate);
                //throw new ValidationException(e);
            }
        }else{
          entity.setBirth(Calendar.getInstance().getTime());
       }
        
        String firstName = parameterMap.get(FIRST_NAME)[0];
        String lastName = parameterMap.get(LAST_NAME)[0];
        String phone = parameterMap.get(PHONE)[0];
        String address = parameterMap.get(ADDRESS)[0];
        
               
        validator.accept(firstName, 50);
        validator.accept(lastName, 50);
        validator.accept(phone, 15);
        validator.accept(address, 100);
        
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setPhone(phone);
        entity.setAddress(address);
        
        return entity;
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "FirstName", "LastName", "Phone", "Address", "BirthDate");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, FIRST_NAME, LAST_NAME, PHONE, ADDRESS, BIRTH);
    }

    @Override
    public List<?> extractDataAsList(Person e) {
        return Arrays.asList(e.getId(), e.getFirstName(), e.getLastName(), e.getPhone(), e.getAddress(), e.getBirth());
    }
    
}
