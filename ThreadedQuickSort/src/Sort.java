import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


public class Sort {
	public static <TYPE extends Comparable<TYPE>> List<TYPE>  sort(List<TYPE> collection,
			final ThreadPoolExecutor executor, final int startNum) {		
		TYPE example = Iterables.getFirst(collection, null);
		if (example == null){
			return null;
		}
		if (collection.size() * executor.getActiveCount() < startNum){
			Collections.sort(collection);
			return collection;
		}
		
		List<TYPE> lessLst = Lists.newArrayList();
		List<TYPE> moreLst = Lists.newArrayList();
		final CountDownLatch latch = new CountDownLatch(2);
		
		int num = 0;
		for (TYPE type : collection) {
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
				retLessLst.setLst(Sort.sort(finalLessLst, executor, startNum));
				latch.countDown();
			}
		}, retLessLst);

		final ListWrapper<TYPE> retMoreLst = new ListWrapper<TYPE>();
		final List<TYPE> finalMoreLst = moreLst;
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				retMoreLst.setLst(Sort.sort(finalMoreLst, executor, startNum));
				latch.countDown();
			}
		}, retMoreLst);		
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			System.out.println("Interupted Exception!");
		}

		List<TYPE> retLst = Lists.newArrayList();
		List<TYPE> myLessLst = retLessLst.getLst();
		if (myLessLst != null){
			retLst.addAll(myLessLst);
		}
		for (int index = 0; index < num; index++){
			retLst.add(example);
		}
		List<TYPE> myMoreLst = retMoreLst.getLst();
		if (myMoreLst != null){
			retLst.addAll(myMoreLst);
		}
		return retLst;
	}
}
