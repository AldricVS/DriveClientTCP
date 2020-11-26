package test.gui;

import gui.MainWindow;

public class GuiTest {
	
	public static final String DEFAULT_IP_ADRESS = "127.0.0.1";
	public static final int DEFAULT_PORT = 5000;

	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		String ipAdress = DEFAULT_IP_ADRESS;
		
		boolean isPortChanged = false;
		boolean isIpAdressChanged = false;
		
		//check if user gives any args (we need at least 2 in order to proceed  
		if(args.length > 0) {
			if(args.length > 1) {
				//we will iterate through each arg in order to get informations
				for(int i = 0; i < args.length; i += 2) {
					if(i + 1 < args.length) {
						String argument = args[i];
						if(argument.equals("-p")) {
							if(isPortChanged) {
								System.err.println("Le port a déja été renseigné.");
								quitError();
							}else {
								if(isPortValid(args[i + 1])) {
									port = Integer.parseInt(args[i + 1]);
									isPortChanged = true;
								}else {
									System.err.println("Port invalide, il doit être compris entre 0 et 65535.");
									quitError();
								}
							}
						}else if(argument.equals("-i")) {
							if(isIpAdressChanged) {
								System.err.println("L'adresse Ip a déja été renseignée.");
								quitError();
							}else {
								if(isIpAdressValid(args[i + 1])) {
									ipAdress = args[i + 1];
									isIpAdressChanged = true;
								}else {
									System.err.println("Adresse IP invalide, Elle doit respecter le format IPv4.");
									quitError();
								}
							}
						}
					}else {
						System.err.println("Pas assez d'arguments passés.");
						displayHelp();
						quitError();
					}
					
				}
			}else {
				displayHelp();
				quitNormal();
			}
		}
		
		new MainWindow(ipAdress, port);
	}
	
	private static void displayHelp() {
		System.out.println("Paramètres qui peuvent être ajoutés : \n"
				+ "\"-p\" suivi d'un numéro permettra de changer le port sur lequel communiquer avec le serveur.\n"
				+ "\"-i\" suivi d'une adresse au format IPv4 permettra d'indiquer l'adresse du serveur.\n"
				+ "Port par défaut : 5000, Adresse IP par défaut : 127.0.0.1");
	}
	
	private static void quitError() {
		System.exit(1);
	}
	
	private static void quitNormal() {
		System.exit(0);
	}
	
	/**
	 * Check if given string is in the format "X.X.X.X", where 'X' is an integer between 0 and 255 
	 * @param ipAdress the string to check
	 * @return if the string has a valid format or not
	 */
	private static boolean isIpAdressValid(String ipAdress) {
		//we must have 4 fields separed with dots. Each of those fields are integers between 0 & 255
		String[] fields = ipAdress.split("\\.");
		if(fields.length != 4) {
			return false;
		}else {
			for(String field : fields) {
				try {
					int num = Integer.parseInt(field);
					if(num < 0 || num > 255) {
						return false;
					}
				}catch (NumberFormatException e) {
					//this substring is not a number, go away
					return false;
				}
			}
			
			//we managed to be here, so the ip adress is valid
			return true;
		}
	}
	
	private static boolean isPortValid(String port) {
		try{
			int portNum = Integer.parseInt(port);
			return portNum > 0 && portNum < 65535;
		}catch (NumberFormatException e) {
			return false;
		}
	}
}
