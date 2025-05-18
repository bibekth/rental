@extends('layouts.app')

@section('content')
<div class="msjk-container mt-3">
    <h1 class="msjk-singleproduct-heading">
        Order Details for Order ID: {{ $orderDetails->order_id }}</h1>

    <div class="card">
        <div class="card-body">
            <div class="row align-items-center">
                <div class="msjk-singleproduct-desc col-md-6">
                    <p><strong>Product Name:</strong> {{ $orderDetails->order->product->name }}</p>
                    <p><strong>Discount:</strong> {{ $orderDetails->discount }}</p>
                    <p><strong>Delivery Address:</strong> {{ $orderDetails->deliveryaddress }}</p>
                    <p><strong>Payment Method:</strong> {{ $orderDetails->payment_method }}</p>
                    <p><strong>Status Payment:</strong> {{ $orderDetails->statuspayment }}</p>
                    <p><strong>Mobile Number:</strong> {{ $orderDetails->mobilenumber }}</p>
                    <p><strong>Delivery Status:</strong> {{ $orderDetails->deliverystatus }}</p>
                </div>
            </div>
            <div class="msjk-edit-del-back-content d-flex">
                <a href="{{ route('orderdetails.index') }}" class="btn btn-secondary mt-3">Back to List</a>
                <a href="{{ route('orderdetails.edit', $orderDetails->id) }}" class="btn btn-primary mt-3">Edit Order Details</a>

                <form action="{{ route('orderdetails.destroy', $orderDetails->id) }}" method="POST" class="mt-3">
                    @method('DELETE')
                    @csrf
                    <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this item?')">Delete Order Details</button>
                </form>
            </div>
        </div>
    </div>
</div>
@endsection
