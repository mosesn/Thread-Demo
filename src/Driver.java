import java.util.ArrayList;
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
		final int BIG = 1000000;
		Random random = new Random();
		List<Integer> lst = new ArrayList<Integer>();
		for (int x = 0; x < BIG; x++){
			lst.add(random.nextInt(BIG));
		}
		int threadNum = (lst.size() > 100) ? 100 : lst.size(); 
		
		ThreadPoolExecutor executor = new ThreadPoolExecutor(threadNum, 2*lst.size(), 
				1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		List<Integer> retVal = Sort.sort(lst, executor, lst.size());
		executor.shutdown();
		System.out.println(lst.size() == retVal.size());
	}

}