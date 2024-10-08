package ht.domain;

import ht.AccountApplication;
import ht.domain.AccountCreated;
import ht.domain.AccountUpdated;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Account_table")
@Data
//<<< DDD / Aggregate Root
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String accountId;

    private Integer balance;

    private String customerId;

    private String accountId;

    private Integer balance;

    private String customerId;

    @PostPersist
    public void onPostPersist() {
        AccountCreated accountCreated = new AccountCreated(this);
        accountCreated.publishAfterCommit();

        AccountUpdated accountUpdated = new AccountUpdated(this);
        accountUpdated.publishAfterCommit();
    }

    public static AccountRepository repository() {
        AccountRepository accountRepository = AccountApplication.applicationContext.getBean(
            AccountRepository.class
        );
        return accountRepository;
    }

    //<<< Clean Arch / Port Method
    public static void updateAccount(PaymentApproved paymentApproved) {
        //implement business logic here:

        /** Example 1:  new item 
        Account account = new Account();
        repository().save(account);

        AccountUpdated accountUpdated = new AccountUpdated(account);
        accountUpdated.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(paymentApproved.get???()).ifPresent(account->{
            
            account // do something
            repository().save(account);

            AccountUpdated accountUpdated = new AccountUpdated(account);
            accountUpdated.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
