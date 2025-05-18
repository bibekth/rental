@extends('layouts.app')

@section('content')
<div class="msjk-container mt-3">
    <h1 class="msjk-singleproduct-heading">
        {{ $product->name }} Details</h1>

    <div class="card">
        <div class="card-body">
            <div class="row align-items-center">
                <div class="msjk-singleproduct-desc col-md-6">
                    {{-- <h2>{{ $product->name }}</h2> --}}
                    <p>{{ $product->description }}</p>
                    <p><strong>Amount:</strong> Rs. {{ number_format($product->amount, 2) }}</p>
                    <p><strong>Purchase Date:</strong> 
                        @if($product->purchase_date)
                            {{ \Carbon\Carbon::parse($product->purchase_date)->format('d-m-Y') }}
                        @else
                            N/A
                        @endif
                    </p>
                    <p><strong>Category:</strong> {{ $product->category->name }}</p>
                </div>
                <div class="msjk-single-image col-md-6">
                    @if($product->photo)
                        <img src="{{ asset('storage/' . $product->photo) }}" alt="{{ $product->name }}" class="img-fluid">
                    @else
                        <p>No photo available</p>
                    @endif
                </div>
            </div>
            <div class="msjk-edit-del-back-content d-flex">
                <a href="{{ route('product.index') }}" class="btn btn-secondary mt-3">Back to List</a>
                <a href="{{ route('product.edit', $product->id) }}" class="btn btn-primary mt-3">Edit Product</a>

                <form action="{{ route('product.destroy', $product->id) }}" method="POST" class="mt-3">
                    @method('DELETE')
                    @csrf
                    <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this item?')">Delete Product</button>
                </form>
            </div>
        </div>
    </div>
</div>
@endsection
