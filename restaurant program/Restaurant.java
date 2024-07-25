import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
//import java.util.Comparator;
import java.util.Scanner;
//import java.util.stream.Collectors;

class Menu {
    private double burger = 10;
    private double pizza = 15;
    private double espresso = 5;
    private double cocoa = 3;

    protected int burgCount = 0;
    protected int pizzaCount = 0;
    protected int espCount = 0;
    protected int cocoCount = 0;

    protected double total = 0;
    
    public double getPrice(String item) {                
        if ((item.contains("burger")) || (item.contains("Burger"))) {
            total += burger;
            countBurg(1);
        }
        if ((item.contains("pizza")) || (item.contains("Pizza"))) {
            total += pizza;
            countPizza(1);
        }
        if ((item.contains("espresso")) || (item.contains("Espresso"))) {
            total += espresso;
            countEspr(1);
        }
        if ((item.contains("cocoa")) || (item.contains("Cocoa"))) {
            total += cocoa;
            countCoco(1);
        }
        
        return total;
    }

    public void displayMenu() {
        System.out.println("Please select an item from this menu to be ordered:");
        System.out.println("Veg Burger - $10");
        System.out.println("Chicken Pizza - $15");
        System.out.println("Espresso - $5");
        System.out.println("Hot Cocoa - $3");
        System.out.println();
    }

    public int getBurg() {
        return burgCount;
    }

    public void countBurg(int burgers) {
        burgCount = burgCount + burgers;
    }

    public int getPizza() {
        return pizzaCount;
    }

    public void countPizza(int pizzas) {
        pizzaCount = pizzaCount + pizzas;
    }

    public int getEspr() {
        return espCount;
    }

    public void countEspr(int espressos) {
        espCount = espCount + espressos;
    }

    public int getCoco() {
        return cocoCount;
    }

    public void countCoco(int cocoas) {
        cocoCount = cocoCount + cocoas;
    }

    public double getTotal() {
        return total;
    }

    public void logOrder() {
        System.out.println("Sales for today: ");
        if (burgCount > 0) {
            System.out.printf("Total Veg burgers sold- %d\n", getBurg());
        }
        if (pizzaCount > 0) {
            System.out.printf("Total Chicken pizzas sold- %d\n", getPizza());
        }
        if (espCount > 0) {
            System.out.printf("Total Espresso sold- %d\n", getEspr());
        }
        if (cocoCount > 0) {
            System.out.printf("Total Cocoa sold- %d\n", getCoco());
        }
        System.out.printf("Total Sales- $%.2f\n", getTotal());
    }
}

class Order extends Menu {
    private SalesReport sr = new SalesReport();
    //private Recipe rec = new Recipe();
    
    //rec.readInventory("\\Users\\ev879\\Documents\\CS112\\MyWork\\Lab15\\Inventory.txt");
    
