package fr.uha.hassenforder.teams.ui.person;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import fr.uha.hassenforder.teams.databinding.PersonItemBinding;
import fr.uha.hassenforder.teams.databinding.PersonListFragmentBinding;
import fr.uha.hassenforder.teams.model.PersonWithDetails;
import fr.uha.hassenforder.teams.ui.ItemSwipeCallback;

public class PersonListFragment extends Fragment {

    private PersonListViewModel mViewModel;
    private PersonAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        PersonListFragmentBinding binding =  PersonListFragmentBinding.inflate(inflater, container, false);
        binding.add.setOnClickListener(
                view -> NavHostFragment.findNavController(this).navigate(R.id.action_navigation_personList_to_person)
        );

        binding.list.setLayoutManager(new LinearLayoutManager(binding.list.getContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration divider = new DividerItemDecoration(binding.list.getContext(), DividerItemDecoration.VERTICAL);
        binding.list.addItemDecoration(divider);

        ItemTouchHelper touchHelper = new ItemTouchHelper(
                new ItemSwipeCallback(getContext(), ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, new ItemSwipeCallback.SwipeListener() {
                    @Override
                    public void onSwiped(int direction, int position) {
                        PersonWithDetails person = adapter.collection.get(position);
                        switch (direction) {
                        case ItemTouchHelper.LEFT:
                            mViewModel.deletePerson (person.person);
                            break;
                        case ItemTouchHelper.RIGHT:
                            PersonListFragmentDirections.ActionNavigationPersonListToPerson action = PersonListFragmentDirections.actionNavigationPersonListToPerson();
                            action.setId(person.person.getPid());
                            NavHostFragment.findNavController(PersonListFragment.this).navigate(action);
                            break;
                        }
                    }
                })
        );
        touchHelper.attachToRecyclerView(binding.list);
        adapter = new PersonAdapter();
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonListViewModel.class);

        mViewModel.setPersonDao(AppDatabase.get().getPersonDao());
        mViewModel.getPersons().observe(getViewLifecycleOwner(), persons -> adapter.setCollection(persons));
    }

    private class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

        private List<PersonWithDetails> collection;

        private class ViewHolder extends RecyclerView.ViewHolder {
            PersonItemBinding binding;
            public ViewHolder(@NonNull PersonItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }

        @NonNull
        @Override
        public PersonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            PersonItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from((parent.getContext())), R.layout.person_item, parent, false);
            binding.setLifecycleOwner(getViewLifecycleOwner());
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonAdapter.ViewHolder holder, int position) {
            PersonWithDetails p = collection.get(position);
            holder.binding.setP(p);
        }

        @Override
        public int getItemCount() {
            return collection == null ? 0 : collection.size();
        }

        public void setCollection(List<PersonWithDetails> collection) {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return PersonAdapter.this.collection == null ? 0 : PersonAdapter.this.collection.size();
                }

                @Override
                public int getNewListSize() {
                    return collection == null ? 0 : collection.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    PersonWithDetails newPerson = collection.get(newItemPosition);
                    PersonWithDetails oldPerson = PersonAdapter.this.collection.get(oldItemPosition);
                    return newPerson.person.getPid() == oldPerson.person.getPid();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    PersonWithDetails newPerson = collection.get(newItemPosition);
                    PersonWithDetails oldPerson = PersonAdapter.this.collection.get(oldItemPosition);
                    return PersonWithDetails.compare (newPerson, oldPerson);
                }
            });
            this.collection = collection;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.persons_menu, menu);
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