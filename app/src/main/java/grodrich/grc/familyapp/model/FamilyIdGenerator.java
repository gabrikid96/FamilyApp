package grodrich.grc.familyapp.model;

import java.util.UUID;

/**
 * Created by gabri on 3/09/16.
 */
public class FamilyIdGenerator {

    public static String getUnicId(){
        String initialUUID = UUID.randomUUID().toString();
        initialUUID = initialUUID.replace("-","").toUpperCase();
        String familyId = initialUUID.substring(initialUUID.length()-6,initialUUID.length());
        return familyId;
    }
}
