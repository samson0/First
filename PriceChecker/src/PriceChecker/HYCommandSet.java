package PriceChecker;

public class HYCommandSet {
	public final static byte HY_CMD_HEARTBEAT = (byte)0x99;
	public final static byte HY_CMD_GET_DATE_TIME = (byte)0xA7;
	public final static byte HY_CMD_SET_IDLE_LAYOUT = (byte)0xA3;
	public final static byte HY_CMD_GET_IDLE_LAYOUT = (byte)0xA4;
	public final static byte HY_CMD_SET_NONIDLE_LAYOUT = (byte)0xA5;
	public final static byte HY_CMD_GET_NONIDLE_LAYOUT = (byte)0xA6;
	public final static byte HY_CMD_GET_PRODUCT_INFO = (byte)0xA8;
	public final static byte HY_CMD_REBOOT_DEVICE = (byte)0xA9;
	
	public final static byte HY_CMD_STATUS_SUCCESS = (byte)0x00;
}
