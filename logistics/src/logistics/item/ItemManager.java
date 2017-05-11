package logistics.item;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import logistics.exceptions.InitializationException;
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

	private Map<String, Integer> getMappedCatalog() throws InitializationException {
		if (!getStatus())
			throw new InitializationException("Item Manager not initialized");
		if (this.mappedCatalog == null) {
			TreeMap<String, Integer> mappedCatalog = new TreeMap<>();
			for (Item item : this.catalog)
				mappedCatalog.put(item.getId(), item.getPrice());

			this.mappedCatalog = mappedCatalog;
		}
		return this.mappedCatalog;
	}

	public void init(String fileName) throws XmlDataException {
		this.catalog = ItemLoader.load(fileName);
		this.status = true;
	}

	public String getReport() throws InitializationException {
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

}
