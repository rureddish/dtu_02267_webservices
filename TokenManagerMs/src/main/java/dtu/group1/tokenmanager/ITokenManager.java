package dtu.group1.tokenmanager;


import dtu.group1.common.models.AccountID;
import dtu.group1.common.models.Token;

import java.util.List;

public interface ITokenManager {
    List<Token> createTokens(int amount, AccountID cid);

    AccountID useToken(Token token);

    int getTokenCount(AccountID accountID);

    boolean isTokenValid(Token token);
}
