package Models;
import Services.StoreProcess;
import lombok.Data;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Data
public class Sale extends BaseEntity{

    private double sum;

    private List<SaleItem> saleItems;

    public Sale() {
    }

    public Sale(double sum, List<SaleItem> saleItems) {
        this.sum = sum;
        this.saleItems = saleItems;
    }

    Scanner sc = new Scanner(System.in);
    StoreProcess sp = new StoreProcess();

    private void addNewSale(){
        double sum = 0;
        List<SaleItem> saleItems = new ArrayList<>();
        boolean hasNew = true;
        while (hasNew){
            System.out.println("Add product ID");
            long productCode = sp.checkQR(false);
            Product product =  sp.getProductWithId(productCode);
            if (product!=null){
                double priceOfProduct = product.getPrice();
                System.out.print("Enter the count: ");
                int countOfProduct = sp.checkCount();
                if (countOfProduct==0){
                    countOfProduct=1;
                }
                else if(countOfProduct>product.getCount()){
                    countOfProduct=1;
                }
                long saleItemId = randomSaleId();
                SaleItem newSaleItem = new SaleItem(saleItemId,product,priceOfProduct,countOfProduct);
                newSaleItem.setCreatedDate(LocalDate.now());
                sum = sum+(priceOfProduct*countOfProduct);
                saleItems.add(newSaleItem);
                product.setCount(product.getCount()-countOfProduct);
                boolean check = true;
                while (check){
                    System.out.println("Do you want to add other products? ");
                    sp.askForTrueFalseOption();
                    int option = sp.checkTrueOrFalse();
                    if (option==1){
                        check=false;
                        hasNew = true;
                    }
                    else if(option==2){
                        check = false;
                        hasNew = false;
                    }
                    else {
                        check = true;
                        System.out.println("Please, choose correct option");
                    }
                }
            }
            else {
                addNewSale();
                hasNew = true;
            }
        }
        long saleId = randomSaleId();
        System.out.println("create saleId +  : "+saleId);
        sp.addSale(saleId,saleItems,sum);
        selectedOptionOnSale();
    }

    private long randomSaleId(){
        long random = (long) Math.floor(Math.random()*10000);
        Sale findSale =  sp.getSalesWithId(random);
        if (findSale==null){
           return random;
        }
        else {
            randomSaleId();
        }
        return random;
    }

    private void showAllSales(){
        List<Sale> sales = sp.sales();
        showSalesInfo(sales);
        selectedOptionOnSale();
    }

    private void showSalesBetweenTwoDate() {
        while (true){
            try {
                System.out.print("Enter date from: (dd.MM.yyyy)");
                String fromDate = sc.next();
                System.out.print("Enter date to: (dd.MM.yyyy)");
                String toDate = sc.next();
                LocalDate from = parseStringToLocalDate(fromDate);
                LocalDate to = parseStringToLocalDate(toDate);
                List<Sale> sales = sp.showSalesBetweenTwoDate(from,to);
                if (sales==null){
                    System.out.println("There is no any sales on this date!!");
                }
                else {
                    showSalesInfo(sales);
                    selectedOptionOnSale();
                }
            }
            catch (Exception ex){
                System.out.println("Please, enter TIME based on the format");
            }
        }
    }

    private void showSalesOnDate() {
        while (true){
            try{
                System.out.print("Enter date : (dd.MM.yyyy)");
                String onTime = sc.next();
                LocalDate lDate = parseStringToLocalDate(onTime);
                List<Sale> sales =  sp.returnedSalesInOneDay(lDate);
                if (sales==null){
                    System.out.println("There is no any sales on this date!!");
                }
                else {
                    showSalesInfo(sales);
                    selectedOptionOnSale();
                }
            }
            catch (Exception ex){
                System.out.println("Please, enter TIME based on the format");
            }
        }
    }

    private void showSaleInfo(){
        System.out.print("Enter Sale ID: ");
        long num = checkSaleId(false);
        Sale sale = sp.getSalesWithId(num);
        if (sale!=null){
            System.out.println(
                    "-----------------------------------"+
                    "\n1. Sales ID: "+sale.getId()+"\n" +
                            "2. Count Of Product: "+sale.getSaleItems().size()+"\n" +
                            "3. Sum: "+sale.getSum()+"\n"+
                            "4. Sale date: "+sale.getCreatedDate()+"\n"
                    +"------------------------------------"
            );
            List<SaleItem> items = sale.getSaleItems().stream().filter(w->w.getDeletedDate()==null).collect(Collectors.toList());
            items.forEach(p->{
                System.out.println("------------------------------\nSaled Item\n5.1 ID of Saled Item: "+p.getId()+"\n"+
                        "5.2 Price of Saled Item: "+p.getPrice()+"\n"+"5.3 Count of Saled Item: " +p.getCount()
                );

                System.out.println("Product\n5.4 ID of Product: "+p.getProduct().getId()+"\n"+
                        "5.5 Name of Product: "+p.getProduct().getName()+"\n"+"5.6 Count of products: "+p.getProduct().getCount()+
                        "\n5.7. Price of product: "+p.getProduct().getPrice()+"\n-----------------------------"
                );

            });
        }
        else {
            showSaleInfo();
        }
        selectedOptionOnSale();
    }

