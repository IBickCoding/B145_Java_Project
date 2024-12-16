import greenfoot.*;  // imports Actor, World, Greenfoot, GreenfootImage

/**
 * This is the physical board for my Wormhole Dash game. The gameboard acts as a level manager
 * that controls much of the game (e.g. game logic, game state, etc.)
 * 
 * @author bickfori@email.uscb.edu
 * @version Final Version
 */

public class GameBoard extends World
{
    /* FIELDS */  
    private boolean[] isPlayerHuman;

    private Space[] spaces;
    
    public static final int HEIGHT = 720;
    public static final int WIDTH = 720;

    private PlayerPiece[][] playerPieces;
    private PlayerPiece[][] playerPieceTier;
    private PlayerPiece[] tiers;
    private final int NUMBER_OF_PIECES_PER_PLAYER = 7; 
    private final int DELAY_LENGTH = 30;                

    private Button resetButton;
    private Button learnMoreButton;
    private Button rulesButton;
    private Button loreButton;
    private Button changeModeButton;

    private int[] redStartYCoords;                    
    private int[] blueStartYCoords;

    private Space[][] movementPathForPlayerIndex;    

    private Die die;                                    
    private int dieRollValue;                           
    private final int DIE_TEXT_VERTICAL_OFFSET = 50;    

    private int state;
    
    private GreenfootSound music = new GreenfootSound("retro-8bit-happy-adventure-videogame-music-246635.mp3");

    private boolean readyToExitState;                   
    private boolean starSpaceRollAgain;      
    private int[] goalCountForPlayerIndex;
    
    private boolean gameOver;
    private boolean gameStarted = false;
    
    private TitleScreen titleScreen;

    /* PUBLIC CONSTANTS for keeping track of the game's overall state */
    public static final int BOARD_SETUP = 0;            
    public static final int PLAYER1_ROLL_DIE = 1;       
    public static final int PLAYER1_MOVE_RED = 2;      
    public static final int PLAYER2_ROLL_DIE = 3;       
    public static final int PLAYER2_MOVE_BLUE = 4;   
    public static final int PLAYER1_WIN = 5;
    public static final int PLAYER1_WIN2 = 7;
    public static final int PLAYER2_WIN = 6;
    public static final int PLAYER2_WIN2 = 8;              

    /* CONSTRUCTOR(S) */
    /**
     * Initialize the GameBoard object and its variable fields
     */
    public GameBoard() 
    {
        super(WIDTH, HEIGHT, 1, false);

        isPlayerHuman = new boolean[]{ true, false }; // true = human, false = CPU

        spaces = new Space[20];

        playerPieces = new PlayerPiece[2][7]; // Always 2 players, with (normally) 7 pieces per player
        state = BOARD_SETUP;

        readyToExitState = false;
        starSpaceRollAgain = false;
        
        gameOver = false;
        
        goalCountForPlayerIndex = new int[2]; // set size of array to be 2 elements long 
        goalCountForPlayerIndex[0] = 0; // at start of game, no reds in the goal
        goalCountForPlayerIndex[1] = 0; // at start of game, no blues in the goal

        redStartYCoords = new int[]{ HEIGHT - 210, HEIGHT - 180, HEIGHT - 150, HEIGHT - 210, HEIGHT - 180, HEIGHT - 150, HEIGHT - 210 };    
        blueStartYCoords = new int[]{ 90, 120, 150, 87, 117, 147, 90 };

        prepare();
        
        titleScreen = new TitleScreen();
        addObject(titleScreen,getWidth()/2, getHeight()/2 );
    } // end GameBoard no-arg constructor

