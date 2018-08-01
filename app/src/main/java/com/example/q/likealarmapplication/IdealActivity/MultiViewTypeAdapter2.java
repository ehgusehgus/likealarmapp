package com.example.q.likealarmapplication.IdealActivity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.likealarmapplication.HttpInterface;
import com.example.q.likealarmapplication.ProfileActivity.Model;
import com.example.q.likealarmapplication.R;
import com.facebook.AccessToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class MultiViewTypeAdapter2 extends RecyclerView.Adapter {

    private ArrayList<Model>dataSet;
    Context mContext;
    int total_types;
    com.example.q.likealarmapplication.ProfileActivity.MultiViewTypeAdapter.ImageTypeViewHolder mImage;

    private ListView mListView;
    private TextView mTextView;
    AccessToken accessToken;
    Retrofit retrofit;
    HttpInterface httpInterface;
    ButtonTypeViewHolder mName;
    ButtonTypeViewHolder mSex;

    ButtonTypeViewHolder mHeight;
    ButtonTypeViewHolder mChar;
    ButtonTypeViewHolder mAge;
    ButtonTypeViewHolder mAlcohol;


    TextView mRecipe;

    ArrayList<String> recipes = new ArrayList<String>();

    public static class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        TextView txtType2;
        CardView cardView;

        public TextTypeViewHolder(View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.txtType2 = (TextView) itemView.findViewById(R.id.type2);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public static class TextTypeViewHolder2 extends RecyclerView.ViewHolder {

        TextView txtType;
        EditText txtType2;
        CardView cardView;

        public TextTypeViewHolder2(View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.txtType2 = (EditText) itemView.findViewById(R.id.type2);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }


    public static class ButtonTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        Button btn;
        public ButtonTypeViewHolder(View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.btn = (Button) itemView.findViewById(R.id.button4);
        }
    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        ImageView image;

        public ImageTypeViewHolder(View itemView) {
            super(itemView);
            this.txtType = (TextView) itemView.findViewById(R.id.type);
            this.image = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }


    public MultiViewTypeAdapter2(ArrayList<Model>data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Model.EDIT_NAME_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_type, parent, false);
                mName = new ButtonTypeViewHolder(view);
                return mName;
            case Model.EDIT_SEX_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_type, parent, false);
                mSex = new ButtonTypeViewHolder(view);
                return mSex;
            case Model.EDIT_AGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_type, parent, false);
                mAge = new ButtonTypeViewHolder(view);
                return mAge;
            case Model.EDIT_HEIGHT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_type, parent, false);
                mHeight = new ButtonTypeViewHolder(view);
                return mHeight;
            case Model.EDIT_CHAR_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_type, parent, false);
                mChar = new ButtonTypeViewHolder(view);
                return mChar;
            case Model.EDIT_ALCOHOL_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_type, parent, false);
                mAlcohol = new ButtonTypeViewHolder(view);
                return mAlcohol;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return Model.EDIT_NAME_TYPE;
            case 1:
                return Model.EDIT_SEX_TYPE;
            case 2:
                return Model.EDIT_AGE_TYPE;
            case 3:
                return Model.EDIT_HEIGHT_TYPE;
            case 4:
                return Model.EDIT_CHAR_TYPE;
            case 5:
                return Model.EDIT_ALCOHOL_TYPE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Model object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case Model.EDIT_NAME_TYPE:
                    ((ButtonTypeViewHolder) holder).txtType.setText(object.text);
                    ((ButtonTypeViewHolder) holder).btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(mContext);

                            final String data []   = {"김","이","박","최","정","강","조","윤","장","임","한","오", "류", "황보","남궁","상관없음"};
                            final boolean checked[]= {false,  false, false,  false, false,  false, false,  false, false,  false, false, false, false, false, false, false};
                            builder.setTitle("선호 성씨")
                                    .setPositiveButton("선택완료",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String str = "";
                                                    for (int i = 0; i < checked.length; i++) {
                                                        if (checked[i]) {
                                                            if(str.length() ==0)
                                                                str = str+data[i];
                                                            else
                                                                str = str + ","+data[i] ;
                                                        }
                                                    }
                                                    ((ButtonTypeViewHolder) holder).btn.setText(str);
                                                }
                                            })
                                    .setNegativeButton("취소", null)
                                    .setMultiChoiceItems
                                            (data, // 체크박스 리스트 항목
                                                    checked, // 초기값(선택여부) 배열
                                                    new DialogInterface.OnMultiChoiceClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog,
                                                                            int which, boolean isChecked) {
                                                            checked[which] = isChecked;
                                                        }
                                                    });
                            builder.create().show();
                        }
                    });
                    break;

                case Model.EDIT_SEX_TYPE:
                    ((ButtonTypeViewHolder) holder).txtType.setText(object.text);
                    ((ButtonTypeViewHolder) holder).btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(mContext);

                            final String data []   = {"남성", "여성"};
                            final boolean checked[]= {false,  false};
                            builder.setTitle("이상 성별")
                                    .setPositiveButton("선택완료",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String str = "";
                                                    for (int i = 0; i < checked.length; i++) {
                                                        if (checked[i]) {
                                                            if(str.length() ==0)
                                                                str = str+data[i];
                                                            else
                                                                str = str + ","+data[i] ;
                                                        }
                                                    }
                                                    ((ButtonTypeViewHolder) holder).btn.setText(str);
                                                }
                                            })
                                    .setNegativeButton("취소", null)
                                    .setMultiChoiceItems
                                            (data, // 체크박스 리스트 항목
                                                    checked, // 초기값(선택여부) 배열
                                                    new DialogInterface.OnMultiChoiceClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog,
                                                                            int which, boolean isChecked) {
                                                            checked[which] = isChecked;
                                                        }
                                                    });
                            builder.create().show();
                        }
                    });
                    break;
                case Model.EDIT_AGE_TYPE:

                    ((ButtonTypeViewHolder) holder).txtType.setText(object.text);
                    ((ButtonTypeViewHolder) holder).btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(mContext);

                            LinearLayout LL = new LinearLayout(mContext);
                            LL.setOrientation(LinearLayout.HORIZONTAL);
