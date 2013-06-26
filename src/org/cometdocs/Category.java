package org.cometdocs;

public class Category
{
	private int id;
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	private String name;
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	private Category[] subCategories;
	public Category[] getSubCategories() { return subCategories; }
	public void setSubCategories(Category[] subCategories) { this.subCategories = subCategories; }
}
