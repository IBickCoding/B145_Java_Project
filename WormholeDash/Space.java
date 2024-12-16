import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Represents a GameBoard space and its associated attributes (i.e., is it a reroll space or not, is it occupied 
 * by a given player's piece or not, etc.)
 * 
 * @author  bickfori@email.uscb.edu
 * @version Final Version
 */
public class Space extends Actor
{
    /* FIELDS */
    private boolean[] occupiedByPieceForPlayerIndex;
    private boolean rerollSpace;
    private String spaceName = "";

    /* CONSTRUCTORS */
    /**
     * Initializes the attributes of each Space object
     * 
     * @param rerollSpace   true if this is a reroll space, false otherwise
     */
    public Space( boolean rerollSpace ) 
    {
        occupiedByPieceForPlayerIndex = new boolean[]{false, false}; // initializes each space to originally be occupied by no player piece
        this.rerollSpace = rerollSpace;
        if ( this.rerollSpace ) 
        {
            setImage( new GreenfootImage("Lava.png") );
        } // end if
        else 
        {
            setImage( new GreenfootImage("Terran.png") );
        } // end else
    } // end 1-arg Space constructor

    /* METHODS */
    /**
     * Getter method returns `true` if this space is occupied by a piece
     * belonging to the player corresponding to playerIndex;
     * 
     * @param playerIndex   the index of the player whose piece may be occupying this Space
     */
    public boolean isOccupiedByPieceForPlayerIndex(int playerIndex) 
    {
        return occupiedByPieceForPlayerIndex[playerIndex];
    } // end method isOccupiedPieceForPlayerIndex

    /**
     * Setter method updates the value of the current ("this") object's value of who currently occupies the space.
     * 
     * @param playerIndex   the index of the player whose piece may be occupying this Space
     * @param occupied      true if this Space is now occupied by a piece belonging to the given player
     */
    public void setOccupiedByPieceForPlayerIndex( int playerIndex, boolean occupied ) 
    {
        this.occupiedByPieceForPlayerIndex[playerIndex] = occupied;
    } // end method setOccupiedByPieceForPlayerIndex

    /**
     * Getter method returns `true` if this space is a reroll space
     */
    public boolean isRerollSpace() 
    {
        return rerollSpace;
    } // end method isRerollSpace

    /**
     * Setter for the name of planets and stars as part of my tangential learning component
     * 
     * @param name    the name of each of the planets and stars on the gameboard.
     */
    public void setSpaceName(String name)
    {
        spaceName = name;
    } // end method setSpaceName

    /**
     * Getter for acquiring the name of each space to later be displayed for the player.
     * 
     * @return spaceName  the name of the space
     */
    public String getSpaceName()
    {
        return spaceName;
    } // end method getSpaceName
} // end class Space
