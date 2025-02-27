package com.practice;

import java.util.ArrayList;
import java.util.Scanner;

public class Project_Based_Prgm {

    // Enum for product categories
    enum Category {
        ELECTRONICS, CLOTHING, GROCERY
    }

    // Product class to store item details
    static class Product {
        private String name;
        private double price;
        private Category category;
        private int id;
        private static int idCounter = 1000;

        public Product(String name, double price, Category category) {
            this.name = name;
            this.price = price;
            this.category = category;
            this.id = idCounter++;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
        public Category getCategory() { return category; }
        public int getId() { return id; }

        @Override
        public String toString() {
            return id + ": " + name + " - $" + price + " (" + category + ")";
        }
    }

    // Cart class
    static class Cart {
        private ArrayList<Product> items;
        private double discount;

        public Cart() {
            items = new ArrayList<>();
            discount = 0.0;
        }

        public void addItem(Product product) {
            items.add(product);
        }

        public void removeItem(int productId) {
            items.removeIf(product -> product.getId() == productId);
        }

        public double calculateTotal() {
            double total = 0.0;
            for (Product item : items) {
                total += item.getPrice();
            }
            return discount > 0 ? total - (total * discount) : total;
        }

        public void setDiscount(double discount) {
            this.discount = (discount >= 0 && discount <= 1) ? discount : 0;
        }

        public ArrayList<Product> getItems() {
            return items;
        }
    }

    // Customer class
    static class Customer {
        private String name;
        private Cart cart;

        public Customer(String name) {
            this.name = name;
            this.cart = new Cart();
        }

        public String getName() { return name; }
        public Cart getCart() { return cart; }
    }

    // Main Class for ShoppingCartSystem
    public static class ShoppingCartSystem {
        private static Scanner scanner = new Scanner(System.in);
        private static ArrayList<Product> inventory = new ArrayList<>();
        private static Customer customer;

        public static void main(String[] args) {
            customer = new Customer(args.length > 0 ? args[0] : "Guest");
            initializeInventory();

            while (true) {
                displayMenu();
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    switch (choice) {
                        case 1: addToCart(); break;
                        case 2: removeFromCart(); break;
                        case 3: viewCart(); break;
                        case 4: applyDiscount(); break;
                        case 5: checkout(); break;
                        case 6: System.exit(0);
                        default: System.out.println("Invalid choice! Please try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input! Please enter a valid number.");
                    scanner.next(); // Clear invalid input
                }
            }
        }

        private static void initializeInventory() {
            inventory.add(new Product("Laptop", 999.99, Category.ELECTRONICS));
            inventory.add(new Product("Shirt", 29.99, Category.CLOTHING));
            inventory.add(new Product("Milk", 3.99, Category.GROCERY));
            inventory.add(new Product("Phone", 599.99, Category.ELECTRONICS));
        }

        private static void displayMenu() {
            StringBuilder menu = new StringBuilder();
            menu.append("\nWelcome ").append(customer.getName())
                .append("\n1. Add Item\n2. Remove Item\n3. View Cart\n")
                .append("4. Apply Discount\n5. Checkout\n6. Exit\nEnter choice: ");
            System.out.print(menu.toString());
        }

        private static void addToCart() {
            System.out.println("\nAvailable Products:");
            for (int i = 0; i < inventory.size(); i++) {
                System.out.println((i + 1) + ". " + inventory.get(i));
            }

            System.out.print("Enter product number: ");
            try {
                int choice = scanner.nextInt() - 1;
                scanner.nextLine();
                if (choice >= 0 && choice < inventory.size()) {
                    customer.getCart().addItem(inventory.get(choice));
                    System.out.println("Item added successfully!");
                } else {
                    System.out.println("Invalid product number!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.next();
            }
        }

        private static void removeFromCart() {
            ArrayList<Product> items = customer.getCart().getItems();
            if (items.isEmpty()) {
                System.out.println("Cart is empty!");
                return;
            }
            viewCart();
            System.out.print("Enter product ID to remove: ");
            try {
                int id = scanner.nextInt();
                scanner.nextLine();
                customer.getCart().removeItem(id);
                System.out.println("Item removed successfully!");
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.next();
            }
        }

        private static void viewCart() {
            ArrayList<Product> items = customer.getCart().getItems();
            System.out.println("\nCart Contents:");
            if (items.isEmpty()) {
                System.out.println("Cart is empty!");
            } else {
                for (Product item : items) {
                    System.out.println(item);
                }
                System.out.printf("Total: $%.2f%n", customer.getCart().calculateTotal());
            }
        }

        private static void applyDiscount() {
            System.out.print("Enter discount percentage (0-100): ");
            try {
                double discount = scanner.nextDouble() / 100;
                scanner.nextLine();
                customer.getCart().setDiscount(discount);
                System.out.println("Discount applied successfully!");
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.next();
            }
        }

        private static void checkout() {
            viewCart();
            if (!customer.getCart().getItems().isEmpty()) {
                System.out.println("Thank you for shopping, " + customer.getName() + "!");
                customer.getCart().getItems().clear();
            }
        }
    }
}
