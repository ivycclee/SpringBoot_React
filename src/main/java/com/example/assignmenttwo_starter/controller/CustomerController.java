package com.example.assignmenttwo_starter.controller;

import com.example.assignmenttwo_starter.service.HelperFunctions;
import com.example.assignmenttwo_starter.model.Customer;
import com.example.assignmenttwo_starter.service.CustomerService;
import com.example.assignmenttwo_starter.service.OrderItemService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:8080/")
@RequestMapping("/petopia/")
public class CustomerController {

    @Autowired
    private CustomerService cService;

    @Autowired
    private HelperFunctions hf;

    @Autowired
    private OrderItemService oiService;

    @GetMapping(value = "/all", produces = {"application/json", "application/xml"})
    public ResponseEntity<List<Customer>> getAllCustomers(@RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "20", required = false) int size) {
        List<Customer> customers = cService.getAllCustomersPaged(page, size);

        if (customers.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else {
            //self link
            customers.stream().forEach(c -> c.add(linkTo(CustomerController.class).slash(c.getCustomerId()).withSelfRel()));

            //link to next page
            customers.stream().forEach(c -> c.add(linkTo(CustomerController.class).slash("all?page="+ (page+1) +"&size="+ size).withRel("next page")));

            //link to previous page
            if (page != 0)
                customers.stream().forEach(c -> c.add(linkTo(CustomerController.class).slash("all?page="+ (page-1) +"&size="+ size).withRel("previous page")));

            //link to get order info
            customers.stream().forEach(c -> c.add(linkTo(methodOn(OrderController.class).getOrdersByCustomerID(c.getCustomerId())).withRel("orders")));

            return ResponseEntity.ok(customers);
        }
    }

    @GetMapping(value="/{id}", produces={"application/json", "application/xml"})
    public ResponseEntity<Customer> getCustomerByID(@PathVariable Integer id) {
        Optional<Customer> c = cService.findByID(id);

        if (!c.isPresent()) {
            return new ResponseEntity("No customer found with this ID", HttpStatus.NOT_FOUND);
        }

        else
        {
            // link to all customers
            Link all = linkTo(methodOn(CustomerController.class).getAllCustomers(0, 20)).withRel("all customers");
            c.get().add(all);
            return ResponseEntity.ok(c.get());
        }
    }

    // add customer to db
    @PostMapping(value = "/add", consumes = {"application/json", "application/xml"})
    public ResponseEntity addCustomer(@Valid @RequestBody Customer c) {
        try {
            cService.saveCustomer(c);
            return new ResponseEntity("Customer added to database successfully", HttpStatus.CREATED);
        }
        catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    //update a customer
    @PutMapping(value="/update", consumes = {"application/json", "application/xml"})
    public ResponseEntity updateCustomer(@Valid @RequestBody Customer c) {
        Optional<Customer> c1 = cService.findByID(c.getCustomerId());

        if (!c1.isPresent()) {
            return new ResponseEntity("No customer found with this ID", HttpStatus.NOT_FOUND);
        }

        else
        {
            cService.saveCustomer(c);
            return new ResponseEntity("Customer updated successfully", HttpStatus.OK);
        }
    }

    //delete a customer
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCustomer(@PathVariable Integer id) {
        Optional<Customer> c = cService.findByID(id);

        if (!c.isPresent()) {
            return new ResponseEntity("No customer found with this ID", HttpStatus.NOT_FOUND);
        }

        else
        {
            cService.deleteCustomer(id);
            return new ResponseEntity("Request successful", HttpStatus.OK);
        }
    }

    @GetMapping(value = "/invoice/{id}", produces = "application/pdf")
    public ResponseEntity<byte[]> getInvoiceItemsByCustomerId(@PathVariable Integer id) throws DocumentException {
        Customer c;

        if (cService.findByID(id).isPresent()) {
            c = cService.findByID(id).get();
            hf.getInvoiceForCustomer(c);

            byte[] baos = hf.getInvoiceForCustomer(c);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            return new ResponseEntity<>(baos, headers, HttpStatus.OK);

        }

        else {
            Document doc = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(doc, baos);

            Paragraph noCustomer = new Paragraph("No customer found with this ID, cannot retrieve any records");
            noCustomer.setAlignment(Element.ALIGN_CENTER);
            doc.open();
            doc.add(noCustomer);
            doc.close();
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.NOT_FOUND);
        }
    }

}
