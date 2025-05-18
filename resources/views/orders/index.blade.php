@extends('layouts.app')

@section('content')
<div class="msjk-table-list-listing">
    <h1 class="my-4">Orders</h1>
    <div class="msjk-create-btn">
        <a href="{{ route('orders.create') }}">Create Order</a>
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
                    <th scope="col">User</th>
                    <th scope="col">Product</th>
                    <th scope="col">Order Date</th>
                    <th scope="col">Status</th>
                    <th scope="col">Quantity</th>
                    <th scope="col">Offer</th>
                    <th scope="col">Action</th>
                </tr>
            </thead>
            <tbody>
                @forelse ($orders as $order)
                <tr>
                    <td>{{ $order->user->name }}</td>
                    <td>{{ $order->product->name }}</td>
                    <td>
                      @if($order->order_date)
                      {{ \Carbon\Carbon::parse($order->order_date)->format('d-m-Y') }}
                      @else
                      N/A
                      @endif
                    </td>
                    <td>{{ $order->status }}</td>
                    <td>{{ $order->quantity }}</td>
                    <td>{{ $order->offer }}</td>
                    <td class="action-custom-msjk">
                        <a href="{{ route('orders.show', $order->id) }}"><i class="fas fa-eye" style="color: #000;"></i></a>
                        <a href="{{ route('orders.edit', $order->id) }}"><i class="fas fa-edit" style="color: #000;"></i></a>
                        <form action="{{ route('orders.destroy', $order->id) }}" method="POST" class="confirm-delete">
                          @method('DELETE')
                          @csrf
                          <button onclick="return confirm('Are you sure you want to delete this item?')"><i class="fas fa-trash" style="color: red;"></i></button>
                        </form>
                    </td>
                </tr>  
                @empty
                <tr>
                    <td colspan="7">No Data Found</td>  
                </tr>
                @endforelse
            </tbody>
        </table>
      </div>
</div>
@endsection