@extends('layouts.app')
@section('content')
<div class="sa-contact-form-text">
    <h3 class="my-4">Create Order Details</h3>
    @if ($errors->any())
    <ul>
        @foreach ($errors->all() as $error)
        <li style="color: red"> {{$error}} </li>
        @endforeach  
    </ul>
    @endif

    <form action="{{ route('orderdetails.store') }}" method="POST">
        @csrf
        <div class="sa-text-field-block">
            <label for="">Order ID</label>
            <input type="text" name="order_id" id="orderid" placeholder="Enter Order ID">
        </div>
        <div class="sa-text-field-block">
            <label for="">Discount</label>
            <input type="number" name="discount" id="discountid" placeholder="Enter Discount">
        </div>
        <div class="sa-text-field-block">
            <label for="">Delivery Address</label>
            <textarea name="deliveryaddress" id="deliveryaddressid" placeholder="Enter Delivery Address"></textarea>
        </div>
        <div class="sa-text-field-block">
            <label for="">Payment Method</label>
            <input type="text" name="payment_method" id="paymentmethodid" placeholder="Enter Payment Method">
        </div>
        <div class="sa-text-field-block">
            <label for="">Status Payment</label>
            <input type="text" name="statuspayment" id="statuspaymentid" placeholder="Enter Status Payment">
        </div>
        <div class="sa-text-field-block">
            <label for="">Mobile Number</label>
            <input type="text" name="mobilenumber" id="mobilenumberid" placeholder="Enter Mobile Number">
        </div>
        <div class="sa-text-field-block">
            <label for="">Delivery Status</label>
            <input type="text" name="deliverystatus" id="deliverystatusid" placeholder="Enter Delivery Status">
        </div>
        <div class="sa-form-button">
            <input type="submit" value="Submit">
        </div>
    </form>
</div>
@endsection