//
                            final NumberPicker aNumberPicker = new NumberPicker(mContext);
                            aNumberPicker.setMaxValue(120);
                            aNumberPicker.setMinValue(15);
//
                            final NumberPicker aNumberPickerA = new NumberPicker(mContext);
                            aNumberPickerA.setMaxValue(120);
                            aNumberPickerA.setMinValue(15);

                            final TextView tv = new TextView(mContext);
                            tv.setText("\n"+"\n"+"~");
                            tv.setTextSize(30);
                            tv.setGravity(Gravity.CENTER);
//
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
                            params.gravity = Gravity.CENTER_VERTICAL;

//
                            LinearLayout.LayoutParams numPicerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            numPicerParams.weight = 1;
//
                            LinearLayout.LayoutParams qPicerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            qPicerParams.weight = 1;

                            LinearLayout.LayoutParams cPicerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            cPicerParams.weight = 1;
//
                            LL.setLayoutParams(params);
                            LL.addView(aNumberPicker,numPicerParams);
                            LL.addView(tv,cPicerParams);
                            LL.addView(aNumberPickerA,qPicerParams);

                            builder.setView(LL);

                            builder.setTitle("이상 나이")
                                    .setPositiveButton("선택완료",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String str = "";
                                                    if(aNumberPicker.getValue()>aNumberPickerA.getValue()){
                                                        Toast.makeText(mContext.getApplicationContext(), "잘못적었따!!", Toast.LENGTH_LONG).show();
                                                        return;
                                                    }
                                                    str = Integer.toString(aNumberPicker.getValue())+"~"+Integer.toString(aNumberPickerA.getValue());
                                                    ((ButtonTypeViewHolder) holder).btn.setText(str);
                                                }
                                            })
                                    .setNegativeButton("취소", null);
                            builder.create().show();
                        }
                    });
                    break;

                case Model.EDIT_HEIGHT_TYPE:
                    ((ButtonTypeViewHolder) holder).txtType.setText(object.text);
                    ((ButtonTypeViewHolder) holder).btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(mContext);

                            LinearLayout LL = new LinearLayout(mContext);
                            LL.setOrientation(LinearLayout.HORIZONTAL);
//
                            final NumberPicker aNumberPicker = new NumberPicker(mContext);
                            aNumberPicker.setMaxValue(250);
                            aNumberPicker.setMinValue(150);
//
                            final NumberPicker aNumberPickerA = new NumberPicker(mContext);
                            aNumberPickerA.setMaxValue(250);
                            aNumberPickerA.setMinValue(150);

                            final TextView tv = new TextView(mContext);
                            tv.setText("\n"+"\n"+"~");
                            tv.setTextSize(30);
                            tv.setGravity(Gravity.CENTER);
//
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
                            params.gravity = Gravity.CENTER_VERTICAL;

//
                            LinearLayout.LayoutParams numPicerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            numPicerParams.weight = 1;
//
                            LinearLayout.LayoutParams qPicerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            qPicerParams.weight = 1;

                            LinearLayout.LayoutParams cPicerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            cPicerParams.weight = 1;
