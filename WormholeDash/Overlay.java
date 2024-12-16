import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Overlay acts as a filter over the gameboard when shifting focus to a button that displays something.
 * 
 * @author  bickfori@email.uscb.edu 
 * @version Final Version
 */
public class Overlay extends Actor
{
    /* CONSTRUCTOR */
    /**
     * Initilializes how an overlay will appear when used to shift focus off the board
     */
    public Overlay()
    {
        GreenfootImage overlay = new GreenfootImage(720, 720);
        overlay.setColor(new Color( 0, 0, 0, 180));
        overlay.fill();
        
        setImage(overlay);
    } // end no-arg constructor Overlay
} // end class Overlay
