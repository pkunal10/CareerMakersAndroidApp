package com.jk.admin.careermakers.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jk.admin.careermakers.ImportantClasses.XMLParser;
import com.jk.admin.careermakers.Modal.AdmissionCategory;
import com.jk.admin.careermakers.R;
import com.jk.admin.careermakers.ShowDataActivity;
import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 08-04-2018.
 */

public class JobOfferFragment extends Fragment {

    GridView jobCatGv;
    List<AdmissionCategory> jobCategoryList;
    XMLParser xmlParser=new XMLParser();
    JobCategoryFragment jobcategoryFragment;



    public final static String NAME_SPACE = "http://tempuri.org/";
    public final static String URL = "http://carriermakers.com/WebService.asmx";

    public final static String SOAP_ACTION_FETCH_JOBCAT = "http://tempuri.org/GetJobCategory";
    public final static String METHOD_NAME_FETCH_JOBCAT = "GetJobCategory";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.job_offer_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        jobCatGv= (GridView) view.findViewById(R.id.JobCatGv);
        jobCategoryList=new ArrayList<>();

        new GetJobCategory().execute();

    }

    private class GetJobCategory extends AsyncTask<String,Void,SoapObject>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Please Wait....");
            progressDialog.setMessage("Fetching Category");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected SoapObject doInBackground(String... strings) {
            SoapObject response=null;

            SoapObject request=new SoapObject(NAME_SPACE,METHOD_NAME_FETCH_JOBCAT);

            try {
                response=xmlParser.getServiceResultSoapObj(URL,SOAP_ACTION_FETCH_JOBCAT,request);
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
                //Toast.makeText(getActivity(), soapObject.toString(), Toast.LENGTH_LONG).show();
                SetDataInGriedView(soapObject);
            }
        }
    }

    private void SetDataInGriedView(SoapObject soapObject) {


        for(int i=0;i<soapObject.getPropertyCount();i++)
        {
            SoapObject sobj= (SoapObject) soapObject.getProperty(i);
            if(!sobj.getPropertyAsString("Status").equalsIgnoreCase("Nodata"))
            {
                AdmissionCategory admissionCategory=new AdmissionCategory();
                admissionCategory.setId(sobj.getPropertyAsString("Id"));
                admissionCategory.setCategoryName(sobj.getPropertyAsString("CategoryName"));
                admissionCategory.setImage(sobj.getPropertyAsString("Image"));

                jobCategoryList.add(admissionCategory);

                jobcategoryFragment=new JobCategoryFragment(getActivity(),jobCategoryList);
                jobCatGv.setAdapter(jobcategoryFragment);
            }
            else
            {
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }
        }



    }

    private class JobCategoryFragment  extends BaseAdapter {
        Context context;
        List<AdmissionCategory> jobCategoryList;
        public JobCategoryFragment(Context context, List<AdmissionCategory> jobCategoryList) {

            this.context=context;
            this.jobCategoryList=jobCategoryList;
        }

        @Override
        public int getCount() {
            return jobCategoryList.size();
        }

        @Override
        public Object getItem(int i) {
            return jobCategoryList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view =LayoutInflater.from(context).inflate(R.layout.admission_cat_gv_row,viewGroup,false);

            ImageView admissionCatGvIv;
            TextView admissionCatGvCatNameTv;

            admissionCatGvCatNameTv=(TextView) view.findViewById(R.id.AdmissionCatCatNameTv);
            admissionCatGvIv=(ImageView) view.findViewById(R.id.AdmissionCatGvIv);

            final AdmissionCategory jobCategory=jobCategoryList.get(i);

            admissionCatGvCatNameTv.setText(jobCategory.getCategoryName());
            Picasso.with(context).load("http://carriermakers.com"+jobCategory.getImage()).placeholder(R.mipmap.ic_launcher).fit().into(admissionCatGvIv);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("Id",jobCategory.getId());
                    bundle.putString("CatName",jobCategory.getCategoryName());
                    bundle.putString("Type","Job");

                    Intent intent=new Intent(getActivity(), ShowDataActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}
