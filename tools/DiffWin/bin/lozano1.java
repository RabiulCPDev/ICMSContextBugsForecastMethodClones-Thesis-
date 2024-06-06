import java.io.*;

class SourceFileInformation
{
	String fileName ;
	int cloneCount ;

	public String getFileName (){ return fileName; }
	public int getCloneCount () { return cloneCount ; }
	public void setFileName ( String fName ) { fileName = fName; }
	public void setCloneCount (int cCount) { cloneCount = cCount; }
}


class MethodBlockInformation
{
	public int versionNumber ;
	public int fileNumber ;
	public String methodName ;
	public int startingTokenNumber, endingTokenNumber;
	public int startingLineNumber, endingLineNumber ;
	public boolean isCloned ;

	public String getMethodName () { return methodName ; }
	public int getFileNumber () { return fileNumber ; }
	public int getStartingTokenNumber () { return startingTokenNumber; }
	public int getEndingTokenNumber () { return endingTokenNumber; }
	public int getStartingLineNumber () { return startingLineNumber ; }
	public int getEndingLineNumber () { return endingLineNumber ; }

	public void setMethodInformation ( int fno, String mname, int stn, int etn, int sln, int eln )
	{
		fileNumber = fno ;
		methodName = mname ;
		startingTokenNumber = stn ;
		endingTokenNumber = etn;
		startingLineNumber = sln ;
		endingLineNumber = eln;
	}
}




public class lozano
{

	private static SourceFileInformation [] sourceFiles = new SourceFileInformation[50000] ;
	private static int sourceFileCount = 1;

	private static MethodBlockInformation [] methodBlocks = new MethodBlockInformation[10000] ;
	private static int methodCount = 0;

	public static void main (String args[])
	{
		/*retrieveSourceFileInformation () ;

		for (int i=1;i<sourceFileCount;i++)
		{
			System.out.println (i+"  "+sourceFiles[i].getFileName () + "  " + sourceFiles[i].getCloneCount () );
		}*/

		try
		{
			retrieveMethodsFromFile (1) ;
		}
		catch (Exception e)
		{
			System.out.print ("I am caught. Rescue me! "+ e) ;
		}

		for (int i=0;i<methodCount; i++)
			System.out.println (methodBlocks[i].getMethodName() + " " + methodBlocks[i].getStartingTokenNumber() + " " + methodBlocks[i].getEndingTokenNumber()+" " + methodBlocks[i].getStartingLineNumber() + " " + methodBlocks[i].getEndingLineNumber()) ;


	}

	//Retrieving source file information.
	//---------------------------------------------------------------------------
	public static void retrieveSourceFileInformation ()
	{
		String str = "", delimiter = "[ ,\t]+" ;

		int fileInfoRetrieved = 0;

		try
		{
			FileInputStream fstream = new FileInputStream ("F://junit.txt");
			DataInputStream dstream = new DataInputStream (fstream) ;

			while ( dstream.available() != 0 )
			{
				str = dstream.readLine () ;

				//Retrieving the source file information.
				//---------------------------------------------------------------
				if (str.split(delimiter)[0].equals("source_files"))
				{
					while  (dstream.available() != 0)
					{
						str = dstream.readLine () ;
						if ( str.trim().equals("}") ) break ;

						sourceFiles[sourceFileCount] =  new SourceFileInformation () ;
						sourceFiles[sourceFileCount].setFileName (str.split(delimiter)[1].trim()) ;
						sourceFiles[sourceFileCount].setCloneCount (Integer.parseInt(str.split(delimiter)[2].trim())) ;
						sourceFileCount++ ;
					}
					fileInfoRetrieved = 1 ;
				}
				if ( fileInfoRetrieved == 1 )
					break ;
			}
		}
		catch  (Exception e)
		{
			System.out.print ("I am caught. Rescue me, please.\t\t" +e) ;
		}
	}


	//Retrieving method information from a given file.
	//---------------------------------------------------------------
	public static void retrieveMethodsFromFile1 (int fileID) throws Exception
	{
		String str = "", delimiter = "[ ,\t]+", methodStart= "", methodEnd = "", methodName = "" ;
		int idFound = 0, tokenStart, tokenEnd, tokenCount=0, lineStart, lineEnd;


		FileInputStream fstream = new FileInputStream ("f://Test.txt") ;
		DataInputStream dstream = new DataInputStream (fstream) ;



		while (dstream.available () != 0)
		{
			str = dstream.readLine () ;
			tokenCount++ ;

			if (str.split(delimiter)[2].equals ("(def_block"))
			{
				methodStart = str ;
				tokenStart = tokenCount ;

				while (dstream.available () != 0)
				{
					str = dstream.readLine () ;
					tokenCount++ ;

					if (str.split(delimiter)[2].split("[|]")[0].equals("id"))
					{


						while (dstream.available () != 0)

						methodName = str.split(delimiter)[2].split("[|]")[1] ;
						break;
					}
				}


				if (dstream.available () == 0)
					break;

				str = dstream.readLine () ;
				tokenCount++ ;


				if (str.split(delimiter)[2].equals("c_func"))
				{

					//find the ending def_block.
					int sCount = 0, eCount = 0;

					while (dstream.available () != 0)
					{
						str = dstream.readLine () ;
						tokenCount++ ;

						if (str.split(delimiter)[2].equals ("(def_block"))
							sCount++ ;
						if (str.split(delimiter)[2].equals (")def_block"))
						{
							eCount++ ;
							if (eCount - sCount == 1) //ending def block is found.
								break;
						}
					}

					//ending def_block is found.

					//So, we have got a complete method.
					methodEnd = str ;
					tokenEnd = tokenCount ;

					lineStart = Integer.parseInt(methodStart.split(delimiter)[0].split("[.]")[0],16);
					lineEnd = Integer.parseInt(methodEnd.split(delimiter)[0].split("[.]")[0],16);


					methodBlocks[methodCount] = new MethodBlockInformation () ;
					methodBlocks[methodCount].setMethodInformation ( fileID, methodName, tokenStart, tokenEnd, lineStart, lineEnd ) ;
					methodCount++ ;
				}

			}
		}
	}





