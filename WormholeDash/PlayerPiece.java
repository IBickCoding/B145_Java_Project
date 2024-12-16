import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Represents a player's game piece -- player 1's pieces (for player index 0) are red spaceships,
 * player 2's pieces (for player index 1) are blue spaceships.
 * 
 * @author  bickfori@email.uscb.edu
 * @version Final Version
 */
public class PlayerPiece extends Actor 
{
    /* FIELDS */
    private boolean moveable;   
    private int originalXcoord;
    private int originalYcoord;
    
    private int gameBoardLocationIndex;
    private int targetGameBoardLocationIndex; 
    private int playerIndex; // 0 for red (player NUMBER 1), 1 for blue (player NUMBER 2)
    
    private int pieceTier;
    private int wins = 0;
    private int losses = 0;
    
    private String shipTierName = "";
    
    /* CONSTRUCTORS */
    /**
     * Initializes a newly-instantiated PlayerPiece object
     * 
     * @param playerIndex       the index of the player that "owns" this PlayerPiece (0 = red, 1 = blue)
     * @param originalXcoord    the original X-coordinate of where this playerPiece was located for this player's "starting zone"
     * @param originalYcoord    the original Y-coordinate of where this playerPiece was located for this player's "starting zone"
     * @param pieceTier         the tier level of a given playerPiece
     */
    public PlayerPiece(int playerIndex, int originalXcoord, int originalYcoord, int pieceTier)
    {
        this.playerIndex = playerIndex;
        this.originalXcoord = originalXcoord;
        this.originalYcoord = originalYcoord;
        this.pieceTier = pieceTier;
        
        // initialize other instance variables that aren't assigned using parameter values
        moveable = true;
        gameBoardLocationIndex = -1; // starting position
        targetGameBoardLocationIndex = -1; 
    
        setImageForPiece();
    } // end PlayerPiece 4-arg constructor
    
    /* METHODS */
    /**
     * The `act` method for the PlayerPiece is fairly simple
     * since most of the game logic is handled by the GameBoard class;
     * but we will use the `act` method to set the opacity (transparency)
     * of the object depending on whether or not the piece is moveable
     */
    public void act()
    {
        if ( moveable ) 
        {
            getImage().setTransparency( 255 ); // if moveable, piece is fully opaque (0 = transparent, 255 = opaque)
        } // end if
        else 
        {
            getImage().setTransparency( 128 ); // if moveable, piece is 50% transparent
        } // end else
    } // end method act
    
    /**
     * This sets the appropriate image based off the player's team and the tier of the playerPiece.
     */
    public void setImageForPiece()
    {
        String imageSelection;
        
        if (playerIndex == 0)
        {
            switch (pieceTier)
            {
                case 1:
                    imageSelection = "Kla'ed - Fighter - Base.png";
                    break;
                case 2:
                    imageSelection = "Kla'ed - Frigate - Base.png";
                    break;
                case 3:
                    imageSelection = "Kla'ed - Dreadnought - Base.png";
                default:
                    imageSelection = "Kla'ed - Dreadnought - Base.png";
            } // end switch
        } // end if
        else
        {
             switch (pieceTier) 
             {
                case 1:
                    imageSelection = "Nautolan Ship - Bomber - Base.png";
                    break;
                case 2:
                    imageSelection = "Nautolan Ship - Scout - Base.png";
                    break;
                case 3:
                    imageSelection = "Nautolan Ship - Dreadnought - Base.png";
                    break;
                default:
                    imageSelection = "Nautolan Ship - Scout - Base.png";
            } // end switch
        } // end else
        
        setImage(new GreenfootImage(imageSelection));
    }
        
    /**
     * Getter method for retrieving this player piece's `moveable` state value (true or false)
     */
    public boolean isMoveable() 
    {
        return moveable;
    } // end method isMoveable;
    
    /**
     * Updates the moveable state of this PlayerPiece object
     * 
     * @param moveable  whether this object should be moveable or not
     */
    public void setMoveable( boolean moveable ) 
    {
        this.moveable = moveable; 
    } // end method setMoveable
    
