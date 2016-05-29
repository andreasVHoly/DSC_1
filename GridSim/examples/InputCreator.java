//package datagrid.example04;


import java.util.*;
import java.io.*;


public class InputCreator {

    //to prevent allocations that cause errors
    private LinkedList<LinkedList<Integer>> resourceAllocation;
	private int noUsers, noResources, noFiles;
	private String headRouter;
	private Random rn;
	private BufferedWriter bfr;
    private Scanner in;

    //constructor
	public InputCreator(int resources, int users, boolean manual){
		rn = new Random();
        resourceAllocation = new LinkedList<LinkedList<Integer>>();
        in = new Scanner(System.in);
		try {
            createNetwork();
            if (manual){
                createFile();
                createResource();
                createUser();
                createParameters(true);
            }
            else{
    			createFiles(10,1024);
    			createResources(resources,100,200);
    			createUsers(users);
                createParameters(false);
            }
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

    //manual user creation
    private void createUser() throws IOException{
        //query the user for input until they quit and then write to file
        //relies on correct input otherwise it will crash
        bfr = new BufferedWriter(new FileWriter("usersMan.txt"));
        while (true){
            System.out.println("Enter a user entry for the users file in the format or q to finish user entry\n<user_name> <router_name> <rc_name> <baud_rate> <list_of_grid_tasks>\ngrid tasks can be:\n-get filename\n-attribute filename\n-replicate filename resource\n-delete filename resource\n(e.g. user1 router3 Res_0 0.1 replicate file2 Res_1 delete file2 Res_1)");
            String line = in.nextLine();
            if (line.equals("q")){
                break;
            }
            line += "\n";
            bfr.write(line);
            noUsers++;
        }
        bfr.close();
    }

    //manual resource creation
    private void createResource() throws IOException{
        //query the user for input until they quit and then write to file
        //relies on correct input otherwise it will crash
        bfr = new BufferedWriter(new FileWriter("resourcesMan.txt"));
        while (true){
            System.out.println("Enter a resource entry for the resources file in the format or q to finish resource entry\n<resource_name> <storage_size(in GB)> <bandwidth> <router_name> <list_of_containing_files>\n(e.g. Res_0 10 0.1 router2 file2)");
            String line = in.nextLine();
            if (line.equals("q")){
                break;
            }
            line += "\n";
            bfr.write(line);
            noResources++;
        }
        bfr.close();
    }

    //manual file creation
    private void createFile() throws IOException{
        //query the user for input until they quit and then write to file
        //relies on correct input otherwise it will crash
        bfr = new BufferedWriter(new FileWriter("filesMan.txt"));
        while (true){
            System.out.println("Enter a file entry for the files file in the format or q to finish file entry\n<file_name> <file_size(in MB)>\n(e.g. fileM 10)");
            String line = in.nextLine();
            if (line.equals("q")){
                break;
            }
            line += "\n";
            bfr.write(line);
            noFiles++;
        }
        bfr.close();
    }

    //checks if a file is in a resource & returns outcome
    private boolean checkForFile(int resource, int file){
        LinkedList<Integer> listoFiles = resourceAllocation.get(resource);
        if (listoFiles.contains(file)){
            return true;
        }
        return false;
    }

	//create no amount of users with random attributes
	private void createUsers(int no) throws IOException{
		noUsers = no;
		//create file writer
		bfr = new BufferedWriter(new FileWriter("usersNew.txt"));
		//create no amount of users
		int router, res, command, commands, filamount, resamount;
		for (int i = 1; i <= no; i++){
            //get random variables
			router = rn.nextInt(5)+1;
			res = rn.nextInt(noResources);
			bfr.write("user" + Integer.toString(i)
			+ " router" + Integer.toString(router)
			+ " Res_"  +Integer.toString(res)
			+ " 0.1");

            //make random number of commands
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
                    //to not create duplicates and in order to replicate, file needs to exist in own resource
                    while(!checkForFile(resamount,filamount) && checkForFile(res,filamount)){
                        filamount = rn.nextInt(noFiles)+1;
    					resamount = rn.nextInt(noResources);
                    }
					bfr.write(" replicate file" + Integer.toString(filamount) + " Res_" + Integer.toString(resamount));
					break;
				//delete
				case 3:
					filamount = rn.nextInt(noFiles)+1;
					resamount = rn.nextInt(noResources);
                    //to check if the file is there
                    while(!checkForFile(resamount,filamount)){
                        filamount = rn.nextInt(noFiles)+1;
    					resamount = rn.nextInt(noResources);
                    }
					bfr.write(" delete file" + Integer.toString(filamount) + " Res_" + Integer.toString(resamount));
					break;
				}
			}
			bfr.write("\n");
		}
		//close file
		bfr.close();
	}

