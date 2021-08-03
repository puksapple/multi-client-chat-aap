import java.util.function.Consumer;
import java.util.function.Supplier;

public class Lambda {
	public static void main(String[] args) {
		
	//Consumer<String> c=s->System.out.println(s.length());
		//c.accept("hello");
		
		
		Supplier<String> s= ()->{
			
			return("hello");
			
		};
		
		System.out.println(s.get());
		
	}
	
	}



