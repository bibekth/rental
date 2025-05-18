@extends('layouts.app')

@section('content')
<div class="sa-contact-form-text">
    <h3 class="my-4">Create Order</h3>
    @if ($errors->any())
    <ul>
        @foreach ($errors->all() as $error)
        <li style="color: red"> {{$error}} </li>
        @endforeach  
    </ul>
    @endif

    <form action="{{ route('orders.store') }}" method="POST">
        @csrf
        <div class="sa-text-field-block">
            <label for="">User</label>
            <select name="user_id" id="userid">
                <option value="" selected disabled>Select a user</option>
                @foreach ($users as $user)
                <option value="{{ $user->id }}">{{ $user->name }}</option>
                @endforeach
            </select>
        </div>
        <div class="sa-text-field-block">
            <label for="">Product</label>
            <select name="product_id" id="productid">
                <option value="" selected disabled>Select a product</option>
                @foreach ($products as $product)
                <option value="{{ $product->id }}">{{ $product->name }}</option>
                @endforeach
            </select>
        </div>
        <div class="sa-text-field-block">
            <label for="">Order Date</label>
            <input type="date" name="order_date" id="orderdate">
        </div>
        <div class="sa-text-field-block">
            <label for="">Status</label>
            <input type="text" name="status" id="status">
        </div>
        <div class="sa-text-field-block">
            <label for="">Quantity</label>
            <input type="number" name="quantity" id="quantity">
        </div>
        <div class="sa-text-field-block">
            <label for="">Offer</label>
            <input type="text" name="offer" id="offer">
        </div>
        <div class="sa-form-button">
            <input type="submit" value="Submit">
        </div>
    </form>
</div>
@endsection
