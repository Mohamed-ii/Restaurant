import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Restaurant restaurant = new Restaurant();
        seedSampleMenu(restaurant);

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Choose an option: ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number between 1 and 15.\n");
                continue;
            }

            switch (choice) {
                case 1 -> restaurant.addMenuItem(sc);
                case 2 -> restaurant.removeMenuItem(sc);
                case 3 -> restaurant.displayMenu();
                case 4 -> restaurant.searchMenuItem(sc);
                case 5 -> restaurant.createOrder(sc);
                case 6 -> restaurant.addItemToOrder(sc);
                case 7 -> restaurant.removeItemFromOrder(sc);
                case 8 -> restaurant.displayOrder(sc);
                case 9 -> restaurant.addOrderToKitchenQueue(sc);
                case 10 -> restaurant.processNextOrder();
                case 11 -> restaurant.searchOrder(sc);
                case 12 -> restaurant.checkOrderStatus(sc);
                case 13 -> restaurant.displayCompletedOrders();
                case 14 -> restaurant.cancelOrder(sc);
                case 15 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please choose 1-15.");
            }
            System.out.println();
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("===== Restaurant Order Manager =====");
        System.out.println(" 1. Add Menu Item");
        System.out.println(" 2. Remove Menu Item");
        System.out.println(" 3. Display Menu");
        System.out.println(" 4. Search Menu Item");
        System.out.println(" 5. Create Order");
        System.out.println(" 6. Add Item to Order");
        System.out.println(" 7. Remove Item from Order");
        System.out.println(" 8. Display Order");
        System.out.println(" 9. Add Order to Kitchen Queue");
        System.out.println("10. Process Next Order");
        System.out.println("11. Search Order");
        System.out.println("12. Check Order Status");
        System.out.println("13. Display Completed Orders");
        System.out.println("14. Cancel Order");
        System.out.println("15. Exit");

    }


    private static void seedSampleMenu(Restaurant restaurant) {
        // Feed the sample rows through the same addMenuItem() method the
        // user's own input goes through, so there's exactly one code path.
        Scanner seedScanner = new Scanner(
                ("1\nBurger\n150\nMain Course\n"
                + "2\nPizza\n200\nMain Course\n"
                + "3\nPasta\n180\nMain Course\n"
                + "4\nCola\n40\nDrinks\n")
        );
        for (int i = 0; i < 4; i++) {
            restaurant.addMenuItem(seedScanner);
        }
    }
}
