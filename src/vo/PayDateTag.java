package vo;

public class PayDateTag {
	private int ptagID;
	private int year;
	private int month;
	private int day;
	private int PayID;
	
	public PayDateTag() {}
	public PayDateTag(int ptagID, int year, int month, int day, int PayID){
		this.ptagID  = ptagID;
		this.year = year;
		this.month = month;
		this.day = day;
		this.PayID = PayID;
	}
	
	public int getPTagID(){
		return ptagID;
	}
	
	public void setPTagID(int ptagID){
		this.ptagID = ptagID;
	}
	
	public int getYear(){
		return year;
	}
	
	public void setYear(int year){
		this.year = year;
	}
	
	public int getMonth(){
		return month;
	}
	
	public void setMonth(int month){
		this.month = month;
	}
	
	public int getDay(){
		return day;
	}
	
	public void setDay(int day){
		this.day = day;
	}
	
	public int getPayID(){
		return PayID;
	}
	
	public void setPayID(int PayID){
		this.PayID = PayID;
	}
	
}
