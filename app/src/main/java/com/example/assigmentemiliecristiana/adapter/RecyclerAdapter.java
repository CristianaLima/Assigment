package com.example.assigmentemiliecristiana.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.example.assigmentemiliecristiana.util.RecyclerViewItemClickListener;

import java.util.List;
import java.util.Objects;


public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<T> mData;
    private RecyclerViewItemClickListener mListener;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        TextView mTextView;
        ViewHolder(TextView textView) {
            super(textView);
            mTextView = textView;
        }
    }

    public RecyclerAdapter(RecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assignments_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(view -> mListener.onItemClick(view, viewHolder.getAdapterPosition()));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        T item = mData.get(position);
        if (item.getClass().equals(AssignmentEntity.class))
            holder.mTextView.setText(((AssignmentEntity) item).getName());
        if (item.getClass().equals(StudentEntity.class))
            holder.mTextView.setText(((StudentEntity) item).getUsername());
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public void setData(final List<T> data) {
        if (mData == null) {
            mData = data;
            notifyItemRangeInserted(0, data.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mData.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof AssignmentEntity) {
                        return ((AssignmentEntity) mData.get(oldItemPosition)).getId().equals(((AssignmentEntity) data.get(newItemPosition)).getId());
                    }
                    if (mData instanceof StudentEntity) {
                        return ((StudentEntity) mData.get(oldItemPosition)).getEmail().equals(
                                ((StudentEntity) data.get(newItemPosition)).getEmail());
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof AssignmentEntity) {
                        AssignmentEntity newAssigment = (AssignmentEntity) data.get(newItemPosition);
                        AssignmentEntity oldAssigment = (AssignmentEntity) mData.get(newItemPosition);
                        return newAssigment.getId().equals(oldAssigment.getId())
                                && Objects.equals(newAssigment.getName(), oldAssigment.getName())
                                && Objects.equals(newAssigment.getStatus(), oldAssigment.getStatus())
                                && newAssigment.getOwner().equals(oldAssigment.getOwner());
                    }
                    if (mData instanceof StudentEntity) {
                        StudentEntity newStudent = (StudentEntity) data.get(newItemPosition);
                        StudentEntity oldStudent = (StudentEntity) mData.get(newItemPosition);
                        return Objects.equals(newStudent.getEmail(), oldStudent.getEmail())
                                && Objects.equals(newStudent.getUsername(), oldStudent.getUsername())
                                && Objects.equals(newStudent.getEmail(), oldStudent.getEmail())
                                && newStudent.getPassword().equals(oldStudent.getPassword());
                    }
                    return false;
                }
            });
            mData = data;
            result.dispatchUpdatesTo(this);
        }
    }
}
