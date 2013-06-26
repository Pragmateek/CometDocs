package org.cometdocs;

/**
 * Represents a CometDocs file category.
 */
public class Category
{
	/**
	 * The numerical ID of the category.
	 * e.g. 1 for the "Art" category.
	 */
	private int id;
	
	/**
	 * {@link Category#id}
	 */
	public int getId() { return id; }
	
	/**
	 * {@link Category#id}
	 */
	public void setId(int id) { this.id = id; }
	
	/**
	 * The name of the category.
	 * e.g. "Art" for the "Art" category.
	 */
	private String name;
	
	/**
	 * {@link Category#name}
	 */
	public String getName() { return name; }
	
	/**
	 * {@link Category#name}
	 */
	public void setName(String name) { this.name = name; }
	
	/**
	 * The sub-categories of the current category.
	 * e.g. "Comics", "Drawings", "Photos" for the "Art" category.
	 */
	private Category[] subCategories;
	
	/**
	 * {@link Category#subCategories}
	 */
	public Category[] getSubCategories() { return subCategories; }
	
	/**
	 * {@link Category#subCategories}
	 */
	public void setSubCategories(Category[] subCategories) { this.subCategories = subCategories; }
}
