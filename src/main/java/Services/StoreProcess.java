package Services;
import Interface.Imarketable;
import Models.Product;
import Models.Sale;
import Models.SaleItem;
import com.sun.tools.javac.Main;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StoreProcess implements Imarketable {

    private static List<Sale> allSales = new ArrayList<>();
    private static List<Product> allSalesProducts = new ArrayList<>();
    private static List<Product> remainProducts = new ArrayList<>();
    private static List<Sale> returnedSales = new ArrayList<>();
    Scanner sc = new Scanner(System.in);
    int saleId=1;
    public StoreProcess(){

    }
    public static List<Product> getAllSalesProducts() {
        return allSalesProducts;
    }

    public static List<Product> getRemainProducts() {
        return remainProducts;
    }
    public List<Sale> getAllSales() {
        return allSales;
    }
    @Override
    public List<Sale> sales() {
        return allSales;
    }
    @Override
    public void addSale(long id, List<SaleItem> saleItems,double sum) {
        Sale newSale = new Sale();
        newSale.setId(id);
        newSale.setSaleItems(saleItems);
        newSale.setSum(sum);
        newSale.setCreatedDate(LocalDate.now());
        sales().add(newSale);
    }

    @Override
    public List<Sale> returnedSalesInOneDay(LocalDate onTime) {
        List<Sale> sales = getAllSales().stream().filter(w->w.getDeletedDate()==null&&w.getCreatedDate().isEqual(onTime)).collect(Collectors.toList());
        if (sales.size()!=0){
            return sales;
        }
        return null;
    }

    @Override
    public List<Sale> showSalesBetweenTwoDate(LocalDate from,LocalDate to) {
        List<Sale> sales = getAllSales().stream().filter(w->w.getDeletedDate()==null
                &&w.getCreatedDate().isAfter(from.minusDays(1))
                &&w.getCreatedDate().isBefore(to.plusDays(1))).collect(Collectors.toList());
        return sales;
    }
    @Override
    public List<Sale> showSalesBetweenTwoAmount(double fromAmount, double toAmount) {
        List<Sale> sales = allSales.stream().filter(w->w.getDeletedDate()==null&&w.getSum()>fromAmount&&w.getSum()<toAmount).collect(Collectors.toList());
        if (sales.size()!=0){
            return sales;
        }
        System.out.println("--There is no any sales between two amount--");
        return null;
    }

    @Override
    public void returnOneProductFromSale(Sale findSale,List<SaleItem> saleItems,long salesItemId,int cnt) {
        int currentCount = 0;
        int prevCount = 0;
        for (SaleItem saleItem : saleItems){
            if (saleItem.getId() == salesItemId){
                Product pp = getProductWithId(saleItem.getProduct().getCode());
                pp.setCount(pp.getCount()+saleItem.getCount());
                prevCount = saleItem.getCount();
                if (saleItem.getCount()>cnt){
                    currentCount = saleItem.getCount()-cnt;
                    findSale.setSum(findSale.getSum()-(saleItem.getPrice()*prevCount)+(saleItem.getPrice()*currentCount));
                }
                else {
                    currentCount=0;
                    findSale.setSum(findSale.getSum()-saleItem.getPrice()*prevCount);
                }
                saleItem.setCount(currentCount);
                saleItem.setDeletedDate(LocalDate.now());
            }
        }

    }

    @Override
    public List<Product> showProductsByCategoryName(String categoryName) {
        List<Product> products = remainProducts.stream().filter(w->w.getDeletedDate()==null&&w.getCategoryName().equals(categoryName)).collect(Collectors.toList());
        if (products.size()==0){
            System.out.println("There is no any products based on this category!!");
            return null;
        }
        else {
            return products;
        }
    }

    @Override
    public void addProduct(long id, String name, String categories, int count, double price, long code) {
        Product newProduct = new Product(code,name,categories,count,price,code);
        newProduct.setCreatedDate(LocalDate.now());
        remainProducts.add(newProduct);
    }

    @Override
    public void removeProduct(long code) {
       Optional<Product> findProduct =  remainProducts.stream().filter(w->w.getDeletedDate()==null&&w.getCode()==code).findFirst();
       findProduct.get().setDeletedDate(LocalDate.now());
    }

    @Override
    public Product editProduct(long id, String name, String categories, int count, double price, long code) {
        Optional<Product> findProduct = remainProducts.stream().filter(w->w.getCode()==code).findFirst();
        findProduct.get().setCount(count);
        findProduct.get().setPrice(price);
        findProduct.get().setName(name);
        findProduct.get().setCategoryName(categories);
        return findProduct.get();
    }

    @Override
    public List<Product> showAllProducts() {
        List<Product> allProducts = remainProducts.stream().filter(w->w.getDeletedDate()==null).collect(Collectors.toList());
        return allProducts;
    }

    @Override
    public Sale removeSale(long sailIdd) {
        Sale findSale = getSalesWithId(sailIdd);
        if (findSale!=null){
            for(SaleItem st: findSale.getSaleItems()) {
                Product findProduct =  getProductWithId(st.getProduct().getCode());
                findProduct.setCount(findProduct.getCount()+st.getCount());
            }
            returnedSales.add(findSale);
            findSale.setDeletedDate(LocalDate.now());
            return findSale;
        }
        else {

            return null;
        }
    }

    @Override
    public List<Product> searchByName(String name) {
        List<Product> products = remainProducts.stream().filter(w->w.getName().startsWith(name)).collect(Collectors.toList());
        if (products.size()==0){
            System.out.println("There is no any products begin with this name!!");
            return null;
        }
        else {
            return products;
        }

    }

    public void backToMainMenu(){
        System.out.println("Welcome to our Store");
        System.out.println("1. Operation on product\n2. Operation on sale\n3. Log out");
        System.out.println("Please select one of these options!!");
        System.out.print("Enter number: " );
        int option2 = checkTrueOrFalse();
        switch (option2){
            case 1:
                Product p = new Product();
                p.selectedOptionOnProduct();
                break;
            case 2:
                Sale s = new Sale();
                s.selectedOptionOnSale();
            case 3:
                return;
            default:
                System.out.println("Please, enter correct option!");
                backToMainMenu();
        }
    }

    public Product getProductWithId(long enteredCode){

        Optional<Product> pr =  getRemainProducts().stream().filter(w->w.getCode()==enteredCode&&w.getDeletedDate()==null).findFirst();
        if (!pr.isEmpty()){
            return pr.get();
        }
        else {
            System.out.println("There is not any product with this code ("+enteredCode+")");
        }
        return null;
    }

    @Override
    public Sale getSalesWithId(long salesId){
        StoreProcess st = new StoreProcess();
        Optional<Sale> sl =  st.getAllSales().stream().filter(w->w.getId()==salesId&&w.getDeletedDate()==null).findFirst();
        if (!sl.isEmpty()){
            return sl.get();
        }
        else {
            System.out.println("There is not any sale with this id ("+salesId+")");
        }
        return null;
    }

    public void askForTrueFalseOption(){
        System.out.println("1. True");
        System.out.println("2. False");
        System.out.print("Select option: ");
    }

    public int checkTrueOrFalse(){
        String op="";
        while (true){
            op = sc.next();
            if (checkWithMatcher(op)){
                return Integer.parseInt(op);
            }
            else {
                System.out.println("Only Should be numbers!! ");
            }
        }
    }

    public boolean checkWithMatcher(String text){
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public long checkQR(boolean isCheckedForExist){
        String QR = "";
        while (true){
            QR = sc.next();
            if (checkWithMatcher(QR)){
                if (isCheckedForExist==true){
                    Product p =  getProductWithId(Long.parseLong(QR));
                    if(p!=null){
                        System.out.println("This product has already existed: ");
                        System.out.print("Enter code of product: " );
                    }
                    else {
                        break;
                    }
                }
                else {
                    break;
                }
            }
            else {
                System.out.println("QR code of product only should consist of numbers");
                System.out.print("Enter code of product: " );
            }
        }
        return Long.parseLong(QR);
    }

    public int checkCount(){
        String cnt = "";
        while (true){
            cnt = sc.next();
            if (checkWithMatcher(cnt)){
                break;
            }
            else {
                System.out.println("Count only should consist of numbers!!");
                System.out.print("Enter count of product: " );
            }
        }
        return Integer.parseInt(cnt);
    }

    public double checkPrice(){
        String prc="";
        while (true){
            prc=sc.next();
            if (checkWithMatcher(prc)){
                break;
            }
            else {
                System.out.println("Should be consist of numbers!!");
                System.out.print("Enter price of product: " );
            }
        }
        return Double.parseDouble(prc);
    }

}
