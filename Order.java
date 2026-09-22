import java.util.ArrayList;


public class Order {
    private int orderId;
    private String customerName;
    private ArrayList<OrderItem> items;
    private double total;
    private OrderStatus status;

    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.status = OrderStatus.PENDING;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void addItem(OrderItem orderItem) {
        items.add(orderItem);
        calculateTotal();
    }


    public boolean removeItem(int menuItemId) {
        boolean removed = items.removeIf(oi -> oi.getItem().getId() == menuItemId);
        calculateTotal();
        return removed;
    }


    public void calculateTotal() {
        double sum = 0.0;
        for (OrderItem oi : items) {
            sum += oi.calculateSubtotal();
        }
        this.total = sum;
    }


    public void displayOrder() {
        System.out.println("Order #" + orderId + " | Customer: " + customerName);
        System.out.println("Status: " + status);
        if (items.isEmpty()) {
            System.out.println("  (no items yet)");
        } else {
            for (OrderItem oi : items) {
                System.out.println("  " + oi);
            }
        }
        System.out.printf("TOTAL: $%.2f%n", total);

    }
}
