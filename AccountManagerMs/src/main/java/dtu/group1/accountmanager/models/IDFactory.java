package dtu.group1.accountmanager.models;

import dtu.group1.common.models.AccountID;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.EqualsAndHashCode;

@Getter
@EqualsAndHashCode
public class IDFactory {
    private static Set<AccountID> usedIDs = ConcurrentHashMap.newKeySet();

    public static AccountID generateUniqueID() {
        AccountID id;
        do {
            id = new AccountID(UUID.randomUUID().toString());
        } while (!usedIDs.add(id));
        return id;
    }
}
