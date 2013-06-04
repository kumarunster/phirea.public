package template;

public class TestJRebel {
	
	public static void main(String[] args) throws Exception {
		
		while(true){
			System.out.println("sleep... zzz");
			
			Thread.currentThread().sleep(2000);
			
			System.out.println("wakedup");
		}
	}

}
