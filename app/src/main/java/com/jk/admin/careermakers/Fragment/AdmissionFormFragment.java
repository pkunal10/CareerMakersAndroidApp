package com.jk.admin.careermakers.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.jk.admin.careermakers.ImportantClasses.XMLParser;
import com.jk.admin.careermakers.Modal.AdmissionCategory;
import com.jk.admin.careermakers.R;
import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ADMIN on 08-04-2018.
 */

public class AdmissionFormFragment extends Fragment {


    byte[] bStuImg, bTenMS, bTwelveMS;
    String stuImgPath, tenMSImgPath, twelveMSImgPath = "";
    Calendar myCalendar = Calendar.getInstance();
    XMLParser xmlParser = new XMLParser();
    ImageView stuIv, tenMSIv, twelveMSIv;
    Spinner admInSp;
    List<AdmissionCategory> admissionCategoryList;
    ArrayAdapter arrayAdapter;
    Button submitBtn,clearBtn;
    EditText fNameEt, mNameEt, lNameEt, dobEt, addressEt, stateEt, pinCodeEt, emailEt, faNameEt, moNameEt, stuMobEt, faMobEt;
    RadioGroup genderRg;

    public final static String NAME_SPACE = "http://tempuri.org/";
    public final static String URL = "http://carriermakers.com/WebService.asmx";

    public final static String SOAP_ACTION_SAVE_DATA = "http://tempuri.org/SaveAdmData";
    public final static String METHOD_NAME_SAVE_DATA = "SaveAdmData";

    public final static String SOAP_ACTION_GET_ADMCAT = "http://tempuri.org/GetAdminssionCtegory";
    public final static String METHOD_NAME_GET_ADMCAT = "GetAdminssionCtegory";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admission_form_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fNameEt = (EditText) view.findViewById(R.id.FirstNameEt);
        mNameEt = (EditText) view.findViewById(R.id.MiddleNameEt);
        lNameEt = (EditText) view.findViewById(R.id.LastNameEt);
        addressEt = (EditText) view.findViewById(R.id.AddressEt);
        stateEt = (EditText) view.findViewById(R.id.StateEt);
        pinCodeEt = (EditText) view.findViewById(R.id.PINCodeEt);
        emailEt = (EditText) view.findViewById(R.id.EmailEt);
        faNameEt = (EditText) view.findViewById(R.id.FatherNameEt);
        moNameEt = (EditText) view.findViewById(R.id.MotherNameEt);
        stuMobEt = (EditText) view.findViewById(R.id.StudentMobEt);
        faMobEt = (EditText) view.findViewById(R.id.FatherMobEt);
        dobEt = (EditText) view.findViewById(R.id.DOBEt);
        genderRg = (RadioGroup) view.findViewById(R.id.GenderRg);
        stuIv = (ImageView) view.findViewById(R.id.StuIv);
        tenMSIv = (ImageView) view.findViewById(R.id.TenThMarkSheetIv);
        twelveMSIv = (ImageView) view.findViewById(R.id.TwelveMarSheetIv);
        admInSp = (Spinner) view.findViewById(R.id.AdmInSp);
        submitBtn = (Button) view.findViewById(R.id.SubmitBtn);
        clearBtn=(Button) view.findViewById(R.id.ClearBtn);

        admissionCategoryList = new ArrayList<>();

