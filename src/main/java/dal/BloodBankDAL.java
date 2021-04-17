package dal;

import entity.BloodBank;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return findResults("BloodBank.findAll", null);
    }

    @Override
    public BloodBank findById(int bankId) {
        Map<String, Object> map = new HashMap<>();
        map.put( "bankId", bankId);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id" and value for it is put in map
        return findResult("BloodBank.findByBankId", map);
    }
    
    public BloodBank findByName(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return findResult("BloodBank.findByName", map);
    }
    
    public List<BloodBank> findByPrivatelyOwned(boolean privatelyOwned) {
        Map<String, Object> map = new HashMap<>();
        map.put("privatelyOwned", privatelyOwned);
        return findResults("BloodBank.findByPrivatelyOwned", map);
    }
    
    public List<BloodBank> findByEstablished(Date established) {
        Map<String, Object> map = new HashMap<>();
        map.put("established", established);
        return findResults("BloodBank.findByEstablished", map);
    }
    
    public List<BloodBank> findByEmployeeCount(int emplyeeCount) {
        Map<String, Object> map = new HashMap<>();
        map.put("emplyeeCount", emplyeeCount);
        return findResults("BloodBank.findByEmplyeeCount", map);
    }
    
    public BloodBank findByOwner(int ownerId) {
        Map<String, Object> map = new HashMap<>();
        map.put("ownerId", ownerId);
        return findResult("BloodBank.findByOwner", map);
    }
    
    public List<BloodBank> findContaining(String search) {
        Map<String, Object> map = new HashMap<>();
        map.put("search", search);
        return findResults("BloodBank.findContaining", map);
    }
}
