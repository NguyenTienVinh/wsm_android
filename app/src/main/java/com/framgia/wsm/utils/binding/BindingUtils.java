package com.framgia.wsm.utils.binding;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.framgia.wsm.R;
import com.framgia.wsm.data.model.TimeSheetDate;
import com.framgia.wsm.databinding.NavHeaderMainBinding;
import com.framgia.wsm.screen.login.LoginPresenter;
import com.framgia.wsm.screen.main.MainViewModel;
import com.framgia.wsm.utils.Constant;
import com.framgia.wsm.widget.timesheet.OnDayClickListener;
import com.framgia.wsm.widget.timesheet.TimeSheetView;
import java.util.List;

/**
 * Created by le.quang.dao on 20/03/2017.
 */

public final class BindingUtils {

    private BindingUtils() {
        // No-op
    }

    /**
     * setAdapter For RecyclerView
     */
    @BindingAdapter({ "recyclerAdapter" })
    public static void setAdapterForRecyclerView(RecyclerView recyclerView,
            RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter({ "itemSelected", "currentItem", "viewModel" })
    public static void setNavigationItemSelected(NavigationView navigationView,
            NavigationView.OnNavigationItemSelectedListener listen, int currentItem,
            MainViewModel viewModel) {
        navigationView.setNavigationItemSelectedListener(listen);
        navigationView.setCheckedItem(currentItem);
        if (navigationView.getHeaderCount() == 0) {
            NavHeaderMainBinding binding =
                    NavHeaderMainBinding.inflate(LayoutInflater.from(navigationView.getContext()));
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
            navigationView.addHeaderView(binding.getRoot());
        }
    }

    @BindingAdapter({ "statusDrawerLayout" })
    public static void setStatusDrawerLayout(DrawerLayout drawerLayout, final String status) {
        if (status != null) {
            if (status.equals(Constant.DRAWER_IS_CLOSE)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            if (status.equals(Constant.DRAWER_IS_OPEN)) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
    }

    @BindingAdapter({ "currentFragment" })
    public static void setCurrentViewPager(final ViewPager viewPager, final int currentPage) {
        viewPager.setCurrentItem(currentPage);
    }

    @BindingAdapter({ "adapter" })
    public static void setViewPagerAdapter(final ViewPager viewPager,
            final FragmentPagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    @BindingAdapter("errorText")
    public static void setErrorText(EditText editText, String text) {
        editText.setError(text);
    }

    @BindingAdapter("imageUrlUser")
    public static void loadImageUser(ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        Uri uri = Uri.parse(url);
        Glide.with(imageView.getContext())
                .load(uri)
                .placeholder(R.drawable.ic_placeholder_user)
                .into(imageView);
    }

    @BindingAdapter({ "timeSheetDates", "month", "year" })
    public static void setTimeSheetDates(TimeSheetView timeSheetView,
            List<TimeSheetDate> timeSheetDates, int month, int year) {
        timeSheetView.setTime(month, year);
        timeSheetView.reuse();
        timeSheetView.invalidate();
        timeSheetView.setTimeSheetDates(timeSheetDates);
    }

    @BindingAdapter({ "onDayClick" })
    public static void setOnDayClick(TimeSheetView timeSheetView,
            OnDayClickListener onDayClickListener) {
        timeSheetView.setOnDayClickListener(onDayClickListener);
    }

    @BindingAdapter({ "toolbar" })
    public static void setToolbar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
    }

    @BindingAdapter("errorTextInputLayout")
    public static void setErrorTextInputLayout(final TextInputLayout textInputLayout,
            final String text) {
        textInputLayout.setError(text);
        EditText editText = textInputLayout.getEditText();
        if (editText == null) {
            return;
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //No-Op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    textInputLayout.setError(text);
                }
                if (s.length() > 0) {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //No-Op
            }
        });
    }

    @BindingAdapter({ "errorTextInputLayoutEmail" })
    public static void setErrorTextInputLayoutEmail(final TextInputLayout textInputLayout,
            final String text) {
        textInputLayout.setError(text);
        EditText editText = textInputLayout.getEditText();
        if (editText == null) {
            return;
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //No-Op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //No-Op
            }

            @Override
            public void afterTextChanged(Editable s) {
                LoginPresenter.getInstance().validateUserNameInput(s.toString());
            }
        });
    }
}
