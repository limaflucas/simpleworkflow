package br.com.limaflucas.simpleworkflow;

import java.util.Optional;
import java.util.UUID;

public interface GetAccountPort {
    
    Optional<AccountEntity> get(UUID accountID);
}
