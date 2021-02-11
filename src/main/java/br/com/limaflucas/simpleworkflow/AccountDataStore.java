package br.com.limaflucas.simpleworkflow;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Repository
public class AccountDataStore implements GetAccountPort {

    private final AccountRepository accountRepository;

    @Override
    public Optional<AccountEntity> get(UUID accountID) {
        
        return this.accountRepository.findById(accountID);

    }
}
