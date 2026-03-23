package com.ecommerce.ecommerce_backend.service;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_backend.model.Order;
import com.ecommerce.ecommerce_backend.model.OrderItem;
import com.ecommerce.ecommerce_backend.repository.OrderRepository;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final OrderRepository orderRepository;

    public byte[] generateInvoice(Long orderId, String email) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // USER can only access own order
        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("INVOICE")
                .setBold()
                .setFontSize(20));

        document.add(new Paragraph("Order ID: " + order.getId()));
        document.add(new Paragraph("Order Date: " + order.getOrderDate()));
        document.add(new Paragraph("Order Status: " + order.getStatus()));
document.add(new Paragraph("Payment Status: " + order.getPaymentStatus()));
document.add(new Paragraph("Payment Method: " + order.getPaymentMethod()));
document.add(new Paragraph(" "));

        document.add(new Paragraph("Shipping Address")
                .setBold());

        document.add(new Paragraph(order.getShippingName()));
        document.add(new Paragraph(order.getShippingStreet()));
        document.add(new Paragraph(order.getShippingCity() + ", " + order.getShippingState()));
        document.add(new Paragraph(order.getShippingCountry() + " - " + order.getShippingZip()));

        document.add(new Paragraph(" "));

        Table table = new Table(4);
        table.addHeaderCell("Product");
        table.addHeaderCell("Price");
        table.addHeaderCell("Qty");
        table.addHeaderCell("Total");

        for (OrderItem item : order.getItems()) {
            table.addCell(item.getProductName());
            table.addCell(String.valueOf(item.getPrice()));
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.valueOf(item.getPrice() * item.getQuantity()));
        }

        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total Amount: ₹" + order.getTotalAmount())
                .setBold());
        

        document.close();
        

        return out.toByteArray();
    }
}
