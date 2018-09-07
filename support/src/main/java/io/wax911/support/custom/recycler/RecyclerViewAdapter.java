package io.wax911.support.custom.recycler;

import android.animation.Animator;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import io.wax911.support.base.animation.AnimationBase;
import io.wax911.support.base.event.ItemClickListener;
import io.wax911.support.base.event.RecyclerChangeListener;
import io.wax911.support.custom.animation.ScaleAnimation;
import io.wax911.support.custom.presenter.SupportPresenter;
import io.wax911.support.util.ActionModeUtil;
import io.wax911.support.util.SupportUtil;

/**
 * Created by max on 2017/06/09.
 * Recycler view adapter implementation
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder<T>> implements Filterable, RecyclerChangeListener<T> {

    protected Context context;
    protected @NonNull List<T> data;
    protected @Nullable List<T> clone;
    protected SupportPresenter presenter;
    protected ItemClickListener<T> clickListener;

    private ActionModeUtil<T> actionMode;
    private AnimationBase customAnimation;

    private int lastPosition;

    private boolean isLowRamDevice;

    public RecyclerViewAdapter(Context context) {
        this.context = context.getApplicationContext();
        this.isLowRamDevice = SupportUtil.isLowRamDevice(this.context);
        this.data = new ArrayList<>();
    }

    public @NonNull List<T> getData() {
        return data;
    }

    @Override
    public long getItemId(int position) {
        if(!hasStableIds())
            return super.getItemId(position);
        return data.get(position).hashCode();
    }

    public void setActionModeCallback(ActionModeUtil<T> selectorCallback) {
        this.actionMode = selectorCallback;
        this.actionMode.setRecyclerAdapter(this);
    }

    public void setClickListener(ItemClickListener<T> clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onItemsInserted(@NonNull List<? extends T> swap) {
        data = new ArrayList<>(swap);
        notifyDataSetChanged();
    }

    @Override
    public void onItemRangeInserted(@NonNull List<? extends T> swap) {
        int startRange = getItemCount(), difference;
        data.addAll(swap);
        difference = getItemCount() - startRange;
        if(difference > 5)
            notifyItemRangeInserted(startRange, difference);
        else if(difference != 0)
            notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(@NonNull List<? extends T> swap) {
        int startRange = getItemCount();
        int difference = swap.size() - startRange;
        data = new ArrayList<>(swap);
        notifyItemRangeChanged(startRange, difference);
    }


    @Override
    public void onItemChanged(@NonNull T swap, int position) {
        data.set(position, swap);
        notifyItemChanged(position);
    }

    @Override
    public void onItemRemoved(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public abstract @NonNull RecyclerViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerViewHolder<T> holder) {
        super.onViewAttachedToWindow(holder);
        if(holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams)
            setLayoutSpanSize((StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams(), holder.getAdapterPosition());
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerViewHolder<T> holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager)
            setLayoutSpanSize(((GridLayoutManager)layoutManager));
    }

    /**
     * Calls the the recycler view holder to perform view binding
     * @see RecyclerViewHolder
     * <br/><br/>
     * default implemation is already done for you
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder<T> holder, int position) {
        if(getItemCount() > 0) {
            animateViewHolder(holder, position);
            T model = data.get(position);
            holder.setActionMode(actionMode);
            holder.onBindViewHolder(model);
            holder.onBindSelectionState(model);
        }
    }

    /**
     * Calls the the recycler view holder impl to perform view recycling
     * @see RecyclerViewHolder
     * <br/><br/>
     * default implemation is already done for you
     */
    @Override
    public void onViewRecycled(@NonNull RecyclerViewHolder<T> holder) {
        holder.onViewRecycled();
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * <br/>
     * The default method has already been implemented for you.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * Clears data sets and notifies the recycler observer about the changed data set
     */
    public void clearDataSet() {
        data = new ArrayList<>();
        if(clone != null)
            clone = new ArrayList<>();
        notifyDataSetChanged();
    }

    /**
     * Initial implementation is only specific for group types of recyclers,
     * in order to customize this an override is required.
     * <br/>
     * @param layoutManager grid layout manage for your recycler
     */
    private void setLayoutSpanSize(GridLayoutManager layoutManager) {
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(isFullSpanItem(position))
                    return 1;
                return layoutManager.getSpanCount();
            }
        });
    }

    /**
     * Initial implementation is only specific for group types of recyclers,
     * in order to customize this an override is required.
     * <br/>
     * @param layoutParams StaggeredGridLayoutManager.LayoutParams for your recycler
     */
    private void setLayoutSpanSize(StaggeredGridLayoutManager.LayoutParams layoutParams, int position) {
        if(isFullSpanItem(position))
            layoutParams.setFullSpan(true);
    }

    /**
     * Get currently set animation type for recycler view holder items,
     * if no custom animation is set @{@link ScaleAnimation}
     * will be assigned in {@link #onAttachedToRecyclerView(RecyclerView)}
     * <br/>
     *
     * @see AnimationBase
     */
    private AnimationBase getCustomAnimation() {
        if(customAnimation == null)
            customAnimation = new ScaleAnimation();
        return customAnimation;
    }

    /**
     * Set your own custom animation that will be used in
     * {@link #onAttachedToRecyclerView(RecyclerView)}
     * <br/>
     *
     * @see AnimationBase
     */
    public void setCustomAnimation(AnimationBase customAnimation) {
        this.customAnimation = customAnimation;
    }

    protected abstract boolean isRecyclerStateType(int viewType);

    protected abstract boolean isFullSpanItem(int position);

    private void animateViewHolder(RecyclerViewHolder<T> holder, int position) {
        if(!isLowRamDevice && position > lastPosition) {
            if(holder != null && holder.itemView != null && getCustomAnimation() != null) {
                Animator[] animators = getCustomAnimation().getAnimators(holder.itemView);
                for (Animator animator : animators) {
                    animator.setDuration(getCustomAnimation().getAnimationDuration());
                    animator.setInterpolator(getCustomAnimation().getInterpolator());
                    animator.start();
                }
            }
        }
        lastPosition = position;
    }
}
