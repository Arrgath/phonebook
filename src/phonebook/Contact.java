package phonebook;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Contact {

	Scanner scan = new Scanner(System.in);
	String userInput1;
	String userInput2;
	String userInput3;
	int userSelect;
	int infoCount = 0;

	LinkedHashMap<String, ArrayList> contacts = new LinkedHashMap<String, ArrayList>();
	
	public void select() throws IOException {

		Menu.showMenu();
		
		userSelect = scan.nextInt();

		if (userSelect < 1 || userSelect > 6) {
			System.out.println("PLEASE ENTER A VALID NUMBER.");
			select();
		}
		if (userSelect == 1) {
			showContacts();
			select();
		}
		if (userSelect == 2) {
			createContact();
			select();
		}

		if (userSelect == 3) {
			editContact();
			select();
		}

		if (userSelect == 4) {
			deleteContact();
			select();
		}
		
		if (userSelect == 5) {
			sortContact();
			select();
		}
		if (userSelect == 6) {
			System.exit(0);
		}
		

	}

	public void createContact() throws IOException {
		
		ArrayList<String>contactInfo = new ArrayList<String>();
		
		System.out.println("ENTER THE CONTACT'S NAME:");
		scan.nextLine();
		userInput1 = scan.nextLine();
		
		while (userInput1.trim().isEmpty()) {
			System.out.println("PLEASE ENTER A VALID NAME.");
			userInput1 = scan.nextLine();
		}
	
		System.out.println("ENTER THE CONTACT'S NUMBER:");
		userInput2 = scan.nextLine();

		boolean allNumbers = userInput2.chars().allMatch(Character::isDigit);
		while (userInput2.isEmpty() || allNumbers == false) {
			System.out.println("PLEASE ENTER A VALID NUMBER.");
			userInput2 = scan.nextLine();
			allNumbers = userInput2.chars().allMatch(Character::isDigit);
		}
		
		System.out.println("ENTER THE CONTACT'S E-MAIL:");
		userInput3 = scan.nextLine();
		while (userInput3.trim().isEmpty() || !userInput3.contains("@") || !userInput3.contains(".")) {
			System.out.println("PLEASE ENTER A VALID E-MAIL.");
			userInput3 = scan.nextLine();
		}
	
		contactInfo.add(userInput2);
		contactInfo.add(userInput3);
		
		contacts.put(userInput1, contactInfo);

		saveData();

	}

	public void showContacts() throws IOException {
		int count = 0;
			if (contacts.isEmpty()) {
				System.out.println("THERE ARE NO CONTACTS IN THE PHONEBOOK.\n");
				select();
			}
			System.out.printf("%-23s %-21s %-21s \n", "   NAME:", "PHONE NUMBER:", "E-MAIL:");
			for (String i : contacts.keySet()) {
				
				String key = i.toString();
				String value = contacts.get(i).toString()
				.replace(",", "           ")  //remove the commas, add space for format
				.replace("[", "")             //remove the right bracket
				.replace("]", "");            //remove the left bracket
				
				System.out.printf("%d. %-20s %-20s \n", count + 1, key, value);
				count++;
			}
			System.out.println();
		
	}

	public void deleteContact() throws IOException {

		showContacts();

		System.out.println("ENTER THE FULL NAME OF THE CONTACT YOU WISH TO DELETE:");
		scan.nextLine();
		userInput1 = scan.nextLine();

		if (contacts.containsKey(userInput1)) {
			contacts.remove(userInput1);
			System.out.println(userInput1 + " WAS SUCCESFULLY REMOVED.");
		}

		else {
			System.out.println("THE CONTACT " + userInput1 + " WAS NOT FOUND IN THE PHONEBOOK.\n");
		}
		saveData();
	}

	public void editContact() throws IOException {
		
		ArrayList<String> contInfo = new ArrayList<String>();
		
		showContacts();
		System.out.println("ENTER THE FULL NAME OF THE CONTACT YOU WISH TO EDIT:");
		scan.nextLine();
		userInput1 = scan.nextLine();
		
		if (contacts.containsKey(userInput1)) {
			
			contInfo = contacts.get(userInput1);

			System.out.println("ENTER THE CORESPONDING NUMBER:");
			System.out.println("1. EDIT NAME\t 2. EDIT PHONE NUMBER \t 3. EDIT E-MAIL");
			userSelect = scan.nextInt();

			while (userSelect < 1 || userSelect > 3) {
				System.out.println("PLEASE ENTER A VALID NUMBER.");
				userSelect = scan.nextInt();
			}

			if (userSelect == 1) {

				System.out.println("ENTER THE NEW NAME FOR THE SELECTED CONTACT:");
				scan.nextLine();
				userInput2 = scan.nextLine();
				
				while (userInput2.trim().isEmpty()) {
					System.out.println("PLEASE ENTER A VALID NAME.");
					userInput2 = scan.nextLine();
				}

				contacts.put(userInput2, contacts.get(userInput1));
				contacts.remove(userInput1);
				System.out.println("THE NAME WAS SUCCESFULLY MODIFIED.");
			}

			if (userSelect == 2) {

				System.out.println("ENTER THE NUMBER FOR THE SELECTED CONTACT:");
				scan.nextLine();
				userInput2 = scan.nextLine();
				
				boolean allNumbers = userInput2.chars().allMatch(Character::isDigit);
				while (userInput2.isEmpty() || allNumbers == false) {
					System.out.println("PLEASE ENTER A VALID NUMBER.");
					userInput2 = scan.nextLine();
					allNumbers = userInput1.chars().allMatch(Character::isDigit);
				}
				
				contInfo.set(0, userInput2);
				contacts.put(userInput1, contInfo);
				System.out.println("THE NUMBER WAS SUCCESFULLY MODIFIED.");
			}
			
			if (userSelect == 3) {

				System.out.println("ENTER THE E-MAIL FOR THE SELECTED CONTACT:");
				scan.nextLine();
				userInput3 = scan.nextLine();
				while (userInput3.trim().isEmpty() || !userInput3.contains("@") || !userInput3.contains(".")) {
					System.out.println("PLEASE ENTER A VALID E-MAIL.");
					userInput3 = scan.nextLine();
				}
				contInfo.set(1, userInput3);
				contacts.put(userInput1, contInfo);
				System.out.println("THE E-MAIL WAS SUCCESFULLY MODIFIED.");
			}
		}

		else {
			System.out.println("THE CONTACT " + userInput1 + " WAS NOT FOUND IN THE PHONEBOOK.\n");
		}
		saveData();
	}
	
	public void sortContact() throws IOException {
		
		TreeMap<String, ArrayList> contactsSorted = new TreeMap<String, ArrayList>(contacts);
		contacts.clear();
		
		for (HashMap.Entry<String, ArrayList> entry : contactsSorted.entrySet()){
		String key = entry.getKey(); 
		ArrayList value = entry.getValue();
		contacts.put(key, value);	
		}
		System.out.println("THE CONTACTS HAVE BEEN SORTED ALPHABETICALY.\n");
		saveData();
	}
	
	public void saveData() throws IOException {

		Writer writer = new FileWriter("phonebook.json");
		Gson gson = new GsonBuilder().create();
		gson.toJson(contacts, writer);
		writer.close();

	}

	public void loadData() throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader("phonebook.json"));
		Gson gson = new Gson();
		contacts = gson.fromJson(br, new TypeToken<LinkedHashMap<String, ArrayList>>() {
		}.getType());
	}
}
