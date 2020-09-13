package com.example.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.map.databinding.ActivityMapsBinding
import com.example.map.roodatabase.FeaturedDataBase
import com.example.map.roodatabase.FeaturedRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import java.io.IOException


class MapsActivity : AppCompatActivity() , OnMapReadyCallback {
    lateinit var locationRequest: LocationRequest
    lateinit var locationManager: LocationManager
    val PERMISSION_ID = 1010
    var addresses : ArrayList<String> ? = null
    private lateinit var mMap: GoogleMap
    var vm:ActVM? =null
    var onClick : Boolean? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var repository : Any ? = null
    var binding:ActivityMapsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        search()
        mapFragment.getMapAsync(this)

        binding?.mapsActivity  = this
        addresses = ArrayList<String>()
        val dao = FeaturedDataBase.getInstanceDatabase(this.application)?.featuredDao
        repository  = FeaturedRepository(dao!!)
        var factory = MainViewModelFactory(repository as FeaturedRepository)
        vm= ViewModelProviders.of(this,factory).get(ActVM::class.java)

        getAddress()

    }

    private fun getAddress() {
       var location =  intent.getStringExtra("address")
        if (location !=null){
            var addressList : List<Address> ? = null
            if(!(location == null && location == "")){
                val geoCoder = Geocoder(this@MapsActivity)
                try {
                    addressList = geoCoder.getFromLocationName(location, 1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val address = addressList!![0]
                val latLng = LatLng(address.latitude, address.longitude)
                mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))

            }}
        else{
            getLocation()
        }
    }

    override fun onStart() {
        super.onStart()


    }

    fun showRecentAddresses(){
        onClick = true
        if (addresses?.isEmpty()!!){
            Toast.makeText(getApplicationContext(),
                "address list is empty",
                Toast.LENGTH_SHORT).show()
        }else{

            vm?.features?.observe(this , Observer {
                Log.d("sdfkbs","dsfvbjh "+it.get(0).address)
                var al = ArrayList<String>()
                for (i in it.indices){
                    al.add(it?.get(i)?.address!!)
                }
                if ( onClick == true){
                    var intent = Intent(this, AddressActivity::class.java)
                    intent.putStringArrayListExtra("key", al)
                    startActivity(intent)
                    onClick = false
                }



            })

        }


    }
    private fun getLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
           getLastLocation()
        }else{
            askLocationPermisson()
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        var locationTask : Task<Location> = fusedLocationProviderClient.lastLocation
        locationTask.addOnSuccessListener {
            if (it != null){
                Log.d("onSuccess" ,"location "+it)
                Log.d("onSuccess" ,"lat  "+it.latitude)
                Log.d("onSuccess" ,"long  "+it.longitude)
                  // Add a marker in Sydney and move the camera
           val latLng = LatLng(it.latitude,it.longitude)
           mMap.addMarker(MarkerOptions().position(latLng).title("current location"))

                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))

            }
        }
        locationTask.addOnFailureListener {

        }
    }
    private fun askLocationPermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
           if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
               ActivityCompat.requestPermissions(
                   this,
                   arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                   PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
           }
            else{
               ActivityCompat.requestPermissions(
                   this,
                   arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                   PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
           }
        }
    }

    private fun search() {
        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                var location = binding?.searchView?.query.toString()
                var addressList : List<Address> ? = null
                if(!(location == null && location == "")){
                    val geoCoder = Geocoder(this@MapsActivity)
                    try {
                        addressList = geoCoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))

                    addresses?.add(location)
                    vm?.storeAddresses(location)

                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }


        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }
        }
    }


    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }



}