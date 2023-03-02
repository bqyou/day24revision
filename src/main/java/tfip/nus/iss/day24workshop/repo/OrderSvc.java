package tfip.nus.iss.day24workshop.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tfip.nus.iss.day24workshop.OrderException;
import tfip.nus.iss.day24workshop.model.Order;

@Service
public class OrderSvc {

    @Autowired
    private OrderRepo orderRepo;

    @Transactional(rollbackFor = OrderException.class)
    public void checkOut(Order o) throws OrderException {
        orderRepo.insertOrder(o);
        if (o.getLineItems().size() > 4) {
            throw new OrderException("More than 4 brah");
        }
        orderRepo.addLineItems(o.getLineItems());
    }

}
