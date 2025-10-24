package elmos.oxono.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a bag containing pink and black pawns of different types,
 * as well as two totems representing CROSS and CIRCLE types.
 */

public class Bag {
    private Pawn[] pinkPawns;
    private Pawn[] blackPawns;
    private Totem totemCross;
    private Totem totemCircle;
    private int pinkCirclePawnsCount = 8;
    private int pinkCrossPawnsCount = 8;
    private int blackCirclePawnsCount = 8;
    private int blackCrossPawnCount = 8;


        /**
         * Initializes a new Bag instance with pink and black pawns, and two totems.
         */
        public Bag() {
            this.pinkPawns = new Pawn[16];
            this.blackPawns = new Pawn[16];
            init();
        }
        private void init() {
            for (int i = 0; i < 16; i++) {
                if (i < 8) {
                    this.pinkPawns[i] = new Pawn(PieceType.CROSS, PieceColor.PINK);
                    this.blackPawns[i] = new Pawn(PieceType.CROSS, PieceColor.BLACK);
                } else {
                    this.pinkPawns[i] = new Pawn(PieceType.CIRCLE, PieceColor.PINK);
                    this.blackPawns[i] = new Pawn(PieceType.CIRCLE, PieceColor.BLACK);
                }
            }
            this.totemCross = new Totem(PieceType.CROSS);
            this.totemCircle = new Totem(PieceType.CIRCLE);
        }

        /**
         * Returns the CROSS totem.
         *
         * @return the totem representing the CROSS piece type
         */
        public Totem getTotemCross() {
            return totemCross;
        }

        /**
         * Returns the CIRCLE totem.
         *
         * @return the totem representing the CIRCLE piece type
         */
        public Totem getTotemCircle() {
            return totemCircle;
        }

        /**
         * Removes a pink pawn of the specified piece type from the bag.
         *
         * @param pieceType the type of the pawn to remove (CROSS or CIRCLE)
         * @return true if a pawn was successfully removed, false otherwise
         */
        public boolean removePinkPawn(PieceType pieceType) {
            if (pieceType.equals(PieceType.CROSS)) {
                for (int i = 0; i < 8; i++) {
                    if (this.pinkPawns[i].getSymbol().equals(PieceType.CROSS)) {
                        this.pinkPawns[i] = new Pawn(PieceType.NONE, PieceColor.PINK);
                        this.pinkCrossPawnsCount -= 1;
                        return true;
                    }
                }
            } else {
                for (int i = 8; i < pinkPawns.length; i++) {
                    if (this.pinkPawns[i].getSymbol().equals(PieceType.CIRCLE)) {
                        this.pinkPawns[i] = new Pawn(PieceType.NONE, PieceColor.PINK);
                        this.pinkCirclePawnsCount -= 1;
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Removes a black pawn of the specified piece type from the bag.
         *
         * @param pieceType the type of the pawn to remove (CROSS or CIRCLE)
         * @return true if a pawn was successfully removed, false otherwise
         */
        public boolean removeBlackPawn(PieceType pieceType) {
            if (pieceType.equals(PieceType.CROSS)) {
                for (int i = 0; i < 8; i++) {
                    if (this.blackPawns[i].getSymbol().equals(PieceType.CROSS)) {
                        this.blackPawns[i] = new Pawn(PieceType.NONE, PieceColor.BLACK);
                        this.blackCrossPawnCount -= 1;
                        return true;
                    }
                }
            } else {
                for (int i = 8; i < blackPawns.length; i++) {
                    if (this.blackPawns[i].getSymbol().equals(PieceType.CIRCLE)) {
                        this.blackPawns[i] = new Pawn(PieceType.NONE, PieceColor.BLACK);
                        this.blackCirclePawnsCount -= 1;
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Adds a pink pawn of the specified piece type to the bag.
         *
         * @param pieceType the type of the pawn to add (CROSS or CIRCLE)
         * @return true if a pawn was successfully added, false otherwise
         */
        public boolean addPinkPawn(PieceType pieceType) {
            if (pieceType.equals(PieceType.CROSS)) {
                for (int i = 0; i < 8; i++) {
                    if (this.pinkPawns[i].getSymbol().equals(PieceType.NONE)) {
                        this.pinkPawns[i] = new Pawn(PieceType.CROSS, PieceColor.PINK);
                        this.pinkCrossPawnsCount += 1;
                        return true;
                    }
                }
            } else {
                for (int i = 8; i < pinkPawns.length; i++) {
                    if (this.pinkPawns[i].getSymbol().equals(PieceType.NONE)) {
                        this.pinkPawns[i] = new Pawn(PieceType.CIRCLE, PieceColor.PINK);
                        this.pinkCirclePawnsCount += 1;
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Adds a black pawn of the specified piece type to the bag.
         *
         * @param pieceType the type of the pawn to add (CROSS or CIRCLE)
         * @return true if a pawn was successfully added, false otherwise
         */
        public boolean addBlackPawn(PieceType pieceType) {
            if (pieceType.equals(PieceType.CROSS)) {
                for (int i = 0; i < 8; i++) {
                    if (this.blackPawns[i].getSymbol().equals(PieceType.NONE)) {
                        this.blackPawns[i] = new Pawn(PieceType.CROSS, PieceColor.BLACK);
                        this.blackCrossPawnCount += 1;
                        return true;
                    }
                }
            } else {
                for (int i = 8; i < blackPawns.length; i++) {
                    if (this.blackPawns[i].getSymbol().equals(PieceType.NONE)) {
                        this.blackPawns[i] = new Pawn(PieceType.CIRCLE, PieceColor.BLACK);
                        this.blackCirclePawnsCount += 1;
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Returns the count of pink pawns of type CIRCLE in the bag.
         *
         * @return the number of pink CIRCLE pawns
         */
        public int getPinkCirclePawnsCount() {
            return pinkCirclePawnsCount;
        }

        /**
         * Returns the count of pink pawns of type CROSS in the bag.
         *
         * @return the number of pink CROSS pawns
         */
        public int getPinkCrossPawnsCount() {
            return pinkCrossPawnsCount;
        }

        /**
         * Returns the count of black pawns of type CIRCLE in the bag.
         *
         * @return the number of black CIRCLE pawns
         */
        public int getBlackCirclePawnsCount() {
            return blackCirclePawnsCount;
        }

        /**
         * Returns the count of black pawns of type CROSS in the bag.
         *
         * @return the number of black CROSS pawns
         */
        public int getBlackCrossPawnCount() {
            return blackCrossPawnCount;
        }
    }

