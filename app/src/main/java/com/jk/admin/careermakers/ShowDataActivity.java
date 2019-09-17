package com.jk.admin.careermakers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jk.admin.careermakers.ImportantClasses.XMLParser;
import com.jk.admin.careermakers.Modal.ShowData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowDataActivity extends AppCompatActivity {

    RecyclerView showDataRv;
    String id,catName,type;
    List<ShowData> showDataList;
    XMLParser xmlParser=new XMLParser();
    ShowDataRvAdapter showDataRAdapter;

    public final static String NAME_SPACE = "http://tempuri.org/";
    public final static String URL = "http://carriermakers.com/WebService.asmx";

    public final static String SOAP_ACTION_FETCH_DATA = "http://tempuri.org/GetShowData";
    public final static String METHOD_NAME_FETCH_DATA = "GetShowData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showDataList=new ArrayList<>();
        showDataRv=(RecyclerView) findViewById(R.id.ShowDataRv);

        Bundle bundle=getIntent().getExtras();
        id=bundle.getString("Id");
        catName=bundle.getString("CatName");
        type=bundle.getString("Type");

        getSupportActionBar().setTitle(catName);

        new GetData().execute();

    }

    class GetData extends AsyncTask<String,Void,SoapObject>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(ShowDataActivity.this);
            progressDialog.setTitle("Please Wait....");
            progressDialog.setMessage("Fetching Data....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected SoapObject doInBackground(String... strings) {
            SoapObject response=null;

            SoapObject request=new SoapObject(NAME_SPACE,METHOD_NAME_FETCH_DATA);
            request.addProperty("Id",id);
            request.addProperty("Type",type);

            try {
                response=xmlParser.getServiceResultSoapObj(URL,SOAP_ACTION_FETCH_DATA,request);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }


        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);
            progressDialog.dismiss();
            if(soapObject!=null)
            {
               // Toast.makeText(ShowDataActivity.this, soapObject.toString(), Toast.LENGTH_SHORT).show();
                SetDataInRv(soapObject);
            }
        }
    }

    private void SetDataInRv(SoapObject soapObject) {
        for(int i=0;i<soapObject.getPropertyCount();i++)
        {
            SoapObject sobj= (SoapObject) soapObject.getProperty(i);
            if(!sobj.getPropertyAsString("Status").equalsIgnoreCase("NoData"))
            {
                ShowData showData=new ShowData();
                showData.setId(sobj.getPropertyAsString("Id"));
                showData.setCreatedOn(sobj.getPropertyAsString("CreatedOn"));
                showData.setTitle(sobj.getPropertyAsString("Title"));
                showData.setImage(sobj.getPropertyAsString("Image"));
                showData.setContent(sobj.getPropertyAsString("Content"));
                showData.setCategoryId(sobj.getPropertyAsString("CategoryId"));

                showDataList.add(showData);
            }
            else
            {
                Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            }
        }
        showDataRAdapter=new ShowDataRvAdapter(this,showDataList);
        showDataRv.setAdapter(showDataRAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ShowDataActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        showDataRv.setLayoutManager(linearLayoutManager);

    }
    class ShowDataRvViewHolder extends RecyclerView.ViewHolder
    {
        ImageView showDataRvIv;
        TextView showDataTitleTv,showDataRvDateTv;
        public ShowDataRvViewHolder(View itemView) {
            super(itemView);

            showDataRvIv= (ImageView) itemView.findViewById(R.id.ShowDataRvIv);
            showDataRvDateTv=(TextView) itemView.findViewById(R.id.ShowDataRvDateTv);
            showDataTitleTv=(TextView) itemView.findViewById(R.id.ShowDataRvTitleTv);

        }
    }

    class ShowDataRvAdapter extends RecyclerView.Adapter<ShowDataRvViewHolder>
    {
        Context context;
        List<ShowData> showDataList;

        public ShowDataRvAdapter(Context context, List<ShowData> showDataList) {
            this.context=context;
            this.showDataList=showDataList;
        }

        @Override
        public ShowDataRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(context).inflate(R.layout.showdata_rv_row,parent,false);
            ShowDataRvViewHolder  showDataRvViewHolder=new ShowDataRvViewHolder(view);
            return showDataRvViewHolder;
        }

        @Override
        public void onBindViewHolder(ShowDataRvViewHolder holder, int position) {

            final ShowData showData=showDataList.get(position);

            holder.showDataTitleTv.setText(showData.getTitle());
            holder.showDataRvDateTv.setText(showData.getCreatedOn());
            Picasso.with(context).load("http://carriermakers.com"+showData.getImage().substring(1)).transform(new CircleTransform()).into(holder.showDataRvIv);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OpenPopUp(showData.getTitle(),showData.getContent());
                }
            });

        }

        @Override
        public int getItemCount() {
            return showDataList.size();
        }
    }

    private void OpenPopUp(String title, String content) {

        TextView detailsPopUpTitleTv,detailsPopUpContentTv;
        ImageView detailsPoUpCloseBtn;

        View view=LayoutInflater.from(this).inflate(R.layout.details_popup,null,false);
        detailsPopUpTitleTv=(TextView) view.findViewById(R.id.DetailsPopUpTitleTv);
        detailsPopUpContentTv=(TextView) view.findViewById(R.id.DetailsContentPopUpTv);
        detailsPoUpCloseBtn=(ImageView) view.findViewById(R.id.DetailsPopUpCloseBtn);
        detailsPopUpContentTv.setMovementMethod(new ScrollingMovementMethod());

        AlertDialog.Builder builder=new AlertDialog.Builder(ShowDataActivity.this);
        builder.setCancelable(false);
        builder.setView(view);
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();

        detailsPopUpTitleTv.setText(title);
        detailsPopUpContentTv.setText(Html.fromHtml(content));

        detailsPoUpCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });




    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
