import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;
public class Restaurant {
    private ArrayList<MenuItem> menu;
    private LinkedList<Order> kitchenQueue;
    private HashMap<Integer, Order> orders;
    private LinkedHashMap<Integer, Order> completedOrders;

    public Restaurant() {
        menu = new ArrayList<>();
        kitchenQueue = new LinkedList<>();
        orders = new HashMap<>();
        completedOrders = new LinkedHashMap<>();
    }

    private MenuItem findMenuItem(int id) {
        for (MenuItem m : menu) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    private boolean menuIdExists(int id) {
        return findMenuItem(id) != null;
    }

    public void addMenuItem(Scanner sc) {
        System.out.print("Enter item ID: ");
        int id = readInt(sc);
        if (menuIdExists(id)) {
            System.out.println("A menu item with ID " + id + " already exists. Menu item IDs must be unique.");
            return;
        }
        System.out.print("Enter item name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter item price: ");
        double price = readDouble(sc);
        System.out.print("Enter item category: ");
        String category = sc.nextLine().trim();

        menu.add(new MenuItem(id, name, price, category));
        System.out.println("Menu item added successfully.");
    }

    public void removeMenuItem(Scanner sc) {
        System.out.print("Enter item ID to remove: ");
        int id = readInt(sc);
        MenuItem toRemove = findMenuItem(id);
        if (toRemove == null) {
            System.out.println("No menu item found with ID " + id + ".");
            return;
        }
        menu.remove(toRemove);
        System.out.println("Menu item removed successfully.");
    }

    public void displayMenu() {
        if (menu.isEmpty()) {
            System.out.println("The menu is currently empty.");
            return;
        }
        System.out.println(" MENU ");
        for (MenuItem m : menu) {
            System.out.println(m);
        }

    }

    public void searchMenuItem(Scanner sc) {
        System.out.print("Enter item ID to search: ");
        int id = readInt(sc);
        MenuItem found = findMenuItem(id);
        if (found == null) {
            System.out.println("No menu item found with ID " + id + ".");
        } else {
            System.out.println("Found: " + found);
        }
    }

    public void createOrder(Scanner sc) {
        System.out.print("Enter order ID: ");
        int id = readInt(sc);
        if (orders.containsKey(id)) {
            System.out.println("An order with ID " + id + " already exists. Order IDs must be unique.");
            return;
        }
        System.out.print("Enter customer name: ");
        String customerName = sc.nextLine().trim();

        Order order = new Order(id, customerName);
        orders.put(id, order);
        System.out.println("Order #" + id + " created for " + customerName + " (status: PENDING).");
    }

    public void addItemToOrder(Scanner sc) {
        System.out.print("Enter order ID: ");
        int orderId = readInt(sc);
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
            return;
        }
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Cannot add items — this order is already " + order.getStatus() + ".");
            return;
        }

        System.out.print("Enter menu item ID: ");
        int menuId = readInt(sc);
        MenuItem menuItem = findMenuItem(menuId);
        if (menuItem == null) {
            System.out.println("That item does not exist in the menu.");
            return;
        }

        System.out.print("Enter quantity: ");
        int qty = readInt(sc);
        if (qty <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        order.addItem(new OrderItem(menuItem, qty));
        System.out.println("Added " + qty + "x " + menuItem.getName() + " to order #" + orderId + ".");
    }

    public void removeItemFromOrder(Scanner sc) {
        System.out.print("Enter order ID: ");
        int orderId = readInt(sc);
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
            return;
        }
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Cannot modify items — this order is already " + order.getStatus() + ".");
            return;
        }

        System.out.print("Enter menu item ID to remove from the order: ");
        int menuId = readInt(sc);
        boolean removed = order.removeItem(menuId);
        if (removed) {
            System.out.println("Item removed from order #" + orderId + ".");
        } else {
            System.out.println("That menu item was not found in order #" + orderId + ".");
        }
    }

    public void displayOrder(Scanner sc) {
        System.out.print("Enter order ID: ");
        int orderId = readInt(sc);
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
            return;
        }
        order.displayOrder();
    }

    public void addOrderToKitchenQueue(Scanner sc) {
        System.out.print("Enter order ID: ");
        int orderId = readInt(sc);
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
            return;
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Only PENDING orders can be sent to the kitchen. This order is " + order.getStatus() + ".");
            return;
        }

        kitchenQueue.addLast(order);
        order.setStatus(OrderStatus.IN_KITCHEN);
        System.out.println("Order #" + orderId + " sent to the kitchen queue.");
    }

    public void processNextOrder() {
        if (kitchenQueue.isEmpty()) {
            System.out.println("The kitchen queue is empty. Nothing to process.");
            return;
        }

        Order next = kitchenQueue.peekFirst();
        if (next.getItems().isEmpty()) {
            System.out.println("This order has no items and cannot be processed.");
            return;
        }

        kitchenQueue.removeFirst();
        next.setStatus(OrderStatus.COMPLETED);
        completedOrders.put(next.getOrderId(), next);
        System.out.println("Order #" + next.getOrderId() + " processed and marked COMPLETED.");
    }

    public void searchOrder(Scanner sc) {
        System.out.print("Enter order ID to search: ");
        int orderId = readInt(sc);
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
        } else {
            order.displayOrder();
        }
    }

    public void checkOrderStatus(Scanner sc) {
        System.out.print("Enter order ID: ");
        int orderId = readInt(sc);
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
        } else {
            System.out.println("Order #" + orderId + " status: " + order.getStatus());
        }
    }


    public void displayCompletedOrders() {
        if (completedOrders.isEmpty()) {
            System.out.println("No orders have been completed yet.");
            return;
        }
        System.out.println(" COMPLETED ORDERS (in completion order) ");
        for (Order o : completedOrders.values()) {
            o.displayOrder();
        }

    }

    public void cancelOrder(Scanner sc) {
        System.out.print("Enter order ID to cancel: ");
        int orderId = readInt(sc);
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
            return;
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            System.out.println("Order #" + orderId + " is already COMPLETED and cannot be cancelled.");
            return;
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("Order #" + orderId + " is already CANCELLED.");
            return;
        }

        if (order.getStatus() == OrderStatus.IN_KITCHEN) {
            kitchenQueue.remove(order);
        }


        order.setStatus(OrderStatus.CANCELLED);

        System.out.println("Order #" + orderId + " has been cancelled.");
    }


    private int readInt(Scanner sc) {
        while (true) {
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid whole number: ");
            }
        }
    }

    private double readDouble(Scanner sc) {
        while (true) {
            String line = sc.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
