package logistics.order.processor;

import logistics.exceptions.InitializationException;
import logistics.exceptions.InvalidDataException;

public interface OrderProcessor {

	public void processOrders() throws InitializationException, InvalidDataException;

	public String getReport();
}
