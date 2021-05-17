package Models;
import Services.StoreProcess;
import lombok.Data;
import java.util.List;
import java.util.Scanner;

@Data
public class Product extends BaseEntity{

    private long id;

    private String name;

    private String categoryName;

    private int count;

    private double price;

    private long code;

    StoreProcess sp = new StoreProcess();
    Scanner sc = new Scanner(System.in);

    public enum Categories {
        DRINK,
        DAIRY_PRODUCTS,
        MEAT,
        FLOUR_PRODUCTS,
        SWEETS,
        OTHERS,
    }

    public Product(){

    }

    public Product(long id, String name, String categoryName, int count, double price, long code) {
        this.id = id;
        this.name = name;
        this.categoryName = categoryName;
        this.count = count;
        this.price = price;
        this.code = code;
    }

    private int checkSelectOption(){
        String option = "";
        while (true){
            option = sc.next();
            if (sp.checkWithMatcher(option)){
                break;
            }
            else {
                System.out.println("Enter only digits!!!");
                System.out.print("Enter number: " );
            }
        }
        return Integer.parseInt(option);
    }

    private void getProductsByName() {
        System.out.print("Enter product name:");
        String name = sc.next();
        List<Product> products =  sp.searchByName(name);
        if (products==null){
            getProductsByName();
        }
        else {
            showProductsInfo(products);
        }
        selectedOptionOnProduct();
    }

    private void getProductsByCategory() {
        System.out.print("Select the category: \n1. "+Categories.DRINK+"\n2. "+Categories.MEAT+"\n3. "+Categories.DAIRY_PRODUCTS+"\n4. "+Categories.FLOUR_PRODUCTS+"\n5. "+Categories.SWEETS+"\n");
        String number=selectedCategory(checkCategory());
        categoryName = number;
        List<Product> products = sp.showProductsByCategoryName(categoryName);
        if (products==null){
            getProductsByCategory();
        }
        else {
            showProductsInfo(products);
        }
        selectedOptionOnProduct();
    }

    private void getAllProduct() {
        List<Product> allproducts = sp.showAllProducts();
        System.out.println("-----All Products-------");
        showProductsInfo(allproducts);
        selectedOptionOnProduct();
    }

    public void createProduct(){
        System.out.print("Enter code of product: " );
        long QRcode = sp.checkQR(true);
        System.out.println("Name of Product: ");
        String name = sc.next();
        System.out.println("Select the category: \n1. "+Categories.DRINK+"\n2. "+Categories.MEAT+"\n3. "+Categories.DAIRY_PRODUCTS+"\n4. "+Categories.FLOUR_PRODUCTS+"\n5. "+Categories.SWEETS);
        int category = checkCategory();
        String categoryName=selectedCategory(category);
        System.out.print("Enter count of product: " );
        int countOfNew =  checkCount();
        System.out.print("Enter price of product: " );
        double priceOfNew = checkPrice();
        sp.addProduct(QRcode,name,categoryName,countOfNew,priceOfNew,QRcode);
        System.out.println("Product was added");
        System.out.println("Do you want to add another new product");
        sp.askForTrueFalseOption();
        int select = checkTrueOrFalse();
        if (select==1){
            createProduct();
        }
        else {
            selectedOptionOnProduct();
        }
    }

    Product findProduct;

    public void editProduct(){
        System.out.print("Enter code of product: " );
        long Qr = sp.checkQR(false);
        findProduct = sp.getProductWithId(Qr);
        if (findProduct==null){
            editProduct();
        }
        else{
            int isTrue;
            infoProduct(findProduct);
            System.out.println("Do you want to change NAME OF PRODUCT?");
            sp.askForTrueFalseOption();
            isTrue = checkTrueOrFalse();
            if (isTrue==1){
                System.out.print("Enter name of product: " );
                this.name = sc.next();
            }
            System.out.println("Do you want to change CATEGORY OF PRODUCT?");
            sp.askForTrueFalseOption();
            isTrue = checkTrueOrFalse();
            if (isTrue==1){
                System.out.println("Select the category: \n1. "+Categories.DRINK+"\n2. "+Categories.MEAT+"\n3. "+Categories.DAIRY_PRODUCTS+"\n4. "+Categories.FLOUR_PRODUCTS+"\n5. "+Categories.SWEETS);
                this.categoryName = selectedCategory(checkCategory());
            }
            System.out.println("Do you want to change COUNT OF PRODUCT?");
            sp.askForTrueFalseOption();
            isTrue = checkTrueOrFalse();
            if (isTrue==1){
                System.out.print("Enter count of product: " );
                this.count = checkCount();
            }
            System.out.println("Do you want to change PRICE OF PRODUCT? ");
            sp.askForTrueFalseOption();
            isTrue = checkTrueOrFalse();
            if(isTrue==1){
                System.out.print("Enter price of product: " );
                this.price = checkPrice();
            }
            StoreProcess st = new StoreProcess();
            Product edited = st.editProduct(findProduct.getCode(),this.name,categoryName,count,price, findProduct.getCode());
            infoProduct(edited);
            System.out.println("Do you want to edit another product?");
            sp.askForTrueFalseOption();
            isTrue = checkTrueOrFalse();
            if(isTrue==1){
                editProduct();
            }
            else {
                selectedOptionOnProduct();
            }
        }
    }

