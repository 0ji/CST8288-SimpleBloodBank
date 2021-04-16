package logic;

import common.EMFactory;
import common.ValidationException;
import dal.DonationRecordDAL;
import entity.BloodBank;
import entity.BloodDonation;
import entity.DonationRecord;
import entity.Person;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import javax.persistence.EntityManager;
import static logic.BloodDonationLogic.BANK_ID;
import static logic.BloodDonationLogic.CREATED;

/**
 *
 * @author ugsli
 */
public class DonationRecordLogic extends GenericLogic<DonationRecord, DonationRecordDAL>{
    
    
    public static final String PERSON_ID = "person_id";
    public static final String DONATION_ID = "donation_id";
    public static final String TESTED = "tested";
    public static final String ADMINISTRATOR = "administrator";
    public static final String HOSPITAL = "hospital";
    public static final String CREATED = "created";
    public static final String ID = "id";
    
    DonationRecordLogic() {
        super( new DonationRecordDAL() );
    }     

    @Override
    public List<DonationRecord> getAll() {
        return get(  () -> dal().findAll() );
    }

    @Override
    public DonationRecord getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    public List<DonationRecord> getDonationRecordWithTested(boolean tested) {
        return get(() -> dal().findByTested(tested));
    }

    public List<DonationRecord> getDonationRecordWithAdministrator(String administrator) {
        return get(() -> dal().findByAdministrator(administrator));
    }

    public List<DonationRecord> getDonationRecordWithHospital(String username) {
        return get(() -> dal().findByHospital(username));
    }

    public List<DonationRecord> getDonationRecordWithCreated(Date created) {
        return get(() -> dal().findByCreated(created));
    }

    public List<DonationRecord> getDonationRecordWithPerson(int personId) {
        return get(() -> dal().findByPerson(personId));
    }

    public List<DonationRecord> getDonationRecordWithDonation(int donationId) {
        return get(() -> dal().findByDonation(donationId));
    }

    @Override
    public DonationRecord createEntity(Map<String, String[]> parameterMap) {
        //do not create any logic classes in this method.

//        return new AccountBuilder().SetData( parameterMap ).build();
        Objects.requireNonNull( parameterMap, "parameterMap cannot be null" );
        //same as if condition below
//        if (parameterMap == null) {
//            throw new NullPointerException("parameterMap cannot be null");
//        }

        //create a new Entity object
        DonationRecord donationRecordEntity = new DonationRecord();  //NEVER CREATE ANY OTHER TYPE OF ENTTIY. ONLY ACCOUNT ENTITY HERE
        //ID is generated, so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
         //the only time that we will have id is for update behaviour.
        if( parameterMap.containsKey( ID ) ){
            try {
                donationRecordEntity.setId( Integer.parseInt( parameterMap.get( ID )[ 0 ] ) );
            } catch( java.lang.NumberFormatException ex ) {
                throw new ValidationException( ex );
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
        if (parameterMap.containsKey(PERSON_ID)) {
            try{    
              
                EntityManager entityManager = EMFactory.getEMF().createEntityManager();
                Integer personID = Integer.parseInt(parameterMap.get(BANK_ID)[0]);
                Person personEntity = entityManager.find(Person.class, personID);
                donationRecordEntity.setPerson(personEntity);
             
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }
        
        if (parameterMap.containsKey(DONATION_ID)) {
            try{    
              
                EntityManager entityManager = EMFactory.getEMF().createEntityManager();
                Integer bloodDonationID = Integer.parseInt(parameterMap.get(DONATION_ID)[0]);
                BloodDonation bloodDonationEntity = entityManager.find(BloodDonation.class, bloodDonationID);
                donationRecordEntity.setBloodDonation(bloodDonationEntity);
             
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }
        Boolean tested;
         if( parameterMap.containsKey( TESTED ) ){
            try {
                 tested = Boolean.parseBoolean(parameterMap.get( TESTED )[ 0 ]);
                 donationRecordEntity.setTested(tested );
            } catch (IllegalArgumentException e){
                throw new ValidationException(e);
            }
           
        }
        
        String administrator = null;
        if( parameterMap.containsKey( ADMINISTRATOR ) ){
            try {
                 administrator = parameterMap.get( ADMINISTRATOR )[ 0 ];
                 validator.accept( administrator, 100 );
                 donationRecordEntity.setAdministrator( administrator );
            } catch (Exception e){
                throw new ValidationException(e);
            }
           
        }
        String hospital = null;
        if( parameterMap.containsKey( HOSPITAL ) ){
            try {
                 administrator = parameterMap.get( HOSPITAL )[ 0 ];
                 validator.accept( hospital, 100 );
                 donationRecordEntity.setHospital(hospital );
            } catch (Exception e){
                throw new ValidationException(e);
            }
           
        }
        if (parameterMap.containsKey(CREATED)) {
            try {
                Date date = convertStringToDate(parameterMap.get(CREATED)[0]);
                donationRecordEntity.setCreated(date);
            } catch (Exception e) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                Date currentDate = new Date(System.currentTimeMillis());
                String formattedDate = (formatter.format(currentDate));
                Date convertedFormattedDate = convertStringToDate(formattedDate);
                donationRecordEntity.setCreated(convertedFormattedDate);
                //throw new ValidationException(e);
            }
        }else{
          donationRecordEntity.setCreated(Calendar.getInstance().getTime());
       }
                       
        return donationRecordEntity;
    }
    
    /**
     * 
        
     */
    
    /**
     * method lists the Donation Record table column names in 
     * @return a list of column names in Donation Record table. 
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Person_id", "Donation_id", "Tested", "Administrator", "Hospital", "Created");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, PERSON_ID, DONATION_ID, TESTED, ADMINISTRATOR, HOSPITAL, CREATED);
    }

    @Override
    public List<?> extractDataAsList(DonationRecord e) {
        return Arrays.asList(e.getId(), e.getPerson(), e.getBloodDonation(), e.getTested(), e.getAdministrator(), e.getHospital(), e.getCreated());
    }    
}
    