    /* METHODS */
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world, along with additional HUD buttons
     */
    private void prepare()
    {
        setBackground("starryGalaxy.jpg");

        // set reroll spaces
        for ( int spaceIndex = 0; spaceIndex < 20; spaceIndex++ )
        {
            if ( spaceIndex == 3 || spaceIndex == 7 || spaceIndex == 11 || spaceIndex == 15 || spaceIndex == 19  ) // star spaces (re-rolls)
            {
                spaces[spaceIndex] = new Space( true ); // is a star space
            } // end if
            else // otherwise, normal planet space
            {
                spaces[spaceIndex] = new Space( false ); // is a planet space
            } // end else
        } // end for

        // add spaces to the board
        addObject( spaces[0], 445, 400 );
        addObject( spaces[1], 445, 485 );
        addObject( spaces[2], 445, 570 );
        addObject( spaces[3], 445, 655 );
        addObject( spaces[4], 275, 400 );
        addObject( spaces[5], 275, 485 );
        addObject( spaces[6], 275, 570 );
        addObject( spaces[7], 275, 655 );
        addObject( spaces[8], 360, 655 );
        addObject( spaces[9], 360, 570 );
        addObject( spaces[10], 360, 485 );
        addObject( spaces[11], 360, 400 );
        addObject( spaces[12], 360, 315 );
        addObject( spaces[13], 360, 230 );
        addObject( spaces[14], 360, 145 );
        addObject( spaces[15], 275, 145 );
        addObject( spaces[16], 275, 60 );
        addObject( spaces[17], 360, 60 );
        addObject( spaces[18], 445, 60 );
        addObject( spaces[19], 445, 145 );

        // naming spaces for my tangential learning component
        nameSpaces();

        // add wormholes to end of movement paths, they do nothing and are just aesthetic
        addObject( new Wormhole(), 275, 230);
        addObject( new Wormhole(), 445, 230);

        // array for referencing different spaces on the board for both players movement paths
        movementPathForPlayerIndex = new Space[2][20]; 

        // movement path for red player, or player 1
        movementPathForPlayerIndex[0][0] = spaces[0];
        movementPathForPlayerIndex[0][1] = spaces[1]; 
        movementPathForPlayerIndex[0][2] = spaces[2]; 
        movementPathForPlayerIndex[0][3] = spaces[3]; 
        movementPathForPlayerIndex[0][4] = spaces[8]; 
        movementPathForPlayerIndex[0][5] = spaces[9]; 
        movementPathForPlayerIndex[0][6] = spaces[10];
        movementPathForPlayerIndex[0][7] = spaces[11];
        movementPathForPlayerIndex[0][8] = spaces[12]; 
        movementPathForPlayerIndex[0][9] = spaces[13]; 
        movementPathForPlayerIndex[0][10] = spaces[14]; 
        movementPathForPlayerIndex[0][11] = spaces[15]; 
        movementPathForPlayerIndex[0][12] = spaces[16]; 
        movementPathForPlayerIndex[0][13] = spaces[17];
        movementPathForPlayerIndex[0][14] = spaces[18];
        movementPathForPlayerIndex[0][15] = spaces[19];

        // movement path for blue player, or player 2
        movementPathForPlayerIndex[1][0] = spaces[4];
        movementPathForPlayerIndex[1][1] = spaces[5]; 
        movementPathForPlayerIndex[1][2] = spaces[6]; 
        movementPathForPlayerIndex[1][3] = spaces[7]; 
        movementPathForPlayerIndex[1][4] = spaces[8]; 
        movementPathForPlayerIndex[1][5] = spaces[9]; 
        movementPathForPlayerIndex[1][6] = spaces[10];
        movementPathForPlayerIndex[1][7] = spaces[11];
        movementPathForPlayerIndex[1][8] = spaces[12]; 
        movementPathForPlayerIndex[1][9] = spaces[13]; 
        movementPathForPlayerIndex[1][10] = spaces[14]; 
        movementPathForPlayerIndex[1][11] = spaces[19]; 
        movementPathForPlayerIndex[1][12] = spaces[18]; 
        movementPathForPlayerIndex[1][13] = spaces[17];
        movementPathForPlayerIndex[1][14] = spaces[16];
        movementPathForPlayerIndex[1][15] = spaces[15];

        // add red team pieces
        // Tier 1 pieces (red)
        for (int redIndex = 0; redIndex < 3; redIndex++) {
            int redXcoord = (int)(0.75 * WIDTH);
            int redYcoord = redStartYCoords[redIndex];
            playerPieces[0][redIndex] = new PlayerPiece(0, redXcoord, redYcoord, 1);
            addObject(playerPieces[0][redIndex], redXcoord, redYcoord);
        }

        // Tier 2 pieces (red)
        for (int redIndex = 3; redIndex < 6; redIndex++) {
            int redXcoord = (int)(0.8 * WIDTH);
            int redYcoord = redStartYCoords[redIndex];
            playerPieces[0][redIndex] = new PlayerPiece(0, redXcoord, redYcoord, 2);
            addObject(playerPieces[0][redIndex], redXcoord, redYcoord);
        }

        // Tier 3 piece (red)
        playerPieces[0][6] = new PlayerPiece(0, (int)(0.87 * WIDTH), redStartYCoords[6], 3);
        addObject(playerPieces[0][6], (int)(0.87 * WIDTH), redStartYCoords[6]);

        // add blue team pieces
        // Tier 1 pieces (blue)
        for (int blueIndex = 0; blueIndex < 3; blueIndex++) {
            int blueXcoord = (int)(0.1 * WIDTH);
            int blueYcoord = blueStartYCoords[blueIndex];
            playerPieces[1][blueIndex] = new PlayerPiece(1, blueXcoord, blueYcoord, 1);
            addObject(playerPieces[1][blueIndex], blueXcoord, blueYcoord);
        }

        // Tier 2 pieces (blue)
        for (int blueIndex = 3; blueIndex < 6; blueIndex++) {
            int blueXcoord = (int)(0.16 * WIDTH);
            int blueYcoord = blueStartYCoords[blueIndex];
            playerPieces[1][blueIndex] = new PlayerPiece(1, blueXcoord, blueYcoord, 2);
            addObject(playerPieces[1][blueIndex], blueXcoord, blueYcoord);
        }

        // Tier 3 piece (blue)
        playerPieces[1][6] = new PlayerPiece(1, (int)(0.23 * WIDTH), blueStartYCoords[6], 3);
        addObject(playerPieces[1][6], (int)(0.23 * WIDTH), blueStartYCoords[6]);

        // naming all the blue and red pieces for my tangential learning component
        nameShips();

        // add die
        die = new Die();
        addObject( die, (int)(0.8 * WIDTH), (int)(0.5 * HEIGHT ) ); 

        // add buttons that stay in place, in order of how they appear top to bottom
        rulesButton = new Button(Button.RULES_BUTTON);
        addObject(rulesButton, 670, 100);

        learnMoreButton = new Button(Button.LEARN_MORE_BUTTON);
        addObject(learnMoreButton, 670 ,150);

        loreButton = new Button(Button.LORE_BUTTON);
        addObject(loreButton, 670, 200);
        
        loreButton = new Button(Button.TOGGLE_MUSIC_BUTTON);
        addObject(loreButton, 670, 270);

        changeModeButton = new Button (Button.CHANGE_MODE_BUTTON);
        addObject(changeModeButton, 670 , HEIGHT - 50);
    } // end method prepare

