package logistics.item;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import logistics.exceptions.DuplicatedDataException;
import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;
import logistics.exceptions.XmlReadingException;
import logistics.loaders.ItemLoader;

public class ItemManager {
	private static ItemManager ourInstance = new ItemManager();
	private List<Item> catalog;
	private Map<String, Integer> mappedCatalog = null;
	private boolean status = false;

	private ItemManager() {}

	private Map<String, Integer> getMappedCatalog() throws InitializationException, InvalidDataException {
		checkStatus();
		if (this.mappedCatalog == null) {
			TreeMap<String, Integer> mappedCatalog = new TreeMap<>();
			for (Item item : getCatalog())
				mappedCatalog.put(item.getId(), item.getPrice());

			setMappedCatalog(mappedCatalog);
		}
		return this.mappedCatalog;
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

	private Item getItem(String id) throws InvalidDataException {
		for (Item item : getCatalog()) {
			if (item.getId().equals(id))
				return item;
		}
		throw new InvalidDataException(String.format("Item %s not found in the Item Manager", id));
	}

	private void setStatus(boolean status) {
		this.status = status;
	}

	private void checkStatus() throws InitializationException {
		if (!status)
			throw new InitializationException("Item manager is not initialized");
	}

	public static ItemManager getInstance() {
		return ourInstance;
	}

	public void init(String fileName) throws XmlReadingException, InvalidDataException, DuplicatedDataException {
		setCatalog(ItemLoader.load(fileName));
		setStatus(true);
	}

	public String getReport() throws InitializationException, InvalidDataException {
		checkStatus();
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

	public boolean contains(String item) throws InitializationException, InvalidDataException {
		return getMappedCatalog().containsKey(item);
	}

	public int getPrice(String id) throws InitializationException, InvalidDataException {
		checkStatus();
		return getItem(id).getPrice();
	}

}
