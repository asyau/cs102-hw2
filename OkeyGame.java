import java.util.Arrays;
import java.util.Collections;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    int currentTile = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < 2; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already sorted
     */
    public void distributeTilesToPlayers() {
        int currentDistributingPlayer = 0;
        while (currentTile < 57) {
            players[currentDistributingPlayer].addTile(tiles[currentTile]);
            if (currentTile == 14 || currentTile == 28 || currentTile == 42) {
                currentDistributingPlayer ++;
            }
            currentTile ++;

        }
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        players[currentPlayerIndex].addTile(lastDiscardedTile);

        return lastDiscardedTile.toString();
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {
        Tile tileToPick = tiles[currentTile];
        players[currentPlayerIndex].addTile(tileToPick);
        currentTile++;

        return tileToPick.toString();
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {
        Collections.shuffle(Arrays.asList(tiles));
    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game. Use calculateLongestChainPerTile method to get the
     * longest chains per tile.
     * To win, you need one of the following cases to be true:
     * - 8 tiles have length >= 4 and remaining six tiles have length >= 3 the last one can be of any length
     * - 5 tiles have length >= 5 and remaining nine tiles have length >= 3 the last one can be of any length
     * These are assuming we check for the win condition before discarding a tile
     * The given cases do not cover all the winning hands based on the original
     * game and for some rare cases it may be erroneous but it will be enough
     * for this simplified version
     */
    public boolean didGameFinish() {
        int threeLength = 0;
        int fourLength = 0;
        int fiveLength = 0;

        for (int index : players[currentPlayerIndex].calculateLongestChainPerTile()) {
            if (index >= 3) {
                threeLength++;
            }
            else if (index >= 4) {
                fourLength++;
            }
            else if (index >= 5) {
                fiveLength++;
            }
        }
        if ((fourLength == 8 && threeLength >= 6) || (fiveLength == 5 && threeLength >= 9)) {
            return true;
        }

        return false;
    }

    /*

     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You may choose randomly or consider if the discarded tile is useful for
     * the current status. Print whether computer picks from tiles or discarded ones.
     */
    public void pickTileForComputer() {
        Player currentPlayer = players[currentPlayerIndex];
        if (currentPlayer.findLongestChainOf(lastDiscardedTile) >= 3 && !doesPlayerTilesContain(currentPlayer, lastDiscardedTile)) {
            getLastDiscardedTile();
        }
        else {
            getTopTile();
        }


    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * For this use the findLongestChainOf method in Player class to calculate
     * the longest chain length per tile of this player,
     * then choose the tile with the lowest chain length and discard it
     * this method should print what tile is discarded since it should be
     * known by other players
     */
    public void discardTileForComputer() {
        int shortestChain = 15;
        int currentDiscardingTileIndex = 0;
        for (int i = 0; i < 15; i++) {
            if (shortestChain > players[currentPlayerIndex].findLongestChainOf(players[currentPlayerIndex].playerTiles[i])) {
                shortestChain = players[currentPlayerIndex].findLongestChainOf(players[currentPlayerIndex].playerTiles[i]);
                currentDiscardingTileIndex = i;
            }
        }
        discardTile(currentDiscardingTileIndex);
        
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        lastDiscardedTile = players[currentPlayerIndex].getAndRemoveTile(tileIndex);
    }

    public boolean doesPlayerTilesContain(Player player, Tile givenTile){
        for (Tile tile  : player.getTiles()) {
            if (tile.toString().equals(givenTile.toString())) {
                return true;
            }
        }
        return false;
    }

    public void currentPlayerSortTilesColorFirst() {
        players[currentPlayerIndex].sortTilesColorFirst();
    }

    public void currentPlayerSortTilesValueFirst() {
        players[currentPlayerIndex].sortTilesValueFirst();
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