    public  void removeProduct(){
        System.out.print("Enter code of product: " );
        this.code = sp.checkQR(false);
        findProduct = sp.getProductWithId(code);
        if (findProduct!=null){
            infoProduct(findProduct);
            sp.removeProduct(code);
            System.out.println("Product was deleted");
            while (true){
                System.out.println("Do you want to remove another product?");
                sp.askForTrueFalseOption();
                int enterOption = checkTrueOrFalse();
                if (enterOption==1){
                    removeProduct();
                    break;
                }
                else {
                    selectedOptionOnProduct();
                    System.out.println("Please, select correct option!!");
                }
            }
        }
        else {
            removeProduct();
        }
    }

    public String selectedCategory(int category){
        switch (category){
            case 1:
                return Categories.DRINK.toString();
            case 2:
                return Categories.MEAT.toString();
            case 3:
                return Categories.DAIRY_PRODUCTS.toString();
            case 4:
                return Categories.FLOUR_PRODUCTS.toString();
            case 5:
                return Categories.SWEETS.toString();
            default:
                return Categories.OTHERS.toString();
        }
    }

    private void infoProduct(Product findProduct){
        this.id = findProduct.getId();
        this.name = findProduct.getName();
        this.categoryName = findProduct.getCategoryName();
        this.code = findProduct.getCode();
        this.count = findProduct.getCount();
        this.price = findProduct.getPrice();
        System.out.println("---Code of product: "+findProduct.getCount()+"---");
        System.out.println("Name of the product: "+findProduct.getName());
        System.out.println("Category: "+findProduct.getCategoryName());
        System.out.println("Count of product: "+findProduct.getCount());
        System.out.println("Price of product: "+findProduct.getPrice());
    }

    private void showProductsInfo(List<Product> allproducts){
        allproducts.forEach(w->{
            System.out.println(
                    "1. Code: "+w.getCode()+"\n" +
                            "2. Name: "+w.getName()+"\n" +
                            "3. Category: "+w.getCategoryName()+"\n" +
                            "4. Count: "+w.getCount()+"\n" +
                            "5. Price: "+w.getPrice()
            );
            System.out.println("------------------------");
        });
    }


    private int checkCount(){
        String cnt = "";
        while (true){
            cnt = sc.next();
            if (sp.checkWithMatcher(cnt)){
                break;
            }
            else {
                System.out.println("Count only should consist of numbers!!");
                System.out.print("Enter count of product: " );
            }
        }
        return Integer.parseInt(cnt);
    }

    private double checkPrice(){
        String prc="";
        while (true){
            prc=sc.next();
            if (sp.checkWithMatcher(prc)){
                break;
            }
            else {
                System.out.println("Price only should consist of numbers!!");
                System.out.print("Enter price of product: " );
            }
        }
        return Double.parseDouble(prc);
    }

    private int checkCategory(){
        String ctgr="";
        while (true){
            ctgr=sc.next();
            if (sp.checkWithMatcher(ctgr)){
                if (ctgr.length()>Categories.values().length){
                    System.out.print("Select correct category of product: " );
                }
                else {
                    break;
                }
            }
            else {
                System.out.println("Should be number!!");
                System.out.print("Select correct category of product: " );
            }
        }
        return Integer.parseInt(ctgr);
    }

    private int checkTrueOrFalse(){
        String op="";
        while (true){
            op = sc.next();
            if (sp.checkWithMatcher(op)){
                switch (Integer.parseInt(op)){
                    case 1:
                        return 1;
                    case 2:
                        return 0;
                    default:
                        checkTrueOrFalse();
                        System.out.print("Select correct option: ");
                }
            }
            else {
                System.out.println("Select correct option!! ");
            }
        }
    }

    public  void selectedOptionOnProduct(){
        System.out.println("1. Create new product");
        System.out.println("2. Edit product");
        System.out.println("3. Delete product");
        System.out.println("4. Show all products");
        System.out.println("5. Search product based on Category");
        System.out.println("6. Search products based on name");
        System.out.println("7. Back to Main Menu");
        System.out.println("Please select one of these options!!");
        int option = checkSelectOption();
        switch (option){
            case 1:
                createProduct();
                break;
            case 2:
                editProduct();
                break;
            case 3:
                removeProduct();
                break;
            case 4:
                getAllProduct();
                break;
            case 5:
                getProductsByCategory();
                break;
            case 6:
                getProductsByName();
                break;
            case 7:
                sp.backToMainMenu();
            default:
                selectedOptionOnProduct();
                System.out.println("Please, choose correct option!!");
        }
    }

}
