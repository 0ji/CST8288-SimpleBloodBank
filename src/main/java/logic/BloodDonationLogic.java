package logic;

import common.EMFactory;
import common.ValidationException;
import dal.BloodDonationDAL;
import entity.Account;
import entity.BloodBank;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjIntConsumer;
import javax.persistence.EntityManager;

/**
 *
 * @author camyk
 */
public class BloodDonationLogic extends GenericLogic<BloodDonation, BloodDonationDAL> {

    public static final String BANK_ID = "bank_id";
    public static final String MILLILITERS = "milliliters";
    public static final String BLOOD_GROUP = "blood_group";
    public static final String RHESUS_FACTOR = "rhesus_factor";
    public static final String CREATED = "created";
    public static final String ID = "id";

    BloodDonationLogic() {
        super(new BloodDonationDAL());
    }

    @Override
    public List<BloodDonation> getAll() {
        return get(() -> dal().findAll());
    }

    @Override
    public BloodDonation getWithId(int id) {
        return get(() -> dal().findById(id));
    }

    public List<BloodDonation> getBloodDonationWithMilliliters(int milliliters) {
        return get(() -> dal().findByMilliliters(milliliters));
    }

    public List<BloodDonation> getBloodDonationWithBloodGroup(BloodGroup bloodGroup) {
        return get(() -> dal().findByBloodGroup(bloodGroup));
    }

    public List<BloodDonation> getBloodDonationWithCreated(Date created) {
        return get(() -> dal().findByCreated(created));
    }

    public List<BloodDonation> getBloodDonationsWithRhd(RhesusFactor rhd) {
        return get(() -> dal().findByRhd(rhd));
    }

    public List<BloodDonation> getBloodDonationsWithBloodBank(int bankId) {
        return get(() -> dal().findByBloodBank(bankId));
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("id", "bank_id", "milliliters", "blood_group", "rhesus_factor", "created");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, BANK_ID, MILLILITERS, BLOOD_GROUP, RHESUS_FACTOR, CREATED);
    }

    @Override
    public List<?> extractDataAsList(BloodDonation e) {
        return Arrays.asList(e.getId(), e.getBloodBank(), e.getMilliliters(), e.getBloodGroup(), e.getRhd(), e.getCreated());
    }

    @Override
    public BloodDonation createEntity(Map<String, String[]> parameterMap) {
        //camy-takes in parameterMap (keys associated to values.  String=key, string[] is map
        //all the keys were the public statis final variables (NICKNAME, PASSWORD, ETC)

        Objects.requireNonNull(parameterMap, "parameterMap cannot be null");

        //create a new Entity object
        BloodDonation bloodDonation = new BloodDonation();  //NEVER CREATE ANY OTHER TYPE OF ENTTIY. ONLY ACCOUNT ENTITY HERE

        if( parameterMap.containsKey( ID ) ){
            try {
                bloodDonation.setId( Integer.parseInt( parameterMap.get( ID )[ 0 ] ) );
            } catch( java.lang.NumberFormatException ex ) {
                throw new ValidationException( ex );
            }
        }
        
        if (parameterMap.containsKey(MILLILITERS)) {
            String error = "";
            if(Integer.parseInt(parameterMap.get(MILLILITERS)[0]) > 0 ){
            try {
                bloodDonation.setMilliliters(Integer.parseInt(parameterMap.get(MILLILITERS)[0]));
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }else{
                error="value cannot be less than zero";
                throw new IllegalArgumentException();
            }
        }

        if (parameterMap.containsKey(BANK_ID)) {
            try{    
              
                EntityManager em = EMFactory.getEMF().createEntityManager();
                Integer bbInt = Integer.parseInt(parameterMap.get(BANK_ID)[0]);
                BloodBank bb = em.find(BloodBank.class, bbInt);
                bloodDonation.setBloodBank(bb);
             
            } catch (java.lang.NumberFormatException ex) {
                throw new ValidationException(ex);
            }
        }
        
         /**
          * only really applies for testing as there is no Created key yet for new entries
          */
       if (parameterMap.containsKey(CREATED)) {
            try {
                Date date = convertStringToDate(parameterMap.get(CREATED)[0]);
                bloodDonation.setCreated(date);
            } catch (Exception e) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                Date currentDate = new Date(System.currentTimeMillis());
                String formattedDate = (formatter.format(currentDate));
                Date convertedFormattedDate = convertStringToDate(formattedDate);
                bloodDonation.setCreated(convertedFormattedDate);
                //throw new ValidationException(e);
            }
        }else{
           /**
         * this will generate a timestamp based on the data and time of data submission
         */   
           bloodDonation.setCreated(Calendar.getInstance().getTime());
       }

        if (parameterMap.containsKey(BLOOD_GROUP)) {
            try {
                BloodGroup bloodtype = BloodGroup.valueOf(parameterMap.get(BLOOD_GROUP)[0]);
                bloodDonation.setBloodGroup(bloodtype);

            } catch (Exception e) {
                throw new ValidationException(e);
            }
        }

        if (parameterMap.containsKey(RHESUS_FACTOR)) {
            RhesusFactor rhf = RhesusFactor.valueOf(parameterMap.get(RHESUS_FACTOR)[0]);
            bloodDonation.setRhd(rhf);
        }
        
        return bloodDonation;
    }
    
}
