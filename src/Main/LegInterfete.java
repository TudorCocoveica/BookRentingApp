package Main;

import LogIn.InterfataLogin;
import InchiriereCarti.InterfataInchiriere;

public class LegInterfete {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		InterfataInchiriere obiect=new InterfataInchiriere();
		obiect.seteazaToateCartileDisponibile();

		InterfataLogin interfataLogin = new InterfataLogin();

		/**
		 * Rularea programului
		 */
		
		interfataLogin.setVisible(true);

		while (!interfataLogin.ok()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	

	}
}
