import java.io.Serializable;

public class MessageMusic extends Message implements Serializable{

	private static final long serialVersionUID = 1L;
	
	// 0 start // 1 play // 2 pause
	private int value ; 
	
	public MessageMusic(int value) {
		this.value=value ; 
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	

}
