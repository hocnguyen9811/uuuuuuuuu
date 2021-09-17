package com.example.app_doctruyen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ReadComicActivity extends Activity implements OnTouchListener,
        OnClickListener {
    public static final String LOG_TAG = "TTTH";
    private ImageView img;
    private Bundle bundle;
    private String pathzip;
    @SuppressWarnings("rawtypes")
    private ArrayList arrImages;
    private int i = 1;
    private Bitmap result;
    private AlertDialog.Builder builderGoto;

    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    private ImageButton imgBtnNext;
    private ImageButton imgBtnBack;
    private AlertDialog.Builder buiderBrowser;
    private AlertDialog.Builder builderMode;
    protected static final int FIT_MATRIX = 0;
    protected static final int FIT_XY = 1;
    protected static final int FIT_CENTER = 2;
    protected static final int FIT_START = 3;
    protected static final int FIT_END = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        img = (ImageView) findViewById(R.id.imageView1);
        img.setOnTouchListener(this);



        // ImageButton
        imgBtnNext = (ImageButton) findViewById(R.id.imgb_next);
        // imgBtnNext.setBackgroundColor(Color.TRANSPARENT);
        imgBtnBack = (ImageButton) findViewById(R.id.imgb_back);
        // imgBtnBack.setBackgroundColor(Color.TRANSPARENT);
        imgBtnBack.setOnClickListener(this);
        imgBtnNext.setOnClickListener(this);

        imgBtnNext.setVisibility(View.INVISIBLE);
        imgBtnBack.setVisibility(View.INVISIBLE);

    }

    public void processoptMnuGoto() {
        if (arrImages == null) {
            return;
        }
        final String[] itemsGoto = { "Go to First Page", "Go to Middle Page",
                "Go to Last Page" };

        builderGoto = new AlertDialog.Builder(this);
        builderGoto.setTitle("Go to ");
        builderGoto.setItems(itemsGoto, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (arrImages == null) {
                    Toast.makeText(getApplicationContext(),
                            "Please open zip file ", 3000).show();
                } else {

                    if (item == 0) {
                        String firstImages = Integer.toString(0);
                        Toast.makeText(getApplicationContext(), firstImages,
                                5000).show();
                        img.setImageBitmap((Bitmap) arrImages.get(1));
                    } else if (item == 1) {
                        String haftImages = Integer.toString((int) arrImages
                                .size() / 2);
                        Toast.makeText(getApplicationContext(), haftImages,
                                5000).show();
                        img.setImageBitmap((Bitmap) arrImages
                                .get((int) arrImages.size() / 2));
                    } else if (item == 2) {
                        String totalImages = Integer.toString(arrImages.size());
                        Toast.makeText(getApplicationContext(), totalImages,
                                5000).show();
                        img.setImageBitmap((Bitmap) arrImages.get(arrImages
                                .size() - 1));
                    }
                }
            }
        });
        builderGoto.show();
    }

    public void getImageList() {
        if (pathzip == null) {
            return;
        }
        try {
            FileInputStream fis;
            fis = new FileInputStream(pathzip);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = null;
            result = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTempStorage = new byte[32 * 1024];
            options.inDither = false;
            options.inPurgeable = true;
            options.inPurgeable = true;
            arrImages = new ArrayList();
            while ((ze = zis.getNextEntry()) != null) {
                result = BitmapFactory.decodeStream(zis, null, options);
                arrImages.add(result);
            }

            zis.close();
            fis.close();
            Log.i(LOG_TAG, arrImages.toString());
            Log.i(LOG_TAG, Integer.toString(arrImages.size()));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getNextImage() {
        if (arrImages == null) {
            return;
        }
        img.setImageBitmap((Bitmap) arrImages.get(i));
    }

    public void getBackImage() {
        if (arrImages == null) {
            return;
        }
        img.setImageBitmap((Bitmap) arrImages.get(i));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        getMenuInflater().inflate(R.menu.mainmenu, menu);
        setMenuBackground();
        return super.onCreateOptionsMenu(menu);
    }

    protected void setMenuBackground() {
        // TODO Auto-generated method stub
        getLayoutInflater().setFactory(new Factory() {
            public View onCreateView(String name, Context context,
                                     AttributeSet attrs) {
                if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
                    try { // Ask our inflater to create the view
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView(name, null, attrs);

                        new Handler().post(new Runnable() {
                            public void run() {
                                // sets the background color
                                // view.setBackgroundResource(R.color.androidcolor);
                                view.setBackgroundColor(Color
                                        .parseColor("#4464BB"));
                                // sets the text color
                                ((TextView) view).setTextColor(Color.GREEN);
                                // sets the text size
                                ((TextView) view).setTextSize(14);
                            }
                        });
                        return view;
                    } catch (InflateException e) {
                    } catch (ClassNotFoundException e) {
                    }
                }
                return null;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {
            case R.id.mnu_open:
                // To do open comics screen
                Intent i = new Intent(this, OpenActivity.class);
                startActivity(i);
                return true;
            case R.id.mnu_download:
                // Go to download screen
                Intent ii = new Intent(this, ListComicActivity.class);
                startActivity(ii);
                return true;
            case R.id.mnu_goto:
                // method handler option menu
                processoptMnuGoto();
                return true;
            case R.id.mnu_setting:
                Intent iSetting = new Intent(this, SettingActivity.class);
                startActivity(iSetting);
                return true;
            case R.id.mnu_browser:
                selectBrower();
                return true;
            case R.id.mnu_mode:
                selectMode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i("TTTH", "onStop");
        if (arrImages != null) {
            arrImages.clear();
            arrImages = null;
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TTTH", "onStart");
        bundle = getIntent().getExtras();
        if (bundle != null) {
            pathzip = bundle.getString("path");
            getImageList();
            getNextImage();
        } else {
            Log.i(LOG_TAG, "bundle is null");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (arrImages == null) {
            // Toast.makeText(getApplicationContext(), "Please open a zip file",
            // 1000).show();
            return false;
        } else {

            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                i = i + 1;
                getNextImage();
                if (i == arrImages.size() - 1) {
                    i = 1;
                    getNextImage();
                }
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                i = i - 1;
                getBackImage();
                if (i == 0) {
                    i = arrImages.size() - 1;
                    getBackImage();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // Start touch
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Dump touch event to log

        ImageView view = (ImageView) v;

        dumpEvent(event);
        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (arrImages == null) {
                    Toast.makeText(this, "please open zip file on sdcard", 2000)
                            .show();
                }
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d("TTTH", "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_UP:
                imgBtnNext.setVisibility(View.VISIBLE);
                imgBtnBack.setVisibility(View.VISIBLE);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d("TTTH", "mode=NONE");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d("TTTH", "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d("TTTH", "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                imgBtnNext.setVisibility(View.INVISIBLE);
                imgBtnBack.setVisibility(View.INVISIBLE);
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);

                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    Log.d("TTTH", "newDist=" + newDist);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }
        view.setImageMatrix(matrix);
        return true; // indicate event was handled
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);

    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    private void dumpEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d("TTTH", sb.toString());
    }

    // End Touch

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if (arrImages == null) {
            Toast.makeText(this, "please open zip file on sdcard", 2000).show();
            return;
        }
        if (view.getId() == R.id.imgb_next) {
            Log.i(LOG_TAG, "ImageButton NEXT clicked --!! ");
            i = i + 1;
            getNextImage();
            if (i == arrImages.size() - 1) {
                i = 1;
                getNextImage();
            }
        } else if (view.getId() == R.id.imgb_back) {
            Log.i(LOG_TAG, "ImageButton BACK clicked --!! ");
            i = i - 1;
            getBackImage();
            if (i == 0) {
                i = arrImages.size() - 1;
                getBackImage();
            }
        }
    }

    public void selectMode() {
        if(arrImages == null){
            return;
        }
        final String[] mode = { "MATRIX", "FIT XY", "FIT CENTER", "FIT START",
                "FIT END" };
        builderMode = new AlertDialog.Builder(this);
        builderMode.setTitle("Config Mode");
        builderMode.setItems(mode, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("TTTH", "Items of AlertDialog Mode selected !!!");
                if (which == FIT_MATRIX) {
                    Log.i("TTTH", "Item FIT_MATRIX has selected");
                    img.setScaleType(ScaleType.MATRIX);
                } else if (which == FIT_XY) {
                    Log.i("TTTH", "Item FIT_XY has selected");
                    img.setScaleType(ScaleType.FIT_XY);
                } else if (which == FIT_CENTER) {
                    Log.i("TTTH", "Item FIT_CENTER has selected");
                    img.setScaleType(ScaleType.FIT_CENTER);
                } else if (which == FIT_START) {
                    Log.i("TTTH", "Item FIT_START has selected");
                    img.setScaleType(ScaleType.FIT_START);
                } else if (which == FIT_END) {
                    Log.i("TTTH", "Item FIT_END has selected");
                    img.setScaleType(ScaleType.FIT_END);
                }
            }
        });
        builderMode.show();

    }

