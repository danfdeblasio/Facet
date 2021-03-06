package facet;

import java.io.*;
import java.util.Scanner;
import java.util.Locale;

public class CoreColumn {
	public static float[] getCoreColumns(FacetAlignment a, Configuration c){
		float[] columns = new float[a.width];
		for(int i=0;i<a.width;i++){
			columns[i] = 0;
			int max_same = 0;
			for(int j=0;j<a.numberOfSequences;j++){
				if(a.sequence[j].charAt(i)=='-'){
					columns[i] = 0;
				}else{
					int same = 0;
					for(int k=0;k<a.numberOfSequences;k++){
						if(c.basesEquivelant(a.sequence[j].charAt(i), a.sequence[k].charAt(i))) same++;
					}
					if(same > max_same) max_same = same;
				}
			}
			if(((double)max_same/(double)a.numberOfSequences) > 0.9) columns[i] = 1;
			else if(((double)max_same/(double)a.numberOfSequences) > 0.35) columns[i] = (float) 0.5;
			else columns[i] = 0;
		}
		return columns;
	}

	public static double percentage(FacetAlignment a, Configuration c){
		float core_columns[] = getCoreColumns(a,c);
		int ct_core = 0;
		int non_gap = 0;
		for(int i=0;i<a.width;i++){
  			if(core_columns[i] == 1){ ct_core++; }
		}

		int normalize = a.width;
        	ct_core *= a.numberOfSequences;
        	normalize = 0;
        	for(int i=0;i<a.numberOfSequences;i++){
                	for(int j=0;j<a.width;j++){
                        	if(a.sequence[i].charAt(j)!='-'){
                                	normalize++;
                        	}
                	}
        	}

		if(normalize==0){ return 0; }
		return ((double)ct_core/(double)normalize);
	}
	
	public static double consensus(FacetAlignment a, Configuration c){
		float core_columns[] = getCoreColumns(a,c);
		int count_total=0;
		int count_satisfy=0;
		for(int i=0;i<a.width;i++){
			for(int j=i+1;j<a.width;j++){
				if(core_columns[i]>0 && core_columns[j]>0){
					count_total++;
					int cc = 0;
					int ce = 0;
					int ec = 0;
					int ee = 0;
					for(int k=0;k<a.numberOfSequences;k++){
						if(c.basesEquivelant(a.sequence[k].charAt(i), a.consensus[i]) &&
								c.basesEquivelant(a.sequence[k].charAt(j), a.consensus[j])) cc++;
						else if(c.basesEquivelant(a.sequence[k].charAt(i), a.consensus[i])) ce++;
						else if(c.basesEquivelant(a.sequence[k].charAt(j), a.consensus[j])) ec++;
						else ee++;
						
						count_total++;
						if(ee==0 || ec==0 || ce==0 || cc==0) count_satisfy++;
					}
				}
			}
		}
		if(count_total==0){ return 0; }
		return ((double)count_satisfy/(double)count_total);
	}

}