//
                            LL.setLayoutParams(params);
                            LL.addView(aNumberPicker,numPicerParams);
                            LL.addView(tv,cPicerParams);
                            LL.addView(aNumberPickerA,qPicerParams);

                            builder.setView(LL);

                            builder.setTitle("이상 키")
                                    .setPositiveButton("선택완료",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String str = "";
                                                    if(aNumberPicker.getValue()>aNumberPickerA.getValue()){
                                                        Toast.makeText(mContext.getApplicationContext(), "잘못적었따!!", Toast.LENGTH_LONG).show();
                                                        return;
                                                    }
                                                    str = Integer.toString(aNumberPicker.getValue())+"~"+Integer.toString(aNumberPickerA.getValue());
                                                    ((ButtonTypeViewHolder) holder).btn.setText(str);
                                                }
                                            })
                                    .setNegativeButton("취소", null);
                            builder.create().show();
                        }
                    });
                    break;
                case Model.EDIT_CHAR_TYPE:
                    ((ButtonTypeViewHolder) holder).txtType.setText(object.text);
                    ((ButtonTypeViewHolder) holder).btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(mContext);

                            final String data []   = {"지적인","차분한","유머있는","내향적인","외향적인","감성적인","상냥한","귀여운","열정적인","듬직한","개성있는", "상관없음"};
                            final boolean checked[]= {false,  false, false,  false,false,  false,false,  false,false,  false,false,false};
                            builder.setTitle("성격")
                                    .setPositiveButton("선택완료",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String str = "";
                                                    if(checked[11])
                                                        str="상관없음";
                                                    else {
                                                        for (int i = 0; i < checked.length; i++) {

                                                            if (checked[i]) {
                                                                if(str.length() ==0)
                                                                    str = str+data[i];
                                                                else
                                                                    str = str + ","+data[i] ;
                                                            }
                                                        }
                                                    }
                                                    ((ButtonTypeViewHolder) holder).btn.setText(str);
                                                }
                                            })
                                    .setNegativeButton("취소", null)
                                    .setMultiChoiceItems
                                            (data, // 체크박스 리스트 항목
                                                    checked, // 초기값(선택여부) 배열
                                                    new DialogInterface.OnMultiChoiceClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog,
                                                                            int which, boolean isChecked) {
                                                            checked[which] = isChecked;
                                                        }
                                                    });
                            builder.create().show();
                        }
                    });
                    break;
                case Model.EDIT_ALCOHOL_TYPE:
                    ((ButtonTypeViewHolder) holder).txtType.setText(object.text);
                    ((ButtonTypeViewHolder) holder).btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(mContext);

                            final String data []   = {"마시지않음", "어느정도즐김", "적당히즐김", "미침", "상관없음"};
                            final boolean checked[]= {false,  false, false,  false, false};
                            builder.setTitle("성격")
                                    .setPositiveButton("선택완료",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    String str = "";
                                                    if(checked[4])
                                                        str="상관없음";
                                                    else {
                                                        for (int i = 0; i < checked.length; i++) {
                                                            if (checked[i]) {
                                                                if(str.length() ==0)
                                                                    str = str+data[i];
                                                                else
                                                                    str = str + ","+data[i] ;
                                                            }
                                                        }
                                                    }
                                                    ((ButtonTypeViewHolder) holder).btn.setText(str);
                                                }
                                            })
                                    .setNegativeButton("취소", null)
                                    .setMultiChoiceItems
                                            (data, // 체크박스 리스트 항목
                                                    checked, // 초기값(선택여부) 배열
                                                    new DialogInterface.OnMultiChoiceClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog,
                                                                            int which, boolean isChecked) {
                                                            checked[which] = isChecked;
                                                        }
                                                    });
                            builder.create().show();
                        }
                    });
                    break;
            }
        }
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void finishClick() {

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpInterface.BaseURL)
                .build();
        httpInterface = retrofit.create(HttpInterface.class);

//        String name = mName.btn.getText().toString();
//        if(name.equals("")){
//            Toast.makeText(mContext.getApplicationContext(), "이름 적으세용!!", Toast.LENGTH_LONG).show();
//            return;
//        }

        String sex = mSex.btn.getText().toString();
        if(sex.equals("Select")){
            Toast.makeText(mContext.getApplicationContext(), "성별 고르세용!!", Toast.LENGTH_LONG).show();
            return;
        }
        String age = mAge.btn.getText().toString();
        if(age.equals("")){
            Toast.makeText(mContext.getApplicationContext(), "나이 적으세용!!", Toast.LENGTH_LONG).show();
            return;
        }
        String height = mHeight.btn.getText().toString();
        if(height.equals("Select")){
            Toast.makeText(mContext.getApplicationContext(), "키 고르세용!!", Toast.LENGTH_LONG).show();
            return;
        }
        String personal= mChar.btn.getText().toString();
        if(personal.equals("Select")){
            Toast.makeText(mContext.getApplicationContext(), "성격 고르세용!!", Toast.LENGTH_LONG).show();
            return;
        }
        String alcohol= mAlcohol.btn.getText().toString();
        if(alcohol.equals("Select")){
            Toast.makeText(mContext.getApplicationContext(), "음주 선택!!", Toast.LENGTH_LONG).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpInterface.BaseURL)
                .build();
        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        AccessToken a = AccessToken.getCurrentAccessToken();
        Call<JsonObject> addPage = httpInterface.createIdeal(a.getUserId(), sex, age, height, personal, alcohol);
        addPage.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                    Intent intent = new Intent(getApplication(), ProfilecreateActivity.class);
//                    intent.putExtra("username", text);
//                    startActivity(intent);
                ((Activity) mContext).finish();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(mContext.getApplicationContext(), "FAILURE", Toast.LENGTH_LONG).show();
            }
        });
    }
}