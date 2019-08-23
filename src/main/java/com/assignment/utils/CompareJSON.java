package com.assignment.utils;

public class CompareJSON {
	public int compare(String res1, String res2) {
			int status = 2;

			try {
				if(res1.equals(res2))
					status= 0;

				else
					status= -1;
			}
			catch(NullPointerException ne) {

				if(res1==null && res2 == null)
					return 0;
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
			return status;
	}
}

