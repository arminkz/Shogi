import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.ArrayList;

public class ChessGUI{

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] chessBoardSquares = new JButton[9][9];

    public ArrayList<ChessPiece> chessPieces = new ArrayList<ChessPiece>();
    public ChessPiece selectedPiece;
    public ArrayList<Point> readyToMoveSquares = new ArrayList<Point>();
    public ArrayList<Point> readyToCaptureSquares = new ArrayList<Point>();

    public ArrayList<ChessPiece> p1CapturedPieces = new ArrayList<ChessPiece>();
    private JButton[] p1CapturedSquares = new JButton[7];
    public ArrayList<ChessPiece> p2CapturedPieces = new ArrayList<ChessPiece>();
    private JButton[] p2CapturedSquares = new JButton[7];

    private boolean isPlayingWithAI = false;

    public ChessAI cpuAI;

    private BufferedImage kingImage1;
    private BufferedImage rookImage1;
    private BufferedImage bishopImage1;
    private BufferedImage goldImage1;
    private BufferedImage silverImage1;
    private BufferedImage knightImage1;
    private BufferedImage lanceImage1;
    private BufferedImage pawnImage1;
    private BufferedImage rookPImage1;
    private BufferedImage bishopPImage1;
    private BufferedImage silverPImage1;
    private BufferedImage knightPImage1;
    private BufferedImage lancePImage1;
    private BufferedImage pawnPImage1;

    private BufferedImage kingImage2;
    private BufferedImage rookImage2;
    private BufferedImage bishopImage2;
    private BufferedImage goldImage2;
    private BufferedImage silverImage2;
    private BufferedImage knightImage2;
    private BufferedImage lanceImage2;
    private BufferedImage pawnImage2;
    private BufferedImage rookPImage2;
    private BufferedImage bishopPImage2;
    private BufferedImage silverPImage2;
    private BufferedImage knightPImage2;
    private BufferedImage lancePImage2;
    private BufferedImage pawnPImage2;

    private JPanel chessBoard;

    private int turn = 1;

    private final JLabel message = new JLabel(
            "AKP Shogi v0.9");

    private Color mainbg = new Color(230,230,230);
    private Color ochre = new Color(204,119,34);

    //Chess Square Buttons ActionListener
    private class ChessButtonActionListener implements ActionListener {
        private int x;
        private int y;

        public ChessButtonActionListener(int x,int y) {
            this.x = x;
            this.y = y;
        }

        public void actionPerformed(ActionEvent e) {
            handleButtonPress(x,y);
        }
    }

    //Promotion Button ActionListener
    private class PromoteButtonActionListener implements ActionListener {
        private ChessPiece chp;

        public PromoteButtonActionListener(ChessPiece chp) {
            this.chp = chp;
        }

        public void actionPerformed(ActionEvent e) {
            switch(chp.type){
                case SILVERGEN:
                    chp.type = ChessPiece.pieceType.SILVERGEN_P;
                    break;
                case KNIGHT:
                    chp.type = ChessPiece.pieceType.KNIGHT_P;
                    break;
                case LANCE:
                    chp.type = ChessPiece.pieceType.LANCE_P;
                    break;
                case PAWN:
                    chp.type = ChessPiece.pieceType.PAWN_P;
                    break;
                case ROOK:
                    chp.type = ChessPiece.pieceType.ROOK_P;
                    break;
                case BISHOP:
                    chp.type = ChessPiece.pieceType.BISHOP_P;
            }
            redrawBoard();
        }
    }

    //Chess Square Buttons Handler
    public void handleButtonPress(int x,int y){

        boolean isHandled = false;

        //Handle Piece Move
        for(Point rtm : readyToMoveSquares){
            if(rtm.x == x && rtm.y == y) {
                //Move to Square
                selectedPiece.x = rtm.x;
                selectedPiece.y = rtm.y;

                if(selectedPiece.mustAdd) {
                    chessPieces.add(selectedPiece);
                    if(selectedPiece.mustAddplayer==1) {
                        p1CapturedSquares[selectedPiece.mustAddbutIndex].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[selectedPiece.mustAddbutIndex].getText()) - 1));
                    }else{
                        p2CapturedSquares[selectedPiece.mustAddbutIndex].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[selectedPiece.mustAddbutIndex].getText()) - 1));
                    }
                    selectedPiece.mustAdd = false;
                }
                //redrawBoard();
                isHandled=true;
                if (turn == 1) {
                    turn = 2;
                } else if(turn==2){
                    turn = 1;
                }
                break;
            }
        }

        //Handle Piece Capture
        for(Point rtm : readyToCaptureSquares){
            if(rtm.x == x && rtm.y == y) {
                if(selectedPiece.player == 1){
                    for(ChessPiece chp : chessPieces) {
                        if (chp.x == rtm.x && chp.y == rtm.y) {
                            //p1CapturedPieces.add(chp);
                            if(chp.type == ChessPiece.pieceType.KING){
                                JOptionPane.showMessageDialog(null,"Player 1 Wins !","Game Over",JOptionPane.INFORMATION_MESSAGE);
                                turn=3;
                            }
                            addP1CapturedPiece(chp);
                            chessPieces.remove(chp);
                            break;
                        }
                    }
                }else{
                    for(ChessPiece chp : chessPieces) {
                        if (chp.x == rtm.x && chp.y == rtm.y) {
                            //p2CapturedPieces.add(chp);
                            if(chp.type == ChessPiece.pieceType.KING){
                                JOptionPane.showMessageDialog(null,"Player 2 Wins !","Game Over",JOptionPane.INFORMATION_MESSAGE);
                                turn=3;
                            }
                            addP2CapturedPiece(chp);
                            chessPieces.remove(chp);
                            break;
                        }
                    }
                }
                //Move to Square
                selectedPiece.x = rtm.x;
                selectedPiece.y = rtm.y;
                //redrawBoard();
                isHandled=true;
                if (turn == 1) {
                    turn = 2;
                } else if(turn==2) {
                    turn = 1;
                }
                break;
            }
        }


        /*for(int ii=0;ii<9;ii++){
            for(int jj=0;jj<9;jj++){
                chessBoardSquares[ii][jj].setBackground(mainbg);
                chessBoardSquares[ii][jj].setComponentPopupMenu(null);
            }
        }*/

        if(turn==2 && isPlayingWithAI){
            //AI Plays Here !'
            PieceMove pm = cpuAI.playMove();
            System.out.println("AI Moving " + pm.chessPieceIndex + " to " + pm.finalPos.toString());
            if(!pm.isGoingToCapture){
                chessPieces.get(pm.chessPieceIndex).x= pm.finalPos.x;
                chessPieces.get(pm.chessPieceIndex).y= pm.finalPos.y;

                //TODO : AI NOT SUPPORTS DROPPING AT THIS TIME
                /*if(selectedPiece.mustAdd) {
                    chessPieces.add(selectedPiece);
                    if(selectedPiece.mustAddplayer==1) {
                        p1CapturedSquares[selectedPiece.mustAddbutIndex].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[selectedPiece.mustAddbutIndex].getText()) - 1));
                    }else{
                        p2CapturedSquares[selectedPiece.mustAddbutIndex].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[selectedPiece.mustAddbutIndex].getText()) - 1));
                    }
                    selectedPiece.mustAdd = false;
                }*/
            }else{
                ChessPiece pieceGoingToCapture = getPieceAt(pm.finalPos.x,pm.finalPos.y);
                if(chessPieces.get(pm.chessPieceIndex).player == 1){
                    if(pieceGoingToCapture.type == ChessPiece.pieceType.KING){
                        JOptionPane.showMessageDialog(null,"Player 1 Wins !","Game Over",JOptionPane.INFORMATION_MESSAGE);
                        turn=3;
                    }
                    addP1CapturedPiece(pieceGoingToCapture);
                    chessPieces.remove(pieceGoingToCapture);
                }else{
                    if(pieceGoingToCapture.type == ChessPiece.pieceType.KING){
                        JOptionPane.showMessageDialog(null,"Player 2 Wins !","Game Over",JOptionPane.INFORMATION_MESSAGE);
                        turn=3;
                    }
                    addP2CapturedPiece(pieceGoingToCapture);
                    chessPieces.remove(pieceGoingToCapture);
                }
                //Move to Square
                    chessPieces.get(pm.chessPieceIndex).x= pm.finalPos.x;
                    chessPieces.get(pm.chessPieceIndex).y= pm.finalPos.y;
            }
            if (turn == 1) {
                turn = 2;
            } else if(turn==2) {
                turn = 1;
            }
            isHandled = true;
        }


        selectedPiece = null;
        readyToMoveSquares = new ArrayList<Point>();
        readyToCaptureSquares = new ArrayList<Point>();

        redrawBoard();

        if(isHandled){
            return;
        }
        //Clear Previous State



        //Handle Piece Select
        for(ChessPiece chp : chessPieces){
            if(chp.x == x && chp.y == y) {
                //Select the Piece
                if (turn == chp.player) {
                    selectedPiece = chp;
                    chessBoardSquares[x][y].setBackground(Color.green);

                    //Check for Promotion
                    if (isPromotable(chp)) {
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem promoteButton = new JMenuItem("Promote !");
                        promoteButton.addActionListener(new PromoteButtonActionListener(chp));
                        popup.add(promoteButton);
                        chessBoardSquares[x][y].setComponentPopupMenu(popup);
                    } else {
                        chessBoardSquares[x][y].setComponentPopupMenu(null);
                    }

                    for (Point move : getPossibleMoves(chp)) {
                        int pf = (chp.player == 2) ? 1 : -1;
                        int newx = (chp.x + move.x * pf);
                        int newy = (chp.y + move.y * pf);

                        if (newx >= 0 && newy >= 0 && newx <= 8 && newy <= 8) {
                            if (isOccupied(newx, newy) == 0) {
                                chessBoardSquares[newx][newy].setBackground(Color.cyan);
                                readyToMoveSquares.add(new Point(newx, newy));
                            } else if (isOccupied(newx, newy) != chp.player) {
                                chessBoardSquares[newx][newy].setBackground(Color.magenta);
                                readyToCaptureSquares.add(new Point(newx, newy));
                            }
                        }
                    }
                }
            }else{
                //chessBoardSquares[x][y].setBackground(new Color(230,230,230));
            }
        }
    }

    private class CapturedPieceButtonActionListener implements ActionListener {
        private ChessPiece.pieceType pieceKind;
        private int player;
        private int butIndex;

        public CapturedPieceButtonActionListener(int player,ChessPiece.pieceType pieceKind,int butIndex) {
            this.player = player;
            this.pieceKind = pieceKind;
            this.butIndex = butIndex;
        }

        public void actionPerformed(ActionEvent e) {
            boolean b;
            if (turn == player) {
                if (player == 1) {
                    b = Integer.parseInt(p1CapturedSquares[butIndex].getText()) > 0;
                } else {
                    b = Integer.parseInt(p2CapturedSquares[butIndex].getText()) > 0;
                }
                if (b) {
                    ChessPiece droppingPiece = new ChessPiece(player, pieceKind, 0, 0);
                    droppingPiece.mustAdd = true;
                    droppingPiece.mustAddbutIndex = butIndex;
                    droppingPiece.mustAddplayer = player;
                    selectedPiece = droppingPiece;
                    readyToMoveSquares = new ArrayList<Point>();
                    readyToCaptureSquares = new ArrayList<Point>();

                    redrawBoard();
                    if (pieceKind != ChessPiece.pieceType.PAWN) {
                        for (int ii = 0; ii < 9; ii++) {
                            for (int jj = 0; jj < 9; jj++) {
                                if (isOccupied(ii, jj) == 0) {
                                    //Select the Piece
                                    chessBoardSquares[ii][jj].setBackground(Color.cyan);
                                    readyToMoveSquares.add(new Point(ii, jj));
                                }
                            }
                        }
                    }else{
                        for(int ii=0;ii<9;ii++){
                            boolean isHavePawn = false;
                            for(int jj=0;jj<9;jj++){
                                //check for pawn
                                for(ChessPiece chp : chessPieces){
                                    if(chp.x == ii && chp.y == jj){
                                        if(chp.player == player && chp.type == ChessPiece.pieceType.PAWN){
                                            isHavePawn = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if(!isHavePawn){
                                for (int jj = 0; jj < 9; jj++) {
                                    if (isOccupied(ii, jj) == 0) {
                                        //Select the Piece
                                        chessBoardSquares[ii][jj].setBackground(Color.cyan);
                                        readyToMoveSquares.add(new Point(ii, jj));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void depromote(ChessPiece chp){
        switch(chp.type){
            case SILVERGEN_P:
                chp.type = ChessPiece.pieceType.SILVERGEN;
                break;
            case KNIGHT_P:
                chp.type = ChessPiece.pieceType.KNIGHT;
                break;
            case LANCE_P:
                chp.type = ChessPiece.pieceType.LANCE;
                break;
            case PAWN_P:
                chp.type = ChessPiece.pieceType.PAWN;
                break;
            case ROOK_P:
                chp.type = ChessPiece.pieceType.ROOK;
                break;
            case BISHOP_P:
                chp.type = ChessPiece.pieceType.BISHOP;
                break;
        }
    }

    private void addP1CapturedPiece(ChessPiece chp){

        //Depromote
        depromote(chp);

        p1CapturedPieces.add(chp);

        switch(chp.type){
            case KNIGHT:
                p1CapturedSquares[0].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[0].getText())+1));
                break;
            case BISHOP:
                p1CapturedSquares[1].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[1].getText())+1));
                break;
            case ROOK:
                p1CapturedSquares[2].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[2].getText())+1));
                break;
            case GOLDGEN:
                p1CapturedSquares[3].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[3].getText())+1));
                break;
            case SILVERGEN:
                p1CapturedSquares[4].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[4].getText())+1));
                break;
            case PAWN:
                p1CapturedSquares[5].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[5].getText())+1));
                break;
            case LANCE:
                p1CapturedSquares[6].setText(Integer.toString(Integer.parseInt(p1CapturedSquares[6].getText())+1));
                break;
        }

        redrawBoard();
    }

    private void addP2CapturedPiece(ChessPiece chp){

        //Depromote
        depromote(chp);

        p2CapturedPieces.add(chp);

        switch(chp.type){
            case KNIGHT:
                p2CapturedSquares[6].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[6].getText())+1));
                break;
            case BISHOP:
                p2CapturedSquares[4].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[4].getText())+1));
                break;
            case ROOK:
                p2CapturedSquares[5].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[5].getText())+1));
                break;
            case GOLDGEN:
                p2CapturedSquares[2].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[2].getText())+1));
                break;
            case SILVERGEN:
                p2CapturedSquares[3].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[3].getText())+1));
                break;
            case PAWN:
                p2CapturedSquares[0].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[0].getText())+1));
                break;
            case LANCE:
                p2CapturedSquares[1].setText(Integer.toString(Integer.parseInt(p2CapturedSquares[1].getText())+1));
                break;
        }

        redrawBoard();
    }

    //TODO Add other pieces moves
    //Get Each Piece Possible Moves
    public ArrayList<Point> getPossibleMoves(ChessPiece chp){
        ArrayList<Point> possibleMoves = new ArrayList<Point>();
        int pf = (chp.player == 2) ? 1 : -1;
        switch(chp.type){
            case KING:
                possibleMoves.add(new Point(-1,1));
                possibleMoves.add(new Point(0,1));
                possibleMoves.add(new Point(1,1));
                possibleMoves.add(new Point(-1,0));
                possibleMoves.add(new Point(1,0));
                possibleMoves.add(new Point(-1,-1));
                possibleMoves.add(new Point(0,-1));
                possibleMoves.add(new Point(1,-1));
                break;
            case ROOK:
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,0));
                    if(isOccupied(chp.x + pf*r,chp.y)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,0));
                    if(isOccupied(chp.x - pf*r,chp.y)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(0,r));
                    if(isOccupied(chp.x,chp.y + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(0,-r));
                    if(isOccupied(chp.x,chp.y - pf*r)!=0) break;
                }
                break;
            case BISHOP:
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,r));
                    if(isOccupied(chp.x + pf*r,chp.y + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,-r));
                    if(isOccupied(chp.x - pf*r,chp.y - pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,r));
                    if(isOccupied(chp.x - pf*r,chp.y + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,-r));
                    if(isOccupied(chp.x + pf*r,chp.y - pf*r)!=0) break;
                }
                break;
            case GOLDGEN:
            case SILVERGEN_P:
            case KNIGHT_P:
            case LANCE_P:
            case PAWN_P:
                possibleMoves.add(new Point(-1,1));
                possibleMoves.add(new Point(0,1));
                possibleMoves.add(new Point(1,1));
                possibleMoves.add(new Point(-1,0));
                possibleMoves.add(new Point(1,0));
                possibleMoves.add(new Point(0,-1));
                break;
            case SILVERGEN:
                possibleMoves.add(new Point(-1,1));
                possibleMoves.add(new Point(0,1));
                possibleMoves.add(new Point(1,1));
                possibleMoves.add(new Point(-1,-1));
                possibleMoves.add(new Point(1,-1));
                break;
            case KNIGHT:
                possibleMoves.add(new Point(-1,2));
                possibleMoves.add(new Point(1,2));
                break;
            case LANCE:
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(0,r));
                    if(isOccupied(chp.x,chp.y + pf*r)!=0) break;
                }
                break;
            case PAWN:
                possibleMoves.add(new Point(0,1));
                break;
            case ROOK_P:
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,0));
                    if(isOccupied(chp.x + pf*r,chp.y)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,0));
                    if(isOccupied(chp.x - pf*r,chp.y)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(0,r));
                    if(isOccupied(chp.x,chp.y + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(0,-r));
                    if(isOccupied(chp.x,chp.y - pf*r)!=0) break;
                }
                possibleMoves.add(new Point(1,1));
                possibleMoves.add(new Point(1,-1));
                possibleMoves.add(new Point(-1,1));
                possibleMoves.add(new Point(-1,-1));
                break;
            case BISHOP_P:
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,r));
                    if(isOccupied(chp.x + pf*r,chp.y + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,-r));
                    if(isOccupied(chp.x - pf*r,chp.y - pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(-r,r));
                    if(isOccupied(chp.x - pf*r,chp.y + pf*r)!=0) break;
                }
                for(int r=1;r<9;r++){
                    possibleMoves.add(new Point(r,-r));
                    if(isOccupied(chp.x + pf*r,chp.y - pf*r)!=0) break;
                }
                possibleMoves.add(new Point(0,1));
                possibleMoves.add(new Point(0,-1));
                possibleMoves.add(new Point(-1,0));
                possibleMoves.add(new Point(1,0));
                break;

        }
        return possibleMoves;
    }

    // 0 1 2
    public int isOccupied(int x, int y) {
        for(ChessPiece chp : chessPieces){
            if(chp.x == x && chp.y == y){
                if(chp.player == 1){
                    return 1;
                }else{
                    return 2;
                }
            }
        }
        return 0; //not occupied
    }


    public ChessPiece getPieceAt(int x, int y) {
        for(ChessPiece chp : chessPieces){
            if(chp.x == x && chp.y == y){
                return chp;
            }
        }
        return null;
    }

    private boolean isPromotable(ChessPiece chp) {
        if (chp.type == ChessPiece.pieceType.LANCE || chp.type == ChessPiece.pieceType.PAWN || chp.type == ChessPiece.pieceType.SILVERGEN
                || chp.type == ChessPiece.pieceType.KNIGHT || chp.type == ChessPiece.pieceType.ROOK || chp.type == ChessPiece.pieceType.BISHOP){
            if (chp.player == 1) {
                if (chp.y <= 2) {
                    return true;
                }
            } else {
                if (chp.y >= 6) {
                    return true;
                }
            }
        }
        return false;
    }

    public ChessGUI() {
        initializeGui();
    }

    public final void initializeGui() {
        // loads the images for the chess pieces
        loadImages();

        JToolBar tools = new JToolBar();
        tools.setFloatable(false);

        gui.setBackground(ochre);
        gui.add(tools, BorderLayout.NORTH);

        Action newGame2pAction = new AbstractAction("New Game (2 Player)") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupNewGame(false);
            }
        };
        tools.add(newGame2pAction);

        Action newGame1pAction = new AbstractAction("New Game (with AI)") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupNewGame(true);
            }
        };
        tools.add(newGame1pAction);


        //tools.add(message);


        JPanel cp = new JPanel(new BorderLayout());
        cp.setBackground(ochre);
        cp.setBorder(new EmptyBorder(9,9,9,9));

        JPanel p1cp = new JPanel(new GridLayout(4,4));
        p1cp.setBackground(ochre);

        p1CapturedSquares[0] = new JButton("0",new ImageIcon(knightImage1));
        p1CapturedSquares[0].addActionListener(new CapturedPieceButtonActionListener(1, ChessPiece.pieceType.KNIGHT,0));
        p1CapturedSquares[1] = new JButton("0",new ImageIcon(bishopImage1));
        p1CapturedSquares[1].addActionListener(new CapturedPieceButtonActionListener(1, ChessPiece.pieceType.BISHOP,1));
        p1CapturedSquares[2] = new JButton("0",new ImageIcon(rookImage1));
        p1CapturedSquares[2].addActionListener(new CapturedPieceButtonActionListener(1, ChessPiece.pieceType.ROOK,2));
        p1CapturedSquares[3] = new JButton("0",new ImageIcon(goldImage1));
        p1CapturedSquares[3].addActionListener(new CapturedPieceButtonActionListener(1, ChessPiece.pieceType.GOLDGEN,3));
        p1CapturedSquares[4] = new JButton("0",new ImageIcon(silverImage1));
        p1CapturedSquares[4].addActionListener(new CapturedPieceButtonActionListener(1, ChessPiece.pieceType.SILVERGEN,4));
        p1CapturedSquares[5] = new JButton("0",new ImageIcon(pawnImage1));
        p1CapturedSquares[5].addActionListener(new CapturedPieceButtonActionListener(1, ChessPiece.pieceType.PAWN,5));
        p1CapturedSquares[6] = new JButton("0",new ImageIcon(lanceImage1));
        p1CapturedSquares[6].addActionListener(new CapturedPieceButtonActionListener(1, ChessPiece.pieceType.LANCE,6));

        for(int g=0;g<7;g++){
            p1CapturedSquares[g].setFont(new Font("Segoe UI",Font.BOLD , 28));
            p1CapturedSquares[g].setOpaque(false);
            p1CapturedSquares[g].setContentAreaFilled(false);
            p1CapturedSquares[g].setBorderPainted(false);
            if(g==1){
                JButton emptyButton = new JButton("",new ImageIcon(new BufferedImage(70, 80, BufferedImage.TYPE_INT_ARGB)));
                emptyButton.setOpaque(false);
                emptyButton.setContentAreaFilled(false);
                emptyButton.setBorderPainted(false);
                p1cp.add(emptyButton);
            }
            p1cp.add(p1CapturedSquares[g]);
        }


        JPanel p2cp = new JPanel(new GridLayout(4,4));
        p2cp.setBackground(ochre);

        p2CapturedSquares[0] = new JButton("0",new ImageIcon(pawnImage2));
        p2CapturedSquares[0].addActionListener(new CapturedPieceButtonActionListener(2, ChessPiece.pieceType.PAWN,0));
        p2CapturedSquares[1] = new JButton("0",new ImageIcon(lanceImage2));
        p2CapturedSquares[1].addActionListener(new CapturedPieceButtonActionListener(2, ChessPiece.pieceType.LANCE,1));
        p2CapturedSquares[2] = new JButton("0",new ImageIcon(goldImage2));
        p2CapturedSquares[2].addActionListener(new CapturedPieceButtonActionListener(2, ChessPiece.pieceType.GOLDGEN,2));
        p2CapturedSquares[3] = new JButton("0",new ImageIcon(silverImage2));
        p2CapturedSquares[3].addActionListener(new CapturedPieceButtonActionListener(2, ChessPiece.pieceType.SILVERGEN,3));
        p2CapturedSquares[4] = new JButton("0",new ImageIcon(bishopImage2));
        p2CapturedSquares[4].addActionListener(new CapturedPieceButtonActionListener(2, ChessPiece.pieceType.BISHOP,4));
        p2CapturedSquares[5] = new JButton("0",new ImageIcon(rookImage2));
        p2CapturedSquares[5].addActionListener(new CapturedPieceButtonActionListener(2, ChessPiece.pieceType.ROOK,5));
        p2CapturedSquares[6] = new JButton("0",new ImageIcon(knightImage2));
        p2CapturedSquares[6].addActionListener(new CapturedPieceButtonActionListener(2, ChessPiece.pieceType.KNIGHT,6));

        for(int g=0;g<7;g++){
            p2CapturedSquares[g].setFont(new Font("Segoe UI",Font.BOLD , 28));
            p2CapturedSquares[g].setOpaque(false);
            p2CapturedSquares[g].setContentAreaFilled(false);
            p2CapturedSquares[g].setBorderPainted(false);
            p2cp.add(p2CapturedSquares[g]);
        }

        cp.add(p1cp,BorderLayout.SOUTH);
        cp.add(p2cp,BorderLayout.NORTH);


        gui.add(cp,BorderLayout.WEST);

        chessBoard = new JPanel(new GridLayout(9,9));

        chessBoard.setBorder(new CompoundBorder(
                new EmptyBorder(8,8,8,8),
                new LineBorder(Color.BLACK)
        ));
        // Set BG Color
        chessBoard.setBackground(ochre);

        /*JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(ochre);
        boardConstrain.add(chessBoard);*/

        //gui.add(boardConstrain);
        gui.add(chessBoard);

        // create the chess board squares
        Insets buttonMargin = new Insets(2, 2, 2, 2);
        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
            for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);

                ImageIcon icon = new ImageIcon(
                        new BufferedImage(70, 80, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);

                b.addActionListener(new ChessButtonActionListener(ii,jj));

                Color brown = new Color(230,230,230);
                b.setBackground(brown);
                chessBoardSquares[ii][jj] = b;
            }
        }

        /*
         * fill the chess board
         */
        //chessBoard.add(new JLabel(""));
        // fill the top row
        /*for (int ii = 0; ii < 8; ii++) {
            chessBoard.add(
                    new JLabel(COLS.substring(ii, ii + 1),
                            SwingConstants.CENTER));
        }*/
        // fill the black non-pawn piece row
        for (int ii = 0; ii < 9; ii++) {
            for (int jj = 0; jj < 9; jj++) {
                chessBoard.add(chessBoardSquares[jj][ii]);
            }
        }
    }

    public final JComponent getGui() {
        return gui;
    }

    //Load Piece Images from Resource
    private final void loadImages() {
        try{
            kingImage1 =  ImageIO.read(this.getClass().getResource("0.png"));
            rookImage1 =  ImageIO.read(this.getClass().getResource("1.png"));
            bishopImage1 =  ImageIO.read(this.getClass().getResource("2.png"));
            goldImage1 =  ImageIO.read(this.getClass().getResource("3.png"));
            silverImage1 =  ImageIO.read(this.getClass().getResource("4.png"));
            knightImage1 =  ImageIO.read(this.getClass().getResource("5.png"));
            lanceImage1 =  ImageIO.read(this.getClass().getResource("6.png"));
            pawnImage1 =  ImageIO.read(this.getClass().getResource("7.png"));
            rookPImage1 =  ImageIO.read(this.getClass().getResource("8.png"));
            bishopPImage1 =  ImageIO.read(this.getClass().getResource("9.png"));
            silverPImage1 =  ImageIO.read(this.getClass().getResource("10.png"));
            knightPImage1 =  ImageIO.read(this.getClass().getResource("11.png"));
            lancePImage1 =  ImageIO.read(this.getClass().getResource("12.png"));
            pawnPImage1 =  ImageIO.read(this.getClass().getResource("13.png"));

            kingImage2 =  rotate180(kingImage1);
            rookImage2 =  rotate180(rookImage1);
            bishopImage2 =  rotate180(bishopImage1);
            goldImage2 =  rotate180(goldImage1);
            silverImage2 =  rotate180(silverImage1);
            knightImage2 =  rotate180(knightImage1);
            lanceImage2 =  rotate180(lanceImage1);
            pawnImage2 =  rotate180(pawnImage1);
            rookPImage2 =  rotate180(rookPImage1);
            bishopPImage2 =  rotate180(bishopPImage1);
            silverPImage2 =  rotate180(silverPImage1);
            knightPImage2 =  rotate180(knightPImage1);
            lancePImage2 =  rotate180(lancePImage1);
            pawnPImage2 =  rotate180(pawnPImage1);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    //Initializes chess board piece places
    private final void setupNewGame(boolean isWithAI) {

        isPlayingWithAI = isWithAI;
        if(isWithAI) cpuAI = new ChessAI(this);

        chessPieces = new ArrayList<ChessPiece>();

        chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.LANCE,0,8));
        chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.KNIGHT,1,8));
        chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.SILVERGEN,2,8));
        chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.GOLDGEN,3,8));
        chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.KING,4,8));
        chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.GOLDGEN,5,8));
        chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.SILVERGEN,6,8));
        chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.KNIGHT,7,8));
        chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.LANCE,8,8));

        chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.BISHOP,1,7));
        chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.ROOK,7,7));
        
        for(int pawni=0;pawni<9;pawni++){
            chessPieces.add(new ChessPiece(1, ChessPiece.pieceType.PAWN,pawni,6));
        }

        chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.LANCE,0,0));
        chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.KNIGHT,1,0));
        chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.SILVERGEN,2,0));
        chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.GOLDGEN,3,0));
        chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.KING,4,0));
        chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.GOLDGEN,5,0));
        chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.SILVERGEN,6,0));
        chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.KNIGHT,7,0));
        chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.LANCE,8,0));

        chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.ROOK,1,1));
        chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.BISHOP,7,1));

        for(int pawni=0;pawni<9;pawni++){
            chessPieces.add(new ChessPiece(2, ChessPiece.pieceType.PAWN,pawni,2));
        }

        turn = 1;

        redrawBoard();
    }

    public BufferedImage rotate180(BufferedImage bi) {
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.PI, bi.getWidth() / 2, bi.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_BILINEAR);
        return op.filter(bi, null);
    }

    public void redrawBoard(){
        //Clear Previous State
        for(int ii=0;ii<9;ii++){
            for(int jj=0;jj<9;jj++){
                chessBoardSquares[ii][jj].setIcon(new ImageIcon(new BufferedImage(70, 80, BufferedImage.TYPE_INT_ARGB)));
                chessBoardSquares[ii][jj].setBackground(mainbg);
                //chessBoardSquares[ii][jj].setComponentPopupMenu(null);
            }
        }

        for(ChessPiece chp : chessPieces){
            if(chp.player == 1){
                switch(chp.type){
                    case KING:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(kingImage1));
                        break;
                    case ROOK:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(rookImage1));
                        break;
                    case BISHOP:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(bishopImage1));
                        break;
                    case GOLDGEN:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(goldImage1));
                        break;
                    case SILVERGEN:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(silverImage1));
                        break;
                    case KNIGHT:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(knightImage1));
                        break;
                    case LANCE:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(lanceImage1));
                        break;
                    case PAWN:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(pawnImage1));
                        break;
                    case ROOK_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(rookPImage1));
                        break;
                    case BISHOP_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(bishopPImage1));
                        break;
                    case SILVERGEN_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(silverPImage1));
                        break;
                    case KNIGHT_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(knightPImage1));
                        break;
                    case LANCE_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(lancePImage1));
                        break;
                    case PAWN_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(pawnPImage1));
                        break;
                }
            }else{
                switch(chp.type){
                    case KING:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(kingImage2));
                        break;
                    case ROOK:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(rookImage2));
                        break;
                    case BISHOP:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(bishopImage2));
                        break;
                    case GOLDGEN:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(goldImage2));
                        break;
                    case SILVERGEN:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(silverImage2));
                        break;
                    case KNIGHT:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(knightImage2));
                        break;
                    case LANCE:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(lanceImage2));
                        break;
                    case PAWN:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(pawnImage2));
                        break;
                    case ROOK_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(rookPImage2));
                        break;
                    case BISHOP_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(bishopPImage2));
                        break;
                    case SILVERGEN_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(silverPImage2));
                        break;
                    case KNIGHT_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(knightPImage2));
                        break;
                    case LANCE_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(lancePImage2));
                        break;
                    case PAWN_P:
                        chessBoardSquares[chp.x][chp.y].setIcon(new ImageIcon(pawnPImage2));
                        break;
                }
            }
        }
    }


    public static void main(String[] args) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                ChessGUI cg = new ChessGUI();

                JFrame f = new JFrame("Shogi v0.9");
                f.add(cg.getGui());
                // Ensures JVM closes after frame(s) closed and
                // all non-daemon threads are finished
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                // See http://stackoverflow.com/a/7143398/418556 for demo.
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();
                // ensures the minimum size is enforced.
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
        SwingUtilities.invokeLater(r);
    }
}