package vo;

public class PayVO {
	private int PayID;
	private String use;
	private String cost;

	public PayVO() {}
	
	public PayVO(int PayID,String use ,String cost){
		this.PayID = PayID;
		this.use = use;
		this.cost = cost;
	}
	
	public int getPayID(){
		return PayID;
	}
	
	public void setPayID(int PayID){
		this.PayID = PayID;
	}
	
	public String getuse(){
		return use;
	}
	
	public void setuse(String use){
		this.use = use;
	}
	
	public String getcost(){
		return cost;
	}
	
	public void setcost(String cost){
		this.cost = cost;
	}
	
}
