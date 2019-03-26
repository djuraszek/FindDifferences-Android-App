package com.cookbook.android.znajdzroznice;

import android.content.Context;

public class ImagePiece extends android.support.v7.widget.AppCompatImageView{

    public int xCoord = 0;
    public int yCoord = 0;
    public int pieceWidth;
    public int pieceHeight;
    public boolean canMove = true;
    public int position;

    public ImagePiece(Context context) {
        super(context);
    }

}
