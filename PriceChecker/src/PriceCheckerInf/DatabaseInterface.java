package PriceCheckerInf;

public interface DatabaseInterface {
	public boolean connect_DB();
	public void disconnect_DB();
	public boolean queryProductInfo(String strBarcode, String[] rPrice, String[] rProductName, String[] rProductImage);
}
