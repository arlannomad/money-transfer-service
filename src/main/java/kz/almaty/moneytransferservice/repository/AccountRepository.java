package kz.almaty.moneytransferservice.repository;

import kz.almaty.moneytransferservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Boolean existsByAccountNumber(String accountNumber);
    Account findByAccountNumber(String accountNumber);
}
