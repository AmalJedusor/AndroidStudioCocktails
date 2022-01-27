package fr.uha.hassenforder.teams.ui.cocktail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.uha.hassenforder.teams.R;
import fr.uha.hassenforder.teams.database.AppDatabase;
import fr.uha.hassenforder.teams.database.FeedDatabase;
import fr.uha.hassenforder.teams.databinding.MyCocktailsItemBinding;
import fr.uha.hassenforder.teams.databinding.MyCocktailsListFragmentBinding;
import fr.uha.hassenforder.teams.databinding.PersonItemBinding;
import fr.uha.hassenforder.teams.databinding.PersonListFragmentBinding;
import fr.uha.hassenforder.teams.model.CocktailWithDetails;
import fr.uha.hassenforder.teams.model.PersonWithDetails;
import fr.uha.hassenforder.teams.ui.ItemSwipeCallback;
import fr.uha.hassenforder.teams.ui.person.PersonListFragment;
import fr.uha.hassenforder.teams.ui.person.PersonListFragmentDirections;

public class CocktailListFragment extends Fragment {

    private CocktailListViewModel mViewModel;
    private CocktailAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        MyCocktailsListFragmentBinding binding =  MyCocktailsListFragmentBinding.inflate(inflater, container, false);
        binding.add.setOnClickListener(
                view -> NavHostFragment.findNavController(this).navigate(R.id.action_navigation_cocktailList_to_navigation_cocktail)
        );
        binding.list.setLayoutManager(new LinearLayoutManager(binding.list.getContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration divider = new DividerItemDecoration(binding.list.getContext(), DividerItemDecoration.VERTICAL);
        binding.list.addItemDecoration(divider);
        adapter = new CocktailAdapter();
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CocktailListViewModel.class);

        mViewModel.setCocktailDao(AppDatabase.get().getCocktailDao());
        mViewModel.getCocktails().observe(getViewLifecycleOwner(), cocktails -> adapter.setCollection(cocktails));
    }

    private class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.ViewHolder> {

        private List<CocktailWithDetails> collection;

        private class ViewHolder extends RecyclerView.ViewHolder {
            MyCocktailsItemBinding binding;
            public ViewHolder(@NonNull MyCocktailsItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            MyCocktailsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from((parent.getContext())), R.layout.my_cocktails_item, parent, false);
            binding.setLifecycleOwner(getViewLifecycleOwner());
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CocktailWithDetails p = collection.get(position);
            holder.binding.setC(p);
        }

        @Override
        public int getItemCount() {
            return collection == null ? 0 : collection.size();
        }

        public void setCollection(List<CocktailWithDetails> collection) {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return CocktailAdapter.this.collection == null ? 0 : CocktailAdapter.this.collection.size();
                }

                @Override
                public int getNewListSize() {
                    return collection == null ? 0 : collection.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    CocktailWithDetails newCocktail = collection.get(newItemPosition);
                    CocktailWithDetails oldCocktail = CocktailAdapter.this.collection.get(oldItemPosition);
                    return newCocktail.cocktail.getCid() == oldCocktail.cocktail.getCid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    CocktailWithDetails newCocktail = collection.get(newItemPosition);
                    CocktailWithDetails oldCocktail = CocktailAdapter.this.collection.get(oldItemPosition);
                    return CocktailWithDetails.compare (newCocktail, oldCocktail);
                }
            });
            this.collection = collection;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cocktails_menu, menu);
    }

    private boolean doPopulate (){

        FeedDatabase feeder = new FeedDatabase();
        feeder.feed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
        case R.id.populate: return doPopulate();
        }
        return super.onOptionsItemSelected(item);
    }

}