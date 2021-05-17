package Interface;

import Models.Product;
import Models.Sale;
import Models.SaleItem;
import java.time.LocalDate;
import java.util.List;

public interface Imarketable {
    List<Sale> sales();


    void addSale(long id, List<SaleItem> saleItems, double sum);

    List<Sale> returnedSalesInOneDay(LocalDate onTime);

    List<Sale> showSalesBetweenTwoDate(LocalDate from, LocalDate to);

    List<Sale> showSalesBetweenTwoAmount(double fromAmount,double toAmount);

    void returnOneProductFromSale(Sale findSale,List<SaleItem> saleItem,long salesItemId,int cnt);

    Sale getSalesWithId(long id);

    List<Product> showProductsByCategoryName(String name);

    void addProduct(long id, String name,String categories, int count, double price,long code);

    void removeProduct(long code);

    Product editProduct(long id, String name,String categories, int count, double price,long code);

    List<Product> showAllProducts();

    Sale removeSale(long id);

    List<Product> searchByName(String name);
}
