package com.example.atmdemoappforoasis.serviceImplementation;

import com.example.atmdemoappforoasis.dto.TransactionDto;
import com.example.atmdemoappforoasis.enums.TransType;
import com.example.atmdemoappforoasis.exception.UserNotVerifiedException;
import com.example.atmdemoappforoasis.models.Account;
import com.example.atmdemoappforoasis.models.Transactions;
import com.example.atmdemoappforoasis.models.Users;
import com.example.atmdemoappforoasis.repository.AccountRepository;
import com.example.atmdemoappforoasis.repository.TransactionRepository;
import com.example.atmdemoappforoasis.repository.UserRepository;
import com.example.atmdemoappforoasis.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TransactionServiceImplementation implements TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository tansactionRepository;
    private  final AccountRepository accountRepository;
    private final EmailServiceImpl emailServiceImpl;
@Autowired
    public TransactionServiceImplementation(UserRepository userRepository, TransactionRepository tansactionRepository, AccountRepository accountRepository, EmailServiceImpl emailServiceImpl) {
        this.userRepository = userRepository;
        this.tansactionRepository = tansactionRepository;
        this.accountRepository = accountRepository;
        this.emailServiceImpl = emailServiceImpl;
}


    @Override
    public String doTransaction(TransactionDto transactionDto) {
       try {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           if (transactionDto.getTransactionType() != null && transactionDto != null) {
               String username = authentication.getName();
               Users users = (Users) userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
               Transactions transactions = new Transactions();
               transactions.setTransactionType(transactionDto.getTransactionType());
               transactions.setOwner(users);
               transactions.setAccountId(users.getAccount());
               transactions.setAmount(transactionDto.getAmount());
               transactions.setDescription(transactionDto.getDescription());
               transactions.setRecipientAccNo(transactionDto.getRecipientAccountNo());
               if (transactionDto.getTransactionType() == TransType.DEPOSIT) {
                   users.getAccount().setBalance(users.getAccount().getBalance().add(transactionDto.getAmount()));
                  Account account= accountRepository.save(users.getAccount());

                   Transactions transactions1 = tansactionRepository.save(transactions);
                   return "Deposit successful " + " " + " Your account balance is now  " + " " + account.getBalance();
               }
               if (transactionDto.getTransactionType() == TransType.WITHDRAW) {
                   if (users.getAccount().getBalance().compareTo(transactionDto.getAmount()) > 0) {
                       users.getAccount().setBalance(users.getAccount().getBalance().subtract(transactionDto.getAmount()));
                       accountRepository.save(users.getAccount());
                       Transactions transactions1 = tansactionRepository.save(transactions);
                       return "You have successfully withdrawn" + " " + transactionDto.getAmount() +"  "+ "your balance is now"+" "+ users.getAccount().getBalance();

                   } else {
                       return "You don't have enough balance";
                   }
               }
               if (transactionDto.getTransactionType() == TransType.TRANSFER) {
                   Account account = accountRepository.findAccountByAccountNo(transactionDto.getRecipientAccountNo()).orElseThrow(() -> new UsernameNotFoundException("this user is does not have an account with us, double check the accountNo"));
                   account.setBalance(account.getBalance().add(transactionDto.getAmount()));
                   users.getAccount().setBalance(users.getAccount().getBalance().subtract(transactionDto.getAmount()));
                   accountRepository.save(account);
                   tansactionRepository.save(transactions);
                   emailServiceImpl.sendMail(account.getUser().getEmail(),"CREDIT FROM  "+users.getAccount().getAccountName(), "YOU HAVE JUST BEEN CREDITED  "+ transactionDto.getAmount()+" "+" Naira only " + " "+ " your balance is now  "+account.getBalance());
                   return "Transfer Successful"+" "+ "YOUR BALANCE IS NOW "+" "+ users.getAccount().getBalance()+" "+"Naira only ";
               }
           }
           return "please state your transaction details";
       }catch(Exception ex){
           ex.printStackTrace();
           return "transaction encountered error";
       }
    }

    @Override

    public Page<TransactionDto> getTransaction(LocalDate date1, LocalDate date2, Pageable pageable) {
       try {
if(date1==null|| date2==null){
    throw new UserNotVerifiedException("dates must be present");
}

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Users users = (Users) userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            Page<Transactions> transactionDetails = tansactionRepository.findByAccountIdAndTransDatBetween(users.getAccount(), date1, date2, pageable);
            if(transactionDetails==null){
                throw new UserNotVerifiedException("Transaction not found, try to adjust your search criteria");
            }
            List<TransactionDto> transactionDtos = convertListTransactionToDto(transactionDetails.getContent());
       return new PageImpl<>(transactionDtos,pageable,transactionDetails.getTotalElements());
       } catch (Exception ex){
           ex.printStackTrace();
           return new PageImpl<>(Collections.emptyList());
       }
    }
        public List<TransactionDto> convertListTransactionToDto (List < Transactions > transactionsList) {
          // try{
            List<TransactionDto> transactionDtoList = new ArrayList<>();
            for (Transactions transactions : transactionsList) {
                TransactionDto transactionDto = new TransactionDto();
                transactionDto.setTransactionType(transactions.getTransactionType());
                transactionDto.setDescription(transactions.getDescription());
                transactionDto.setTransDat(transactions.getTransDat());
                transactionDto.setAccountNo(transactions.getOwner().getAccount().getAccountNo());
                transactionDto.setAmount(transactions.getAmount());
                transactionDto.setTransTime(transactions.getTransTime());
                transactionDto.setRecipientAccountNo(transactions.getRecipientAccNo());
                transactionDtoList.add(transactionDto);
            }

            return transactionDtoList;
        }

        public Page<Transactions>  findall(Pageable pageable){
     return  tansactionRepository.findAll(pageable);
        }



}
