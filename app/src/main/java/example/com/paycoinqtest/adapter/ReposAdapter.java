package example.com.paycoinqtest.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.com.paycoinqtest.R;
import example.com.paycoinqtest.model.RepoInfo;

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder> {
	private List<RepoInfo> data = new ArrayList<>();
	private Context context;

	public ReposAdapter(@NonNull Context context, @NonNull List<RepoInfo> data) {
		this.data = data;
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item, parent, false);
		TextView txtName = (TextView) view.findViewById(R.id.txt_name);

		return new ViewHolder(view, txtName);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
	}

	public void add(RepoInfo newItem) {
		data.add(newItem);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		TextView txtName;

		ViewHolder(View itemView, TextView txtName) {
			super(itemView);
			this.txtName = txtName;
		}
	}
}