    public void order(Scanner scn) throws IOException{
        System.out.println("Choose one of the below action items:");
        System.out.println("1. Display menu");
        System.out.println("2. Check Inventory");
        System.out.println("3. Sales Report");
        System.out.println("4. Exit");
        System.out.println();

        int choice = scn.nextInt();
        System.out.println();

        boolean sentinel = true;


        Recipe rec = new Recipe();
        rec.readRecipe();

        rec.readInventory("\\Users\\ev879\\Documents\\CS112\\MyWork\\Lab15\\Inventory.txt");

        if (choice == 1) {
            displayMenu();

            while (sentinel) {
                String order = scn.next();
                System.out.println();

                Double sale = getPrice(order);
                sr.addSales(sale);
                //Recipe temp = rec.getFoodRecipe(order);
                rec.updateInventory(order);
                rec.writeInventory();

                rec.readInventory("\\Users\\ev879\\Documents\\CS112\\MyWork\\Lab15\\Inventory.txt");

                System.out.println("Do you want to add another item? Yes/No");
                System.out.println();
                String next = scn.next();

                if ((next.equals("Yes")) || (next.equals("yes"))) {
                    sentinel = true;
                    System.out.println();

                    displayMenu();
                } else if ((next.equals("No")) || (next.equals("no"))) {
                    sentinel = false;
                    System.out.println();
                } else {
                    System.out.println("Wrong decision!");
                    break;
                }
            }

            System.out.printf("Your order total bill with tax is $%.2f\n", sr.getSales());
            System.out.println();
            

        } else if (choice == 2) {
            rec.readInventory("\\Users\\ev879\\Documents\\CS112\\MyWork\\Lab15\\Inventory.txt");
            //rec.writeInventory();
            rec.printStuff();

            while (true) {
                System.out.println("Would you like to add more items to Inventory? y/n");
                String response = scn.next();
                if (response.contains("y")) {
                    System.out.println("What Item?");
                    String questItem = scn.next();
                    System.out.println();
                    System.out.println("How Much?");
                    int questInt = scn.nextInt();

                    for (int i = 0; i < rec.getInventory().size(); i++) {
                        if (rec.getInventory().get(i).getItemName().equals(questItem)) {
                            rec.setInventory(rec.addInventory(questItem, questInt));
                        }
                    }
                    rec.writeInventory();
                } else if (response.contains("n")) {
                    System.out.println("Inventory Updated!");
                    System.out.println();
                    break;
                } else {
                    System.out.println("Wrong Decision!");
                }
            }
            
            
        } else if (choice == 3) {
            logOrder();
            //sr.displaySales(menu);
            System.out.println();
        } else {
            System.out.println("Thank you. Goodbye.");
            System.exit(0);
        }
    }
}

class Inventory extends Menu{
    private File inven = new File("Inventory.txt");

    protected String itemName;
    protected int amount;
    protected String unit;
    
    //private Scanner scan = new Scanner(inven);
    protected ArrayList<Inventory> itemList = new ArrayList<Inventory>();
    //protected ArrayList<Integer> itemCount = new ArrayList<Integer>();
   // protected ArrayList<String> units = new ArrayList<String>();
    //private int count = 0;

    public Inventory() {
        itemName = "NA";
        amount = 0;
        unit = "NONE";
    }
    
    public Inventory(String name, int num, String article) {
        itemName = name;
        amount = num;
        unit = article;
    }
    
