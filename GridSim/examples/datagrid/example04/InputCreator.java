package datagrid.example04;


import java.util.*;
import java.io.*;


public class InputCreator {

 
	private int noUsers, noResources, noFiles;
	private String headRouter;
	private Random rn;
	private BufferedWriter bfr;
	
	
	public InputCreator(int resources, int users){
		rn = new Random();
		try {
			createNetwork();
			createFiles(10,1024);
			createResources(resources,100,200);
			createUsers(users);
			createParameters();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		 
	}
	
//	#user_name router_name rc_name baud_rate list_of_grid_tasks
//	#grid tasks can be: 
//	#get filename
//	#attribute filename
//	#replicate filename resource
//	#delete filename resource
//user1 router2 Res_0 0.1 get testFile2 get testFile1 attribute testFile1 get fileF
	
	//create no amount of users
	public void createUsers(int no) throws IOException{
		noUsers = no;
		//create file writer
		bfr = new BufferedWriter(new FileWriter("usersNew.txt"));
		//create no amount of users
		int router, res, command, commands, filamount, resamount;
		float blimit;
		for (int i = 1; i <= no; i++){
			router = rn.nextInt(5)+1;
			blimit = rn.nextFloat()*10.0f;
			res = rn.nextInt(noResources);
			bfr.write("user" + Integer.toString(i) 
			+ " router" + Integer.toString(router) 
			+ " Res_"  +Integer.toString(res)
			+ " " + Float.toString(Math.round(blimit*10)/10.0f));
			
			
			commands = rn.nextInt(5)+1;
			for (int j = 0; j < commands; j++){
				command =  rn.nextInt(4);
				switch(command){
				//get file
				case 0:
					filamount = rn.nextInt(noFiles)+1;
					bfr.write(" get file" + Integer.toString(filamount));
					break;
				//attribute
				case 1:
					filamount = rn.nextInt(noFiles)+1;
					bfr.write(" attribute file" + Integer.toString(filamount));
					break;
				//replicate
				case 2:
					filamount = rn.nextInt(noFiles)+1;
					resamount = rn.nextInt(noResources);
					bfr.write(" replicate file" + Integer.toString(filamount) + " Res_" + Integer.toString(resamount));
					break;
				//delete
				case 3:
					filamount = rn.nextInt(noFiles)+1;
					resamount = rn.nextInt(noResources);
					bfr.write(" delete file" + Integer.toString(filamount) + " Res_" + Integer.toString(resamount));
					break;
					
				}
			}
			bfr.write("\n");
			
		}
		//close file
		bfr.close();
	}
	
	
	//creates no amount of files
	public void createFiles(int no, int sizeMax) throws IOException{
		noFiles = no;
		//create file writer
		bfr = new BufferedWriter(new FileWriter("filesNew.txt"));
		//create no amount of files with random size
		for (int i = 1; i <= no; i++){
			int size = rn.nextInt(sizeMax);
			bfr.write("file" + Integer.toString(i) + " " + Integer.toString(size) + "\n");
		}	
		//close file
		bfr.close();	
	}
	
	//creates the resources
	public void createResources(int no, int sizeMin, int sizeMax) throws IOException{
		noResources = no;
		//create file writer
		bfr = new BufferedWriter(new FileWriter("resourcesNew.txt"));
		int router, size, filamount, fileIndex;
		float blimit;
		//create no amount of resources
		for (int i = 0; i < no; i++){
			size = rn.nextInt(sizeMax);
			blimit = rn.nextFloat()*10.0f;
			router = rn.nextInt(5)+1;
			filamount = rn.nextInt(noFiles)+1;	
			bfr.write("Res_" + 
					Integer.toString(i) + " " + 
					Integer.toString(size+sizeMin) + " " + 
					Float.toString(Math.round(blimit*10)/10.0f) + " " +
					"router" + Integer.toString(router) + " ");
			
			for (int j = 0; j < filamount; j++){
				fileIndex = rn.nextInt(noFiles)+1;
				
				bfr.write("File" + Integer.toString(fileIndex) + " ");
			}
			bfr.write("\n");			
		}	
		//close file
		bfr.close();
	}
	
	
	
	//creates the paramaters
	public void createParameters() throws IOException{
		bfr = new BufferedWriter(new FileWriter("parameters.txt"));
		//create no amount of resources
		bfr.write("numUsers=" + noUsers);
		bfr.write("\nfiles=filesNew.txt\n");
		bfr.write("resources=resourcesNew.txt\n");
		bfr.write("network=network.txt\n");
		bfr.write("users=usersNew.txt\n");
		bfr.write("topRCrouter=router1");
		//close file
		bfr.close();
	}
	
	//creates the origial network
	public void createNetwork() throws IOException{
		bfr = new BufferedWriter(new FileWriter("network.txt"));
		bfr.write("5\nrouter1\nrouter2\nrouter3\nrouter4\nrouter5\nrouter1 router2 0.1 10.0 1500\nrouter1 router5 0.1 10.0 1500\nrouter2 router5 0.1 10.0 1500\nrouter2 router3 0.1 10.0 1500\nrouter4 router5 0.1 10.0 1500");
		bfr.close();
	}
	

	
}





