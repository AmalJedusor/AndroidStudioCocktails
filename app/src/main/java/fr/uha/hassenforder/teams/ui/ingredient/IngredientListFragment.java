package fr.uha.hassenforder.teams.ui.ingredient;

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
import androidx.databinding.ViewDataBinding;
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
import fr.uha.hassenforder.teams.databinding.MyIngredientsItemBinding;
import fr.uha.hassenforder.teams.databinding.MyIngredientsListFragmentBinding;
import fr.uha.hassenforder.teams.model.CocktailWithDetails;
import fr.uha.hassenforder.teams.model.Ingredient;
import fr.uha.hassenforder.teams.model.IngredientType;
import fr.uha.hassenforder.teams.ui.ItemSwipeCallback;
import fr.uha.hassenforder.teams.ui.cocktail.CocktailFragmentArgs;
import fr.uha.hassenforder.teams.ui.cocktail.CocktailListFragment;
import fr.uha.hassenforder.teams.ui.cocktail.CocktailListViewModel;
import fr.uha.hassenforder.teams.ui.person.PersonListFragmentDirections;

public class IngredientListFragment extends Fragment {

    private IngredientListViewModel lm;
    private IngredientAdapter adapter;
    private IngredientViewModel lm2;
    private MyIngredientsListFragmentBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding =  MyIngredientsListFragmentBinding.inflate(inflater, container, false);
        binding.ingredientlist.setLayoutManager(new LinearLayoutManager(binding.ingredientlist.getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(binding.ingredientlist.getContext(), DividerItemDecoration.VERTICAL);
        binding.ingredientlist.addItemDecoration(divider);
        adapter = new IngredientListFragment.IngredientAdapter();
        binding.ingredientlist.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = IngredientListFragmentArgs.fromBundle(getArguments()).getId();
                if (id == 0) {
                    String item = binding.itemEditText.getText().toString();
                    Ingredient i = new Ingredient(id,item, IngredientType.FOOD);
                    lm2.save(i);
                } else {
                    lm2.setId(id);
                }

                adapter.notifyDataSetChanged();
                binding.itemEditText.setText("");
            }
        });
        lm = new ViewModelProvider(this).get(IngredientListViewModel.class);
        lm2 = new ViewModelProvider(this).get(IngredientViewModel.class);
        lm.setIngredientDao(AppDatabase.get().getIngredientDao());
        lm2.setIngredientDao(AppDatabase.get().getIngredientDao());
        lm.getIngredients().observe(getViewLifecycleOwner(), ingredients -> adapter.setCollection(ingredients));


    }

    private class IngredientAdapter extends RecyclerView.Adapter<IngredientListFragment.IngredientAdapter.ViewHolder> {

        private List<Ingredient> collection;

        private class ViewHolder extends RecyclerView.ViewHolder {
            MyIngredientsItemBinding binding;
            public ViewHolder(@NonNull MyIngredientsItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            MyIngredientsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from((parent.getContext())), R.layout.my_ingredients_item, parent, false);

            return new IngredientListFragment.IngredientAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull IngredientListFragment.IngredientAdapter.ViewHolder holder, int position) {
            Ingredient p = collection.get(position);
            holder.binding.setI(p);
        }

        @Override
        public int getItemCount() {
            return collection == null ? 0 : collection.size();
        }

        public void setCollection(List<Ingredient> collection) {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return IngredientListFragment.IngredientAdapter.this.collection == null ? 0 : IngredientListFragment.IngredientAdapter.this.collection.size();
                }

                @Override
                public int getNewListSize() {
                    return collection == null ? 0 : collection.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Ingredient newIngredient = collection.get(newItemPosition);
                    Ingredient oldIngredient = IngredientListFragment.IngredientAdapter.this.collection.get(oldItemPosition);
                    return newIngredient.getIngid() == oldIngredient.getIngid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Ingredient newIngredient = collection.get(newItemPosition);
                    Ingredient oldIngredient = IngredientListFragment.IngredientAdapter.this.collection.get(oldItemPosition);
                    return Ingredient.compare (newIngredient, oldIngredient);
                }
            });
            this.collection = collection;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.ingredients_menu, menu);
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
