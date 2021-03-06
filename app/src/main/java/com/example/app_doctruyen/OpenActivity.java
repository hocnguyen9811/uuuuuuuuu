package com.example.app_doctruyen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class OpenActivity extends AppCompatActivity {
    public static final String OPEN_LOG = "TTTH";
    private List<String> item = null;
    private List<String> path = null;
    private String root = "/sdcard";

    ListView lv_sdcard;
    private ImageView img;
    private ProgressDialog myProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.open);
        img = (ImageView) findViewById(R.id.imageView1);
        lv_sdcard = getListView();
        getDir(root);
    }

    private void getDir(String dirPath) {
        item = new ArrayList<String>();
        path = new ArrayList<String>();

        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (!dirPath.equals(root)) {

            item.add(root);
            path.add(root);

            item.add("../");
            path.add(f.getParent());

        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            path.add(file.getPath());
            if (file.isDirectory())
                item.add(file.getName() + "/");
            else
                item.add(file.getName());
        }

        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, item);
        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        File file = new File(path.get(position));

        if (file.isDirectory()) {
            if (file.canRead())
                getDir(path.get(position));
            else {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle(
                                "[" + file.getName()
                                        + "] folder can't be read!")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                    }
                                }).show();
            }
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("file://" + file.getPath());
            String fname = file.getName();
            if (fname.endsWith(".jpg") || fname.endsWith("png")
                    || fname.endsWith(".gif")) {
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);
            } else if (fname.endsWith(".mp4") || fname.endsWith(".3gp")) {
                intent.setDataAndType(uri, "video/*");
                startActivity(intent);
            } else if (fname.endsWith(".mp3")) {
                intent.setDataAndType(uri, "audio/*");
                startActivity(intent);
            } else if (fname.endsWith(".zip")) {
                Log.w(OPEN_LOG, "zip click click");
                Log.i(OPEN_LOG, "Path file .zip : " + file.getPath());

                Intent i = new Intent(this, ReadComicActivity.class);
                i.putExtra("path", file.getPath());
                final String strLength = Long.toString(file.length() / 1000);
                if (file.length() / 1000 > 1600) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Please choose zip file have less than or equal 1.5 Mb ",
                            2000).show();
                } else {

                    myProgressDialog = ProgressDialog.show(OpenActivity.this,
                            "Please wait...", "Loading data from SDCard", true);
                    new Thread() {
                        public void run() {
                            try {
                                Log.i("TTTH", strLength);
                                sleep(5000);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            myProgressDialog.dismiss();
                        }
                    }.start();
                    startActivity(i);
                }
            } else {

            }
        }
    }
}
