package InchiriereCarti;

public interface ObserverCarti {
	void actualizeazaStare(InterfataInchiriere carte);
	
	void adaugaObserver(ObserverCarti observer);
	
	void stergeObserver(ObserverCarti observer);
	
	void notificaObservatori();
	
	void actualizeazaStareInchiriata(boolean[] stareaInchiriere);
	
	void actualizeazaStareTotala(boolean[] stareaInchiriere);
}
