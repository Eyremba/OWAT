package com.ebp.owat.lib.datastructure.matrix.Linked;

import com.ebp.owat.lib.datastructure.matrix.Matrix;
import com.ebp.owat.lib.datastructure.matrix.utils.Plane;

public class FixedLinkedNodePos<T> extends LinkedNodePos<T> {
	
	/** The fixed position this value is. */
	public final FixedNodePosition pos;
	
	/**
	 * Most basic constructor for a LinkedNodePos.
	 *
	 * @param matrix The matrix this potision is a part of.
	 */
	public FixedLinkedNodePos(Matrix<T> matrix, FixedNodePosition posIn) {
		super(matrix);
		this.pos = posIn;
		this.determinePos();
	}
	
	/**
	 * Method used by the position to recalculate and update the appropriate position of this value.
	 * <p>
	 * Sets the x&y coordinates
	 */
	@Override
	protected void determinePos() {
		if(this.node == null){
			if(this.matrix.isEmpty()){
				throw new IllegalStateException("Cannot setup fixed value position in empty matrix.");
			}
			this.matrix.get(0L, 0L);
		}
		//go to the border value this position is supposed to go to
		for(NodeDir curDirToGo : this.pos.getBorderDirs()){
			while(!this.node.isBorder(curDirToGo)){
				this.node = this.node.getNeighbor(curDirToGo);
				this.setX(
						curDirToGo.incDec(Plane.X, this.getX())
				);
				this.setY(
						curDirToGo.incDec(Plane.Y, this.getY())
				);
			}
		}
	}
}