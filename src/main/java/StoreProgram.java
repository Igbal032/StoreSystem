import Models.Product;
import Models.Sale;
import Models.SaleItem;
import Services.StoreProcess;

import java.security.PublicKey;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreProgram {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StoreProcess sp = new StoreProcess();
        Product newPr = new Product(12345,"Milla Main Ayin 15","DRINK",10,15,12345);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localdate = LocalDate.parse("15.05.2021",formatter);
        newPr.setCreatedDate(localdate);
        Product newPr2 = new Product(123456,"Alma 2","MEAT",85,120,123456);
        LocalDate localdate2 = LocalDate.parse("14.05.2021",formatter);
        newPr.setCreatedDate(localdate2);
        Product newPr3 = new Product(1234567,"Toyuq","MEAT",25,100,1234567);
        newPr3.setCreatedDate(LocalDate.now());
        Product newPr4 = new Product(1234568,"Toyuq 2","MEAT",25,100,1234568);
        newPr4.setCreatedDate(LocalDate.now());
        List<SaleItem> pr = new ArrayList<>();
        SaleItem s = new SaleItem(2132153,newPr,25,5);
        SaleItem s2 = new SaleItem(45855,newPr2,9,2);
        pr.add(s);
        pr.add(s2);
        Sale newSale = new Sale(250,pr);
        LocalDate loca22 = LocalDate.parse("14.05.2021",formatter);
        LocalDate loca232 = LocalDate.parse("16.05.2021",formatter);
        newSale.setId(54545);
        newSale.setCreatedDate(loca22);
        Sale newSale2 = new Sale(180,pr);
        Sale newSa3le2 = new Sale(600,pr);
        newSale2.setId(45456465);
        newSa3le2.setId(54548455);
        newSa3le2.setCreatedDate(loca232);
        newSale2.setCreatedDate(loca22);
        sp.getRemainProducts().add(newPr);
        sp.getRemainProducts().add(newPr2);
        sp.getRemainProducts().add(newPr3);
        sp.getRemainProducts().add(newPr4);
        sp.sales().add(newSale);
        sp.sales().add(newSale2);
        sp.sales().add(newSa3le2);
        mainMenu();
    }

    public static void mainMenu(){
        StoreProcess sp = new StoreProcess();
        Scanner sc = new Scanner(System.in);
        sp.backToMainMenu();
        String selectedOption="";

    }
}
