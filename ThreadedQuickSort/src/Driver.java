import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		List<String> lst = Arrays.asList(
//				new String[] {"a", "b", "c", "z", "y", "x", "d", "e", "e", "f", "t", "p", "q", "m"});
		final int BIG = 1000000;
		Random random = new Random();
		List<Integer> lst = new ArrayList<Integer>();
		for (int x = 0; x < BIG; x++){
			lst.add(random.nextInt(BIG));
		}
		ThreadPoolExecutor executor = new ThreadPoolExecutor(lst.size(), 2*lst.size(), 
				1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		List<Integer> retVal = Sort.sort(lst, executor);
		//System.out.println(retVal);
		System.out.println(lst.size() == retVal.size());
	}

}