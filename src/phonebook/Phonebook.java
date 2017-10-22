package phonebook;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Phonebook {

	public static void main(String[] args) throws IOException {
		
		Contact contact = new Contact();
		try {
		contact.loadData();
		}
		catch (FileNotFoundException e){
		}
		contact.select();
	}
}
