package dtu.group1.tokenmanager;

import dtu.group1.common.models.AccountID;
import dtu.group1.common.models.Token;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Singleton
public class TokenManager implements ITokenManager {
    private Map<Token, AccountID> tokens = new HashMap<>();
    private Map<AccountID, Integer> userTokens = new HashMap<>();

    @Override
    public List<Token> createTokens(int amount, AccountID cid) {
        List<Token> list = new ArrayList<>();
        int userTokensCount = userTokens.getOrDefault(cid, 0);

        if (userTokensCount > 1) {
//            throw new TooManyTokensForTokenRequestException(cid);
            return new ArrayList<>();
        }
        if (userTokensCount + amount > 6) {
//            throw new InvalidTokenAmountRequestException(amount);
            return new ArrayList<>();
        }

        userTokens.put(cid, userTokensCount + amount);

        for (int i = 0; i < amount; i++) {
            list.add(generateToken(cid));
        }
        return list;
    }

    @Override
    public AccountID useToken(Token token) {
        var id = tokens.get(token);
        tokens.remove(token);
        return id;
    }

    public int getTokenCount(AccountID accountID) {
        return userTokens.get(accountID);
    }

    private Token generateToken(AccountID id) {
        Token t;
        do {
            t = new Token(UUID.randomUUID().toString());
        } while (tokens.putIfAbsent(t, id) != null);
        return t;
    }

    public boolean isTokenValid(Token token) {
        System.out.printf("Token %s is valid: %s", token, tokens.containsKey(token));
        return tokens.containsKey(token);
    }
}
