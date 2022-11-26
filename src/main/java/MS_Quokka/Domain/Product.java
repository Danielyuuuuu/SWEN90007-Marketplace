package MS_Quokka.Domain;

import MS_Quokka.Mapper.ProductMapper;

import java.util.UUID;

public class Product extends DomainObject {

    private Category category;
    private String brand;
    private String name;

    public Product(String id) {
        super(id);
        category = null;
        brand = null;
        name = null;
    }

    public Product(Category category, String brand, String name){
        super(UUID.randomUUID().toString());
        this.category = category;
        this.brand = brand;
        this.name = name;
    }

    public Product(String id, Category category, String brand, String name) {
        super(id);
        this.category = category;
        this.brand = brand;
        this.name = name;
    }

    private void reload() {
        Product fullProduct = new ProductMapper().readOne(getId());
        category = fullProduct.getCategory();
        brand = fullProduct.getBrand();
        name = fullProduct.getName();
    }

    public Category getCategory() {
        if (category == null) {
            reload();
        }
        return category;
    }

    public String getBrand() {
        if (brand == null) {
            reload();
        }
        return brand;
    }

    public String getName() {
        if (name == null) {
            reload();
        }
        return name;
    }

    public String toString() {return "Product: " + getId() + " " + category.toString() + " " + brand + " " + name + " |";}

}
