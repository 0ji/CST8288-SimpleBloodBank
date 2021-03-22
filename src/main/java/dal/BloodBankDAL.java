package dal;

import entity.BloodBank;
import java.util.List;

/**
 *
 * @author koji
 */
public class BloodBankDAL extends GenericDAL<BloodBank>{

    public BloodBankDAL() {
        super( BloodBank.class );
    }
    
    @Override
    public List<BloodBank> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BloodBank findById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
