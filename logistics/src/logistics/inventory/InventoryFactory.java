package logistics.inventory;

import logistics.exceptions.ClassInstantiationException;

public class InventoryFactory {
	private InventoryFactory() {}

	public static Inventory createInventory(String implName) throws ClassInstantiationException {
		try {
			return (Inventory) Class.forName(implName).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new ClassInstantiationException("There was a problem getting a new instance of " + implName);
		}
	}
}