	//creates no amount of files with random attributes
	private void createFiles(int no, int sizeMax) throws IOException{
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
	private void createResources(int no, int sizeMin, int sizeMax) throws IOException{
		noResources = no;
		//create file writer
		bfr = new BufferedWriter(new FileWriter("resourcesNew.txt"));
		int router, size, filamount, fileIndex;
		//create no amount of resources with random attributes
		for (int i = 0; i < no; i++){
            //used for suplicate checks
            boolean[] fileUsed =  new boolean[noFiles+1];
            for (int l = 0; l < noFiles+1; l++){
                fileUsed[l] = false;
            }
            //random attributes
			size = rn.nextInt(sizeMax);
			router = rn.nextInt(5)+1;
			filamount = rn.nextInt(noFiles)+1;

            LinkedList<Integer> temp = new LinkedList<Integer>();
            resourceAllocation.add(i,temp);
			bfr.write("Res_" +
					Integer.toString(i) + " " +
					Integer.toString(size+sizeMin) +
					" 0.1 " +
					"router" + Integer.toString(router) + " ");

			for (int j = 0; j < filamount; j++){
				fileIndex = rn.nextInt(noFiles)+1;
                //check for duplicates
                if (fileUsed[fileIndex]){
                    continue;
                }
                //mark file as added
                fileUsed[fileIndex] = true;
                resourceAllocation.get(i).add(fileIndex);
				bfr.write("file" + Integer.toString(fileIndex) + " ");
			}
			bfr.write("\n");
		}
		//close file
		bfr.close();
	}

	//creates the paramaters.txt file based on what more we are in
	private void createParameters(boolean manual) throws IOException{
		bfr = new BufferedWriter(new FileWriter("parameters.txt"));
        //if we have manually enetered configuration files the text files have different names
        //once determined, we write all the needed content
		if (manual){
            bfr.write("numUsers=" + noUsers);
            bfr.write("\nfiles=filesMan.txt\n");
            bfr.write("resources=resourcesMan.txt\n");
            bfr.write("network=network.txt\n");
            bfr.write("users=usersMan.txt\n");
            bfr.write("topRCrouter=router1");
        }
        else{
            bfr.write("numUsers=" + noUsers);
    		bfr.write("\nfiles=filesNew.txt\n");
    		bfr.write("resources=resourcesNew.txt\n");
    		bfr.write("network=network.txt\n");
    		bfr.write("users=usersNew.txt\n");
    		bfr.write("topRCrouter=router1");
        }
		//close file
		bfr.close();
	}

	//creates the origial network topology
	private void createNetwork() throws IOException{
		bfr = new BufferedWriter(new FileWriter("network.txt"));
		bfr.write("5\nrouter1\nrouter2\nrouter3\nrouter4\nrouter5\nrouter1 router2 0.1 10.0 1500\nrouter1 router5 0.1 10.0 1500\nrouter2 router5 0.1 10.0 1500\nrouter2 router3 0.1 10.0 1500\nrouter4 router5 0.1 10.0 1500");
		bfr.close();
	}

    //used if the program is called directly
	public static void main(String args[]){
        //get desired mode from user
        System.out.println("Would you like to randomise files or manually enter? (r/m): ");
        Scanner in = new Scanner(System.in);
        String selection = in.nextLine();
        InputCreator ip;
        //random mode
        if (selection.equals("r")){
            ip = new InputCreator(10, 20, false);
        }
        //manual mode
        else if (selection.equals("m")){
            ip = new InputCreator(10, 20, true);
        }
        else{
            System.out.println("Wrong input");
        }
	}
}
