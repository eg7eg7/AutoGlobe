import java.util.Comparator;

public class ProgrammingCompare implements Comparator<Programming>{

	@Override
	public int compare(Programming o1, Programming o2) {
		//compare dates in the format MM/DD/YYYY
		String s1 = o1.getProgram_date();
		String s2 = o1.getProgram_date();
		String s1_arr[] = s1.split("/");
		String s2_arr[] = s2.split("/");
		if(s1_arr.length != 3 ||s2_arr.length != 3) //not a valid string
			return 0;
		int y1 =Integer.parseInt(s1_arr[2]);
		int y2 =Integer.parseInt(s2_arr[2]);
		int m1 =Integer.parseInt(s1_arr[0]);
		int m2 =Integer.parseInt(s1_arr[0]);
		int d1 =Integer.parseInt(s1_arr[1]);
		int d2 =Integer.parseInt(s1_arr[1]);
		if(y1 < y2)
			return 1;
		else if (y1 < y2)
			return -1;
		
		if(m1 < m2)
			return 1;
		else if (m1 < m2)
			return -1;
		
		if(d1 < d2)
			return 1;
		else if (d1 < d2)
			return -1;
		
		
		return 0;
	}

}
