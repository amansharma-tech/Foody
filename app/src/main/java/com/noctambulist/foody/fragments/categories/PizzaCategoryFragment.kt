package com.noctambulist.foody.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.noctambulist.foody.data.Category
import com.noctambulist.foody.util.Resource
import com.noctambulist.foody.viewmodel.CategoryViewModel
import com.noctambulist.foody.viewmodel.factory.BaseCategoryViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class PizzaCategoryFragment : BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore, Category.Pizza)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.specialProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)

                    }

                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()

                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.popularNow.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showPopularNowLoading()
                    }

                    is Resource.Success -> {
                        popularNowAdapter.differ.submitList(it.data)
                        hidePopularNowLoading()
                    }

                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
                        hidePopularNowLoading()
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onSpecialProductPagingRequest() {

    }

    override fun onPopularNowPagingRequest() {

    }
}