package com.example.assignmenttwo_starter.controller;

import com.example.assignmenttwo_starter.service.HelperFunctions;
import com.example.assignmenttwo_starter.model.OrderItem;
import com.example.assignmenttwo_starter.model.Orders;
import com.example.assignmenttwo_starter.model.Product;
import com.example.assignmenttwo_starter.service.CustomerService;
import com.example.assignmenttwo_starter.service.OrderItemService;
import com.example.assignmenttwo_starter.service.OrderService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping("/orders/")
public class OrderController {
    @Autowired
    private OrderService oService;

    @Autowired
    private CustomerService cService;

    @Autowired
    private OrderItemService oiService;

    @Autowired
    private HelperFunctions hf;


    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity<List<Orders>> getOrdersByCustomerID(@PathVariable Integer id) {
        List<Orders> orders = oService.findByCustomerID(id);

        if (orders.isEmpty())
            return new ResponseEntity("No orders found for this customer", HttpStatus.NOT_FOUND);

        else
            return ResponseEntity.ok(orders);
    }

    @GetMapping(value = "/invoice/{id}", produces = "application/pdf")
    public ResponseEntity<byte[]> getInvoiceItemsByInvoiceId(@PathVariable Long id) throws DocumentException {
        //fetch order
        Orders o;

        if (oService.findByOrderID(id).isPresent()) {
            o = oService.findByOrderID(id).get();

            byte[] baos = hf.getInvoiceForOrder(o);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            return new ResponseEntity<>(baos, headers, HttpStatus.OK);
        }

        else
        {
            Document doc = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(doc, baos);

            Paragraph noCustomer = new Paragraph("No order found with this ID, no items to invoice");
            noCustomer.setAlignment(Element.ALIGN_CENTER);
            doc.open();
            doc.add(noCustomer);
            doc.close();
            return new ResponseEntity<>(baos.toByteArray(), HttpStatus.NOT_FOUND);
        }

    }


    // create recommendations for a customer based on their previous purchases
    @GetMapping(value = "/recommendations/{id}", produces = {"application/json", "application/xml"})
    public ResponseEntity createRecommendationsForCustomer(@PathVariable Integer id) throws MessagingException {
        List<Orders> customerOrders = oService.findByCustomerID(id);

        if (customerOrders.isEmpty())
        {
            //return a list of order items that are most frequently purchased

            //db query to get most frequently purchased items
            List<Product> topItems = oiService.getTopProducts();
            //System.out.println(topItems);

            String products_arranged = "Since you haven't ordered anything yet, our customers love these.. \n";

            //get top 6 items
            topItems = topItems.subList(0, 6);

            for (Product p : topItems) {
                products_arranged += p.getName() + "  -- " + p.getDescription() + "\n";
            }

            sendEmail(products_arranged);

            return ResponseEntity.ok(products_arranged);

        }

        else {
            List<Product> allProducts = oiService.getAllProducts();

            //get their orders and check description to see if dog/cat/fish/bird is contained in them
            Map<String, Integer> animalCount = new HashMap<String, Integer>() { {
                put("dog", 0);
                put("cat", 0);
                put("fish", 0);
                put("bird", 0); } };

            String message = "Since you've bought ";

            for (Orders o: customerOrders) {
                List<OrderItem> customerOI = o.getOrderItemCollection();

                for (OrderItem oi : customerOI) {

                    for (Map.Entry<String, Integer> entry : animalCount.entrySet()) {
                        if (oi.getProductId().getDescription().toLowerCase().contains(entry.getKey())) {
                            entry.setValue(entry.getValue() + 1);
                            message += oi.getProductId().getName() + ", ";
                        }
                    }
                }
            }
            System.out.println(animalCount);

            message += " .. \nthen maybe you would like the following: \n";

            for (Map.Entry<String, Integer> entry : animalCount.entrySet()) {
                if (entry.getValue() >= 1) {

                    for (Product p: allProducts) {
                        if (p.getDescription().toLowerCase().contains(entry.getKey())) {
                            message += p.getName() + " -- " + p.getDescription() + "\n";
                        }
                    }

                }
            }

            sendEmail(message);

            return ResponseEntity.ok(message);
        }
    }


    //function to send email
    public void sendEmail(String message) throws MessagingException {

        try {
            //change /n to <br> for email/html format
            message = message.replace("\n", "<br>");

            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.mailtrap.io");
            prop.put("mail.smtp.port", "2525");
            prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("1ed5f1fcb54790", "e29dd4051b6850");
                }
            });

            Message mess = new MimeMessage(session);
            mess.setFrom(new InternetAddress("k00252664@student.lit.ie"));
            //this line should be changed to customer's email, but for testing purposes it was sent to myself
            mess.setRecipients(Message.RecipientType.TO, InternetAddress.parse("k00252664@student.lit.ie"));
            mess.setSubject("Recommendations for you");

            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent(message, "text/html");

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp);

            mess.setContent(mp);

            Transport.send(mess);
        }
        catch(Exception e) {
            System.out.println(e);
            System.out.println("You are probably connected to eduroam so the email function won't work");
        }

    }
}
