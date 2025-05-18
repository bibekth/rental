@extends('layouts.app')

@section('content')
<div class="sa-contact-form-text">
    <h3 class="my-4">Update Order</h3>
    @if ($errors->any())
    <ul>
        @foreach ($errors->all() as $error)
        <li style="color: red"> {{$error}} </li>
        @endforeach  
    </ul>
    @endif

    <form action="{{ route('orders.update', $order->id) }}" method="POST">
        @method('PUT')
        @csrf
        <div class="sa-text-field-block">
            <label for="">User</label>
            <select name="user_id" id="userid">
                @foreach ($users as $user)
                <option value="{{ $user->id }}" {{ $user->id == $order->user_id ? 'selected' : '' }}>{{ $user->name }}</option>
                @endforeach
            </select>
        </div>
        <div class="sa-text-field-block">
            <label for="">Product</label>
            <select name="product_id" id="productid">
                @foreach ($products as $product)
                <option value="{{ $product->id }}" {{ $product->id == $order->product_id ? 'selected' : '' }}>{{ $product->name }}</option>
                @endforeach
            </select>
        </div>
        <div class="sa-text-field-block">
            <label for="">Order Date</label>
            <input type="date" name="order_date" id="orderdate" value="{{ old('order_date', $order->order_date) }}">
        </div>
        <div class="sa-text-field-block">
            <label for="">Status</label>
            <input type="text" name="status" id="status" value="{{ old('status', $order->status) }}">
        </div>
        <div class="sa-text-field-block">
            <label for="">Quantity</label>
            <input type="number" name="quantity" id="quantity" value="{{ old('quantity', $order->quantity) }}">
        </div>
        <div class="sa-text-field-block">
            <label for="">Offer</label>
            <input type="text" name="offer" id="offer" value="{{ old('offer', $order->offer) }}">
        </div>
        <div class="sa-form-button">
            <input type="submit" value="Update">
        </div>
    </form>
</div>
@endsection
