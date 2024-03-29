package com.example.mychallenge.core.base

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager

import android.os.Build
import android.os.Bundle


import android.view.View
import android.view.inputmethod.InputMethodManager


import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.mychallenge.core.CommonUtils
import com.example.mychallenge.core.NetworkUtils

import dagger.android.AndroidInjection
import dagger.android.DaggerActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : DaggerActivity(),
    BaseFragment.Callback {

    // TODO
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private var mProgressDialog: ProgressDialog? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkConnected(applicationContext)

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }

    fun openActivityOnTokenExpire() {
        /* startActivity(LoginActivity.newIntent(this));
        finish();*/
    }

    fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    fun showLoading() {
        hideLoading()
        mProgressDialog = CommonUtils.showLoadingDialog(this)
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }

    /*protected void setSupportToolbar(@NonNull Toolbar toolbar) {
        setSupportToolbar(toolbar, true, true);
    }
    protected void setSupportToolbar(@NonNull Toolbar toolbar, boolean showTitle, boolean showHome) {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            if (showTitle)
                getSupportActionBar().setTitle(getTitle());

            getSupportActionBar().setDisplayShowTitleEnabled(showTitle);
            getSupportActionBar().setDisplayShowHomeEnabled(showHome);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHome);
        }

    }*/
}

