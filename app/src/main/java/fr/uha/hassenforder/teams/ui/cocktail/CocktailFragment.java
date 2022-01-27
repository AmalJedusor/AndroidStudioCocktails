package fr.uha.hassenforder.teams.ui.cocktail;

import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.uha.hassenforder.teams.Permissions;
import fr.uha.hassenforder.teams.Picture;
import fr.uha.hassenforder.teams.R;
import fr.uha.hassenforder.teams.database.AppDatabase;
import fr.uha.hassenforder.teams.databinding.CocktailFragmentBinding;
import fr.uha.hassenforder.teams.ui.person.PersonFragmentArgs;


public class CocktailFragment extends Fragment {

    static private final String TAG = CocktailFragment.class.getSimpleName();
    private CocktailViewModel mViewModel;
    private CocktailFragmentBinding binding;
    private Picture picture;
    private boolean fullPictureGranted;

    private ActivityResultLauncher<Uri> captureFullPhoto = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (! result) return;
                    Picture.addToGallery(getContext(), picture.getPhotoPath());
                    mViewModel.setHighRes (picture.getPhotoPath());
                }
            }
    );

    private ActivityResultLauncher<Void> captureSmallPhoto = registerForActivityResult(
            new ActivityResultContracts.TakePicturePreview(),
            new ActivityResultCallback<Bitmap>() {
                @Override
                public void onActivityResult(Bitmap result) {
                    if (result == null) return;
                    mViewModel.setLowRes (result);
                }
            }
    );

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.cocktail_fragment, container, false);
        binding.setLifecycleOwner(this);

        binding.setTakePicture(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullPictureGranted) {
                    if (picture == null) picture = new Picture();
                    picture.createPhotoFile(getContext());
                    captureFullPhoto.launch(picture.getPhotoURI(getContext()));
                } else {
                    captureSmallPhoto.launch(null);
                }
            }
        });

        return binding.getRoot();
    }

    private void rebuildTitle(boolean modified) {
        String title = getResources().getString(R.string.title_person_edit, modified ? "*" : "");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Permissions.get().requestPermissions(view,
                R.string.permissions_store_required,
                new String [] { Manifest.permission.WRITE_EXTERNAL_STORAGE }
        );
        Permissions.get().getGrantedPermissions().observe(getViewLifecycleOwner(), gp -> fullPictureGranted = gp != null && gp.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        mViewModel = new ViewModelProvider(this).get(CocktailViewModel.class);
        mViewModel.setCocktailDao(AppDatabase.get().getCocktailDao());
        mViewModel.getModified().observe(getViewLifecycleOwner(), v -> rebuildTitle(v));
        mViewModel.getSavable().observe(getViewLifecycleOwner(), v -> { getActivity().invalidateOptionsMenu(); });
        long id = CocktailFragmentArgs.fromBundle(getArguments()).getId();
        if (id == 0) {
            mViewModel.createCocktail();
        } else {
            mViewModel.setId(id);
        }
        binding.setVm(mViewModel);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cocktail_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.savecocktail).setEnabled(true);
    }

    private boolean doSave() {
        mViewModel.save ();
        Navigation.findNavController(getView()).popBackStack();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.savecocktail:
            System.out.println("saving ?");
            return doSave();
        }
        return super.onOptionsItemSelected(item);
    }

}