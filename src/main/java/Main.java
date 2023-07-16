import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductAnalytics productAnalytics = new ProductAnalytics();

        int count = 0;
        while (sc.hasNext() && count < 5) {
            String s = sc.nextLine();
            if ("exit".equals(s)) {
                break;
            }
            String[] parts = s.split(" ");
            if (parts.length != 3) {
                System.out.println("Wrong number of arguments! Retry!");
                continue;
            }
            String productName = parts[0];
            Integer productCost;
            Integer productCount;
            try {
                productCost = Integer.parseInt(parts[1]);
                productCount = Integer.parseInt(parts[2]);
            } catch (NumberFormatException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Retry!");
                continue;
            }

            productAnalytics.addProduct(productName, productCost, productCount);

            count++;
        }
        sc.close();

        Product[] sortedProducts = productAnalytics.getSortedProducts();

        long sum = productAnalytics.calculateTotalCost();

        System.out.println(sum);

        for (int j = 0; j < 3; j++) {
            Product mostPopularProduct = productAnalytics.getMostPopularProduct();
            if (mostPopularProduct != null) {
                System.out.println("Most popular product is " + mostPopularProduct.getName());
                productAnalytics.removeProduct(mostPopularProduct);
            }
        }
    }
}

class Product {
    protected String name;
    protected int cost;
    protected int count;

    public Product(String name, int cost, int count) {
        this.name = name;
        this.cost = cost;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getCount() {
        return count;
    }
}

class ProductAnalytics {
    protected Product[] products;
    private int size;

    public ProductAnalytics() {
        products = new Product[5];
        size = 0;
    }

    public void addProduct(String name, int cost, int count) {
        boolean productAlreadyExists = false;
        for (int i = 0; i < size; i++) {
            if (name.equals(products[i].getName())) {
                products[i] = new Product(name, cost, products[i].getCount() + count);
                productAlreadyExists = true;
                break;
            }
        }

        if (!productAlreadyExists) {
            if (size < 5) {
                products[size] = new Product(name, cost, count);
                size++;
            }
        }
    }

    public Product[] getSortedProducts() {
        Product[] sortedProducts = Arrays.copyOf(products, size);

        Arrays.sort(sortedProducts, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(p1.getName(), p2.getName());
                if (res == 0) {
                    res = p1.getName().compareTo(p2.getName());
                }
                return res;
            }
        });

        return sortedProducts;
    }

    public long calculateTotalCost() {
        long sum = 0;
        for (int i = 0; i < size; i++) {
            sum += (long) products[i].getCost() * products[i].getCount();
        }
        return sum;
    }

    public Product getMostPopularProduct() {
        if (size == 0) {
            return null;
        }

        int indexOfMostPopularProduct = 0;
        for (int i = 0; i < size; i++) {
            if (products[i].getCount() > products[indexOfMostPopularProduct].getCount()) {
                indexOfMostPopularProduct = i;
            }
        }

        return products[indexOfMostPopularProduct];
    }

    public void removeProduct(Product product) {
        for (int i = 0; i < size; i++) {
            if (product == products[i]) {
                products[i] = null;
                break;
            }
        }

        // Сдвигаем оставшиеся продукты влево
        for (int i = 0; i < size - 1; i++) {
            if (products[i] == null) {
                products[i] = products[i + 1];
                products[i + 1] = null;
            }
        }

        size--;
    }
}