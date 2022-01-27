package fr.uha.hassenforder.teams.ui;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.uha.hassenforder.teams.Picture;
import fr.uha.hassenforder.teams.R;
import fr.uha.hassenforder.teams.model.Gender;

public class DataBindingAdapters {

    @BindingAdapter("android:background")
    public static void setBackground (View view, Gender gender) {
        int colorId = R.color.color_no;
        if (gender != null) {
            switch (gender) {
            case GIRL: colorId = R.color.color_girl; break;
            case BOY: colorId = R.color.color_boy; break;
            }
        }
        int color = view.getResources().getColor(colorId, null);
        view.setBackgroundColor(color);
    }
    @BindingAdapter("android:cocktailimage")
    public static void setCocktailImage (ImageView view, String image) {

        int drawableId = view.getResources().getIdentifier(image, "drawable",view.getContext().getPackageName());
        System.out.println(drawableId);
        view.setImageResource(drawableId);
    }

/*
    @BindingAdapter("level")
    public static void setLevel (ImageView view, Gender gender) {
        if (gender == null) {
            view.setImageLevel(0);
        } else {
            view.setImageLevel(gender.ordinal());
        }
    }
*/
    private static SimpleDateFormat output = null;

    @BindingAdapter("android:text")
    public static void setDate (TextView view, Date date) {
        String text = null;
        if (date == null) {
            text = view.getResources().getString(R.string.nodate);
        } else {
            if (output == null) {
                output = new SimpleDateFormat ("dd MMMM yyyy");
            }
            text = output.format(date);
        }
        view.setText(text);
    }

    @BindingAdapter("android:text")
    public static void setText(TextView view, int currentValue) {
        boolean todo = false;
        if (! todo && view.getText() == null) todo = true;
        if (! todo && view.getText().toString().isEmpty()) todo = true;
        if (! todo) {
            try {
                int inView = Integer.parseInt(view.getText().toString());
                if (inView != currentValue) todo = true;
            } catch (NumberFormatException e) {
                todo = true;
            }
        }
        if (todo) view.setText(Integer.toString(currentValue));
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getText(EditText view) {
        if (view.getText() == null) return 0;
        if (view.getText().toString().isEmpty()) return 0;
        try {
            int inView = Integer.parseInt(view.getText().toString());
            return inView;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @BindingAdapter(value={"selectedValue", "selectedValueAttrChanged"}, requireAll = false)
    public static void setSpinnerBinding(Spinner view, int newSelectedValue, final InverseBindingListener attrChange) {
        view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                attrChange.onChange();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (newSelectedValue != -1) {
            String nextValue = Integer.toString(newSelectedValue);
            int index = ((ArrayAdapter<String>) view.getAdapter()).getPosition(nextValue);
            view.setSelection(index, true);
        }
    }

    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    public static int setSpinnerInverseBinding(Spinner view) {
        String selectedItem = (String) view.getSelectedItem();
        try {
            int inView = Integer.parseInt(selectedItem);
            return inView;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @BindingAdapter({ "highRes", "lowRes", "avatar" })
    public static void setPicture (ImageView view, String highRes, Bitmap lowRes, int avatar) {
        if (highRes != null) {
            int w = view.getWidth();
            int h = view.getHeight();
            Bitmap bm = Picture.getBitmapFromUri(view.getContext(), highRes, w, h);
            view.setImageBitmap(bm);
        } else if (lowRes != null) {
            view.setImageBitmap(lowRes);
        } else if (avatar != 0) {
            view.setImageResource(avatar);
        } else {
            view.setImageResource(R.drawable.ic_baseline_person_24);
        }
    }

    @BindingAdapter("error")
    public static void setErrorMessage (TextInputLayout view, int messageId) {
        // messageID can be
        // 0 -> no error && nothing to say
        // >0 -> error && a message negative to display
        // <0 -> no error && a message positive to display
        String message = null;
        if (messageId > 0) {
            message = view.getResources().getString(messageId);
        } else if (messageId < 0) {
            message = view.getResources().getString(-messageId);
        }
        view.setError(message);
        view.setErrorEnabled(message != null);
    }

}
