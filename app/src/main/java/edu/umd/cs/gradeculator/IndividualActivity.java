package edu.umd.cs.gradeculator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.gradeculator.model.Work;

public class IndividualActivity extends SingleFragmentActivity {


    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_STORY_ID = "STORY_ID";

    @Override
    protected Fragment createFragment() {
        String storyId = getIntent().getStringExtra(EXTRA_STORY_ID);
        return IndividualFragment.newInstance(storyId);
    }

    public static Intent newIntent(Context context, String storyId) {
        Intent intent = new Intent(context, IndividualActivity.class);
        intent.putExtra(EXTRA_STORY_ID, storyId);
        return intent;
    }

    public static Work getStoryCreated(Intent data) {
        return IndividualFragment.getworkCreated(data);
    }

}
