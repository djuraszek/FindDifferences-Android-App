package com.cookbook.android.znajdzroznice;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.StrictMath.abs;

public class MainActivity extends AppCompatActivity {
//    ArrayList<ImagePiece> pieces;
    int DIFFERENCES_TO_FIND = 5;
    int differencesFound = 0;
    long startTime;
    ArrayList<Integer> positionsFound = new ArrayList<>();
    Button startBtn;
    TextView timer, points;
    ImageView image1, image2;
    RelativeLayout layout, startBtnLayout;

    ArrayList<Bitmap> listImage1, listImage2;
    ArrayList<ImagePiece> listImageButton1, listImageButton2;


    Handler timerHandler = new Handler();
    Runnable timerRunnable ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVals();
        addPiecesToLayout();
    }

    public void addPiecesToLayout(){

        // run image related code after the view was laid out
        // to have all dimensions calculated
        image1.post(new Runnable() {
            @Override
            public void run() {
                listImageButton1 = splitImage(image1);
                System.out.println("r1: "+listImageButton1.size());
                int position = 0;
                for(ImagePiece piece : listImageButton1) {
                    layout.addView(piece);
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                    lParams.leftMargin = piece.xCoord;
                    lParams.topMargin = piece.yCoord ;
                    piece.position = position;
                    piece.setLayoutParams(lParams);


                    piece.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ImagePiece thisPiece = (ImagePiece) v;
                            if(!positionsFound.contains(thisPiece.position))
                                if(!compareBitmaps(listImageButton2.get(thisPiece.position),thisPiece)){
                                    System.out.println("p1: "+listImageButton2.get(thisPiece.position).position);
                                    System.out.println("p2: "+thisPiece.position);
                                    System.out.println("znaleziono! "+thisPiece.position);
                                    findDifference(thisPiece,listImageButton2.get(thisPiece.position));
                                    positionsFound.add(thisPiece.position);
                                    differencesFound++;
                                    actualiseScore();
                                }
                        }
                    });
                    position++;
                }
            }
        });

        image2.post(new Runnable() {
            @Override
            public void run() {
                listImageButton2 = splitImage(image2);
                System.out.println("r2: "+listImageButton2.size());
                int position = 0;
                for(ImagePiece piece : listImageButton2) {
                    layout.addView(piece);
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                    lParams.leftMargin = piece.xCoord ; //+ layout.getLeft()
                    lParams.topMargin = piece.yCoord;   // + layout.getTop();
                    piece.setLayoutParams(lParams);
                    piece.position = position;
                    System.out.println(position);

                    piece.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ImagePiece thisPiece = (ImagePiece) v;
                            if(!positionsFound.contains(thisPiece.position))
                                if(!compareBitmaps(thisPiece, listImageButton1.get(thisPiece.position))){
                                    System.out.println("p1: "+listImageButton1.get(thisPiece.position).position);
                                    System.out.println("p2: "+thisPiece.position);
                                    System.out.println("znaleziono! "+thisPiece.position);
                                    findDifference(listImageButton1.get(thisPiece.position),thisPiece);
                                    positionsFound.add(thisPiece.position);
                                    differencesFound++;
                                    actualiseScore();
                                }

                        }
                    });
                    position++;
                }
            }
        });
    }

    public void actualiseScore(){
        points.setText(differencesFound+"/"+DIFFERENCES_TO_FIND);

        //end
        if(differencesFound==DIFFERENCES_TO_FIND){
            //todo stop stoper
            timerHandler.removeCallbacks(timerRunnable);


        }
    }

    public boolean compareBitmaps(ImagePiece img1, ImagePiece img2 ){
        Bitmap first = ((BitmapDrawable)img1.getDrawable()).getBitmap();
        Bitmap second = ((BitmapDrawable)img2.getDrawable()).getBitmap();
        System.out.println(first.sameAs(second));
        System.out.println();
        return first.sameAs(second);
    }

    private void findDifference(ImagePiece img1, ImagePiece img2 ) {
        Bitmap firstImage = ((BitmapDrawable)img1.getDrawable()).getBitmap();
        Bitmap secondImage = ((BitmapDrawable)img2.getDrawable()).getBitmap();
        Bitmap bmp = secondImage.copy(secondImage.getConfig(), true);

        if (firstImage.getWidth() != secondImage.getWidth()
                || firstImage.getHeight() != secondImage.getHeight()) {
            return;
        }

        for (int i = 0; i < firstImage.getWidth(); i++) {
            for (int j = 0; j < firstImage.getHeight(); j++) {
                if (firstImage.getPixel(i, j) != secondImage.getPixel(i, j)) {
                    bmp.setPixel(i, j, Color.RED);
                }
            }
        }
        img2.setImageBitmap(bmp);
    }



    private ArrayList<ImagePiece> splitImage(ImageView imageView) {
        int piecesNumber = 100;
        int rows = (int)Math.sqrt(piecesNumber);
        int cols = (int)Math.sqrt(piecesNumber);

        ArrayList<ImagePiece> pieces = new ArrayList<>(piecesNumber);

        // Get the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        // Calculate the with and height of the pieces
        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;

        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord, yCoord, pieceWidth, pieceHeight);
                ImagePiece piece = new ImagePiece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.xCoord = xCoord+ imageView.getLeft();
                piece.yCoord = yCoord+ imageView.getTop();
                piece.pieceWidth = pieceWidth;
                piece.pieceHeight = pieceHeight;
                pieces.add(piece);
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }

        return pieces;
    }




    private int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    public void setVals(){
//        piecesLayout = (RelativeLayout)findViewById(R.id.imageLayout);
        layout = (RelativeLayout)findViewById(R.id.layout);
        timer = (TextView)findViewById(R.id.timer);
        points = (TextView)findViewById(R.id.countFound);
        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        startBtnLayout = (RelativeLayout)findViewById(R.id.startBtnLayout);
        startBtn = (Button)findViewById(R.id.startBtn);

        listImageButton1 = new ArrayList<>();
        listImageButton2 = new ArrayList<>();

        startBtnLayout.setVisibility(View.VISIBLE);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtnLayout.setVisibility(View.GONE);
                //todo start timer

                startTime = System.currentTimeMillis();

                timerRunnable = new Runnable() {

                    @Override
                    public void run() {
                        long millis = System.currentTimeMillis() - startTime;
                        int seconds = (int) (millis / 1000);
                        int minutes = seconds / 60;
                        seconds = seconds % 60;

                        timer.setText(String.format("%d:%02d", minutes, seconds));

                        timerHandler.postDelayed(this, 500);
                    }
                };
                timerRunnable.run();
            }
        });


    }
}
