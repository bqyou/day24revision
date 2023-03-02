package tfip.nus.iss.day24workshop.repo;

public class Queries {

    public static final String INSERT_ORDER = """
            insert into orders (order_id, order_date, customer_name, ship_address, notes, tax)
            values (?, ?, ?, ?, ?, ?)
                """;

    public static final String INSERT_ORDER_DETAILS = """
            insert into order_details (order_id, product, unit_price, discount, quantity)
            values (?, ?, ?, ?, ?)
                """;

}