    /**
     * Depending on the game's state, determines what the GameBoard does during
     * each frame or cycle of the `act` method
     */
    public void act()
    {
        startGame(); // checks to see if the game has started by clicking on the title screen
        if (gameStarted)
        {
            displayGameMode(); // we check and update the gamemode every act cycle to accurately display and change the gamemode at the player's will
            
            switch ( state ) 
            {
    
                case PLAYER1_ROLL_DIE:
                    determineDieRollValueForPlayerIndex(0);
                    break; // continue checking for die roll until one is detected
    
                case PLAYER1_MOVE_RED:
                    determineWhichPiecesAreMoveableForPlayerIndex( 0 );
                    if ( !readyToExitState ) 
                    {
                        determineMoveForPlayerIndex(0);
                        return; // exits `act` method (and skips additional method calls for the current `act` method call)
                        // because player 1 (red, for player index 0) hasn't selected a piece to move yet
                    } // end if
    
                    // only way we end up here is if `readyToExitState` is `true` (for the current value of `state`)
                    makeAllPiecesMoveableAgainForPlayerIndex(0);  
                    updateGameStateAfterTurnForPlayerIndex(0);  
                    break; 
    
                case PLAYER2_ROLL_DIE:
                    determineDieRollValueForPlayerIndex(1); 
                    break;
    
                case PLAYER2_MOVE_BLUE:
                    determineWhichPiecesAreMoveableForPlayerIndex( 1 );
                    if ( !readyToExitState ) 
                    {
                        determineMoveForPlayerIndex(1);
                        return; // exits `act` method (and skips additional method calls for the current `act` method call)
                        // because player 2 (blues, for player index 1) hasn't selected a piece to move yet
    
                    } // end if ( !readyToExitState )
    
                    // only way we end up here is if `readyToExitState` is `true` (for the current value of `state`)
                    makeAllPiecesMoveableAgainForPlayerIndex(1);        
                    updateGameStateAfterTurnForPlayerIndex(1);  
                    break;
    
                case PLAYER1_WIN: // win condition for red team getting all pieces in goal
                    if(!gameOver)
                    {
                        showText( "\nPlayer 1\nWINS!!", getWidth()/2, getHeight()/2 );
                        resetButton = new Button(Button.RESET_BUTTON);
                        addObject(new Overlay(), WIDTH / 2 , 360);
                        addObject(resetButton, die.getX(), die.getY() - 100 );
                        stopMusic();
                        gameOver = true;
                    }
                    break;
    
                case PLAYER1_WIN2: // win condition for red team capturing the blue team's tier 3 piece
                    if(!gameOver)
                    {
                        showText( "\nPlayer 1 WINS, \nthey have captured Player 2's Tier 3 piece!!", getWidth()/2, getHeight()/2 );
                        resetButton = new Button(Button.RESET_BUTTON);
                        addObject(new Overlay(), WIDTH / 2, 360);
                        addObject(resetButton, die.getX(), die.getY() - 100 );
                        stopMusic();
                        gameOver = true;
                    }
                    break;
    
                case PLAYER2_WIN2: // win condition for blue team capturing the red team's tier 3 piece
                    if (!gameOver)
                    {
                        showText( "\nPlayer 2 WINS, \nthey have captured Player 1's Tier 3 piece!!", getWidth()/2, getHeight()/2 );
                        resetButton = new Button(Button.RESET_BUTTON);
                        addObject(new Overlay(), WIDTH / 2, 360);
                        addObject(resetButton, die.getX(), die.getY() - 100 );
                        stopMusic();
                        gameOver = true;
                    }
                    break;
    
                case PLAYER2_WIN: // win condition for blue team getting all pieces in goal
                    if (!gameOver)
                    {
                        showText( "\nPlayer 2\nWINS!!", getWidth()/2, getHeight()/2 );
                        resetButton = new Button(Button.RESET_BUTTON);
                        addObject( new Overlay(), WIDTH / 2, 360);
                        addObject(resetButton, die.getX(), die.getY() - 100 );
                        stopMusic();
                        gameOver = true;
                    }
                    break;
    
                default:
                    break;
            } // end switch
        } // end if
    } // end method act

    /**
     * Starts the background music and sets its volume to an appropriate level
     */
    public void playMusic()
    {
        music.playLoop();
        music.setVolume(20);
    } // end method playMusic
    
    /**
     * Stops the background music
     */
    public void stopMusic()
    {
        music.stop();
    } // end method stopMusic

    /**
     * Determines the die roll for the current player. If the player is human,
     * then the human player clicks on the die object to roll the die; otherwise,
     * the die is automatically rolled by the CPU.
     * 
     * @param playerIndex  the index of the player rolling the die
     */
    public void determineDieRollValueForPlayerIndex( int playerIndex )
    {
        // Determine player number (not index), for use in string expressions later in this method.
        String playerNumberString = ( playerIndex == 0 ? "1" : "2" );

        // NOTE: Using a "guard condition" to avoid the need for nesting if-statements
        if ( isPlayerHuman[playerIndex] && !Greenfoot.mouseClicked(die) )
        {
            showText( "\nPlayer " + playerNumberString + "\nclick to roll", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET );
            return; // skip remaining statements and return to this method's caller
        } // end if
        Greenfoot.playSound("diceRoll.mp3");
        dieRollValue = (Greenfoot.getRandomNumber(3) + 1); // rolls a 1 to 3, inclusively

        if (dieRollValue == 1)
        {
            this.die.setImage( new GreenfootImage("dieWhite_border1.png") );
        } // end if
        else if (dieRollValue == 2)
        {
            this.die.setImage( new GreenfootImage("dieWhite_border2.png") );
        } // end else if
        else
        {
            this.die.setImage( new GreenfootImage("dieWhite_border3.png") );
        } // end else

        showText( "\nPlayer " + playerNumberString + "\nrolls a " + dieRollValue, die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET );
        Greenfoot.delay(DELAY_LENGTH); // allow time to view on-screen message

        // this was needed for force advancing the state, it fixed a bug where the game did not allow players to click the die and move a piece after.
        if (playerIndex == 0)
        {
            state = PLAYER1_MOVE_RED;
        } //end if
        else
        {
            state = PLAYER2_MOVE_BLUE;
        } // end else
    } // end method determineDieRollValueForPlayerIndex

    /**
     * Determines the die roll for the current player. If the player is human,
     * then the human player clicks on the die object to roll the die; otherwise,
     * the die is automatically rolled by the CPU. This is similiar but has key differences from the previous
     * determine die roll method. To keep the battle die logic seperate and clear, it has its own
     * method here for battles. Additionally, this method uses a "6-sided die".
     * 
     * @param playerIndex  the index of the player rolling the die
     */
    public int determineDieRollBattle( int playerIndex )
    {
        
        // Determine player number (not index), for use in string expressions later in this method.
        String playerNumberString = ( playerIndex == 0 ? "1" : "2" );
        int dieBattleRollValue = 0; // initial die roll value  compare
        Greenfoot.playSound("diceRoll.mp3");
        dieBattleRollValue = (Greenfoot.getRandomNumber(6) + 1); // rolls a 1 to 6, inclusively. automatic for player and cpu to save time and seemless battles

        switch (dieBattleRollValue) 
        {
            case 1:
                this.die.setImage(new GreenfootImage("dieWhite_border1.png"));
                break;
            case 2:
                this.die.setImage(new GreenfootImage("dieWhite_border2.png"));
                break;
            case 3:
                this.die.setImage(new GreenfootImage("dieWhite_border3.png"));
                break;
            case 4:
                this.die.setImage(new GreenfootImage("dieWhite_border4.png"));
                break;
            case 5:
                this.die.setImage(new GreenfootImage("dieWhite_border5.png"));
                break;
            default:
                this.die.setImage(new GreenfootImage("dieWhite_border6.png"));
                break;
        } // end switch

        showText( "\nPlayer " + playerNumberString + "\nrolls a " + dieBattleRollValue, die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET );
        Greenfoot.delay(DELAY_LENGTH); // allow time to view on-screen message
        return dieBattleRollValue;
    } // end method determineDieRollBattle

