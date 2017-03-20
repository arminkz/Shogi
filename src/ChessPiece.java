/**
 * Created by Armin on 3/28/2016.
 */
public class ChessPiece {

    public enum pieceType{
        KING,
        ROOK,
        BISHOP,
        GOLDGEN,
        SILVERGEN,
        KNIGHT,
        LANCE,
        PAWN,
        ROOK_P,
        BISHOP_P,
        SILVERGEN_P,
        KNIGHT_P,
        LANCE_P,
        PAWN_P
    }

    public pieceType type;
    public int player;

    public int x;
    public int y;

    public boolean mustAdd;
    public int mustAddplayer;
    public int mustAddbutIndex;

    public ChessPiece(int player,pieceType type,int x,int y){
        this.player = player;
        this.type = type;
        this.x = x;
        this.y = y;
        this.mustAdd = false;
    }
}
