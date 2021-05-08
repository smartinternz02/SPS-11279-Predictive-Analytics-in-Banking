package org.an;

import java.io.IOException;

import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.BoxTrace;
import tech.tablesaw.plotly.traces.HistogramTrace;
import tech.tablesaw.io.string.*;


public class BankData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.print("having the data to be analysing");
		
		try {
			Table bank_data = Table.read().csv("C:\\Users\\home\\eclipse-workspace\\org.an\\src\\main\\java\\org\\an\\bank.csv");
			
			System.out.println(bank_data.shape());
			System.out.println(bank_data.first(6));
			System.out.println(bank_data.last(3));
			
			System.out.println(bank_data.structure());
			System.out.println(bank_data.missingValueCounts());
			
			System.out.println(bank_data.summary());
			
			System.out.println(bank_data.hashCode());
			
			
////		Histogram
			Layout layout1 = Layout.builder().title("Distribution of  job").build();
			HistogramTrace trace1 = HistogramTrace.builder(bank_data.nCol("job")).build();
		    Plot.show(new Figure(layout1, trace1));
		    
		 // try all these numerical & nominal features for the distribution get visualizers 
		//	age,balance,day,duration,campaign,pdays,previous -- job,marital,education,default,housing,loan,contact,month,poutcome,deposit    
		    
		    Layout layout3 = Layout.builder().title("age by deposit ").build();
		    BoxTrace trace3 =BoxTrace.builder(bank_data.categoricalColumn("deposit"), bank_data.nCol("age")).build();
		    Plot.show(new Figure(layout3, trace3));
		    
			}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
	}

}
}