
package ch.hearc.meteo.imp.use.remote.pccentral;

import ch.hearc.meteo.spec.afficheur.AffichageOptions;

public class UsePCCentral
	{

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public static void main(String[] args)
		{
		main();
		}

	public static void main()
		{

		String title = "PC-Central";
		int n = 1;
		AffichageOptions affichageOptions = new AffichageOptions(n, title);
		new PCCentral(affichageOptions).run();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	}
