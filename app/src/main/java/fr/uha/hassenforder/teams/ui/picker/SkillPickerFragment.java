package fr.uha.hassenforder.teams.ui.picker;

import fr.uha.hassenforder.teams.model.Skill;

public class SkillPickerFragment extends SmallListPickerFragment {

    private SkillPickerFragmentArgs arg = null;

    private SkillPickerFragmentArgs getArg() {
        if (arg == null) {
            arg = SkillPickerFragmentArgs.fromBundle(getArguments());
        }
        return arg;
    }

    @Override
    protected String getRequestKey() {
        return getArg().getRequestKey();
    }

    @Override
    protected String getTitle() {
        int title = getArg().getTitle();
        return getResources().getString(title);
    }

    protected String [] buildInputs () {
        String[] inputs = new String [Skill.values().length];
        for (int i=0; i<Skill.values().length;++i) {
            inputs[i] = Skill.values()[i].name();
        }
        return inputs;
    }

    protected String buildOutput (int position) {
        if (position == -1) return "";
        if (position >= Skill.values().length) return "";
        return Skill.values()[position].name();
    }

}
