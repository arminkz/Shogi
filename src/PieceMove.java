import java.awt.*;

/**
 * Created by Armin on 4/4/2016.
 */
public class PieceMove {

    public int chessPieceIndex;
    public Point finalPos;

    public PieceMove(int pieceIndex , Point finalPos){
        this.chessPieceIndex = pieceIndex;
        this.finalPos = finalPos;
        isGoingToCapture = false;
        moveScore = 0;
    }

    public int moveScore;

    public boolean isGoingToCapture;
    public ChessPiece CapturingPiece;
}
