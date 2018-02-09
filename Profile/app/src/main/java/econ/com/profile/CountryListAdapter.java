/**
 * Created by Ram on 7/2/18.
 */


package econ.com.profile;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.LinkedList;




public class CountryListAdapter
        extends RecyclerView.Adapter<CountryListAdapter.CountryViewHolder> {

    private static final String TAG = Profile.class.getSimpleName();

    private final LinkedList<String> mCountryList;
    private final LayoutInflater mInflater;

    public interface RecyclerViewClickListener
    {
        public void recyclerViewListClicked(String text, int position);
    }

    class CountryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final TextView countryItemView; // have to check do we need as public.
        final CountryListAdapter mAdapter;

        /**
         * Creates a new custom view holder to hold the view to display
         * in the RecyclerView.
         *
         * @param itemView The view in which to display the data.
         * @param adapter The adapter that manages the the data and
         *                views for the RecyclerView.
         */
        public CountryViewHolder(View itemView, CountryListAdapter adapter) {
            super(itemView);
            countryItemView = (TextView) itemView.findViewById(R.id.country);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            String clickOutput = countryItemView.getText().toString();
            itemListener.recyclerViewListClicked(clickOutput, getAdapterPosition());

        }
    }

    private Context context;
    private static RecyclerViewClickListener itemListener;

    public CountryListAdapter(Context context, LinkedList<String> countryList , RecyclerViewClickListener itemListener) {
        mInflater = LayoutInflater.from(context);
        this.mCountryList = countryList;
        this.itemListener = itemListener;
    }

    /**
     * Inflates an item view and returns a new view holder that contains it.
     * Called when the RecyclerView needs a new view holder to represent an item.
     *
     * @param parent The view group that holds the item views.
     * @param viewType Used to distinguish views, if more than one
     *                 type of item view is used.
     * @return a view holder.
     */
    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate an item view.
        View itemView = mInflater.inflate(R.layout.countrylist, parent, false);
        return new CountryViewHolder(itemView, this);
    }

    /**
     * Sets the contents of an item at a given position in the RecyclerView.
     *
     * @param holder The view holder for that position in the RecyclerView.
     * @param position The position of the item in the RecycerView.
     */
    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        // Retrieve the data for that position.
        String current = mCountryList.get(position);
        // Add the data to the view holder.
        holder.countryItemView.setText(current);
    }

    /**
     * Returns the size of the container that holds the data.
     *
     * @return Size of the list of data.
     */
    @Override
    public int getItemCount() {
        return mCountryList.size();
    }
}