package g.g.d.com;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import g.g.d.com.R;

public class GDGalleryAdapter extends BaseAdapter {
    int GDGalleryItemBg; // 앞서 정의해 둔 attrs.xml의 resource를 background로 받아올 변수 선언
    Context mContext; // CustomGalleryAdapter를 선언할 때 해당 activity의 context를 받아오기 위한 context 변수
    String[] mImgs; // 위 mBasePath내의 file list를 String 배열로 저장받을 변수
    Bitmap bm; // 지정 경로의 사진을 Bitmap으로 받아오기 위한 변수
    private static HashMap<String, Bitmap> bitmapHash = new HashMap<String, Bitmap>();

    public String TAG = "Gallery Adapter Example :: ";

    public GDGalleryAdapter(Context context, String[] mUrls){ // CustomGalleryAdapter의 생성자
        this.mContext = context;

        mImgs = mUrls; //  method를 통해 directory 내 file 명들을 String[] 에 저장

        /* 앞서 정의한 attrs.xml에서 gallery array의 배경 style attribute를 받아옴 */
        TypedArray array = mContext.obtainStyledAttributes(R.styleable.GalleryTheme);
        GDGalleryItemBg = array.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);
        array.recycle();
    }

    public int getCount() { // Gallery array의 객체 갯수를 앞서 세어 둔 mUrls를 받은 String[]의 원소 갯수와 동일하다는 가정 하에 반환
        return mImgs.length;
    }


    public Object getItem(int position) { // Gallery array의 해당 position을 반환
        return position;
    }

    public long getItemId(int position) { // Gallery array의 해당 position을 long 값으로 반환
        return position;
    }


    // Override this method according to your need
    // 지정 경로 내 사진들을 보여주는 method.
    // Bitmap을 사용할 경우, memory 사용량이 커서 Thumbnail을 사용하거나 크기를 줄일 필요가 있음
    // setImageDrawable()이나 setImageURI() 등의 method로 대체 가능

    public View getView(int index, View view, ViewGroup viewGroup)
    {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(mContext); // Gallery array에 들어갈 ImageView 생성
        String imageUrl = mImgs[index];
        Glide.with(viewGroup).load(imageUrl).into(i);

     //   Drawable d = LoadImageFromWebOperations(mImgs[index], index);
   //     ImageLoadTask ltask = new ImageLoadTask(mImgs[index]);
   //     ltask.execute();
        // 이 부분에서부터 'options.inJustDecodeBounds=false'까지는
        // Bitmap 사용시 나타나는 memory 부족 현상을 예방하기 위한 code. 경우에 따라서는 생략해도 가능하다.
   //     BitmapFactory.Options options = new BitmapFactory.Options();
  //      options.inJustDecodeBounds = true;

    //    int width = options.outWidth;
  //      int height = options.outHeight;
  //      int inSampleSize = 1;
  //      int reqWidth = 256;
//        int reqHeight = 192;
  //      if((width > reqWidth) || (height > reqHeight)){
   //         final int halfHeight = height / 2;
  //          final int halfWidth = width / 2;

   //         while ((halfHeight / inSampleSize) > reqHeight
  //                  && (halfWidth / inSampleSize) > reqWidth) {
  //              inSampleSize *= 2;
   //         }
  //      }
    //    options.inSampleSize = inSampleSize;
   //     options.inJustDecodeBounds = false;

        Log.d(TAG, "Gallery : " + mImgs[index]);
      //  Bitmap bm2 = ThumbnailUtils.extractThumbnail(bm, 300, 300); // 크기가 큰 원본 thumnail을 추출.
        // 이를 통해 view 가 까맣게 보이는 null 값을 피할 수 있었다.
        i.setLayoutParams(new Gallery.LayoutParams(700, 700));
      // i.setImageDrawable(d);

     //   i.setImageBitmap(bm2);
        i.setVisibility(ImageView.VISIBLE);
        i.setBackgroundResource(GDGalleryItemBg); // Gallery 원소의 BackGround
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);

        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
        return i;
    }


    private Drawable LoadImageFromWebOperations(String url,int i)
    {
        try
        {

            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, String.valueOf(i));
            return d;
        }catch (Exception e) {
            System.out.println("Exc="+e);
            return null;
        }
    }

    public class ImageLoadTask extends AsyncTask<Void,Void, Bitmap> {

        private String urlStr;

        public ImageLoadTask(String urlStr) {
            this.urlStr = urlStr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;
            try {
                if (bitmapHash.containsKey(urlStr)) {
                    Bitmap oldbitmap = bitmapHash.remove(urlStr);
                    if(oldbitmap != null) {
                        oldbitmap.recycle();
                        oldbitmap = null;
                    }
                }
                URL url = new URL(urlStr);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                bitmapHash.put(urlStr,bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
            bm = bitmap;
            return bitmap;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            bm = bitmap;
        }
    }

}

