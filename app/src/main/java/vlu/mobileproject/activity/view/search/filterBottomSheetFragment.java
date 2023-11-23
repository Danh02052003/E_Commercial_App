package vlu.mobileproject.activity.view.search;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;

import vlu.mobileproject.R;


public class filterBottomSheetFragment extends BottomSheetDialogFragment {

    RangeSlider rangeSlider;
    TextView start_tooltip, end_tooltip;

    ImageButton imgApDung, imgCancel;
    int startIntValue, endIntValue;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Define the FilterListener interface
    public interface FilterListener {
        void onFilterApplied(int startValue, int endValue);

        void onCancelFilter();
    }


    private FilterListener filterListener; // Declare the filterListener variable

    public void setFilterListener(FilterListener listener) {
        this.filterListener = listener;
    }

    public filterBottomSheetFragment() {
    }

    public static filterBottomSheetFragment newInstance(String param1, String param2) {
        filterBottomSheetFragment fragment = new filterBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_bottom_sheet, container, false);

        // Add controls
        rangeSlider = view.findViewById(R.id.range_slider);
        start_tooltip = view.findViewById(R.id.start_tooltip);
        end_tooltip = view.findViewById(R.id.end_tooltip);
        imgApDung = view.findViewById(R.id.imgApDung);
        imgCancel = view.findViewById(R.id.imgCancel);
        // Set up your filter options and their functionality here
        rangeSlider.setValueFrom(0f);
        rangeSlider.setValueTo(2000f);
        rangeSlider.setValues(0f, 2000f);
        addEvent();


        return view;
    }

    // Method to update the tooltip's position based on the slider's value
    private void updateTooltipPosition(RangeSlider slider, float value, TextView tooltip) {
        Rect thumbRect = getThumbBounds(slider, value);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tooltip.getLayoutParams();
        params.leftMargin = thumbRect.centerX() - tooltip.getWidth() / 2;
        params.topMargin = thumbRect.top - tooltip.getHeight() - 20; // Adjust the vertical position of the tooltip as needed
        tooltip.setLayoutParams(params);
    }

    // Helper method to get the bounds of the thumb based on its value
    private Rect getThumbBounds(RangeSlider slider, float value) {
        Rect thumbRect = new Rect();
        float sliderWidth = slider.getWidth() - slider.getPaddingLeft() - slider.getPaddingRight();
        float valueRange = slider.getValueTo() - slider.getValueFrom();
        float offset = (value - slider.getValueFrom()) / valueRange * sliderWidth;
        thumbRect.left = Math.round(slider.getPaddingLeft() + offset - slider.getThumbRadius());
        thumbRect.right = thumbRect.left + 2 * slider.getThumbRadius();
        thumbRect.top = slider.getPaddingTop();
        thumbRect.bottom = slider.getHeight() - slider.getPaddingBottom();
        return thumbRect;
    }

    private void filter() {

    }

    private void addEvent() {
        // Format and update the tooltip text as the RangeSlider values change
        imgApDung.setOnClickListener(v -> {
            int startValue = Math.round(rangeSlider.getValues().get(0));
            int endValue = Math.round(rangeSlider.getValues().get(1));

            // Notify the listener that the filter should be applied
            if (filterListener != null) {
                filterListener.onFilterApplied(startValue, endValue);
            }

            // Close the bottom sheet
            dismiss();
        });

        imgCancel.setOnClickListener(v -> {
            // Notify the listener that the filter should be canceled
            if (filterListener != null) {
                filterListener.onCancelFilter();
            }

            // Close the bottom sheet
            dismiss();
        });
    }
}