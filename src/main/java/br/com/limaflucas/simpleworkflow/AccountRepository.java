package br.com.limaflucas.simpleworkflow;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountEntity, UUID> {

}
