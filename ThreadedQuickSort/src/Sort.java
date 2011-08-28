import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;


public class Sort {
	public static <TYPE> TYPE getOne(Iterable<TYPE> iterable){
		for (TYPE type : iterable) {
			return type;
		}
		return null;
	}

	public static <TYPE extends Comparable<TYPE>> List<TYPE>  sort(Collection<TYPE> collection,
			final ThreadPoolExecutor executor) {		
		List<TYPE> lessLst = new ArrayList<TYPE>();
		List<TYPE> moreLst = new ArrayList<TYPE>();
		TYPE example = getOne(collection);
		if (example == null){
			return lessLst;
		}
		
		final CountDownLatch latch = new CountDownLatch(2);
		
		int num = 0;
		for (TYPE type : collection) {
//			if (type == "m" || type == "q"){
//				System.out.println(example);
//				System.out.println(type.compareTo(example));
//			}
			if (type.compareTo(example) > 0){
				moreLst.add(type);
			}
			else if (0 > type.compareTo(example)){
				lessLst.add(type);				
			}
			else{
				num++;
			}
		}

		final ListWrapper<TYPE> retLessLst = new ListWrapper<TYPE>();
		final List<TYPE> finalLessLst = lessLst;
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				retLessLst.setLst(Sort.sort(finalLessLst, executor));
				latch.countDown();
			}
		}, retLessLst);

		final ListWrapper<TYPE> retMoreLst = new ListWrapper<TYPE>();
		final List<TYPE> finalMoreLst = moreLst;
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				retMoreLst.setLst(Sort.sort(finalMoreLst, executor));
				latch.countDown();
			}
		}, retMoreLst);		

		
		try {
			latch.await();
		} catch (InterruptedException e) {
			System.out.println("Interupted Exception!");
		}
		List<TYPE> retLst = new ArrayList<TYPE>();
		retLst.addAll(retLessLst.getLst());
		for (int index = 0; index < num; index++){
			retLst.add(example);
		}
		retLst.addAll(retMoreLst.getLst());
		return retLst;
	}
}
