import java.util.List;


public class ListWrapper <TYPE>{
	private List<TYPE> lst;

	public List<TYPE> getLst() {
		return lst;
	}

	public void setLst(List<TYPE> lst) {
		this.lst = lst;
	}
	
	public ListWrapper(){
		super();
		lst = null;
	}
}
