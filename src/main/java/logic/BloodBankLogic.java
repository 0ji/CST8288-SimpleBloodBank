package logic;

import dal.BloodBankDAL;
import entity.BloodBank;
import java.util.List;
import java.util.Map;

/**
 *
 * @author koji
 */
public class BloodBankLogic extends GenericLogic<BloodBank, BloodBankDAL>{

    public BloodBankLogic() {
        super( new BloodBankDAL() );
    }

    @Override
    public List<String> getColumnNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getColumnCodes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<?> extractDataAsList(BloodBank e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BloodBank createEntity(Map<String, String[]> parameterMap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BloodBank> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BloodBank getWithId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
