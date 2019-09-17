package com.jk.admin.careermakers.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jk.admin.careermakers.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 08-04-2018.
 */

public class HomeFragment extends Fragment {

    RecyclerView homeImageRv;
    List<Integer> imageList;
    HomeImageAdapter homeImageAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeImageRv=(RecyclerView) view.findViewById(R.id.HomeImageRv);
        imageList=new ArrayList<>();


        imageList.add(0,R.drawable.a1);
        imageList.add(1,R.drawable.a3);
        imageList.add(2,R.drawable.j4);
        homeImageAdapter=new HomeImageAdapter(getActivity(),imageList);
        homeImageRv.setAdapter(homeImageAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        homeImageRv.setLayoutManager(linearLayoutManager);

    }

    class HomeImageViewHolder extends RecyclerView.ViewHolder
    {
        ImageView homeImageRvIv;
        public HomeImageViewHolder(View itemView) {
            super(itemView);
            homeImageRvIv=(ImageView) itemView.findViewById(R.id.HomeImageRvIv);
        }
    }
    class HomeImageAdapter extends RecyclerView.Adapter<HomeImageViewHolder>
    {
        Context context;
        List<Integer> imageList;

        public HomeImageAdapter(Context context, List<Integer> imageList) {
            this.context=context;
            this.imageList=imageList;
        }

        @Override
        public HomeImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(context).inflate(R.layout.home_image_rv_layout,parent,false);
            HomeImageViewHolder homeImageViewHolder=new HomeImageViewHolder(view);
            return homeImageViewHolder;
        }

        @Override
        public void onBindViewHolder(HomeImageViewHolder holder, int position) {

            holder.homeImageRvIv.setImageResource(imageList.get(position));
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }
}
