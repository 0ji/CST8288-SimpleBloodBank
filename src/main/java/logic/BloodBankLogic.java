package logic;

import common.ValidationException;
import dal.BloodBankDAL;
import entity.BloodBank;
import entity.Person;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author koji
 */
public class BloodBankLogic extends GenericLogic<BloodBank, BloodBankDAL>{

    /**
     * create static final variables with proper name of each column. this way you will never manually type it again,
     * instead always refer to these variables.
     *
     * by using the same name as column id and HTML element names we can make our code simpler. this is not recommended
     * for proper production project.
     */
    public static final String OWNER_ID = "owner_id";
    public static final String PRIVATELY_OWNED = "privately_owned";
    public static final String ESTABLISHED = "established";
    public static final String NAME = "name";
    public static final String EMPLOYEE_COUNT = "employee_count";
    public static final String ID = "id";
    
    public BloodBankLogic() {
        super( new BloodBankDAL() );
    }

    @Override
    public List<BloodBank> getAll() {
        return get( () -> dal().findAll() );
    }
    
    @Override
    public BloodBank getWithId(int id) {
        return get( () -> dal().findById(id) );
    }
    
    public BloodBank getBloodBankWithName(String name) {
        return get( () -> dal().findByName(name) );
    }
    
    public List<BloodBank> getBloodBankWithPrivatelyOwned(boolean privatelyOwned) {
        return get( () -> dal().findByPrivatelyOwned(privatelyOwned) );
    }
    
    public List<BloodBank> getBloodBankWithEstablished(Date established) {
        return get( () -> dal().findByEstablished(established) );
    }
    
    public BloodBank getBloodBanksWithOwner(int ownerId) {
        return get( () -> dal().findByOwner(ownerId) );
    }
    
    public List<BloodBank> getBloodBanksWithEmplyeeCount(int count) {
        return get( () -> dal().findByEmployeeCount(count) );
    }
    
    @Override
    public BloodBank createEntity(Map<String, String[]> parameterMap) {
        //do not create any logic classes in this method.

        //return new AccountBuilder().SetData( parameterMap ).build();
        Objects.requireNonNull( parameterMap, "parameterMap cannot be null" );
        
        //create new Entity object
        BloodBank entity = new BloodBank();
        
        //ID is generated, so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
        //the only time that we will have id is for update behaviour.
        if (parameterMap.containsKey(ID)) {
            try {
                entity.setId(Integer.parseInt(parameterMap.get(ID)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }        
        
        //before using the values in the map, make sure to do error checking.
        //simple lambda to validate a string, this can also be place in another
        //method to be shared amoung all logic classes.
        ObjIntConsumer< String> validator = ( value, length ) -> {
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
        
        //extract the date from map first.
        //everything in the parameterMap is string so it must first be
        //converted to appropriate type. have in mind that values are
        //stored in an array of String; almost always the value is at
        //index zero unless you have used duplicated key/name somewhere.
        
        //TODO: validate other types from parameterMap?
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date established = formatter.parse(parameterMap.get(ESTABLISHED)[0]);
            entity.setEstablished(established);
        } catch (ParseException ex) {
            Logger.getLogger(BloodBankLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int owner_id = Integer.parseInt(parameterMap.get(OWNER_ID)[0]);
        String name = parameterMap.get(NAME)[0];
        Boolean privatelyOwned = Boolean.valueOf(parameterMap.get(PRIVATELY_OWNED)[0]);
        int employee_count = Integer.parseInt(parameterMap.get(EMPLOYEE_COUNT)[0]);
        // validate types other than String?
        
        entity.setOwner(new Person(owner_id)); // TODO: find owner? create? correct?
        entity.setName(name);
        entity.setPrivatelyOwned(privatelyOwned);
        entity.setEmplyeeCount(employee_count);
        return entity;
    }
    
    /**
     * this method is used to send a list of all names to be used form table column headers. by having all names in one
     * location there is less chance of mistakes.
     *
     * this list must be in the same order as getColumnCodes and extractDataAsList
     *
     * @return list of all column names to be displayed.
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Owner", "Name", "Privately Owned", "Established", "Employee Count");
    }

    /**
     * this method returns a list of column names that match the official column names in the db. by having all names in
     * one location there is less chance of mistakes.
     *
     * this list must be in the same order as getColumnNames and extractDataAsList
     *
     * @return list of all column names in DB.
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, OWNER_ID, NAME, PRIVATELY_OWNED, ESTABLISHED, EMPLOYEE_COUNT);
    }

    /**
     * return the list of values of all columns (variables) in given entity.
     *
     * this list must be in the same order as getColumnNames and getColumnCodes
     *
     * @param e - given Entity to extract data from.
     *
     * @return list of extracted values
     */
    @Override
    public List<?> extractDataAsList(BloodBank e) {
        return Arrays.asList( e.getId(), e.getOwner(), e.getName(), e.getPrivatelyOwned(), e.getEstablished(), e.getEmplyeeCount());
    }

    
}