    /**
     * Getter method for retrieving the original X-coordinate of this PlayerPiece
     * 
     * @return originalXcoord   returns the original x-coordinate of a PlayerPiece prior to movement
     */
    public int getOriginalXcoord()
    {
        return originalXcoord;
    } // end method getOriginalXcoord
    
    /**
     * Getter method for retrieving the original Y-coordinate of this PlayerPiece
     * 
     * @return originalYcoord   returns the original y-coordinate of a PlayerPiece prior to movement
     */
    public int getOriginalYcoord()
    {
        return originalYcoord;
    } // end method getOriginalYcoord
    
    /**
     * Getter method for retrieving the current gameBoardLocationIndex for this PlayerPiece
     * 
     * @return gameBoardLocationIndex   returns the index value of where the current PlayerPiece is on the board
     */
    public int getGameBoardLocationIndex() 
    {
        return gameBoardLocationIndex;
    } // end method getGameBoardLocationIndex
    
    /**
     * Setter method for UPDATING the gameBoardLocationIndex for this PlayerPiece
     * 
     * @param gameBoardLocationIndex    the current gameBoardLocationIndex 
     */
    public void setGameBoardLocationIndex(int gameBoardLocationIndex) 
    {
        this.gameBoardLocationIndex = gameBoardLocationIndex;
    } // end method getGameBoardLocationIndex
    
    /**
     * Getter method for retrieving the possible game board location index based on the current die roll
     * 
     * @return targetGameBoardLocationIndex returns the location the index value for the PlayerPiece based on the current die roll if it were the piece to be moved
     */
    public int getTargetGameBoardLocationIndex() {
        return targetGameBoardLocationIndex;
    } // end method gettargetGameBoardLocationIndex
    
    /**
     * Setter method for updating the possible TARGET game board location index based on the current die roll
     * 
     * @param targetGameBoardLocationIndex     The possible TARGET game board location index for this piece, given the current die roll
     */
    public void setTargetGameBoardLocationIndex( int targetGameBoardLocationIndex ) 
    {
        this.targetGameBoardLocationIndex = targetGameBoardLocationIndex;
    } // end method setTargetGameBoardLocationIndex
    
    /**
     * Getter method for getting the tier of a piece.
     * 
     * @returns pieceTier   the tier level for a given piece
     */
    public int getPieceTier( )
    {
        return pieceTier;
    } // end method getPieceTier
    
    /**
     * Getter method for getting the playerIndex of a particular piece
     * 
     * @returns playerIndex     the index number of which player the piece belongs to
     */
    public int getPlayerIndex()
    {
          return playerIndex;
    } // end method getPlayerIndex
    
    /**
     * Getter method for a piece's wins in battle (this is being added but may not be used, this is added for an optional feature of a MVP piece at the end of the game)
     * 
     * @returns wins    the number of wins in battles for a particular piece
     */
    public int getWins()
    {
        return wins;
    } // end method getWins
    
    /**
     * Getter method for a piece's losses in battle
     * 
     * @returns losses  the number of losses in battles for a particular piece
     */
    public int getLosses()
    {
        return losses;
    } // end method getLosses
    
    /**
     * This allows a piece to increment its win value for battles
     */
    public void incrementWins()
    {
        wins++;
    } // end method incrementWins
    
    /**
     * This allows a piece to increment its loss value for battles
     */
    public void incrementLosses()
    {
        losses++;
    } // end method incrementLosses
    
    /**
     * Setter for the name of each tier of spaceship to a historical reference.
     * Although the names are set individually for each ship, they name corresponds to the tier level of the ship
     * 
     * @param name   The name of the tier of ship
     */
    public void setShipTierName(String name)
    {
        shipTierName = name;
    } // end method setShipTierName
    
    /**
     * Getter for the name of each tier of spaceship
     * 
     * @return shipTierName     The name for the space ship
     */
    public String getShipTierName()
    {
        return shipTierName;
    } // end method getShipTierName
    
} // end class PlayerPiece
