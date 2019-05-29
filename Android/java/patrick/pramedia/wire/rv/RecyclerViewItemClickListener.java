package patrick.pramedia.wire.rv;

import android.view.View;

/**
 * Created by PRA on 12/14/2017.
 */

public interface RecyclerViewItemClickListener {

    public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}
