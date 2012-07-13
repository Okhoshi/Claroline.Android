package model;

public class EntitySet<T> 
{
	/**
     * Variable d'instance
     */
    private T valeur;

    
    /**
     * Constructeur par défaut
     */
	public EntitySet() 
	{
		this.valeur = null;
	}
        
        
        /**
         * Constructeur avec paramètre
         * Inconnu pour l'instant
         * @param val
         */
        public EntitySet(T val){
                this.valeur = val;
        }
        
        
        /**
         * Définit la valeur avec le paramètre
         * @param val
         */
        public void setValeur(T val){
                this.valeur = val;
        }
        
        /**
         * retourne la valeur déjà "castée" par la signature de la méthode !
         * @return
         */
        public T getValeur(){
                return this.valeur;
        }       
	}

