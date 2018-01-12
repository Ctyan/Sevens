package protocol;

import java.io.Serializable;

public class Protocol implements Serializable{
	private static final long serialVersionUID = 1861050339906354967L;
	private Integer Protocol_ID;
	public static int CHAT = 0,
					  GAME = 1;

	public Protocol(int id){
		Protocol_ID = id;
	}

	public Integer getProtocol_ID() {
		return Protocol_ID;
	}

	public void setProtocol_ID(Integer protocol_ID) {
		Protocol_ID = protocol_ID;
	}

}
