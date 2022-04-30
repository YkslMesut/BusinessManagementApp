package com.example.myunetlog.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myunetlog.R
import com.example.myunetlog.adapter.ConsignmentAdapter
import com.example.myunetlog.databinding.FragmentMapsBinding
import com.example.myunetlog.model.Consignment
import com.example.myunetlog.viewmodel.MapsFragmentViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.net.URI

class MapsFragment() : Fragment()  {

    private lateinit var viewModel : MapsFragmentViewModel

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMapGlo : GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        googleMapGlo = googleMap
    }

    private val consignmentList : ArrayList<Consignment> = ArrayList()
    private lateinit var consignmentAdapter: ConsignmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        viewModel = ViewModelProviders.of(this).get(MapsFragmentViewModel::class.java)
        viewModel.getData()

        setAdapter()

        prepLocationUpdates()
        observeLiveData()

        binding.btnAction.setOnClickListener {
            val action = MapsFragmentDirections.actionMapsFragmentToSelectImageFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeLiveData() {

        viewModel.consignmentLiveData.observe(viewLifecycleOwner, Observer { consignment ->
            consignment?.let {
                consignmentAdapter.updateData(consignment)
            }

        })

        viewModel.getLocationLiveData().observe(viewLifecycleOwner, Observer {
            val locationPosition = LatLng(it!!.latitude.toDouble(), it!!.longitude.toDouble())
            googleMapGlo.addMarker(MarkerOptions().position(locationPosition).title("Şuan Buradasınız"))
            googleMapGlo.moveCamera(CameraUpdateFactory.newLatLngZoom(locationPosition,18f))
            getAddress(locationPosition)
        })

        viewModel.preferenceLiveData.observe(viewLifecycleOwner) {
            val imageUri = Uri.parse(it)
            binding.imageView.setImageURI(imageUri)

        }
    }

    private fun getAddress(latLng: LatLng) {
        var geocoder = Geocoder(requireContext())
        var locationAddress = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1)
        var returnedAddress : Address  = locationAddress.get(0)
        locationAddress.let {
            binding.txtProvince.text = returnedAddress.subAdminArea + "/" + returnedAddress.adminArea
            binding.txtNeighbourhood.text = returnedAddress.subLocality
            binding.txtStreet.text = returnedAddress.thoroughfare

        }
    }

    private fun prepLocationUpdates() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates()
        } else {
            requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestSinglePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
        if (isGranted) {
            requestLocationUpdates()
        } else {
            Toast.makeText(context, "GPS Unavailable", Toast.LENGTH_LONG)
                .show()
        }
    }


    private fun requestLocationUpdates() {
        viewModel.startLocationUpdates()
    }

    private fun setAdapter(){
        consignmentAdapter = ConsignmentAdapter(consignmentList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = consignmentAdapter
        }
    }



}


