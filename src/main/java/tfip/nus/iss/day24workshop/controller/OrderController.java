package tfip.nus.iss.day24workshop.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import tfip.nus.iss.day24workshop.OrderException;
import tfip.nus.iss.day24workshop.model.LineItem;
import tfip.nus.iss.day24workshop.model.Order;
import tfip.nus.iss.day24workshop.repo.OrderSvc;

@Controller
@RequestMapping(path = "/")
public class OrderController {

    @Autowired
    private OrderSvc orderSvc;

    @GetMapping
    public String showLanding() {
        return "index";
    }

    @PostMapping(path = "/order")
    public String keyOrderInfo(@RequestBody MultiValueMap<String, String> form, Model model, HttpSession sess)
            throws ParseException {
        Order o = new Order();
        o.setCustomerName(form.getFirst("name"));
        String date = form.getFirst("deliveryDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateUtil = sdf.parse(date);
        java.sql.Date DateSql = new java.sql.Date(dateUtil.getTime());
        o.setOrderDate(DateSql);
        o.setShipAddress(form.getFirst("shipAddress"));
        o.setNotes(form.getFirst("notes"));
        o.setOrderId(UUID.randomUUID().toString().substring(0, 8));
        model.addAttribute("order", o);
        sess.setAttribute("order", o);
        return "items";
    }

    @PostMapping(path = "/addItem")
    public String addItems(@RequestBody MultiValueMap<String, String> form, Model model, HttpSession sess) {
        Order o = (Order) sess.getAttribute("order");
        LineItem item = new LineItem();
        item.setProduct(form.getFirst("product"));
        item.setUnitPrice(Float.parseFloat(form.getFirst("unitPrice")));
        item.setDiscount(Float.parseFloat(form.getFirst("discount")));
        item.setQuantity(Integer.parseInt(form.getFirst("quantity")));
        item.setOrderId(o.getOrderId());
        if (o.getLineItems() == null) {
            List<LineItem> list = new LinkedList<LineItem>();
            o.setLineItems(list);
        }
        o.getLineItems().add(item);
        sess.setAttribute("order", o);
        model.addAttribute("order", o);
        System.out.println(o.getLineItems().get(0));
        return "items";
    }

    @PostMapping(path = "/checkout")
    public String totalCost(Model model, HttpSession sess) throws OrderException {
        Order o = (Order) sess.getAttribute("order");
        orderSvc.checkOut(o);
        return "checkout";
    }

}
