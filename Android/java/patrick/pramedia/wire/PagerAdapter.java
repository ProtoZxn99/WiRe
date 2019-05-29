package patrick.pramedia.wire;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by munil on 4/13/2019.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Group tab1 = new Group();
                return tab1;
            case 1:
                Schedule tab2 = new Schedule();
                return tab2;
            case 2:
                Device tab3 = new Device();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
