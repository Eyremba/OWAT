package com.ebp.owat.lib.utils.key;

import com.ebp.owat.lib.datastructure.set.LongLinkedList;
import com.ebp.owat.lib.datastructure.value.Value;
import com.ebp.owat.lib.utils.scramble.ScrambleMove;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Iterator;
import java.util.Objects;

import static com.ebp.owat.lib.utils.key.SerializationConstants.META;
import static com.ebp.owat.lib.utils.key.SerializationConstants.SCRAMBLE;

public class ScrambleKey {
	
	private enum Mode{
		SCRAMBLING,DESCRAMBLING;
	}
	
	/** The metadata that describes the matrix. */
	@JsonProperty(META)
	public final KeyMetaData meta;
	
	/** The moves used to scramble the matrix. */
	private LongLinkedList<ScrambleMove> moves;
	
	/** If this key is part of the scrambling or descrambling process. */
	@JsonIgnore
	public final Mode mode;
	
	public ScrambleKey(
		long originalHeight,
		long originalWidth,
		long dataHeight,
		long dataWidth,
		Class<? extends Value> type,
		long lastRowIndex
	) {
		this.mode = Mode.SCRAMBLING;
		this.meta = new KeyMetaData(originalHeight, originalWidth, dataHeight, dataWidth, KeyMetaData.getTypeStr(type), lastRowIndex);
		this.moves = new LongLinkedList<>();
	}
	
	@JsonCreator
	public ScrambleKey(@JsonProperty(META) KeyMetaData meta, @JsonProperty(SCRAMBLE) String movesStr){
		this.mode = Mode.DESCRAMBLING;
		this.meta = meta;
		this.moves = ScrambleMove.parseMulti(movesStr);
	}
	
	/**
	 * Adds a move to the list.
	 * @param move The move to add to the list.
	 * @throws IllegalStateException If the key is not set to be scrambling.
	 */
	public void addMove(ScrambleMove move){
		if(this.mode != Mode.SCRAMBLING){
			throw new IllegalStateException("The mode of the ScrambleKey is not set to SCRAMBLING. Cannot add a move when descrambling.");
		}
		this.moves.addFirst(move);
	}
	
	/**
	 * Gets an iterator for the moves list.
	 * @return An iterator of the moves held. REMOVES the moves as it iterates.
	 * @throws IllegalStateException If the key is set to be descrambling
	 */
	@JsonIgnore
	public Iterator<ScrambleMove> getMovesIt(){
		if(this.mode != Mode.DESCRAMBLING){
			throw new IllegalStateException("The mode of the ScrambleKey is not set to DESCRAMBLING. Cannot remove moves when scrambling.");
		}
		return this.moves.destructiveIterator();
	}
	
	@JsonGetter(SCRAMBLE)
	public String getMoves(){
		StringBuilder sb = new StringBuilder();
		
		for(ScrambleMove curMove : moves){
			curMove.toKeyString(sb, true);
		}
		
		return sb.toString();
	}
	
	@JsonIgnore
	public long getNumMoves(){
		return this.moves.sizeL();
	}
	
	/**
	 * Same as {@link #equals(Object)}, but includes a flag to include the mode in the comparison.
	 * @param o
	 * @param includeMode
	 * @return
	 */
	public boolean equals(Object o, boolean includeMode){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ScrambleKey that = (ScrambleKey) o;
		return Objects.equals(meta, that.meta) &&
			Objects.equals(moves, that.moves) &&
			(includeMode ? this.mode == that.mode : true);
	}
	
	@Override
	public boolean equals(Object o) {
		return this.equals(o, false);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(meta, moves);
	}
}