	//Retrieving method information from a given file.

	/* Method finding process.
	1. First find the tag c_func.
	2. Then find the first begining parenthesis (.
	3. Then find the corresponding last parenthesis. )
	4. Determine the next token.
	5. If this next token is brace {, then c_func is surely a function block.
	6. Try to find the corresponding ending brace. }.

	*/
	//----------------------------------------------------------------------
	public static void retrieveMethodsFromFile (int fileID) throws Exception
	{
		String str = "", delimiter = "[ ,\t]+", methodStart= "", methodEnd = "", methodName = "", pstr = "" ;
		int idFound = 0, tokenStart, tokenEnd, tokenCount=0, lineStart, lineEnd;


		FileInputStream fstream = new FileInputStream ("f://Test.txt") ;
		DataInputStream dstream = new DataInputStream (fstream) ;



		while (dstream.available () != 0)
		{
			str = dstream.readLine () ;
			tokenCount++ ;


			if (str.split(delimiter)[2].equals ("c_func")) //possibility of getting a function block.
			{
				methodName = pstr.split(delimiter)[2].split("[|]")[1] ; //The method name was contained in the previous string.

				tokenStart = tokenCount;
				methodStart = str ;

				str = dstream.readLine () ;//reading the first parenthesis.
				tokenCount++ ;

				int sparen = 0, eparen = 0;

				while (true)  //finding the corresponding last parenthesis.
				{
					str = dstream.readLine () ;
					tokenCount++ ;

					if (str.split(delimiter)[2].equals("(paren")) sparen++ ;
					if (str.split(delimiter)[2].equals(")paren"))
					{
						eparen++ ;
						if (eparen-sparen == 1) //corresponding last parenthesis got.
							break;
					}
				}

				//checking the first token after the corresponding last parenthesis.

				str = dstream.readLine () ;
				tokenCount++ ;

				if (str.split(delimiter)[2].equals("(brace")) //a function is surely got.
				{
					//finding the last brace of the function.

					int sbrace =0, ebrace =0;

					while (true)
					{
						str = dstream.readLine () ;
						tokenCount++ ;

						if (str.split(delimiter)[2].equals("(brace")) sbrace++;
						if (str.split(delimiter)[2].equals(")brace"))
						{
							ebrace++ ;

							if (ebrace-sbrace == 1) //the end point of the function is got.
								break;
						}
					}

					tokenEnd = tokenCount ;
					methodEnd = str ;


					lineStart = Integer.parseInt(methodStart.split(delimiter)[0].split("[.]")[0],16);
					lineEnd = Integer.parseInt(methodEnd.split(delimiter)[0].split("[.]")[0],16);


					methodBlocks[methodCount] = new MethodBlockInformation () ;
					methodBlocks[methodCount].setMethodInformation ( fileID, methodName, tokenStart, tokenEnd, lineStart, lineEnd ) ;
					methodCount++ ;
				}

			}
			else
			{
				pstr = str; //capturing the previous string for finding the method name.
			}

		}

	}//end of method:----public static void retrieveMethodsFromFile (int fileID) throws Exception



	//This method determines whether a method is cloned or not.
	public static void determineClonedMethods () throws Exception
	{
		String str ;
		int fileSerial1, fileSerial2, startToken1, endToken1, startToken2, endToken2 ;

		FileInputStream fstream = new FileInputStream ("f://a.txt") ;
		DataInputStream dstream = new DataInputStream (fstream) ;

		while (dstream.available() != 0)
		{
			str = dstream.readLine() ;
			if (str.equals("clone_pairs {"))
				break ;
		}

		while (dstream.available() != 0)
		{
			str = dstream.readLine() ;
			if (str.equals("}"))
				break ;

			fileSerial1 = Integer.parseInt(str.split("[ ,\t]+")[1].split("[.,-]+")[0]) ;
			startToken1 = Integer.parseInt(str.split("[ ,\t]+")[1].split("[.,-]+")[1]) ;
			endToken1 = Integer.parseInt(str.split("[ ,\t]+")[1].split("[.,-]+")[2]) ;


			fileSerial2 = Integer.parseInt(str.split("[ ,\t]+")[2].split("[.,-]+")[0]) ;
			startToken2 = Integer.parseInt(str.split("[ ,\t]+")[2].split("[.,-]+")[1]) ;
			endToken2 = Integer.parseInt(str.split("[ ,\t]+")[2].split("[.,-]+")[2]) ;

			for (int i =0;i<methodCount;i++)
			{
				if (methodBlocks[i].getFileNumber() == fileSerial1)
				{
					if (startToken1 >= methodBlocks[i].getStartingTokenNumber() && endToken1 <= methodBlocks[i].getEndingTokenNumber() )
					{
						methodBlocks[i].isCloned = true ;
					}
				}
				else
				{
					if (methodBlocks[i].getFileNumber() == fileSerial2)
					{
						if (startToken2 >= methodBlocks[i].getStartingTokenNumber() && endToken2 <= methodBlocks[i].getEndingTokenNumber())
						{
							methodBlocks[i].isCloned = true ;
						}
					}
				}
			}

		}

	} //end of. public static void determineClonedMethods ()



}