        new GetAdmCat().execute();


        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clear();
            }
        });

        stuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        tenMSIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 2);
            }
        });

        twelveMSIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 3);
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if (validation()) {

                BitmapDrawable bitmapDrawableStuImg = (BitmapDrawable) stuIv.getDrawable();
                // Bitmap bMapStuImg = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                Bitmap bMapStuImg = bitmapDrawableStuImg.getBitmap();
                ByteArrayOutputStream baosStuImg = new ByteArrayOutputStream();
                bMapStuImg.compress(Bitmap.CompressFormat.PNG, 100, baosStuImg);
                bStuImg = baosStuImg.toByteArray();
                stuImgPath = org.kobjects.base64.Base64.encode(bStuImg);

                BitmapDrawable bitmapDrawableTenMSImg = (BitmapDrawable) tenMSIv.getDrawable();
                // Bitmap bMapTenMSImg = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                Bitmap bMapTenMSImg = bitmapDrawableTenMSImg.getBitmap();
                ByteArrayOutputStream baosTenImg = new ByteArrayOutputStream();
                bMapTenMSImg.compress(Bitmap.CompressFormat.PNG, 100, baosTenImg);
                bTenMS = baosTenImg.toByteArray();
                tenMSImgPath = org.kobjects.base64.Base64.encode(bTenMS);

                BitmapDrawable bitmapDrawableTwelveMSImg = (BitmapDrawable) twelveMSIv.getDrawable();
                // Bitmap bMapTwelveImg = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                Bitmap bMapTwelveImg = bitmapDrawableTwelveMSImg.getBitmap();
                ByteArrayOutputStream baosTwelveImg = new ByteArrayOutputStream();
                bMapTwelveImg.compress(Bitmap.CompressFormat.PNG, 100, baosTwelveImg);
                bTwelveMS = baosTwelveImg.toByteArray();
                twelveMSImgPath = org.kobjects.base64.Base64.encode(bTwelveMS);

                String gender = genderRg.getCheckedRadioButtonId() == R.id.MaleRb ? "Male" : "Female";
                AdmissionCategory admissionCategory = (AdmissionCategory) admInSp.getSelectedItem();

                new SaveData(fNameEt.getText().toString(), mNameEt.getText().toString(), lNameEt.getText().toString(), dobEt.getText().toString(), gender, addressEt.getText().toString(), stateEt.getText().toString(), pinCodeEt.getText().toString(), emailEt.getText().toString(), faNameEt.getText().toString(), moNameEt.getText().toString(), stuMobEt.getText().toString(), faMobEt.getText().toString(), admissionCategory.getId(), stuImgPath, tenMSImgPath, twelveMSImgPath).execute();
                //      }
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dobEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    private boolean validation() {

        if (fNameEt.getText().toString().equalsIgnoreCase("")) {
            fNameEt.setError("Enter Proper Value");
            fNameEt.requestFocus();
            return false;
        } else if (mNameEt.getText().toString().equalsIgnoreCase("")) {
            mNameEt.setError("Enter Proper Value");
            mNameEt.requestFocus();
            return false;
        } else if (lNameEt.getText().toString().equalsIgnoreCase("")) {
            lNameEt.setError("Enter Proper Value");
            lNameEt.requestFocus();
            return false;
        } else if (dobEt.getText().toString().equalsIgnoreCase("")) {
            dobEt.setError("Select DOB");
            dobEt.requestFocus();
            return false;
        } else if (addressEt.getText().toString().equalsIgnoreCase("")) {
            addressEt.setError("Enter Proper Value");
            addressEt.requestFocus();
            return false;
        } else if (stateEt.getText().toString().equalsIgnoreCase("")) {
            stateEt.setError("Enter Proper Value");
            stateEt.requestFocus();
            return false;
        } else if (pinCodeEt.getText().toString().equalsIgnoreCase("")) {
            pinCodeEt.setError("Enter Proper Value");
            pinCodeEt.requestFocus();
            return false;
        } else if (emailEt.getText().toString().equalsIgnoreCase("")) {
            emailEt.setError("Enter Proper Value");
            emailEt.requestFocus();
            return false;
        } else if (faNameEt.getText().toString().equalsIgnoreCase("")) {
            faNameEt.setError("Enter Proper Value");
            faNameEt.requestFocus();
            return false;
        } else if (moNameEt.getText().toString().equalsIgnoreCase("")) {
            moNameEt.setError("Enter Proper Value");
            moNameEt.requestFocus();
            return false;
        } else if (stuMobEt.getText().toString().equalsIgnoreCase("")) {
            stuMobEt.setError("Enter Proper Value");
            stuMobEt.requestFocus();
            return false;
        } else if (faMobEt.getText().toString().equalsIgnoreCase("")) {
            faMobEt.setError("Enter Proper Value");
            faMobEt.requestFocus();
            return false;
        } else if (admInSp.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Select Course", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImageURI = data.getData();
            if (requestCode == 1) {
//                Picasso.with(getActivity()).load(selectedImageURI).noPlaceholder().centerCrop().fit()
//                        .into(stuIv);
                stuIv.setImageURI(selectedImageURI);
            } else if (requestCode == 2) {
//                Picasso.with(getActivity()).load(selectedImageURI).noPlaceholder().centerCrop().fit()
//                        .into(tenMSIv);
                tenMSIv.setImageURI(selectedImageURI);
            } else if (requestCode == 3) {
//                Picasso.with(getActivity()).load(selectedImageURI).noPlaceholder().centerCrop().fit()
//                        .into(twelveMSIv);
                twelveMSIv.setImageURI(selectedImageURI);
            }

        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            stuIv.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//        }
//    }

    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dobEt.setText(sdf.format(myCalendar.getTime()));
    }

    class GetAdmCat extends AsyncTask<String, Void, SoapObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SoapObject doInBackground(String... strings) {
            SoapObject result = null;

            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME_GET_ADMCAT);

            try {
                result = xmlParser.getServiceResultSoapObj(URL, SOAP_ACTION_GET_ADMCAT, request);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);

            if (soapObject != null) {
                SoapObject soapObject1 = (SoapObject) soapObject.getProperty(0);
                if (!soapObject1.getPropertyAsString("Status").equalsIgnoreCase("NoData")) {
                    for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                        SoapObject sobj = (SoapObject) soapObject.getProperty(i);
                        AdmissionCategory admissionCategory = new AdmissionCategory();
                        admissionCategory.setId(sobj.getPropertyAsString("Id"));
                        admissionCategory.setCategoryName(sobj.getPropertyAsString("CategoryName"));
                        admissionCategoryList.add(admissionCategory);
                    }

                    AdmissionCategory admissionCategory = new AdmissionCategory();
                    admissionCategory.setId("0");
                    admissionCategory.setCategoryName("-- Select Course --");
                    admissionCategoryList.add(0, admissionCategory);

                    arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, admissionCategoryList);
                    admInSp.setAdapter(arrayAdapter);
                }

            }
        }
    }

    class SaveData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String fname, mname, lname, DOB, gender, address, state, pin, email, faname, moname, stumob, famob, id, stuImgPath, tenMSImgPath, twelveMSImgPath;

        public SaveData(String fname, String mname, String lname, String DOB, String gender, String address, String state, String pin, String email, String faname, String moname, String stumob, String famob, String id, String stuImgPath, String tenMSImgPath, String twelveMSImgPath) {
            this.fname = fname;
            this.mname = mname;
            this.lname = lname;
            this.DOB = DOB;
            this.gender = gender;
            this.address = address;
            this.state = state;
            this.pin = pin;
            this.email = email;
            this.faname = faname;
            this.moname = moname;
            this.stumob = stumob;
            this.famob = famob;
            this.id = id;
            this.stuImgPath = stuImgPath;
            this.tenMSImgPath = tenMSImgPath;
            this.twelveMSImgPath = twelveMSImgPath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Please Wait....");
            progressDialog.setMessage("Saving Data....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String response = "";

            SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME_SAVE_DATA);

            request.addProperty("Fname", fname);
            request.addProperty("Mname", mname);
            request.addProperty("Lname", lname);
            request.addProperty("DOB", DOB);
            request.addProperty("Gender", gender);
            request.addProperty("Address", address);
            request.addProperty("State", state);
            request.addProperty("PIN", pin);
            request.addProperty("Email", email);
            request.addProperty("FAname", faname);
            request.addProperty("MOname", moname);
            request.addProperty("StuMob", stumob);
            request.addProperty("FAMob", famob);
            request.addProperty("Course", id);
            request.addProperty("StuImg", stuImgPath);
            request.addProperty("TenMSImg", tenMSImgPath);
            request.addProperty("TwelveMSImg", twelveMSImgPath);
            response = xmlParser.getServiceResultString(URL, SOAP_ACTION_SAVE_DATA, request);


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equalsIgnoreCase("")) {
                progressDialog.dismiss();
                if (s.equalsIgnoreCase("Success"))
                {
                    Toast.makeText(getActivity(), "Your form is submitted.", Toast.LENGTH_LONG).show();
                    Clear();

                }
                else
                {
                    Toast.makeText(getActivity(), s.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void Clear() {

        fNameEt.setText("");
        mNameEt.setText("");
        lNameEt.setText("");
        dobEt.setText("");
        addressEt.setText("");
        stateEt.setText("");
        pinCodeEt.setText("");
        emailEt.setText("");
        faNameEt.setText("");
        moNameEt.setText("");
        stuMobEt.setText("");
        faMobEt.setText("");
        admInSp.setSelection(0);
        stuIv.setImageResource(R.drawable.noimg);
        tenMSIv.setImageResource(R.drawable.noimg);
        twelveMSIv.setImageResource(R.drawable.noimg);

    }
}
