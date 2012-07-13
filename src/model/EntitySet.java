package model;

public class EntitySet<T> 
{
	/**
     * Variable d'instance
     */
    private T valeur;

    
    /**
     * Constructeur par d�faut
     */
	public EntitySet() 
	{
		this.valeur = null;
	}
        
        
        /**
         * Constructeur avec param�tre
         * Inconnu pour l'instant
         * @param val
         */
        public EntitySet(T val){
                this.valeur = val;
        }
        
        
        /**
         * D�finit la valeur avec le param�tre
         * @param val
         */
        public void setValeur(T val){
                this.valeur = val;
        }
        
        /**
         * retourne la valeur d�j� "cast�e" par la signature de la m�thode !
         * @return
         */
        public T getValeur(){
                return this.valeur;
        }       
	}

