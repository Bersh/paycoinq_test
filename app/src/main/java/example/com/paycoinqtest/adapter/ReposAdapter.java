package example.com.paycoinqtest.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.com.paycoinqtest.R;
import example.com.paycoinqtest.data.RepoInfo;

public class ReposAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	//item types
	private static final int ITEM = 1;
	private static final int FOOTER = 2;

	private List<RepoInfo> data = new ArrayList<>();
	private boolean isFooterAdded = false;

	public ReposAdapter(@NonNull List<RepoInfo> data) {
		this.data = data;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		RecyclerView.ViewHolder viewHolder = null;

		switch (viewType) {
			case ITEM:
				viewHolder = createItemViewHolder(parent);
				break;
			case FOOTER:
				viewHolder = createFooterViewHolder(parent);
				break;
			default:
				break;
		}

		return viewHolder;
	}

	private ItemViewHolder createItemViewHolder(ViewGroup parent) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item, parent, false);
		TextView txtName = (TextView) view.findViewById(R.id.txt_name);

		return new ItemViewHolder(view, txtName);
	}

	private FooterViewHolder createFooterViewHolder(ViewGroup parent) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_progress, parent, false);

		return new FooterViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		switch (getItemViewType(position)) {
			case ITEM:
				((ItemViewHolder) holder).txtName.setText(data.get(position).getName());
				break;
			case FOOTER:
				break;
			default:
				break;
		}
	}

	public void addProgressFooter() {
		if (isFooterAdded) {
			return;
		}

		isFooterAdded = true;
		add(new RepoInfo());
	}

	public void removeProgressFooter() {
		if (!isFooterAdded) {
			return;
		}
		isFooterAdded = false;

		int position = data.size() - 1;
		RepoInfo item = getItem(position);

		if (item != null) {
			data.remove(position);
			notifyItemRemoved(position);
		}
	}

	private RepoInfo getItem(int position) {
		return data.get(position);
	}

	@Override
	public int getItemViewType(int position) {
		return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
	}

	private boolean isLastPosition(int position) {
		return (position == data.size() - 1);
	}

	public void add(@NonNull RepoInfo item) {
		data.add(item);
		notifyItemInserted(data.size() - 1);
	}

	public void setData(@NonNull List<RepoInfo> items) {
		data = new ArrayList<>(items);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	private static class ItemViewHolder extends RecyclerView.ViewHolder {
		TextView txtName;

		ItemViewHolder(View itemView, TextView txtName) {
			super(itemView);
			this.txtName = txtName;
		}
	}

	private static class FooterViewHolder extends RecyclerView.ViewHolder {

		FooterViewHolder(View itemView) {
			super(itemView);
		}
	}
}
