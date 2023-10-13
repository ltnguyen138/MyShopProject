package com.myshop.common.entity.product;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.myshop.common.entity.Brand;
import com.myshop.common.entity.Category;

@Entity
@Table(name="products")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true,length = 255, nullable = false)
	private String name;
	
	@Column(unique = true,length = 255, nullable = false)
	private String alias;
	
	@Column(length = 256, nullable = false, name="short_description")
	private String shortDescription;
	
	@Column(length = 3072, nullable = false, name="full_description")
	private String fullDescription;
	
	@Column(name="created_time")
	private Date createdTime;
	
	@Column(name="updated_time")
	private Date updatedTime;
	
	private boolean enabled;
	
	@Column(nullable = false)
	private Integer quantity;
	
	private String mainImage;
	
	private double cost;
	
	private double price;
	
	@Column(name="discount_percent")
	private float discountPercent;
	
	private float length;
	private float width;
	private float height;
	private float weight;

	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name="brand_id")
	private Brand brand;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductImage> images=new HashSet<>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductDetail> details=new HashSet<>();
	
	
	
	public Product(String name) {
		this.name = name;
	}

	public Product(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date date) {
		this.createdTime = date;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	
	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	
	public Set<ProductImage> getImages() {
		return images;
	}

	public void setImages(Set<ProductImage> images) {
		this.images = images;
	}
	
	public void addExtraImage(String imageName) {
		this.images.add(new ProductImage(imageName,this)	);
	}
	

	public Set<ProductDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<ProductDetail> details) {
		this.details = details;
	}

	public void addDetails(String name, String value) {
		this.details.add(new ProductDetail(name, value,this));
	}
	public void addDetails(Integer id,String name, String value) {
		this.details.add(new ProductDetail(id,name, value,this));
	}
	public Product() {
		
	}
	@Transient
	public String mainImagePath() {
		if (id == null || mainImage == null) return "/images/download.png";
		return "/product-images/"+this.id+"/"+this.mainImage;
	}
	
	public boolean containsImageName(String name) {
		Iterator<ProductImage> iterator=images.iterator();
		while(iterator.hasNext()) {
			ProductImage image=iterator.next();
			if(image.getName().equals(name)	) {
				return true;
			}
		}
		return false;
	}
	
	
	
	@Transient
	public double getDiscountPrice() {
		if (discountPercent > 0) {
			return Math.round((price * ((100 - discountPercent) / 100))/1000)*1000 ;
		}
		return this.price;
	}
	

	@Transient
	public double getDiscountValue() {
		if (discountPercent > 0) {
			return price -  getDiscountPrice();
		}
		return 0;
	}
	
	@Transient
	public String getShortName() {
		
			if(name.length()>50) {
				return name.substring(0, 50).concat("..");

		}
		
		return this.name;
	}
}
