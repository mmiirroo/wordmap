/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.persistence.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.example.android.persistence.R;
import com.example.android.persistence.databinding.ListFragmentBinding;
import com.example.android.persistence.db.entity.LocationEntity;
import com.example.android.persistence.model.Location;
import com.example.android.persistence.viewmodel.LocationListViewModel;

import java.util.ArrayList;
import java.util.List;

public class LocationListFragment extends Fragment implements AMapLocationListener, AMap.OnMapLoadedListener {

    public static final String TAG = "LocationListViewModel";

    private LocationAdapter mLocationAdapter;

    private ListFragmentBinding mBinding;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private MapView mapView;
    private AMap aMap;
    private static final LatLng FOCUSED_POSITION = new LatLng(36.197164, 120.518861);

    private ImageButton mCheck;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);

        mLocationAdapter = new LocationAdapter(mLocationClickCallback);
//        mBinding.locationsList.setAdapter(mLocationAdapter);

        initMap(savedInstanceState);
        initLocation();
        return mBinding.getRoot();
    }

    private void initMap(Bundle savedInstanceState) {
        mapView = mBinding.getRoot().findViewById(R.id.overlay_map);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            mapView.setSelected(true);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17.0f));
            CameraUpdate moveCity = CameraUpdateFactory.newLatLngZoom(FOCUSED_POSITION, 17);
            aMap.moveCamera(moveCity);
            aMap.addMarker(new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(
                            BitmapFactory.decodeResource(getResources(), R.mipmap.location_marker))).
                    position(FOCUSED_POSITION));
        }
        aMap.setOnMapLoadedListener(this);

        mCheck = mBinding.getRoot().findViewById(R.id.check);
        mCheck.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    mLocationClient.startLocation();

                } catch (Exception e) {
                    Log.e(TAG, "check in failed", e);
                }
            }
        });

    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getActivity());
        //设置定位监听
        mLocationClient.setLocationListener(this);

        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final LocationListViewModel viewModel =
                ViewModelProviders.of(this).get(LocationListViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(LocationListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getLocations().observe(this, new Observer<List<LocationEntity>>() {
            @Override
            public void onChanged(@Nullable List<LocationEntity> myLocations) {
                if (myLocations != null) {
                    mBinding.setIsLoading(false);
                    mLocationAdapter.setLocationList(myLocations);
                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
    }

    @Override
    public void onMapLoaded() {

        List<PoiItem> pointOverlayList = new ArrayList<>();

        for (Location l: mLocationAdapter.mLocationList) {
            LatLonPoint point = new LatLonPoint(l.getLatitude(), l.getLongitude());
            PoiItem p = new PoiItem("1", point, null, null);
            pointOverlayList.add(p);
        }
        PointOverlay pointOverlay = new PointOverlay(aMap, pointOverlayList);
        pointOverlay.removeFromMap();
        pointOverlay.addToMap();
        pointOverlay.zoomToSpan();
    }

    private final LocationClickCallback mLocationClickCallback = new LocationClickCallback() {
        @Override
        public void onClick(Location location) {

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(location);
            }
        }
    };

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            Double latitude = aMapLocation.getLatitude();
            Double longitude = aMapLocation.getLongitude();
            Log.d(TAG, "latitude=" + latitude + ", longitude=" + longitude);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLocationClient.stopLocation();
        mLocationClient.unRegisterLocationListener(this);
        mLocationClient = null;
    }
}
