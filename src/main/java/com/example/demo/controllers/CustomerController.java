package com.example.demo.controllers;

import com.africastalking.sms.Recipient;
import com.example.demo.model.*;
import com.example.demo.model.models.*;
import com.example.demo.persistence.entities.Users;
import com.example.demo.services.*;
import com.example.demo.services.bps.ScoreService;
import com.example.demo.services.communication.AfricasTalkingApiService;
import com.infobip.ApiException;
import com.infobip.model.SmsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    public  final SubscriptionService subscriptions;
    public final CustomerService customerService;
    public  final userService userService;
    public final LoanService loanService;
    public final PaymentService paymentService;
    public final LoanAccountService loanAccountService;
    public final ReportService reportService;
    public final ScoreService scoreService;
    public final AfricasTalkingApiService sms;
    public final CommunicationService communicationService;

    public CustomerController(SubscriptionService subscriptions, CustomerService customerService, com.example.demo.services.userService userService, LoanService loanService, PaymentService paymentService, LoanAccountService loanAccountService, ReportService reportService, ScoreService scoreService, AfricasTalkingApiService sms, CommunicationService communicationService) {
        this.subscriptions = subscriptions;
        this.customerService = customerService;
        this.userService = userService;
        this.loanService = loanService;
        this.paymentService = paymentService;
        this.loanAccountService = loanAccountService;
        this.reportService = reportService;
        this.scoreService = scoreService;
        this.sms = sms;
        this.communicationService = communicationService;
    }


    //creating customers
    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        Customer customer1=customerService.saveCustomer(customer);
        return new ResponseEntity<>(customer1, HttpStatus.CREATED);
    }
    //finding customers info
    @GetMapping("/findall")
    public ResponseEntity<List<Customer>> findAll(){
          List<Customer> customers=customerService.findAll();
          return new ResponseEntity<>(customers,HttpStatus.OK);
    }
    //find all suspense payments
    @GetMapping("/findAllSuspense")
    public ResponseEntity<List<SuspensePayments>> findAllSuspense(String cusPhone){
        List<SuspensePayments> suspensePayments=loanAccountService.findAllOverPayment(cusPhone).get();
        return new ResponseEntity<>(suspensePayments,HttpStatus.OK);
    }
    @GetMapping ("/findCus{id}")
    public ResponseEntity<ClientInfo> findIndividual(@PathVariable Long id){
       ClientInfo customer=customerService.findById(id);
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }
    //This method is too expensive to be changed
    @GetMapping ("/findCusByUsername{name}")
    public ResponseEntity<ClientInfo> findIndividualByName(@PathVariable String name){
        Users user=userService.findByName(name).get();
        Customer customerS=customerService.findByPhone(user.getPhone()).get();
        ClientInfo customer=customerService.findById(customerS.getId());
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }
    //updating customers info
    @PutMapping("/update")
    public ResponseEntity<Customer> updateIndividual(@RequestBody Customer customer){
        Customer customer1=customerService.update(customer);
        return new ResponseEntity<>(customer1,HttpStatus.CREATED);
    }
    //deactivating customer
    @PutMapping("/changeStatus")
    public ResponseEntity<Customer> changeStatus(Long id,Boolean status){
        customerService.changeStatus(id,status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/createSubscription")
    public ResponseEntity<subscriptionR> subscribe(@RequestBody subscriptionR req){
        subscriptions.subscribe(req.getPhone(),req.getProductId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/findSubscription{id}")
    public ResponseEntity<Optional<List<Subscriptions>>> findSubscription(@PathVariable String id){
        Optional<List<Subscriptions>> subscription=subscriptions.findCustomerId(id);
        return new ResponseEntity<>(subscription,HttpStatus.OK);
    }
    @GetMapping("/findSubscriptionBybody")
    public ResponseEntity<Optional<Subscriptions>> findSubscription(String id,String productCode){
        Optional<Subscriptions> subscription=subscriptions.findCustomerIdandproductCode(id,productCode);
        return new ResponseEntity<>(subscription,HttpStatus.OK);
    }
    @PostMapping("/loanApplication")
    public ResponseEntity<loanApplication> loanApplication(@RequestBody newApplication application){
       loanService.loanApplication(application.getPhone(),application.getProductCode(),application.getAmount());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/loanRepayment")
    public ResponseEntity<Payments> loanRepayment(String phoneNumber, String productCode, String amount){
        paymentService.paymentRequest(phoneNumber,productCode,amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
   @PostMapping("/score")
    public ResponseEntity score(Long id){
        Integer score=scoreService.loadData(id);
        return new ResponseEntity<>(score,HttpStatus.OK);
    }
    @GetMapping("/dashBoardData")
    public ResponseEntity<DashBoardData> getData(){
         DashBoardData data= reportService.getData();
        return new ResponseEntity<>(data,HttpStatus.OK);
    }
    @PostMapping("/sendSms")
    public ResponseEntity<List<Recipient>> send(String message) throws IOException {
        List<Recipient> data= sms.sendSms(message);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }
    @PostMapping("/sendSms2")
    public ResponseEntity<List<SmsResponse>> send(@RequestBody bulkSmsModel customSms) throws IOException, ApiException {
        List<SmsResponse> data= communicationService.sendBulkSMS(customSms);
        return new ResponseEntity<>(data,HttpStatus.CREATED);
    }


}

