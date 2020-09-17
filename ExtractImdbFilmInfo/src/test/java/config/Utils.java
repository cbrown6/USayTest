package config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

	public Utils() {

	}
	
	/**
	* Adds n days from today
	* @return date in dd/mm/yy format
	*/
	/*
	public static String getDate(int daysToAdd){
        DateFormat dateFormat = new SimpleDateFormat( new PropertiesReader().getProperty("simple.date.format"));
        Calendar calendar = Calendar.getInstance();
 		calendar.add( Calendar.DATE,daysToAdd );
		return dateFormat.format(calendar.getTime());
	}
*/
	public static ArrayList<ArrayList<String>> parseDataFromCsvFile(String filename, String delimeter)
	{
		//Initialise new Array variable to store CSV information
		ArrayList<ArrayList<String>> dataFromFile=new ArrayList<ArrayList<String>>();
		try{

			//Scan file data using configured delimeter
			Scanner scanner=new Scanner(new FileReader(filename));
			scanner.useDelimiter(delimeter);

			//Run until data is complete
			while(scanner.hasNext())
			{
				//Save next line to a string
				String dataInRow=scanner.nextLine();

				//Split the String based on the configured Delimneter
				String []dataInRowArray=dataInRow.split(delimeter);

				//Save row data as new Array List
				ArrayList<String> rowDataFromFile=new ArrayList<String>(Arrays.asList(dataInRowArray));

				//Add array list to initiailised array list
				dataFromFile.add(rowDataFromFile);
			}

			//Close the scanner once data is complete
			scanner.close();

		}catch (FileNotFoundException e){

			//Print stack trace if error is found
			e.printStackTrace();
		}

		//Return data from the file
		return dataFromFile;
	}

}
