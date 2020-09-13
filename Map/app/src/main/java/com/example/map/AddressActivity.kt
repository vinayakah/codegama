package com.example.map

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.map.databinding.ActivityAddressBinding

class AddressActivity : AppCompatActivity() ,ItemClickInterface{
    var binding : ActivityAddressBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_address)
        binding?.rvAddress?.layoutManager = LinearLayoutManager(this)
        loadData()
    }



    private fun loadData() {

    var al = intent.getStringArrayListExtra("key")
        binding?.rvAddress?.adapter = MyAdapter(al!!, this)
        binding?.rvAddress?.adapter?.notifyDataSetChanged()


    }

    override fun <T> onItemClick(t: T) {
        var address = t as String
        var intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("address", address)
        startActivity(intent)
        finish()
    }
}