    private void showSalesBetweenTwoAmount(){
        System.out.print("Enter from amount: ");
        double fromAmount = sp.checkPrice();
        System.out.print("Enter to amount: ");
        double toAmount = sp.checkPrice();
        List<Sale> sales = sp.showSalesBetweenTwoAmount(fromAmount,toAmount);
        if (sales!=null){
            showSalesInfo(sales);
        }
        selectedOptionOnSale();
    }

    private void removeSale(){
        while (true){
            System.out.print("Enter sale id: ");
            long salesId = checkSaleId(false);
            Sale sale =  sp.removeSale(salesId);
            if (sale!=null){
                System.out.println("Sale was deleted!!");
                break;
            }
            break;
        }
        selectedOptionOnSale();
    }

    public  void selectedOptionOnSale(){
        System.out.println("1. Create new sale");
        System.out.println("2. Return some products");
        System.out.println("3. Delete sale");
        System.out.println("4. Show all sales");
        System.out.println("5. Show sales between two date");
        System.out.println("6. Show sales between two amount");
        System.out.println("7. Show sales on specific day");
        System.out.println("8. Show all information about sales");
        System.out.println("9. Show sales between two amount");
        System.out.println("10. Back to Main Menu");
        System.out.println("Please select one of these options!!");
        System.out.print("Enter number: " );
        int option = sp.checkTrueOrFalse();
        switch (option){
            case 1:
                addNewSale();
                break;
            case 2:
                returnOneProductFromSale();
                break;
            case 3:
                removeSale();
                break;
            case 4:
                showAllSales();
                break;
            case 5:
                showSalesBetweenTwoDate();
                break;
            case 6:
                showSalesBetweenTwoAmount();
                break;
            case 7:
                showSalesOnDate();
                break;
            case 8:
                showSaleInfo();
                break;
            case 10:
                sp.backToMainMenu();
            default:
                selectedOptionOnSale();
                System.out.println("Please, choose correct option!!");
        }
    }

    private void returnOneProductFromSale() {
        System.out.print("Enter Sale ID: " );
        long saleId = checkSaleId(true);
        Sale findSale = sp.getSalesWithId(saleId);
        if (findSale!=null){
            System.out.print("Enter SaleItem ID: " );
            long saleItemCode = sp.checkTrueOrFalse();
            System.out.print("Enter Count of Product: " );
            int cnts = sp.checkCount();
            List<SaleItem> saleItems = findSale.getSaleItems();
            sp.returnOneProductFromSale(findSale,saleItems,saleItemCode,cnts);
            selectedOptionOnSale();
        }
        else {
            returnOneProductFromSale();
        }
    }

    private void showSalesInfo(List<Sale> allSales){
        allSales.stream().filter(w->w.getDeletedDate()==null).forEach(w->{
            System.out.println(
                    "1. Sales ID: "+w.getId()+"\n" +
                            "2. Count Of Product: "+w.getSaleItems().size()+"\n" +
                            "3. Sum: "+w.getSum()+"\n"+
                            "4. Date: "+w.getCreatedDate()
            );
            System.out.println("------------------------");
        });
    }

    private LocalDate parseStringToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localdate = LocalDate.parse(date,formatter);
        return localdate;
    }

    private long checkSaleId(boolean isCheckedForExist){
        String saleId = "";
        while (true){
            saleId = sc.next();
            if (sp.checkWithMatcher(saleId)){
                if (isCheckedForExist==true){
                    Sale s =  sp.getSalesWithId(Long.parseLong(saleId));
                    if(s!=null){
                        break;
                    }
                    else {
                        System.out.println("There is no any sale with this ID");
                        break;
                    }
                }
                else {
                    break;
                }
            }
            else {
                System.out.println("Sale ID should consist of numbers");
                System.out.print("Enter Sale ID: " );
            }
        }
        return Long.parseLong(saleId);
    }

}
