@extends('layouts.app')

@section('content')
<div class="container mt-5">
    <div class="row msjk-total-block">
        <div class="col-12 col-xl-3 col-lg-4 col-md-4 col-sm-6 mb-4 msjk-single-blocks">
            <div class="msjk-single-block">
              <div class="singleblock-header">
                <div class="singleblock-headertxt">
                  <p>Total Users</p>
                </div>
                <div class="singleblock-icon">
                    <i class="fas fa-users"></i>
                  {{-- <img src="{{ asset('assets/images/svg/profile.svg') }}" alt="profile icon" /> --}}
                </div>
              </div>
              <div class="singleblock-body">
                <div class="no-of-cases">
                  <p class="single-block-medium">Number of Users</p>
                  <a href="{{ route('user.index') }}" class="single-block-bold">{{ $user }}</a>
                </div>
              </div>
            </div>
        </div>

        <div class="col-12 col-xl-3 col-lg-4 col-md-4 col-sm-6 mb-4 msjk-single-blocks">
            <div class="msjk-single-block">
              <div class="singleblock-header">
                <div class="singleblock-headertxt">
                  <p>Total Products</p>
                </div>
                <div class="singleblock-icon">
                    <i class="fas fa-box"></i>
                  {{-- <img src="{{ asset('assets/images/svg/product.svg') }}" alt="product icon" /> --}}
                </div>
              </div>
              <div class="singleblock-body">
                <div class="no-of-cases">
                  <p class="single-block-medium">Number of Products</p>
                  <a href="{{ route('product.index') }}" class="single-block-bold">{{ $product }}</a>
                </div>
              </div>
            </div>
        </div>

        <div class="col-12 col-xl-3 col-lg-4 col-md-4 col-sm-6 mb-4 msjk-single-blocks">
            <div class="msjk-single-block">
              <div class="singleblock-header">
                <div class="singleblock-headertxt">
                  <p>Total Categories</p>
                </div>
                <div class="singleblock-icon">
                    <i class="fas fa-tags"></i>
                  {{-- <img src="{{ asset('assets/images/svg/category.svg') }}" alt="category icon" /> --}}
                </div>
              </div>
              <div class="singleblock-body">
                <div class="no-of-cases">
                  <p class="single-block-medium">Number of Categories</p>
                  <a href="{{ route('category.index') }}" class="single-block-bold">{{ $category }}</a>
                </div>
              </div>
            </div>
        </div>

        <div class="col-12 col-xl-3 col-lg-4 col-md-4 col-sm-6 mb-4 msjk-single-blocks">
            <div class="msjk-single-block">
              <div class="singleblock-header">
                <div class="singleblock-headertxt">
                  <p>Total Orders</p>
                </div>
                <div class="singleblock-icon">
                    <i class="fas fa-receipt"></i>
                  {{-- <img src="{{ asset('assets/images/svg/order.svg') }}" alt="order icon" /> --}}
                </div>
              </div>
              <div class="singleblock-body">
                <div class="no-of-cases">
                  <p class="single-block-medium">Number of Orders</p>
                  <a href="{{ route('orders.index') }}" class="single-block-bold">{{ $order }}</a>
                </div>
              </div>
            </div>
        </div>

        <div class="col-12 col-xl-3 col-lg-4 col-md-4 col-sm-6 mb-4 msjk-single-blocks">
            <div class="msjk-single-block">
              <div class="singleblock-header">
                <div class="singleblock-headertxt">
                  <p>Total Comments</p>
                </div>
                <div class="singleblock-icon">
                    <i class="fas fa-comments"></i>
                  {{-- <img src="{{ asset('assets/images/svg/comment.svg') }}" alt="comment icon" /> --}}
                </div>
              </div>
              <div class="singleblock-body">
                <div class="no-of-cases">
                  <p class="single-block-medium">Number of Comments</p>
                  <a href="{{ route('comment.index') }}" class="single-block-bold">{{ $comment }}</a>
                </div>
              </div>
            </div>
        </div>
        
    </div>
</div>
@endsection
