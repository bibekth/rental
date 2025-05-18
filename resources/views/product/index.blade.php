@extends('layouts.app')
@section('content')
<div class="msjk-table-list-listing">
    <h1 class="my-4">Product</h1>
    <div class="msjk-create-btn">
        <a href="{{route('product.create')}}">Create Product</a>
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
            
            {{-- <th scope="col">SN</th> --}}
            <th scope="col">Name</th>
            <th scope="col">Description</th>
            <th scope="col">Amount</th>
            <th scope="col">Photo</th>
            <th scope="col">Purchase Date</th> 
            <th scope="col">Category</th>
            <th scope="col">Action</th>
            
          </tr>
        </thead>
        <tbody>
            @forelse ($products as $product)
            <tr>
                <td>{{$product->name}}</td>
                <td>{{$product->description}}</td>
                <td>{{$product->amount}}</td>
                <td><img src="{{asset('storage/'.$product->photo)}}" alt="" width="50px" height="50px"></td>
                <td>
                  @if($product->purchase_date)
                  {{ \Carbon\Carbon::parse($product->purchase_date)->format('d-m-Y') }}
                  @else
                  N/A
                  @endif
                </td>
                <td>{{$product->category->name}}</td>
                <td class="action-custom-msjk">
                    <a href="{{ route('product.show', $product->id) }}"><i class="fas fa-eye" style="color: #000;"></i></a>
                    <a href="{{ route('product.edit',$product->id) }}"><i class="fas fa-edit" style="color: #000;"></i></a>
                    {{-- ani a tag bta del hunna + uta web.api ma resource use gareko cha ni ta hamle so del garda chai yo form banauna parcha --}}
                    <form action="{{route('product.destroy',$product->id)}}" method="POST"  class="confirm-delete">
                      @method('DELETE')
                      @csrf
                      <button onclick="return confirm('Are you sure you want to delete this item?')"><i class="fas fa-trash" style="color: red;"></i></button>
                    </form>
                </td>

        
                
              </tr>  
            @empty
              <td>No Data Found</td>  
            @endforelse
          
         
        
      </table>
    </div>
    
  </div>
@endsection
