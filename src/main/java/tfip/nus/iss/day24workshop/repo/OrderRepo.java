package tfip.nus.iss.day24workshop.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tfip.nus.iss.day24workshop.model.LineItem;
import tfip.nus.iss.day24workshop.model.Order;
import static tfip.nus.iss.day24workshop.repo.Queries.*;

import java.util.List;

@Repository
public class OrderRepo {

    @Autowired
    private JdbcTemplate template;

    public Boolean insertOrder(Order o) {
        return template.update(INSERT_ORDER,
                o.getOrderId(),
                o.getOrderDate(),
                o.getCustomerName(),
                o.getShipAddress(),
                o.getNotes(),
                o.getTax()) > 0; // update will return >0 (rows affected) if success
    }

    public void addLineItems(List<LineItem> lineItems) {
        List<Object[]> arrData = lineItems.stream()
                .map(li -> {
                    Object[] l = new Object[5];
                    l[0] = li.getOrderId();
                    l[1] = li.getProduct();
                    l[3] = li.getDiscount();
                    l[2] = li.getUnitPrice();
                    l[4] = li.getQuantity();
                    return l;
                }).toList();

        template.batchUpdate(INSERT_ORDER_DETAILS, arrData);
    }

}
