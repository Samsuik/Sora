package me.samsuik.sakura.tps.graph;

import me.samsuik.sakura.tps.ServerTickInformation;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public final class DetailedTPSGraph extends TPSGraph {
    public DetailedTPSGraph(int width, int height, double scale, List<ServerTickInformation> tickInformation) {
        super(width, height, scale, tickInformation);
    }

    @Override
    public BuiltComponentCanvas plot() {
        ComponentCanvas canvas = new ComponentCanvas(this.width, this.height);
        canvas.fill(GraphComponents.BACKGROUND);

        this.basicOutline(canvas);
        this.prettifyOutline(canvas);
        this.addColourAndHoverInformation(canvas);

        canvas.flip();
        return canvas.build();
    }

    private void basicOutline(ComponentCanvas canvas) {
        for (int x = 0; x < this.width; ++x) {
            int row = this.rowFromColumn(x);
            int nextRow = this.rowFromColumn(x + 1);
            int minRow = Math.min(row, nextRow);
            int maxRow = Math.max(row, nextRow);

            if (maxRow - minRow >= 2) {
                canvas.set(x, minRow, GraphComponents.TOP_DOTTED_LINE);
                canvas.set(x, maxRow, GraphComponents.BOTTOM_DOTTED_LINE);

                for (int y = minRow + 1; y < maxRow; ++y) {
                    canvas.set(x, y, GraphComponents.VERTICAL_LINE);
                }
            } else {
                canvas.set(x, row, GraphComponents.HORIZONTAL_LINE);
            }
        }
    }

    private void prettifyOutline(ComponentCanvas canvas) {
        for (int x = 0; x < this.width; ++x) {
            int row = this.rowFromColumn(x);
            int nextRow = this.rowFromColumn(x + 1);
            int prevRow = this.rowFromColumn(x - 1);
            int minRow = Math.min(row, nextRow);
            int maxRow = Math.max(row, nextRow);

            if (maxRow - minRow >= 2) {
                this.prettifyVerticalOutline(canvas, x, row, nextRow, prevRow, minRow, maxRow);
            } else {
                this.prettifySlopes(canvas, x, row, nextRow, prevRow);
            }
        }
    }

    private void prettifyVerticalOutline(ComponentCanvas canvas, int x, int row, int nextRow, int prevRow, int minRow, int maxRow) {
        if (minRow == nextRow) {
            canvas.set(x, minRow, GraphComponents.CONE_BOTTOM_LEFT);
        } else if (prevRow <= minRow) {
            canvas.set(x, minRow, GraphComponents.CONE_BOTTOM_RIGHT);
        }
        if (prevRow == row + 1 && nextRow < row) {
            canvas.set(x, maxRow, GraphComponents.CONE_TOP_RIGHT);
        }
        if (maxRow == row && Math.abs(nextRow - maxRow) > 1 && Math.abs(prevRow - maxRow) > 1 && prevRow < maxRow) {
            canvas.set(x - 1, maxRow, GraphComponents.CONE_TOP_LEFT);
            canvas.set(x, maxRow, GraphComponents.CONE_TOP_RIGHT);
        }
        if (minRow == row && Math.abs(nextRow - minRow) > 1 && Math.abs(prevRow - minRow) > 1 && prevRow > minRow) {
            canvas.set(x - 1, minRow, GraphComponents.CONE_BOTTOM_LEFT);
            canvas.set(x, minRow, GraphComponents.CONE_BOTTOM_RIGHT);
        }
    }

    private void prettifySlopes(ComponentCanvas canvas, int x, int row, int nextRow, int prevRow) {
        int slopeDirection = nextRow - prevRow;
        int slopeChange = Math.abs(slopeDirection);

        if (slopeChange >= 2 && Math.max(nextRow, prevRow) == row + 1) {
            canvas.set(x, row, slopeDirection < 0 ? GraphComponents.TL_TO_BR : GraphComponents.BL_TO_TR);
        } else if (Math.abs(row - nextRow) == 1 || slopeDirection == 0) {
            if (row < nextRow) {
                canvas.set(x, row, GraphComponents.TOP_DOTTED_LINE);
            } else if (row > nextRow) {
                canvas.set(x, row, GraphComponents.BOTTOM_DOTTED_LINE);
            }
        } else if (Math.abs(row - prevRow) == 1) {
            if (prevRow > row) {
                canvas.set(x, row, GraphComponents.TOP_DOTTED_LINE);
            } else if (prevRow < row) {
                canvas.set(x, row, GraphComponents.BOTTOM_DOTTED_LINE);
            }
        }
    }
}
