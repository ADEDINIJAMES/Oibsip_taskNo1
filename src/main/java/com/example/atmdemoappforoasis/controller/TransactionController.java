package com.example.atmdemoappforoasis.controller;

import com.example.atmdemoappforoasis.dto.TransactionDto;
import com.example.atmdemoappforoasis.dto.TransactionResponseDto;
import com.example.atmdemoappforoasis.models.Transactions;
import com.example.atmdemoappforoasis.serviceImplementation.TransactionServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/v1/transact")
public class TransactionController {
    private final TransactionServiceImplementation transactionServiceImplementation;

@Autowired
    public TransactionController(TransactionServiceImplementation transactionServiceImplementation) {
        this.transactionServiceImplementation = transactionServiceImplementation;
    }
    @PostMapping("/do-transact")
    public ResponseEntity<String>transact (@RequestBody TransactionDto transactionDto){
    String response = transactionServiceImplementation.doTransaction(transactionDto);
    return ResponseEntity.ok(response);
    }

    @GetMapping("/transaction-details")
    public ResponseEntity<Page<TransactionDto>> getTransactions (
            @RequestParam (name = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam (name = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
//            @RequestParam (name = "specific", required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate specificDate,
            @RequestParam(name = "page", required = false , defaultValue = "0") int page,
            @RequestParam (name ="size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false ,defaultValue = "transDat,asc") String sortParam
    ){
    Logger logger = LoggerFactory.getLogger(getClass());
    try {


//Sort sort = Sort.by(sortParams);
        String [] sortParams= sortParam.split(",");
        Sort sort = Sort.by(sortParams[0]);
        if(sortParams.length ==2){
            sort = sortParams[1].equalsIgnoreCase("asc")? sort.ascending():sort.descending();
        }
        Pageable pageable = PageRequest.of(page,size,sort);
        Page<TransactionDto> transactionResponseDtosPge = transactionServiceImplementation.getTransaction(start, end,pageable);
return ResponseEntity.ok(transactionResponseDtosPge);
    }catch (Exception ex) {
        ex.printStackTrace();
logger.error("An error occurred in processing the request", ex);
   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    }
    @GetMapping("/all")
    public ResponseEntity<Page<Transactions>> getAll (Pageable pageable){
    return ResponseEntity.ok(transactionServiceImplementation.findall(pageable));
    }


}
