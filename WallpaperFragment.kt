package com.example.wallpaperapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpaperapp.databinding.FragmentWallpaperBinding

class WallpaperFragment : Fragment() {

    private lateinit var binding: FragmentWallpaperBinding
    private lateinit var category: Category

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWallpaperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        category = arguments?.getParcelable(ARG_CATEGORY) ?: return // Handle null gracefully
        loadWallpapers(category)
    }

    private fun openWallpaperPreview(wallpaperUrl: String) {
        val intent = Intent(requireContext(), WallpaperPreviewActivity::class.java).apply {
            putExtra("SELECTED_WALLPAPER", wallpaperUrl)
        }
        startActivity(intent)
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: Category): WallpaperFragment {
            return WallpaperFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CATEGORY, category)
                }
            }
        }
    }

    private fun loadWallpapers(category: Category) {
        Log.d("WallpaperFragment", "Wallpapers: ${category.wallpapers}")

        if (category.wallpapers.isNullOrEmpty()) {
            Log.d("WallpaperFragment", "No wallpapers available for this category.")
            return
        }

        // Show progress bar while loading
        binding.progressBar.visibility = View.VISIBLE

        // Setup RecyclerView
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = WallpaperAdapter(requireContext(), category.wallpapers) { wallpaperUrl ->
            openWallpaperPreview(wallpaperUrl)
        }

        // Add ItemDecoration for spacing
        val itemDecoration = ItemOffsetDecoration(resources.getDimensionPixelSize(R.dimen.recycler_view_item_margin))
        binding.recyclerView.addItemDecoration(itemDecoration)

        // Hide progress bar after a short delay to simulate loading
        binding.recyclerView.postDelayed({
            binding.progressBar.visibility = View.GONE
        }, 500) // Adjust the delay as needed
    }
}
