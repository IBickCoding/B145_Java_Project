import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List; 

/**
 * The Button class acts as the manager of all the buttons that appear on the screen. It holds all the logic as to what the buttons do and works
 * in conjuction with the GameBoard class in order to add certain buttons to the game
 * 
 * @author  bickfori@email.uscb.edu 
 * @version Final Version
 */
public class Button extends Actor
{
    /* FIELDS */
    public static final int RESET_BUTTON = 0;
    public static final int LEARN_MORE_BUTTON = 1;
    public static final int EXIT_BUTTON = 2;
    public static final int RULES_BUTTON = 3;
    public static final int LORE_BUTTON = 4;
    public static final int CHANGE_MODE_BUTTON = 5;
    public static final int TOGGLE_MUSIC_BUTTON = 6;

    private int buttonType;
    private Overlay overlay;
    private Button exitButton;
    private boolean isMusicPlaying = true; 

    /* CONSTRUCTOR */
    /**
     *  Initializes the buttons with corresponding images based off the integer value of each button
     *  
     *  @param buttonType   an integer that refers to a specific type of button corresponding to the instance variables above
     */
    public Button(int buttonType)
    {
        this.buttonType = buttonType;
        switch (buttonType) 
        {
            case RESET_BUTTON:
                setImage("ResetButton.png");
                break;

            case LEARN_MORE_BUTTON:
                setImage("LearnMore.png");
                break;

            case EXIT_BUTTON:
                setImage("ExitButton.png");
                break;

            case RULES_BUTTON:
                setImage("rules.png");
                break;

            case LORE_BUTTON:
                setImage("LoreButton.png");
                break;

            case CHANGE_MODE_BUTTON:
                setImage("SmallredButton.png");
                break;

            case TOGGLE_MUSIC_BUTTON:
                setImage("MuteButton.png");
                break;

            default:
                break;
        } // end switch
    } // end 1-arg constructor Button

    /* METHODS */
    /**
     * Checks when buttons are clicked on each `act` cycle, they perform specific actions associated with the name of the button.
     */
    public void act()
    {
        if (Greenfoot.mouseClicked(this))
        {
            switch (buttonType) 
            {
                case RESET_BUTTON:
                    ((GameBoard) getWorld()).resetWorld();
                    break;

                case LEARN_MORE_BUTTON:
                    toggleLearnMore();
                    break;

                case RULES_BUTTON:
                    toggleRules();
                    break;

                case LORE_BUTTON:
                    toggleLore();
                    break;

                case CHANGE_MODE_BUTTON:
                    toggleGameMode();
                    break;

                case TOGGLE_MUSIC_BUTTON:
                    toggleMusic();
                    break;

                case EXIT_BUTTON:
                    exit();
                    break;

                default:
                    break;
            } // end switch
        } // end if
    } // end method act

    /**
     * Toggles music on and off 
     */
    public void toggleMusic()
    {
        if (isMusicPlaying)
        {
            ((GameBoard)getWorld()).stopMusic();
            isMusicPlaying = false;
        } // end if
        else
        {
            ((GameBoard)getWorld()).playMusic();
            isMusicPlaying = true;
        } // end else
    } // end method toggleMusic

    /**
     * Toggles game modes between human and CPU controlled players
     */
    public void toggleGameMode()
    {
        ((GameBoard) getWorld()).cycleGameMode();
    } // end method toggleGameMode

    /**
     * Exits the current overlay/screen
     */
    public void exit()
    {
        getWorld().showText("", getWorld().getWidth() / 2, 360);
        getWorld().showText("", getWorld().getWidth() / 2, 260);
        List<Overlay> overlays = getWorld().getObjects(Overlay.class);
        for (Overlay existingOverlay : overlays) 
        {
            getWorld().removeObject(existingOverlay); // this was needed because simply removing the overlay was not working in Greenfoot. I had to be more aggressive and remove any possible overlay
        } // end for
        getWorld().removeObject(this);
    } // end method exit

    /**
     * Toggles the lore display
     */
    public void toggleLore()
    {
        overlay = new Overlay();
        getWorld().addObject(overlay, getWorld().getWidth() / 2, 360);
        exitButton = new Button(Button.EXIT_BUTTON);
        getWorld().addObject(exitButton, getWorld().getWidth() / 2, getWorld().getHeight() - 100);
        getWorld().showText("You are the commander of the Kla'ed space fleet. You are tasked with \nexfiling from this" + " alien solar system and returning to your home galaxy with \nthe rest of your fleet." +
            " If only it were just that easy... In the area we have \ndetected the Nautolan fleet. These nasty aliens have only one thing on their \nmind, and it is destroying your fleet and the rest of the galaxy." +
            " We need you \nto beat the Nautolan to the Wormhole before they do or I'm certain the \nWormhole will be closed forever. If that is the case... our galaxy and many \nothers may be lost to the Nautolan." +
            "Be careful though, as the Nautolan may \nbattle you. If they fight a lower class ship, I'm sure they will leave it alone \nonce neutralized." +
            "But if they find your Orbiter, they will ensure it is \ncompletely destroyed and your fleet will be lost in space forever without you." +
            "\n\n\n\n\n\n\n\n\nWe are counting on you. Beat those alien scum to the Wormhole!", getWorld().getWidth() / 2, 260);
    } // end method toggleLore

    /**
     * Toggles rules display
     */
    public void toggleRules()
    {
        overlay = new Overlay();
        getWorld().addObject(overlay, getWorld().getWidth() / 2, 360);
        exitButton = new Button(Button.EXIT_BUTTON);
        getWorld().addObject(exitButton, getWorld().getWidth() / 2, getWorld().getHeight() - 100);
        getWorld().showText("Game Rules Can Be Found On:\n" + "https://tinyurl.com/WormholeDash" + "\n\n\n\n\n\n\n\n\nThey can also be found in the ReadMe.txt", getWorld().getWidth() / 2, 360);
    } // end method toggleRules

    /**
     * Toggles learn more display
     */
    public void toggleLearnMore()
    {
        overlay = new Overlay();
        getWorld().addObject(overlay, getWorld().getWidth() / 2, 360);
        exitButton = new Button(Button.EXIT_BUTTON);
        getWorld().addObject(exitButton, getWorld().getWidth() / 2, getWorld().getHeight() - 100);
        getWorld().showText("Wormholes are theoretical holes in the fabric of time and space! \nThey are possibly a way for us to travel faster than the speed of light!" +
                            "\nWormholes served as an inspiration for the theme of this game," + "\nas they provide a way to \"warp\" out of the solar system I have created." +
                            "\n\n\n\n\n\n\n\n\nWant to learn more? Check this out: \nhttps://www.space.com/20881-wormholes.html", getWorld().getWidth() / 2, 360);
    } // end method toggleLearnMore
} // end class Button
