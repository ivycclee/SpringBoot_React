package com.example.assignmenttwo_starter.service;

import com.example.assignmenttwo_starter.model.Customer;
import com.example.assignmenttwo_starter.model.OrderItem;
import com.example.assignmenttwo_starter.model.Orders;
import com.example.assignmenttwo_starter.service.OrderItemService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class HelperFunctions {

    @Autowired
    private OrderItemService oiService;
    public byte[] getInvoiceForCustomer(Customer c) throws DocumentException {
        Document doc = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, baos);

        //add heading
        Paragraph p = new Paragraph("Invoice for " + c.getFirstName() + " " + c.getLastName() + "");
        p.setAlignment(Element.ALIGN_CENTER);

        //filtering orders that are pending or processing
        List<Orders> filtered = c.getOrdersCollection().stream().filter(o -> o.getOrderStatusId().getOrderStatusId() == 3 || o.getOrderStatusId().getOrderStatusId() == 5).toList();

        //use filtered list to retrieve orderID then use these to retrieve order items from repo
        List<Integer> orderIDs = new ArrayList();

        filtered.stream().forEach(o -> orderIDs.add(o.getOrderId()));

        List<OrderItem> oiList = oiService.getOrderItemsByOrderID(orderIDs);

        Table table = makeTable();

        //for each order item, populate
        if (!oiList.isEmpty()) {

            oiList.stream().forEach(oi -> {
                table.addCell(new Cell(oi.getProductId().getName()));
                table.addCell(new Cell(oi.getQuantity().toString()));
                table.addCell(new Cell(oi.getProductId().getPrice().toString()));
            });
            doc.open();
            doc.add(p);
            doc.add(table);
            doc.close();
        } else {
            Paragraph noItems = new Paragraph("No items to invoice");
            noItems.setAlignment(Element.ALIGN_CENTER);
            doc.open();
            doc.add(noItems);
            doc.close();
        }

        return baos.toByteArray();
    }

    public Table makeTable() throws BadElementException {
        Table table = new Table(3);
        Cell c1 = new Cell("Product Name");
        Cell c2 = new Cell("Quantity");
        Cell c3 = new Cell("Price");

        c1.setBackgroundColor(Color.GRAY);
        c2.setBackgroundColor(Color.GRAY);
        c3.setBackgroundColor(Color.GRAY);

        table.addCell(c1);
        table.addCell(c2);
        table.addCell(c3);

        return table;
    }

    public byte[] getInvoiceForOrder(Orders o) throws DocumentException {
        Document doc = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, baos);

        Paragraph p = new Paragraph("Invoice for order ID: " + o.getOrderId() + "\nThis order is " + o.getOrderStatusId().getStatus());
        p.setAlignment(Element.ALIGN_CENTER);
        doc.open();
        doc.add(p);

        //check order status to make sure they are processing or pending
        if (o.getOrderStatusId().getOrderStatusId() == 3 || o.getOrderStatusId().getOrderStatusId() == 5){
            Table t = makeTable();

            if (o.getOrderItemCollection().isEmpty()){
                Paragraph empty = new Paragraph("This order is " + o.getOrderStatusId().getStatus() + ", but has no items in the order");
                empty.setAlignment(Element.ALIGN_CENTER);
                doc.add(empty);
            }

            else {
                o.getOrderItemCollection().stream().forEach(oi -> {
                    t.addCell(new Cell(oi.getProductId().getName()));
                    t.addCell(new Cell(oi.getQuantity().toString()));
                    t.addCell(new Cell(oi.getProductId().getPrice().toString()));
                });

                doc.add(t);
            }
            doc.close();
        }
        else {
            Paragraph noItems = new Paragraph("This order is not processing or pending, no items to invoice");
            noItems.setAlignment(Element.ALIGN_CENTER);
            doc.open();
            doc.add(noItems);
            doc.close();
        }

        return baos.toByteArray();
    }
}
