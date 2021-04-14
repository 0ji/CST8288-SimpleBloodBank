package dal;

import entity.Account;
import entity.BloodDonation;
import entity.BloodGroup;
import entity.RhesusFactor;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author camyk
 */
public class BloodDonationDAL extends GenericDAL<BloodDonation> {

    public BloodDonationDAL() {
        super(BloodDonation.class);
    }

    @Override
    public List<BloodDonation> findAll() {
        return findResults("BloodDonation.findAll", null);
    }

    @Override
    public BloodDonation findById(int donationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("donationId", donationId);

        return findResult("BloodDonation.findByDonationId", map);
    }

    public List<BloodDonation> findByMilliliters(int milliliters) {
        Map<String, Object> milliMap = new HashMap<>();
        milliMap.put("milliliters", milliliters);

        return findResults("BloodDonation.findByMilliliters", milliMap);
    }

    public List<BloodDonation> findByBloodGroup(BloodGroup bloodGroup) {
        Map<String, Object> bloodGMap = new HashMap<>();
        bloodGMap.put("bloodGroup", bloodGroup);

        return findResults("BloodDonation.findByBloodGroup", bloodGMap);
    }

    public List<BloodDonation> findByRhd(RhesusFactor rhd) {
        Map<String, Object> rhdMap = new HashMap<>();
        rhdMap.put("rhd", rhd);

        return findResults("BloodDonation.findByRhd", rhdMap);
    }

    public List<BloodDonation> findByCreated(Date created) {
        Map<String, Object> createdMap = new HashMap<>();
        createdMap.put("created", created);

        return findResults("BloodDonation.findByCreated", createdMap);
    }

    public List<BloodDonation> findByBloodBank(int bloodBankId) {
        Map<String, Object> bloodBankMap = new HashMap<>();
        bloodBankMap.put("bloodBankId", bloodBankId);

        return findResults("BloodDonation.findByBloodBank", bloodBankMap);
    }

}