    //"\\Users\\ev879\\Documents\\CS112\\MyWork\\Lab15\\Inventory.txt"
    public void readInventory(String pathname) {
        try {
            File inventory = new File(pathname);
            Scanner scn = new Scanner(inventory);
            //int count = 0;

            itemList.clear();

            scn.nextLine();
    
            while(scn.hasNextLine()) {
                String data=scn.nextLine();
                //System.out.println(data);

                String[] temp = data.split(",");
                String name = temp[0];
                String quantity = temp[1];
                quantity = quantity.trim();
                int quan = Integer.parseInt(quantity);
                String amount = temp[2];
                //itemList.add(count, temp[0]);
                //itemCount.add(count, Integer.parseInt(temp[1]));
                //units.add(count, temp[2]);
                Inventory newitem = new Inventory(name, quan, amount);
                itemList.add(newitem);
            }
            scn.close();
        } catch (Exception exception) {
            //exception.printStackTrace();
        }
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemCount() {
        return amount;
    }

    public ArrayList<Inventory> getInventory() {
        return itemList;
    }

    public Inventory subtractItemCount(int order) {
        amount -= order;
        System.out.println(this);
        return this;
    }

    public void addItem(int stock) {
        amount += stock;
    }

    public ArrayList<Inventory> subInventory(ArrayList<Inventory> recipe) {
        ArrayList<Inventory> result = new ArrayList<Inventory>();
        //ArrayList<Inventory> clear = new ArrayList<Inventory>();
        boolean Check = true;
        
        try {
            for (int i = 0; i < itemList.size(); i++) {
                for (int j = 0; j < recipe.size(); j++) {
                    if ((itemList.get(i).getItemName()).equals(recipe.get(j).getItemName())) {
                        String oldName = itemList.get(i).getItemName();
                        int newCount = itemList.get(i).getItemCount() - recipe.get(j).getItemCount();
                        String oldUnit = itemList.get(i).getItemUnit();
    
                        if (newCount < 1) {
                            System.out.println("Not enough inventory left. Sorry! Order discarded.");
                            newCount = 0;
                            //return clear;
                        }
                        
                        Inventory temp = new Inventory(oldName, newCount, oldUnit);
                        //System.out.println(temp);
                        
                        result.add(temp);
                        Check = false;
                        //break;
                    } 
                    //break;
                    //result.add(itemList.get(i));
                }
                
                if (Check) {
                    result.add(itemList.get(i));
                }
                Check = true;
            }
    
            //result.sort(Comparator.comparingInt(o -> o.getItemCount()));
            // result = result.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
            return result;
        } catch (Exception e) {

        }
        /*
        for (int i = 0; i < itemList.size(); i++) {
            for (int j = 0; j < recipe.size(); j++) {
                if ((itemList.get(i).getItemName()).equals(recipe.get(j).getItemName())) {
                    String oldName = itemList.get(i).getItemName();
                    int newCount = itemList.get(i).getItemCount() - recipe.get(j).getItemCount();
                    String oldUnit = itemList.get(i).getItemUnit();

                    if (newCount < 1) {
                        System.out.println("Not enough inventory left. Sorry! Order discarded.");
                        return null;
                    }
                    
                    Inventory temp = new Inventory(oldName, newCount, oldUnit);
                    //System.out.println(temp);
                    
                    result.add(temp);
                    Check = false;
                    //break;
                } 
                //break;
                //result.add(itemList.get(i));
            }
            
            if (Check) {
                result.add(itemList.get(i));
            }
            Check = true;
        }

        //result.sort(Comparator.comparingInt(o -> o.getItemCount()));
        // result = result.stream().distinct().collect(Collectors.toCollection(ArrayList::new));/* */
        return result;
    }

    public ArrayList<Inventory> addInventory(String itemName, int moreItems) {
        ArrayList<Inventory> result = new ArrayList<Inventory>();
        boolean Check = true;
        
        try {
            for (int i = 0; i < itemList.size(); i++) {
                
                if ((itemList.get(i).getItemName()).equals(itemName)) {
                    String oldName = itemList.get(i).getItemName();
                    int newCount = itemList.get(i).getItemCount() + moreItems;
                    String oldUnit = itemList.get(i).getItemUnit();
    
                    if (newCount < 1) {
                        System.out.println("Not enough inventory left. Sorry! Order discarded.");
                        return null;
                    }
                        
                    Inventory temp = new Inventory(oldName, newCount, oldUnit);
                        //System.out.println(temp);
                        
                    result.add(temp);
                    Check = false;
                        //break;
                } 
                    //break;
                    //result.add(itemList.get(i));
                
                
                if (Check) {
                    result.add(itemList.get(i));
                }
                Check = true;
            }
    
            //result.sort(Comparator.comparingInt(o -> o.getItemCount()));
            // result = result.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
            return result;
        } catch (Exception e) {

        }
        return result;
    }

    public void setInventory(ArrayList<Inventory> list) {
        itemList = list;
    }

    public String getItemUnit() {
        return unit;
    }

    public void printStuff() {
        System.out.println("Items left in inventory:");
        
        for (int i = 0; i < itemList.size(); i++) {
            System.out.printf("%s-%s%s\n", itemList.get(i).getItemName(), itemList.get(i).getItemCount(), itemList.get(i).getItemUnit());
        }
        System.out.println();
    }

    /*
    public void updateInventory(Recipe stuff) {
        //int index = itemList.indexOf(itemName);
        //itemCount.set(index, (itemCount.get(index) - orderNum));

        ArrayList<Inventory> temp = new ArrayList<Inventory>();
        temp = stuff.getRecipe(stuff);

        for (int i = 0; i < itemList.size(); i++) {
            for (int j = 0; j < stuff)
        }

    }*/

    public void writeInventory() {
        //write all the current items from Itemlist into the file
        try {
            PrintWriter pw = new PrintWriter(inven);
            pw.flush();
            pw.println("INGREDIENTS,QUANTITY,UNITS");

            for (int i = 0; i < itemList.size(); i++) {
                pw.println(itemList.get(i));
            }

            pw.close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public String toString() {
        //Bun, 100, Slices
        //itemName, amount, unit
        String strAmount = Integer.toString(amount);
        return itemName + ", " + strAmount + "," + unit;
    }
}

class SalesReport {
    private double sales4Day = 0.0;
    //private ArrayList<Double> sales = new ArrayList<>();

    public void addSales(double sale) {
        sales4Day += sale;
    }

    public double getSales() {
        final double tax = sales4Day * (2.0 / 3.0);
        return sales4Day + tax;
    }
}

class Recipe extends Inventory{
    final String[] list = {"Veg Burger", "Chicken Pizza", "Espresso", "Hot Cocoa"};
    
    private String foodName;
    private ArrayList<Inventory> recipe = new ArrayList<Inventory>();
    
    private ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    //private ArrayList<Integer> ingredientCount = new ArrayList<Integer>();

    protected ArrayList<Inventory> vegBurger = new ArrayList<>();
    protected ArrayList<Inventory> chickenPizza = new ArrayList<>();
    protected ArrayList<Inventory> espresso = new ArrayList<>();
    protected ArrayList<Inventory> hotCocoa = new ArrayList<>();

    public Recipe() {
        foodName = "Gibberish";
    }
    
    public Recipe(String name, ArrayList<Inventory> ingredients) {
        foodName = name;
        recipe = ingredients;
    }

    public void readRecipe() throws ArrayIndexOutOfBoundsException{
        try {
            File myFile = new File("\\Users\\ev879\\Documents\\CS112\\MyWork\\Lab15\\Recipe.txt");
            Scanner scn = new Scanner(myFile);

            String result = "NA";
            //ArrayList<Inventory> cookBook = new ArrayList<Inventory>();

            scn.nextLine();
            scn.nextLine();

            String demo = scn.nextLine();
            //hasnextLine()
            
            while(scn.hasNextLine()) {
                //\t\t\t
                //recipe.add(scn.nextLine());
                //demo = scn.nextLine();

                if (demo.contains(list[0])) {
                    result = list[0];
                    //demo = scn.nextLine();
                    
                    while (true) {
                        demo = scn.nextLine();
                        //System.out.println(demo);

                        if (demo.contains("///")) {
                            break;
                        }

                        String[] temp = demo.split(", ");
                        String name = temp[0];
                        String quantity = temp[1];
                        quantity = quantity.trim();
                        int quan = Integer.parseInt(quantity);
                        String base = temp[2];
                        Inventory newitem = new Inventory(name, quan, base);
                        vegBurger.add(newitem);
                    }
                    demo = scn.nextLine();
                    Recipe newRecipe = new Recipe(result, vegBurger);
                    recipeList.add(newRecipe);
                }
                if (demo.contains(list[1])) {
                    result = list[1];
                    while (true) {
                        demo = scn.nextLine();

                        if (demo.contains("///")) {
                            break;
                        }

                        String[] temp = demo.split(", ");
                        String name = temp[0];
                        String quantity = temp[1];
                        quantity = quantity.trim();
                        int quan = Integer.parseInt(quantity);
                        String base = temp[2];
                        Inventory newitem = new Inventory(name, quan, base);
                        chickenPizza.add(newitem);
                    }
                    demo = scn.nextLine();
                    //System.out.println(demo);
                    Recipe newRecipe = new Recipe(result, chickenPizza);
                    recipeList.add(newRecipe);
                }
                if (demo.contains(list[2])) {
                    result = list[2];
                    while (true) {
                        demo = scn.nextLine();

                        if (demo.contains("///")) {
                            break;
                        }

                        String[] temp = demo.split(", ");
                        String name = temp[0];
                        String quantity = temp[1];
                        quantity = quantity.trim();
                        int quan = Integer.parseInt(quantity);
                        String base = temp[2];
                        Inventory newitem = new Inventory(name, quan, base);
                        espresso.add(newitem);
                    }
                    demo = scn.nextLine();
                    Recipe newRecipe = new Recipe(result, espresso);
                    recipeList.add(newRecipe);
                }
                if (demo.contains(list[3])) {
                    result = list[3];
                    while (true) {
                        demo = scn.nextLine();

                        if (demo.contains("///")) {
                            break;
                        }

                        String[] temp = demo.split(", ");
                        String name = temp[0];
                        String quantity = temp[1];
                        quantity = quantity.trim();
                        int quan = Integer.parseInt(quantity);
                        String base = temp[2];
                        Inventory newitem = new Inventory(name, quan, base);
                        hotCocoa.add(newitem);
                    }
                    //demo = scn.nextLine();
                    Recipe newRecipe = new Recipe(result, hotCocoa);
                    recipeList.add(newRecipe);
                }
            }

            /* 
            for (Recipe page:recipeList) {
                System.out.println(page);
            }*/
            
            scn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return foodName;
    }

    public String getFoodName(String name) {
        String result = "";

        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getName().equals(name)) {
                result = recipeList.get(i).getName();
            }

        }
        return result;
    }

    public void setName(String name) {
        foodName = name;
    }

    public ArrayList<Inventory> getRecipe() {
        return recipe;
    }

    public ArrayList<Inventory> getBurgerList() {
        return vegBurger;
    }

    public ArrayList<Inventory> getPizzaList() {
        return chickenPizza;
    }

    public ArrayList<Inventory> getEspressoList() {
        return espresso;
    }

    public ArrayList<Inventory> getHotCocoaList() {
        return hotCocoa;
    }

    public void setRecipe(ArrayList<Inventory> list) {
        recipe = list;
    }
    
    public Recipe getFoodRecipe(String name) {
        Recipe result = new Recipe();
        for (int i = 0; i < recipeList.size(); i++) {
            if ((recipeList.get(i).getName().toLowerCase()).contains(name.toLowerCase())) {
                //System.out.println(recipeList.get(i));
                result.setName(recipeList.get(i).getName());
                result.setRecipe(recipeList.get(i).getRecipe());
            }
        }
        //System.out.println(result + "booyah");
        return result;
    }

    public void updateInventory(String recipe) throws NullPointerException{
        //int index = itemList.indexOf(itemName);
        //itemCount.set(index, (itemCount.get(index) - orderNum));

        ArrayList<Inventory> temp = new ArrayList<Inventory>();
        //String nameOfFood = stuff.getFoodName(itemName);
        if (recipe.toLowerCase().contains("burger")) {
            temp = vegBurger;
        }
        if (recipe.toLowerCase().contains("pizza")) {
            temp = chickenPizza;
        }
        if (recipe.toLowerCase().contains("espresso")) {
            temp = espresso;
        }
        if (recipe.toLowerCase().contains("cocoa")) {
            temp = hotCocoa;
        }

        //temp = stuff.getRecipe();
        //System.out.println(temp);

        itemList = subInventory(temp);
        //System.out.println(itemList + "booyah");
        
        /*
        for (int i = 0; i < itemList.size(); i++) {
            for (int j = 0; j < temp.size(); j++) {
                if ((itemList.get(i).getItemName()).equals(temp.get(j).getItemName())) {
                    //Inventory rank = itemList.get(i).subtractItemCount(temp.get(j).getItemCount());
                    itemList.set(i, subInventory(temp));
                    System.out.println(itemList.get(i).subtractItemCount(temp.get(j).getItemCount()));
                    //]itemList.get(i).subtractItemCount(temp.get(j).getItemCount());
                }
                //System.out.println(this.itemList.get(i));
            }
        }*/

        // System.out.println("Inventory Count Updated!");
    }

    public String toString() {
        String result = "Recipe name: " + foodName + "\nIngredients:\n";
        for (int i = 0; i < recipe.size(); i++) {
            String strAmount = Integer.toString(recipe.get(i).getItemCount());
            result += itemName + ", " + strAmount + "," + unit + "\n";
        }
        return result;
    }
}

class Main {
    public static void main(String[] args) throws IOException{
        
        Scanner scn = new Scanner(System.in);
        
        Order odel = new Order();
        boolean king = true;

        while(king) {
            odel.order(scn);
        }
        
    }
}	