    /**
     * Routine for determining which reds are moveable
     * 
     * @param playerIndex   index of the player whose turn is currently active
     */
    public void determineWhichPiecesAreMoveableForPlayerIndex( int playerIndex ) 
    {
        // first check to see which of this player's pieces are moveable 
        // for the given die roll value 
        for ( int playerPieceIndex = 0; playerPieceIndex < playerPieces[playerIndex].length; playerPieceIndex++ ) 
        {    
            // determine index of opposing player using simple arithmetic (no if-statement needed!)
            int opposingPlayerIndex = 1 - playerIndex; // if playerIndex = 1, opposingPlayerIndex = 1 - 1 = 0
                                                       // if playerIndex = 0, opposingPlayerIndex = 1 - 0 = 1

            playerPieces[playerIndex][playerPieceIndex].setMoveable( false ); // "default" state

            // call a "getter" method to retrieve the player piece's current position on the game board 
            int currentPlayerPieceGameBoardLocationIndex = 
                playerPieces[playerIndex][playerPieceIndex].getGameBoardLocationIndex();

            // Here, we "look ahead" by die roll value to determine "target" array index for this piece
            int playerPieceTargetGameBoardLocationIndex = currentPlayerPieceGameBoardLocationIndex + dieRollValue;

            // if the player piece is ALREADY currently in the goal zone, then it shouldn't be moveable
            if ( currentPlayerPieceGameBoardLocationIndex == 16 )
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable( false );
                continue; // we know this piece is NOT moveable so we can skip the remaining
                          // statements in this iteration 
            } // end if

            // if the target space IS the goal, then this piece IS moveable
            if ( playerPieceTargetGameBoardLocationIndex == 16 ) 
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable( true );
                playerPieces[playerIndex][playerPieceIndex].setTargetGameBoardLocationIndex(playerPieceTargetGameBoardLocationIndex);
                continue; // we know this piece IS moveable AND we know it's NOT in the start zone,
                          // so we skip remaining statements in `for` loop 
                          // BUT we will continue w/next iteration so we can determine if any other pieces are moveable
            } // end if

