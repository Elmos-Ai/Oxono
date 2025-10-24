package elmos.oxono.model;

import java.util.HashMap;
import java.util.Map;

public enum PieceType {
    CROSS("x"), CIRCLE("o"), NONE("n"), AVAILABLE("a");

    private static final Map<String, PieceType> map = new HashMap<>(values().length, 1);

    static {
        for (PieceType c : values()) map.put(c.symbol, c);
    }

    private final String symbol;

    private PieceType(String category) {
        this.symbol = category;
    }

    /**
     * Returns the {@link PieceType} corresponding to the given symbol.
     *
     * @param name the symbol of the piece type (e.g., "x" for CROSS, "o" for CIRCLE).
     * @return the corresponding {@link PieceType}.
     * @throws IllegalArgumentException if the provided name is invalid or null.
     */
    public static PieceType of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("the name is null");
        }
        PieceType result = map.get(name);
        if (result == null) {
            throw new IllegalArgumentException("Invalid category name: " + name);
        }
        return result;
    }

    /**
     * Returns the symbol representing the piece type.
     *
     * @return the symbol of the piece type (e.g., "x" for CROSS, "o" for CIRCLE).
     */
    public String getSymbol() {
        return symbol;
    }
}

