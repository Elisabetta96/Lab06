package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	MeteoDAO meteoDao= new MeteoDAO ();
	
	public Model() {

	}
	
	//prof aveva detto = null
	List<SimpleCity>tuttiSC=new ArrayList();
	
	List<SimpleCity>best= new ArrayList(); 
	double costoBest=0.0;
	
	List<String>localitas=this.meteoDao.getAllLocalita();

	public String getUmiditaMedia(int mese) {
		String umiditaMedia="";
		for (String l:localitas) {
			umiditaMedia+=l+"="+meteoDao.getAvgRilevamentiLocalitaMese(mese,l)+"\n";
		}
		return umiditaMedia;
	}

	
	
	public String trovaSequenza(int mese) {
		HashMap<String,Citta> cities=new HashMap<String,Citta>();
		this.best= new ArrayList(); 
		this.costoBest=0.0;
		for (String l:localitas) {
			List<Rilevamento>r=this.meteoDao.getAllRilevamentiLocalitaMese(mese, l);
			cities.put(l,new Citta(l,r));
		}
		
		List<SimpleCity> parziale= new ArrayList();
		
		cerca(cities,parziale,0);
		
		String s="La sequenza ottimale per il mese "+mese+" è ";
		for(SimpleCity sc:this.best) {
			s+= sc.getNome()+" ("+sc.getCosto()+"), ";
	
		}
		s=s.substring(0,s.length() - 2);
		s+=" con un costo di "+this.costoBest;
		return s;
	
	}

	private void cerca(HashMap<String,Citta> cities ,List<SimpleCity> parziale, int l) {
		if (l==NUMERO_GIORNI_TOTALI) {
			if (this.controllaParziale(parziale)) {
				double costoparziale=this.punteggioSoluzione(parziale);
				if(costoparziale<this.costoBest || this.costoBest==0.0) {
					this.costoBest=costoparziale;
					this.best=new ArrayList <SimpleCity>(parziale);
				}
			}
		} else {
			for (Citta c :cities.values()) {
				int costo=c.getRilevamenti().get(l).getUmidita();
				parziale.add(new SimpleCity(c.getNome(),costo));
				cerca(cities,parziale,l+1);
				parziale.remove(l);
			}
		}
	}



	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		double score = 0.0;
		String oldc=null;
		for(SimpleCity sc: soluzioneCandidata) {
			score+=sc.getCosto();
			if (!sc.getNome().equals(oldc)) {
				score+=COST;
				oldc=sc.getNome();
			}
		}
		return score-COST; //1 cambio città
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		HashMap<String,Integer> m = new HashMap<String,Integer>();
		
		for(String l: localitas) {
			m.put(l,0);
		}
		
		int nday=NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN;
		String oldc=null;
		for(SimpleCity sc: parziale) {
			m.replace(sc.getNome(),m.get(sc.getNome())+1);
			if (sc.getNome().equals(oldc)) {
				nday++;
			} else  {
				if (nday>=NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
					nday=0;
					oldc=sc.getNome();
				} else {
					return false;
				}
			}
		}
		
		for (int i=NUMERO_GIORNI_TOTALI-NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN;i<NUMERO_GIORNI_TOTALI;i++) {
		     if (!parziale.get(i).equals(parziale.get(NUMERO_GIORNI_TOTALI-1))) {
		    	 return false;
		     }
		}
				
		for (int v :m.values() ) {
			if (v==0 || v>NUMERO_GIORNI_CITTA_MAX) {
				return false;
			}
		}
		
		return true;
	}

}
