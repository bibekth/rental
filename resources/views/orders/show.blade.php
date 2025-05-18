@extends('layouts.app')

@section('content')
<div class="msjk-container mt-3">
    <h1 class="msjk-singleproduct-heading">
        {{ $order->product->name }} Order Details
    </h1>

    <div class="card">
        <div class="card-body">
            <div class="row align-items-center">
                <div class="msjk-singleproduct-desc col-md-6">
                    <p><strong>Order Date:</strong> 
                        @if($order->order_date)
                            {{ \Carbon\Carbon::parse($order->order_date)->format('d-m-Y') }}
                        @else
                            N/A
                        @endif
                    </p>
                    <p><strong>Status:</strong> {{ $order->status }}</p>
                    <p><strong>Quantity:</strong> {{ $order->quantity }}</p>
                    <p><strong>Offer:</strong> {{ $order->offer }}</p>
                    <p><strong>User:</strong> {{ $order->user->name }}</p>
                    <p><strong>Product:</strong> {{ $order->product->name }}</p>
                </div>
            </div>
            <div class="msjk-edit-del-back-content d-flex">
                <a href="{{ route('orders.index') }}" class="btn btn-secondary mt-3">Back to List</a>
                <a href="{{ route('orders.edit', $order->id) }}" class="btn btn-primary mt-3">Edit Order</a>

                <form action="{{ route('orders.destroy', $order->id) }}" method="POST" class="mt-3">
                    @method('DELETE')
                    @csrf
                    <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this item?')">Delete Order</button>
                </form>
            </div>
        </div>
    </div>
</div>
@endsection
