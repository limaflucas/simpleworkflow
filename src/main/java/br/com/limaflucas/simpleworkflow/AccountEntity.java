package br.com.limaflucas.simpleworkflow;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class AccountEntity {

    @Id private UUID accountID;
    private String name;
    private OffsetDateTime insertedAt;
}
