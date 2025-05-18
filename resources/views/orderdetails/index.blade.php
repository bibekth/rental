@extends('layouts.app')
@section('content')
<div class="msjk-table-list-listing">
    <h1 class="my-4">Order Details</h1>
    <div class="msjk-create-btn">
        <a href="{{ route('orderdetails.create') }}">Create Order Details</a>
    </div>
    @if (Session::has('success'))
    <div class="alert alert-success" role="alert">
      {{ Session::get('success') }}
    </div>
    @endif

    <div class="table-responsive msjk-table-body">
        <table class="table msjk-customize-table table-hover">
            <thead>
                <tr class="custom-class-tr">
                    <th scope="col">Order ID</th>
                    <th scope="col">Product Name</th>
                    <th scope="col">Discount</th>
                    <th scope="col">Delivery Address</th>
                    <th scope="col">Payment Method</th>
                    <th scope="col">Status Payment</th>
                    <th scope="col">Mobile Number</th>
                    <th scope="col">Delivery Status</th>
                    <th scope="col">Action</th>
                </tr>
            </thead>
            <tbody>
                @forelse ($orderDetails as $detail)
                <tr>
                    <td>{{ $detail->order_id }}</td>
                    <td>{{ $detail->order->product->name }}</td> 
                    <td>{{ $detail->discount }}</td>
                    <td>{{ $detail->deliveryaddress }}</td>
                    <td>{{ $detail->payment_method }}</td>
                    <td>{{ $detail->statuspayment }}</td>
                    <td>{{ $detail->mobilenumber }}</td>
                    <td>{{ $detail->deliverystatus }}</td>
                    <td class="action-custom-msjk">
                        <a href="{{ route('orderdetails.show', $detail->id) }}"><i class="fas fa-eye" style="color: #000;"></i></a>
                        <a href="{{ route('orderdetails.edit', $detail->id) }}"><i class="fas fa-edit" style="color: #000;"></i></a>
                        <form action="{{ route('orderdetails.destroy', $detail->id) }}" method="POST" class="confirm-delete">
                            @method('DELETE')
                            @csrf
                            <button onclick="return confirm('Are you sure you want to delete this item?')"><i class="fas fa-trash" style="color: red;"></i></button>
                        </form>
                    </td>
                </tr>  
                @empty
                <tr>
                    <td colspan="8">No Data Found</td>
                </tr>  
                @endforelse
            </tbody>
        </table>
    </div>
</div>
@endsection
