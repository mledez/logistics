package logistics.item;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlDataException;
import logistics.loaders.ItemLoader;

public class ItemManager {
	private static ItemManager ourInstance = new ItemManager();

	public static ItemManager getInstance() {
		return ourInstance;
	}

	private ItemManager() {}

	private List<Item> catalog;
	private Map<String, Integer> mappedCatalog = null;
	private boolean status = false;

	public boolean getStatus() {
		return this.status;
	}

	private Map<String, Integer> getMappedCatalog() throws InitializationException, InvalidDataException {
		if (!getStatus())
			throw new InitializationException("Item Manager not initialized");
		if (this.mappedCatalog == null) {
			TreeMap<String, Integer> mappedCatalog = new TreeMap<>();
			for (Item item : getCatalog())
				mappedCatalog.put(item.getId(), item.getPrice());

			setMappedCatalog(mappedCatalog);
		}
		return this.mappedCatalog;
	}

	public void init(String fileName) throws XmlDataException, InvalidDataException {
		setCatalog(ItemLoader.load(fileName));
		setStatus(true);
	}

	public String getReport() throws InitializationException, InvalidDataException {
		if (!getStatus())
			throw new InitializationException("Item Manager not initialized");
		int lineLimit = 86;
		String report = "   ";
		String newEntry;
		int lineCounter = 0;
		for (String item : getMappedCatalog().keySet()) {
			newEntry = String.format("%-8s: $%,-8d ", item, getMappedCatalog().get(item));
			if ((report + newEntry).length() - lineLimit * lineCounter <= lineLimit)
				report = report + newEntry;
			else {
				report = report + "\n   " + newEntry;
				lineCounter++;
			}
		}
		if (report.equals("   "))
			return "   No items found";
		else
			return report + "\n";
	}

	private List<Item> getCatalog() {
		return catalog;
	}

	private void setCatalog(List<Item> catalog) throws InvalidDataException {
		if (catalog == null)
			throw new InvalidDataException("Facility Catalog can't be null");
		this.catalog = catalog;
	}

	private void setMappedCatalog(Map<String, Integer> mappedCatalog) throws InvalidDataException {
		if (mappedCatalog == null)
			throw new InvalidDataException("Mapped Catalog can't be null");
		this.mappedCatalog = mappedCatalog;
	}

	private void setStatus(boolean status) {
		this.status = status;
	}

}