            // if the target space for this piece is BEYOND the goal (i.e., if die roll is too high to exactly "land on" the goal)
            // then this piece is NOT moveable
            if (playerPieceTargetGameBoardLocationIndex > 16 ) 
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable( false );
                continue; // we know this piece is NOT moveable, so we 
                          // skip remaining statements in `for` loop but continue w/next iteration
            } // end if

            // Check to see if the target space is occupied by one of the current player's pieces,
            // if so, then we CANNOT move to that space
            if ( movementPathForPlayerIndex[playerIndex][ playerPieceTargetGameBoardLocationIndex ].isOccupiedByPieceForPlayerIndex(playerIndex) ) 
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable( false );
                continue; // we know this piece is NOT moveable, so we 
                          // skip remaining statements in `for` loop but continue w/next iteration
            } // end if

            // Check to see if the target space meets both of these conditions:
            // 1) is occupied by one of the OPPOSING player's pieces
            // AND
            // 2) is NOT a safe space
            // ...if BOTH conditions are true, then this piece CAN be moved to that space
            if ( movementPathForPlayerIndex[playerIndex][ playerPieceTargetGameBoardLocationIndex ].isOccupiedByPieceForPlayerIndex(opposingPlayerIndex))  
            {
                playerPieces[playerIndex][playerPieceIndex].setMoveable( true );
                playerPieces[playerIndex][playerPieceIndex].setTargetGameBoardLocationIndex(playerPieceTargetGameBoardLocationIndex);
            } // end if

            // otherwise, if we made it this far, we assume that NOTHING is preventing this piece from being moveable
            playerPieces[playerIndex][playerPieceIndex].setMoveable( true );
            playerPieces[playerIndex][playerPieceIndex].setTargetGameBoardLocationIndex(playerPieceTargetGameBoardLocationIndex);
        } // end for
    } // end method determineWhichPiecesAreMoveableForPlayerIndex

    /**
     * Determines which of the player's moveable pieces will actually be moved
     * 
     * @param playerIndex   the index of the player currently moving 
     */
    public void determineMoveForPlayerIndex( int playerIndex )
    {
        int countOfPlayerPiecesThatAreNotMoveable = 0;

        // First, check to see if there are actually any moves to make
        for ( int playerPieceIndex = 0; playerPieceIndex < NUMBER_OF_PIECES_PER_PLAYER; playerPieceIndex++ )
        {
            PlayerPiece currentPlayerPieceToCheck = playerPieces[playerIndex][playerPieceIndex];

            if ( !currentPlayerPieceToCheck.isMoveable() ) 
            {
                countOfPlayerPiecesThatAreNotMoveable++;   

                if ( countOfPlayerPiecesThatAreNotMoveable == NUMBER_OF_PIECES_PER_PLAYER )
                {
                    showText( "No moves!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET );
                    Greenfoot.delay(DELAY_LENGTH); 
                    readyToExitState = true;
                    return; // terminate method (skipping all statements below) and return to method caller
                } // end INNER if

            } // end OUTER if 
        } // end for

        // If you've gotten this far, then there is at least one moveable piece,
        // so we loop again through the moveable pieces to see which will be moved
        for ( int playerPieceIndex = 0; playerPieceIndex < NUMBER_OF_PIECES_PER_PLAYER; playerPieceIndex++ )
        {
            PlayerPiece currentPlayerPieceToCheck = playerPieces[playerIndex][playerPieceIndex];

            if ( currentPlayerPieceToCheck.isMoveable() )
            {
                String playerPieceName = playerIndex == 0 ? "red" : "blue";
                showText( "\n\n\nSelect\n" + playerPieceName + "\nto move\n" + dieRollValue + (dieRollValue == 1 ? " space" : " spaces"), 
                         die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET );

                if ( isPlayerHuman[playerIndex] && Greenfoot.mouseClicked(currentPlayerPieceToCheck) ) 
                {                                                                          
                    handleSelectedPieceForPlayerIndex( playerIndex, currentPlayerPieceToCheck );
                    readyToExitState = true; // now that a piece is selected (here, by the human player), the game will update its state 
                    return;                  // move has been made, so we exit the method early and return to method caller

                } // end inner if 

                // if we've gotten to this point in the code, then we allow the CPU to determine which piece to move
                // (Specifically, we'll use a random number generator to simulate a 30% chance of the 
                //  CPU "mouse-clicking" on THIS piece -- it's not "smart" AI, but it works well enough)
                if ( !isPlayerHuman[playerIndex] && Greenfoot.getRandomNumber(100) < 30 ) 
                {
                    Greenfoot.delay(DELAY_LENGTH);

                    showText( "\n\nPlayer "+ (playerIndex + 1) + "\nmoves\n" + " a " + (playerPieces[playerIndex][playerPieceIndex].getShipTierName()) + " ship!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET );
                    Greenfoot.delay(DELAY_LENGTH);

                    handleSelectedPieceForPlayerIndex( playerIndex, currentPlayerPieceToCheck );

                    readyToExitState = true; // now that a piece is selected (here, by the CPU), the game will update its state 
                    return;                  // move has been made, so we exit the method early and return to method caller

                } // end inner if 
            } // end outer if
        } // end for
    } // end method determineMoveForPlayerIndex

    /**
     * Updates the given player piece's location index along the movement path for the given playerIndex
     * 
     * @param playerIndex           the index of the player moving the selected piece
     * @param selectedPlayerPiece   a reference to the player's selected piece
     */
    public void handleSelectedPieceForPlayerIndex( int playerIndex, PlayerPiece selectedPlayerPiece )
    {
        if ( selectedPlayerPiece.getTargetGameBoardLocationIndex() >= 0 
             && selectedPlayerPiece.getTargetGameBoardLocationIndex() < 16 ) 
        {
            moveSelectedPieceOntoTargetSpaceForPlayerIndex( playerIndex, selectedPlayerPiece );
        } // end if
        else if ( selectedPlayerPiece.getTargetGameBoardLocationIndex() == 16 ) 
        { 
            moveSelectedPieceIntoGoalZoneForPlayerIndex( playerIndex, selectedPlayerPiece );
        } // end else if

        // since we have now moved the player piece's location on the screen, let's first update
        // the status of the Space it is LEAVING so that it is NO LONGER OCCUPIED by that player...
        if ( selectedPlayerPiece.getGameBoardLocationIndex() >= 0 ) 
        {
            movementPathForPlayerIndex[ playerIndex ][ selectedPlayerPiece.getGameBoardLocationIndex() ].setOccupiedByPieceForPlayerIndex(playerIndex, false);
        } // end if

        int currentSpaceIndex = selectedPlayerPiece.getTargetGameBoardLocationIndex();

        // ...and finally, we UPDATE the selected player piece's Space location to be whatever its TARGET location is 
        selectedPlayerPiece.setGameBoardLocationIndex( selectedPlayerPiece.getTargetGameBoardLocationIndex() );
        
        // displays the name of the planet or star in the top right of the screen for tangential learning component
        displaySpaceName(spaces[currentSpaceIndex]);
    } // end method handleSelectedPieceForPlayerIndex

    /**
     * "Helper" method (called by handleSelectedPieceForPlayerIndex) for moving a
     * piece into a target space that is NOT the goal
     * 
     * @param playerIndex           the index of the player moving a piece into the piece's target space 
     * @param selectedPlayerPiece   a reference to the player's selected piece
     */
    public void moveSelectedPieceOntoTargetSpaceForPlayerIndex( int playerIndex, PlayerPiece selectedPlayerPiece )
    {
        int opposingPlayerIndex = 1 - playerIndex; // if playerIndex = 1, opposingPlayerIndex = 1 - 1 = 0
                                                   // if playerIndex = 0, opposingPlayerIndex = 1 - 0 = 1

        // move the playerPiece SPRITE to its new X- and Y- locations on the screen
        selectedPlayerPiece.setLocation(movementPathForPlayerIndex[ playerIndex ][ selectedPlayerPiece.getTargetGameBoardLocationIndex() ].getX(), 
                                        movementPathForPlayerIndex[ playerIndex ][ selectedPlayerPiece.getTargetGameBoardLocationIndex() ].getY() );

        // update the "occupied" state for the target space where the selected player piece is being moved if it is not occupied already
        if ( movementPathForPlayerIndex[ playerIndex ][ selectedPlayerPiece.getTargetGameBoardLocationIndex() ].isOccupiedByPieceForPlayerIndex(opposingPlayerIndex) == false)
        {
            movementPathForPlayerIndex[ playerIndex ][ selectedPlayerPiece.getTargetGameBoardLocationIndex() ].setOccupiedByPieceForPlayerIndex(playerIndex, true);
        } // end if

        // if the target space is a reroll space, mark for rolling again when the turn is over
        if ( movementPathForPlayerIndex[ playerIndex ][ selectedPlayerPiece.getTargetGameBoardLocationIndex() ].isRerollSpace() )
        {
            starSpaceRollAgain = true;
        } // end if

        // if this space is occupied by a piece belonging to the OPPOSING player, check for winner of the battle using another method and reset the captured piece to starting zone
        if ( movementPathForPlayerIndex[ playerIndex ][ selectedPlayerPiece.getTargetGameBoardLocationIndex() ].isOccupiedByPieceForPlayerIndex(opposingPlayerIndex) ) 
        {
            resetCapturedPieceOnBoardAndReplaceWithSelectedPieceForPlayerIndex( playerIndex, selectedPlayerPiece );
        } // end if
    } // end method moveSelectedPieceOntoTargetSpaceForPlayerIndex

    /**
     * "Helper" method (here called by `moveSelectedPieceOntoTargetSpaceForPlayerIndex`) 
     * for handling the capture of an opposing player's piece 
     * 
     * @param playerIndex           the index of the player moving a piece into that piece's target space 
     * @param selectedPlayerPiece   a reference to the player's selected piece
     */

    public void resetCapturedPieceOnBoardAndReplaceWithSelectedPieceForPlayerIndex( int playerIndex, PlayerPiece selectedPlayerPiece )
    {
        int opposingPlayerIndex = 1 - playerIndex; // if playerIndex = 1, opposingPlayerIndex = 1 - 1 = 0
                                                   // if playerIndex = 0, opposingPlayerIndex = 1 - 0 = 1

        for ( PlayerPiece currentOpponentPieceToCheck : playerPieces[opposingPlayerIndex] ) { 

            // if the captured opponent's piece is located along its board space movement path 
            // (index 0 to 15) then move it back to location "index" -1 (starting zone)
            // AND also move its SPRITE back to its original X- and Y-coordinate locations in the world
            if ( currentOpponentPieceToCheck.getGameBoardLocationIndex() == -1 ||  currentOpponentPieceToCheck.getGameBoardLocationIndex() == 16 )
            {
                continue;
            } // end if   

            if ( movementPathForPlayerIndex[opposingPlayerIndex][currentOpponentPieceToCheck.getGameBoardLocationIndex()]
                 == movementPathForPlayerIndex[playerIndex][selectedPlayerPiece.getTargetGameBoardLocationIndex()]  ) 
            {
                determineBattleWinner(selectedPlayerPiece, currentOpponentPieceToCheck); // determines the winner and loser of the battle and resets the losing piece
                checkTier3PieceHasLost(); // checks to see if the piece that lost was a tier 3 piece, if so switches state to a win condition and ends the game
            } // end if
        } // end for    
    } // end method resetCapturedPieceOnBoardAndReplaceWithSelectedPieceForPlayerIndex

    /**
     * "Helper" method (here called by handleSelectedPieceForPlayerIndex) to move 
     * the selected playerPiece's SPRITE into the goal zone.
     * Note that this only moves the player piece's SPRITE; the player piece's 
     * board location index is updated elsewhere
     * 
     * Actual X- and Y-coordinates of each player's piece in the goal zone are each  
     * computed as a linear function of how many of that player's pieces are already 
     * in the goal zone (i.e., goalCountForPlayer[playerIndex] )
     * 
     * @param playerIndex           the index of the player moving a piece into the goal zone
     * @param selectedPlayerPiece   a reference to the player's selected piece
     */
    public void moveSelectedPieceIntoGoalZoneForPlayerIndex( int playerIndex, PlayerPiece selectedPlayerPiece )
    {
        if ( playerIndex == 0 ) // if it's red...
        {
            selectedPlayerPiece.setLocation( (int)((0.75 + 0.02*goalCountForPlayerIndex[0]) * WIDTH), 20*goalCountForPlayerIndex[0] + 60 );
        } // end if
        else // otherwise, if it's blue
        { 
            selectedPlayerPiece.setLocation( (int)((0.125 + 0.02*goalCountForPlayerIndex[1]) * WIDTH), HEIGHT - 200 + 20 * goalCountForPlayerIndex[1] );
        } // end else        
    } // end method moveSelectedPieceIntoGoalZoneForPlayerIndex

    /**
     * "Turns on" (makes moveable) all of pieces for the given player (specified by `playerIndex`) 
     * at the conclusion of that player's turn
     * 
     * @param playerIndex   the index of the player that is completing their turn
     */
    public void makeAllPiecesMoveableAgainForPlayerIndex( int playerIndex )
    {
        for ( PlayerPiece currentPlayerPieceToCheck : playerPieces[playerIndex] )
        {
            currentPlayerPieceToCheck.setMoveable(true);
        } // end for
    } // end method makeAllPiecesMoveableAgainForPlayerIndex

    /**
     * Updates the game state (and checks for a possible win condition) after the player
     * (specified by `playerIndex`) has just completed their turn
     * 
     * @param playerIndex   the index of the player that has just completed their turn
     */
    public void updateGameStateAfterTurnForPlayerIndex( int playerIndex )
    {
        switch ( playerIndex ) {
            case 0:
                updateGoalCountForPlayerIndex(0);

                if ( goalCountForPlayerIndex[0] == NUMBER_OF_PIECES_PER_PLAYER ) 
                {
                    state = PLAYER1_WIN; // update state for next `act` method call
                } // end if
                else if (starSpaceRollAgain) 
                {
                    showText( "\nPlayer 1\nrolls again!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET );
                    starSpaceRollAgain = false; // reset for next turn
                    Greenfoot.delay(DELAY_LENGTH);
                    readyToExitState = false; // reset for next turn
                    state = PLAYER1_ROLL_DIE; // update state for next `act` method call

                } // end else if
                else 
                { 
                    // player 1 (index 0)'s turn is finished, so update game state for player 2's turn
                    showText( "\nPlayer 2\nup next", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET );
                    Greenfoot.delay(DELAY_LENGTH);
                    readyToExitState = false; // reset for next turn
                    state = PLAYER2_ROLL_DIE; // update state for next `act` method call

                } // end else
                break;

            case 1:
                updateGoalCountForPlayerIndex(1);

                if ( goalCountForPlayerIndex[1] == NUMBER_OF_PIECES_PER_PLAYER ) 
                {
                    state = PLAYER2_WIN; // update state for next `act` method call
                } // end if
                else if (starSpaceRollAgain) 
                {
                    showText( "\nPlayer 2\nrolls again!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET );
                    starSpaceRollAgain = false; // reset for next turn
                    Greenfoot.delay(DELAY_LENGTH);
                    readyToExitState = false; // reset for next turn
                    state = PLAYER2_ROLL_DIE; // update state for next `act` method call

                }  // end else if
                else 
                { 
                    // player 1 (index 0)'s turn is finished, so update game state for player 2's turn
                    showText( "\nPlayer 1\nup next", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET );
                    Greenfoot.delay(DELAY_LENGTH);
                    readyToExitState = false; // reset for next turn
                    state = PLAYER1_ROLL_DIE; // update state for next `act` method call

                } // end else
                break;

            default:
                break;
        } // end switch
    } // end method updateGameStateAfterTurnForPlayerIndex

    /**
     * For the given playerIndex, checks to see how many of that player's pieces have been 
     * moved into that goal zone at the end of that player's movement path
     * 
     * @param playerIndex   the index of the player whose pieces are being checked to see if they're in the goal zone
     */
    public void updateGoalCountForPlayerIndex( int playerIndex )
    {
        goalCountForPlayerIndex[playerIndex] = 0; // resetting for purpose of using 
                                                  // loop to re-compute goal count
  
        for ( PlayerPiece currentPlayerPieceToCheck : playerPieces[playerIndex] )
        {
            if ( currentPlayerPieceToCheck.getGameBoardLocationIndex() == 16 ) 
            {
                goalCountForPlayerIndex[playerIndex]++;
            } // end if
        } // end for 
    } // end method updateGoalCountForPlayerIndex

    /**
     * Determines winner of a battle and sets appropriate win/loss counters for each piece.
     * Additionally resets the piece to the starting zone when captured. Main battle logic.
     * 
     * @param attackingPiece    identifies which piece from which player index is the attacking piece in the battle
     * @param defendingPiece    identifies which piece from which player index is the defending piece in the battle
     */
    public void determineBattleWinner(PlayerPiece attackingPiece, PlayerPiece defendingPiece)
    {
        int attackerPlayerIndex = attackingPiece.getPlayerIndex();
        int defenderPlayerIndex = defendingPiece.getPlayerIndex();
        int attackerRolls = attackingPiece.getPieceTier();
        int defenderRolls = defendingPiece.getPieceTier();

        int attackerHighestRoll = 0;
        int defenderHighestRoll = 0;

        showText("Battle: Player " + (attackerPlayerIndex + 1) + " vs Player " + (defenderPlayerIndex + 1), getWidth()/2, getHeight()/2);
        Greenfoot.delay(100);

        for (int i = 0; i < attackerRolls; i++)
        {
            int currentAttackerRoll = determineDieRollBattle(attackerPlayerIndex);
            Greenfoot.delay(50);
            if (currentAttackerRoll > attackerHighestRoll)
            {
                attackerHighestRoll = currentAttackerRoll; 
            } // end if
        } // end for

        Greenfoot.delay(100);

        for (int i = 0; i < defenderRolls; i++)
        {
            int currentDefenderRoll = determineDieRollBattle(defenderPlayerIndex);
            Greenfoot.delay(50);
            if (currentDefenderRoll > defenderHighestRoll)
            {
                defenderHighestRoll = currentDefenderRoll; 
            } // end if
        } // end for

        // Determines the winner of a battle
        if (attackerHighestRoll > defenderHighestRoll) //if attacker wins this occurs
        {
            showText("Player " + (attackerPlayerIndex + 1) + " wins the battle!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
            Greenfoot.playSound("blaster-2-81267.mp3");
            movementPathForPlayerIndex[attackerPlayerIndex][attackingPiece.getTargetGameBoardLocationIndex()].setOccupiedByPieceForPlayerIndex(attackerPlayerIndex, true); // manual resets of space occupation was required due to a bug
            movementPathForPlayerIndex[defenderPlayerIndex][defendingPiece.getGameBoardLocationIndex()].setOccupiedByPieceForPlayerIndex(defenderPlayerIndex, false);
            defendingPiece.setLocation(defendingPiece.getOriginalXcoord(), defendingPiece.getOriginalYcoord());
            defendingPiece.setGameBoardLocationIndex(-1); // manual resets for location indexes required because of a bug
            defendingPiece.setTargetGameBoardLocationIndex(0);
            attackingPiece.incrementWins();
            defendingPiece.incrementLosses();
        } // end if
        else // if defending piece wins or a tie occurs (favoring the defending piece in the event of a tie) 
        {
            showText("Player " + (defenderPlayerIndex + 1) + " wins the battle!", die.getX(), die.getY() + DIE_TEXT_VERTICAL_OFFSET);
            Greenfoot.playSound("blaster-2-81267.mp3");
            movementPathForPlayerIndex[defenderPlayerIndex][defendingPiece.getGameBoardLocationIndex()].setOccupiedByPieceForPlayerIndex(defenderPlayerIndex, true); // manual resets of space occupation was required due to a bug
            movementPathForPlayerIndex[attackerPlayerIndex][attackingPiece.getTargetGameBoardLocationIndex()].setOccupiedByPieceForPlayerIndex(attackerPlayerIndex, false);
            movementPathForPlayerIndex[attackerPlayerIndex][attackingPiece.getGameBoardLocationIndex()].setOccupiedByPieceForPlayerIndex(attackerPlayerIndex, false);
            attackingPiece.setLocation(attackingPiece.getOriginalXcoord(), attackingPiece.getOriginalYcoord());
            attackingPiece.setGameBoardLocationIndex(-1); // manual resets for location indexes required because of a bug
            attackingPiece.setTargetGameBoardLocationIndex(0);
            defendingPiece.incrementWins();
            attackingPiece.incrementLosses();
        } // end else

        // Clear the text after the battle
        Greenfoot.delay(200);
        showText("", getWidth()/2, getHeight()/2);
        showText("", getWidth()/2, getHeight()/2 + 50);
        showText("", getWidth()/2, getHeight()/2 + 100);
    } // end method determineBattleWinner
    
    /**
     * Checks to see if a tier 3 piece has been lost, if so manually sets condition to switch state for the win of the opposing player
     */
    public void checkTier3PieceHasLost()
    {
        boolean player1Tier3Lost = false;
        boolean player2Tier3Lost = false;

        // Check if tier 3 piece for player 1 has lost
        for (PlayerPiece piece : playerPieces[0]) 
        {
            if (piece.getPieceTier() == 3 && piece.getLosses() > 0) 
            {
                player1Tier3Lost = true;
                break;
            } // end if
        } // end for

        // Check if tier 3 piece for player 2 has lost
        for (PlayerPiece piece : playerPieces[1]) 
        {
            if (piece.getPieceTier() == 3 && piece.getLosses() > 0) 
            {
                player2Tier3Lost = true;
                break;
            } // end if
        } // end for

        // Set win condition
        if (player1Tier3Lost) 
        {
            state = PLAYER2_WIN2;
        } // end if 
        else if (player2Tier3Lost) 
        {
            state = PLAYER1_WIN2;
        } // end else if
    } // end method checkTier3PieceHasLost

    /**
     * Names the planets and stars after initialization of each space
     */
    public void nameSpaces()
    {
        spaces[0].setSpaceName("Gliese 12 b");
        spaces[1].setSpaceName("Gliese 163 c");
        spaces[2].setSpaceName("GJ 1061 c");
        spaces[3].setSpaceName("Polaris"); 
        spaces[4].setSpaceName("GJ 3293 d");
        spaces[5].setSpaceName("HD 20794 d");
        spaces[6].setSpaceName("HD 216520 c");
        spaces[7].setSpaceName("Sirius"); 
        spaces[8].setSpaceName("Kepler-22b");
        spaces[9].setSpaceName("Kepler-62f");
        spaces[10].setSpaceName("LHS 1140 b");
        spaces[11].setSpaceName("Alpha Centuari A"); 
        spaces[12].setSpaceName("LP 890-9 c");
        spaces[13].setSpaceName("Luyten b");
        spaces[14].setSpaceName("Proxima Centauri b");
        spaces[15].setSpaceName("Betelgeuse"); 
        spaces[16].setSpaceName("Ross 128 b");
        spaces[17].setSpaceName("Ross 508 b");
        spaces[18].setSpaceName("TRAPPIST-1d");
        spaces[19].setSpaceName("Rigel"); 
    } // end method nameSpaces

    /**
     * Displays the name of the planets and stars on the gameboard for the user when called
     * 
     * @param landedSpace   space on which a player piece has landed after moving
     */
    public void displaySpaceName(Space landedSpace)
    {
        showText(landedSpace.getSpaceName(), WIDTH - 100, 15);
    } // end method displaySpaceName

    /**
     * When a player piece lands on a space, it displays the name for the user on the gameboard
     * 
     * @param piece         determines which piece is moving to desired space
     * @param spaceIndex    determines which space the piece is moving to
     */
    public void handleLandingOnSpace(PlayerPiece piece, int spaceIndex)
    {
        displaySpaceName(spaces[spaceIndex]);
    } // end method handleLandingOnSpace

    /**
     * Names each ship after each ship has been instantiated
     */
    public void nameShips()
    {
        // red pieces names
        playerPieces[0][0].setShipTierName("red Sputnik");
        playerPieces[0][1].setShipTierName("red Sputnik");
        playerPieces[0][2].setShipTierName("red Sputnik");
        playerPieces[0][3].setShipTierName("red Galileo");
        playerPieces[0][4].setShipTierName("red Galileo");
        playerPieces[0][5].setShipTierName("red Galileo");
        playerPieces[0][6].setShipTierName("red Orbiter");

        // blue pieces names
        playerPieces[1][0].setShipTierName("blue Sputnik");
        playerPieces[1][1].setShipTierName("blue Sputnik");
        playerPieces[1][2].setShipTierName("blue Sputnik");
        playerPieces[1][3].setShipTierName("blue Galileo");
        playerPieces[1][4].setShipTierName("blue Galileo");
        playerPieces[1][5].setShipTierName("blue Galileo");
        playerPieces[1][6].setShipTierName("blue Orbiter");
    } // end method nameShips

    /**
     * Resets the world for a new game
     */
    public void resetWorld()
    {
        Greenfoot.setWorld(new GameBoard());
    } // end method resetWorld
    
    /**
     * Checks for which gamemode the game is currently in by checking values for human or CPU controlled players
     */
    public String getGameModeDescription()
    {
        if (!isPlayerHuman[0] && !isPlayerHuman[1])
        {
            return "CPU vs CPU";
        } // end if
        else if (isPlayerHuman[0] && isPlayerHuman[1])
        {
            return "Human vs Human";
        } // end else if
        else if (isPlayerHuman[0] && !isPlayerHuman[1])
        {
            return "Human vs CPU";
        } // end else if
        else
        {
            return "Unintended";
        } // end else
    } // end method getGameModeDescription
    
    /**
     * Displays the current gamemode determined by a different method
     */
    public void displayGameMode()
    {
        String gameMode = getGameModeDescription();
        showText(gameMode, die.getX(), HEIGHT - 25 );
    } // end method displayGameMode

    /**
     * Cycles game mode between various human and cpu controlled players
     */
    public void cycleGameMode()
    {
        if (!isPlayerHuman[0] && !isPlayerHuman[1])
        {
            isPlayerHuman[0] = true;
        } // end if
        else if (isPlayerHuman[0] && !isPlayerHuman[1]) 
        {
            isPlayerHuman[1] = true;
        } // end else if
        else if (isPlayerHuman[0] && isPlayerHuman[1]) 
        {
            isPlayerHuman[0] = false; 
            isPlayerHuman[1] = false; 
        } // end else if
    } // end method cycleGameMode
    
    /**
     * Checks to see if the player has clicked on the title screen in order to start the game
     */
    public void startGame()
    {
        if (Greenfoot.mouseClicked(titleScreen))
        {
            removeObject(titleScreen);
            gameStarted = true;
            state = PLAYER1_ROLL_DIE;
            showText("Current Mode:", die.getX(), HEIGHT - 50);
            playMusic();
        } // end if
    } // end method startGame
} // end class GameBoard
