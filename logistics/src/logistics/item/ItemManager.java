package logistics.item;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import logistics.loaders.ItemLoader;

public class ItemManager {
	private static ItemManager ourInstance = new ItemManager();

	public static ItemManager getInstance() {
		return ourInstance;
	}

	private ItemManager() {}

	private List<Item> catalog = ItemLoader.load("Items.xml");
	private Map<String, Integer> mappedCatalog = null;

	private Map<String, Integer> getMappedCatalog() {
		if (this.mappedCatalog == null) {
			TreeMap<String, Integer> mappedCatalog = new TreeMap<>();
			for (Item item : this.catalog)
				mappedCatalog.put(item.getId(), item.getPrice());

			this.mappedCatalog = mappedCatalog;
		}
		return this.mappedCatalog;
	}

	public String getReport() {
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
			return report + "\n\n";
	}

	public void printReport() {
		System.out.println(getReport());
	}
}
