package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rental.R;

import java.util.List;

import model.OrderModel;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderModel> orderList;

    public OrderAdapter(List<OrderModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_order_detail, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);
        holder.productNameTextView.setText(order.getProductName());
        holder.productPriceTextView.setText("Rs. " + order.getProductPrice());
        holder.mobileNumberTextView.setText(order.getMobileNumber());
        holder.deliveryAddressTextView.setText(order.getDeliveryAddress());
        holder.paymentMethodTextView.setText(order.getPaymentMethod());
        holder.grandTotalTextView.setText("Rs. " + order.getGrandTotal());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView, productPriceTextView, mobileNumberTextView, deliveryAddressTextView, paymentMethodTextView, grandTotalTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.grandTotalAmount);
            mobileNumberTextView = itemView.findViewById(R.id.mobileNumberEditText);
//            deliveryAddressTextView = itemView.findViewById(R.id.deliveryAddressEditText);
//            paymentMethodTextView = itemView.findViewById(R.id.paymentMethodEditText);
            grandTotalTextView = itemView.findViewById(R.id.grandTotalLabel);
        }
    }
}
